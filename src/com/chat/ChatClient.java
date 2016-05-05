package com.chat;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

public class ChatClient extends Frame{
	
	private TextField textField = new TextField();
	private TextArea textArea = new TextArea();
	
	public void launchFrame(){
		this.setLocation(400, 300);
		this.setSize(300, 300);
		add(textField, BorderLayout.SOUTH);
		add(textArea, BorderLayout.NORTH);
		pack();
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				setVisible(false);
		        System.exit(-1);
		    }	
		});
		textField.addActionListener(new textFieldListener());
	}
	
	private class  textFieldListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String s = textField.getText().trim();
			textArea.setText(s);
			textField.setText("");
		}
		
	}
	
	public static void main(String[] args){
		new ChatClient().launchFrame();
		try {
			Socket socket = new Socket("127.0.0.1",9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
