package com.graphics.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketConnection {

	private ServerSocket serverSocket;
	private Socket socket;
	private InputStream is;
	private OutputStream os;
	
	public SocketConnection(int port) throws IOException {
		serverSocket = new ServerSocket(port, 10);
		System.out.println("Listening for connection...");
		socket = serverSocket.accept();
		System.out.println("Connection made !");
	}
	
	public String waitAndRecieve() throws IOException{
		
		if(is == null)
			is = socket.getInputStream();
		
		byte[] lenBytes = new byte[4];
		is.read(lenBytes, 0, 4);
		int len = (((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16)
				| ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff));
		byte[] receivedBytes = new byte[len];
		is.read(receivedBytes, 0, len);
		String received = new String(receivedBytes, 0, len);
		
		return received;
	}
	
	public void sendMessage(String toSend) throws IOException{
		
		if(os == null)
			os = socket.getOutputStream();
		
		byte[] toSendBytes = toSend.getBytes();
		int toSendLen = toSendBytes.length;
		byte[] toSendLenBytes = new byte[4];
		toSendLenBytes[0] = (byte) (toSendLen & 0xff);
		toSendLenBytes[1] = (byte) ((toSendLen >> 8) & 0xff);
		toSendLenBytes[2] = (byte) ((toSendLen >> 16) & 0xff);
		toSendLenBytes[3] = (byte) ((toSendLen >> 24) & 0xff);
		os.write(toSendLenBytes);
		os.write(toSendBytes);
	}
	
	public boolean isConnected(){
		return socket != null && socket.isConnected();
	}
	
	public void close() throws IOException{
		
		System.out.println("Closing connection...");
		
		if(is != null)
			is.close();
		
		if(os != null)
			os.close();
		
		if(serverSocket != null)
			serverSocket.close();
		
		if(socket != null)
			socket.close();
		
		System.out.println("Connection closed");
	}

}
