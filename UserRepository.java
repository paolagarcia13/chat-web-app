package com.example.demo.repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


import com.example.demo.model.User;

@Repository
public class UserRepository {


	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbcTemplate;
	
	public User findByusernameAndPassword(String username, String password) {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("usuario", username);
		parameter.addValue("clave", password);
		String sql = "select usu_usuario, usu_genero, usu_avatar" + "inner join (select usu_codigo from usuarios where usu_usuario = :usuario and usu_password = :clave)";
		
		List<User> listUser = namedJdbcTemplate.query(sql,parameter, new RowMapper<User>() {

			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			
				User users = new User();
				users.setUsername(rs.getString("usu_usuario"));
				users.setPassword(rs.getString("usu_password"));
				users.setGender(rs.getBoolean("usu_genero"));
				users.setAvatar(rs.getString("usu_avatar"));
				
				
				
				return users;
			}
			
		});
		return listUser.isEmpty() ? null : listUser.get(0);
	}


	public List<User> findFriends(String username) {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("usuario", username);
		String sql = "select distinct u2.usu_usuario, u2.usu_avatar, u2.usu_genero"
		+"from usuarios u1 join amigos f on u1.usu_usuario = f.receiver"
		+"join usuarios u2 on u2.usu_usuario = f.sender"
		+"where u1.usu_usuario LIKE ?";
		
		List<User> listUser = namedJdbcTemplate.query(sql,parameter, new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				
				User users = new User();
				users.setUsername(rs.getString("usu_usuario"));
				users.setGender(rs.getBoolean("usu_genero"));
				users.setAvatar(rs.getString("usu_avatar"));
				
				
								
				return users;
			}
			
		});
		return listUser;
	}

		
	public void saveUser(Boolean isRegister, User users) {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		String username = users.getUsername();
		String password = users.getPassword();
		Boolean gender = users.isGender();
		String avatar = users.getAvatar();
			if (isRegister) {
			parameter.addValue("usuario", username);
			parameter.addValue("clave", password);
			parameter.addValue("genero", gender);
			parameter.addValue("avatar", avatar);
			String sql = "insert into usuarios(usu_usuario, usu_password, usu_genero, usu_avatar)";
			namedJdbcTemplate.update(sql, parameter);
		} else {
			parameter.addValue("usuario", username);
			parameter.addValue("clave", password);
			parameter.addValue("genero", gender);
			parameter.addValue("avatar", avatar);
			String sql = "insert into usuarios(usu_usuario, usu_password, usu_genero, usu_avatar)";
			namedJdbcTemplate.update(sql, parameter);
		}
	}


	public List<User> findFriendsByKeyWord(String username, String keyWord) {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("usuario", username);
		parameter.addValue("codigo", keyWord);
		String sql = "select u2.username, u2.avatar, u2.gender"+"from usuarios u2 where usu_usuario != ? and username like ?";

		List<User> listUser = namedJdbcTemplate.query(sql,parameter, new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				
				User users = new User();
				users.setUsername(rs.getString("usu_usuario"));
				users.setAvatar(rs.getString("usu_avatar"));
				users.setGender(rs.getBoolean("usu_genero"));
				users.setCodigo(rs.getInt("usu_codigo"));
				
				
				
								
				return users;
			}
			
		});
		
		return listUser;

	}


	public List<User> findUsersByConversationId(Integer id){
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("codigo", id);
		String sql ="select u.usu_usuario, u.usu_avatar, u.usu_genero, cu.is_admin"+"from usuarios u join conversations_users cu"
					+" on u.usu_usuario = cu.usu_usuario"+" join conversations c"+" on c.id = cu.conversations_id"
					+" where c.id = ?";
		

		List<User> listUser = namedJdbcTemplate.query(sql,parameter, new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				User users = new User();
				
				users.setCodigo(rs.getInt("usu_codigo"));
				
				return users;
			}
			
		});
		
			
		return listUser;
	}

	
	public List<User> findFriendsNotInConversation(String username, String codigo, Integer conversationId) {
		MapSqlParameterSource parameter = new MapSqlParameterSource();
		parameter.addValue("usuario", username);
		parameter.addValue("codigo", codigo);
		parameter.addValue("admin", conversationId);
		String sql ="select u2.usu_usuario,u2.usu_avatar,u2.usu_genero"+" from usuarios u1 join amigos f on u1.usu_usuario = f.receiver "
					+" join ususarios u2 on u2.usu_usuario = f.sender"+" where u1.usu_usuario = ?"+" and f.status = 1"
					+" and u2.usu_usuario like ?"+" and u2.usu_usuario not in ("+" select u.usu_usuario"
					+" from usuarios u join conversations_users cu"+" on u.usu_usuario = cu.usu_usuario"
					+" join conversations c"+" on c.id = cu.conversations_id"+" where c.id = ?)";
	
	
		List<User> listUser = namedJdbcTemplate.query(sql,parameter, new RowMapper<User>() {
			
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {

				
				User users = new User();
				users.setUsername(rs.getString("usu_usuario"));
				users.setAvatar(rs.getString("usu_avatar"));
				users.setGender(rs.getBoolean("usu_genero"));
				users.setCodigo(rs.getInt("usu_codigo"));
				users.setCodigo(rs.getInt("isAdmin"));
				
				
		return users;
			}
		});
		return listUser;

	}
}
