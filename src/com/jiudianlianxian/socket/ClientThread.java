package com.jiudianlianxian.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ClientThread extends Thread {
	
	public ClientThread(Socket socket){
		this.addClient(socket);
	}
	
	    // 客户端连接的socket列表
	    private ArrayList<Socket> clients = new ArrayList<Socket>();
	 
	    // 添加客户
	    public void addClient(Socket socket) {
	        clients.add(socket);
	    }
	    // 删除客户
	    public void removeClient(Socket socket) {
	        clients.remove(socket);
	    }
	    // 向客户发送数据
	    public void sendMessage(Socket socket, String data) throws IOException {
	        // 给玩家发送数据
	        OutputStream os = socket.getOutputStream();
	        os.write(data.getBytes("UTF-8"));
	    }
	    //登录
	    
	    //注册
	    
	    //
	 
	    @Override
	    public void run() {
	        while (true) {
	            try {
	                for (Socket socket : clients) {
	                    // 获取客户端发来的数据
	                    InputStream is = socket.getInputStream();
	                    int len = is.available() + 1;
	                    System.out.println("len == " + len);
	                    byte[] buff = new byte[len];
	                    for (byte b : buff) {
	                    	 System.out.println("buff == "  + buff);
						}
	                   
	                    try{
	                    	  is.read(buff);
	                    	}catch(SocketException e){
	                    	  System.out.println("有客户断开连接~");
	                    	  this.removeClient(socket);
	                    	  break;
	                    	}
	                    
	                    // 输出接收到的数据
	                    String read = new String(buff);
	                    System.out.println("收到数据：" + read);
	 
	                    // 给玩家发送数据
	                    String data = "恭喜你，连接成功啦~~";
	                    sendMessage(socket, data);
	                }
	                sleep(10);
	            } catch (IOException e) {
	                e.printStackTrace();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }

}
