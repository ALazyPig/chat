package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private List<Clients> clientList = new ArrayList<Clients>();
	public static void main(String[] args) {
		new ChatServer().start();	
	}
	
	public  void start() {
		ServerSocket serverSocket = null;		
		try {		
			serverSocket = new ServerSocket(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while(true){			
				Socket socket = serverSocket.accept();	
				Clients clients = new Clients(socket);
				clientList.add(clients);
				new Thread(clients).start();
				System.out.println("A client connected");					
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}			
		
	}

	private class Clients implements Runnable{
		private Socket socket;
		private DataInputStream dataInputStream;
		private DataOutputStream dataOutputStream;
		public Clients(Socket socket){
			this.socket = socket;
		}
		@Override
		public void run() {
			try {
				dataInputStream = new DataInputStream(socket.getInputStream());
				dataOutputStream = new DataOutputStream(socket.getOutputStream());
				while(true){
					String s = dataInputStream.readUTF();//读到末尾EOFException				
					System.out.println(s);
					for (Clients clients : clientList) {
						clients.dataOutputStream.writeUTF(s);//客户端失去连接抛异常
					}
				}
			}catch (EOFException e){
				if(this != null) clientList.remove(this);
				System.out.println("已关闭");
			} catch (IOException e1) {
				System.out.println("客户端关闭");
			} finally{
				try {
					if(dataInputStream != null)
						dataInputStream.close();
					if(dataOutputStream != null)
						dataOutputStream.close();
					if(socket != null)
						socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}				
			}
		
		}
		
		
	}

}
