package com.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(9999);
			while(true){
				Socket socket = serverSocket.accept();
				System.out.println("a client connected");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
