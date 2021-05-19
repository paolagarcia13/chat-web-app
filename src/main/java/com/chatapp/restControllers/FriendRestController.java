package com.chatapp.restControllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.chatapp.models.User;
import com.chatapp.models.dtos.MessageDTO;
import com.chatapp.services.ChatServiceAbstract;
import com.chatapp.services.MessageServiceInterface;
import com.chatapp.services.UserServiceInterface;
import com.chatapp.services.impl.ChatService;
import com.chatapp.services.impl.MessageService;
import com.chatapp.services.impl.UserService;
import com.chatapp.websockets.ChatWebsocket;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebServlet("/friend-rest-controller")
public class FriendRestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserServiceInterface userServiceInterface = UserService.getInstance();
	private ChatService chatService = ChatService.getInstance();
    public FriendRestController() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyWord = request.getParameter("keyword");
		String userName = request.getParameter("username");
		List<User> listUsers;
		
		if(keyWord == "") {
			listUsers = userServiceInterface.findFriends(userName);
		}
		else listUsers = userServiceInterface.findFriendsByKeyWord(userName,keyWord);
		
		
		
		for(User user : listUsers) {
			user.isOnline = chatService.isUserOnline(user.getUsername());
		}
		
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(listUsers);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		PrintWriter printWriter = response.getWriter();
		printWriter.print(json);
		printWriter.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
