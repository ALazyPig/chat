package com.chat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient extends Frame{
	
	private TextField textField = new TextField();
	private TextArea textArea = new TextArea();
	private static Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	public void launchFrame(){
		this.setLocation(400, 300);
		this.setSize(300, 300);
		add(textField, BorderLayout.SOUTH);
		add(textArea, BorderLayout.NORTH);
		pack();
		this.setVisible(true);	
		try {
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			dataOutputStream = new DataOutputStream(outputStream);
			dataInputStream = new DataInputStream(inputStream);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				/*try {
					dataInputStream.close();
					dataOutputStream.close();//流关闭，socket将关闭
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
				setVisible(false);
		        System.exit(-1);
		        
		    }	
		});
		textField.addActionListener(new textFieldListener());
		new Thread(new Receive()).start();
		
	}
	
	private class  textFieldListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = textField.getText().trim();
			//textArea.setText(s);
			textField.setText("");			
			try {				
				dataOutputStream.writeUTF(s);
				dataOutputStream.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
		}
		
	}
	
	private class Receive implements Runnable{

		@Override
		public void run() {
			String s;
			try {
				while(true){
					s = dataInputStream.readUTF();
					System.out.println(s);
					textArea.setText(s);
				}					
			} catch (EOFException e) {
				System.out.println("客户端关闭");
			} catch (IOException e) {
				e.printStackTrace();
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
	
	public static void main(String[] args){		
		try {
			socket = new Socket("127.0.0.1",9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
		new ChatClient().launchFrame();
	} 
}
