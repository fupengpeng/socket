package com.jiudianlianxian.testsocket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * @Title: Server
 * @Description: 给此类一个描述
 * @Company: 济宁九点连线信息技术有限公司
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017年9月18日 上午10:26:07
 */
public class Server {
    public static final int PORT = 12350;//监听的端口号   
    
    public static void main(String[] args) {  
        System.out.println("服务器启动...\n");  
        Server server = new Server();  
        server.init();  
    }  
  
    public void init() {  
        try {  
            ServerSocket serverSocket = new ServerSocket(PORT);  
            while (true) {  
                // 一旦有堵塞, 则表示服务器与客户端获得了连接  
                Socket client = serverSocket.accept();  
                // 处理这次连接  
                new HandlerThread().addClient(client);;  
            }  
        } catch (Exception e) {  
            System.out.println("服务器异常: " + e.getMessage());  
        }  
    }  
  
    private class HandlerThread implements Runnable {  
    	
    	// 客户端连接的socket列表
	    private ArrayList<Socket> clients = new ArrayList<Socket>();
	    
//        private Socket socket; 
        public HandlerThread() {   
            new Thread(this).start();  
        }  
//        public HandlerThread(Socket client) {  
//            socket = client;  
//            new Thread(this).start();  
//        }  
        
        // 添加客户
	    public void addClient(Socket socket) {
	        clients.add(socket);
	    }
	    // 删除客户
	    public void removeClient(Socket socket) {
	        clients.remove(socket);
	    }
	    public  void timer3() {
	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	          public void run() {
	            System.out.println("-------设定要指定任务--------");
	          }
	        }, 1000, 2000);
	      }
	       
  
        public void run() {  
        	while (true) {
        		for (Socket socket : clients) {
        			try {  
                        // 读取客户端数据  
        				System.out.println(socket+"sssssssssss");
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                        // 处理客户端数据  
                        System.out.println("客户端发过来的内容:" + clientInputStr);  
                        if ("ok".equals(clientInputStr)) {
                        	System.out.println("客户端输入ok，移除此客户端连接的socket");
        					this.removeClient(socket);
        					break;
        				}
          
                        // 向客户端回复信息  
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
                        System.out.print("请输入:\t");  
                        // 发送键盘输入的一行  
                        String s = new BufferedReader(new InputStreamReader(System.in)).readLine();  
                        out.writeUTF(s);  
                        
                        out.close();  
                        input.close();  
                    } catch (IOException e) {
    	                e.printStackTrace();
    	                System.out.println("服务器IOException 异常: " + e.getMessage()); 
    	            } catch (Exception e) {  
    	            	e.printStackTrace();
                        System.out.println("服务器 run 异常: " + e.getMessage());  
                    } 
//        			finally {  
//                        if (socket != null) {  
//                            try {  
//                            	System.out.println("socket不为空，关闭套接字");
//                                socket.close();  
//                            } catch (Exception e) {  
//                                socket = null;  
//                                System.out.println("服务端 finally 异常:" + e.getMessage());  
//                            }  
//                        }  
//                    } 
				}
        		
				
			}
            
        }  
    }  
} 
