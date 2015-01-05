package com.graphics.utils;

import java.io.IOException;

public class SocketHandler implements Runnable{
	
	private SocketConnection con;
	private boolean running = true;
	private String message;
	
	public SocketHandler(int port) throws IOException {
		con = new SocketConnection(port);
	}
	
	@Override
	public void run() {
		while(running && isConnected()){
			try {
				message = con.waitAndRecieve();
			} catch (IOException e) {
				System.err.println("Unable to retrieve message");
				e.printStackTrace();
			}
		}
		try {
			con.close();
		} catch (IOException e) {
			System.err.println("Error closing socket");
			e.printStackTrace();
		}
	}
	
	public void stop(){
		running = false;
	}

	public boolean isConnected() {
		return con.isConnected();
	}
	
	public String getMessage(){
		return message;
	}
	
	public void resetMessage(){
		this.message = "";
	}

}
