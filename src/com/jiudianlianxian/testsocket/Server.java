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
 * @Description: ������һ������
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��18�� ����10:26:07
 */
public class Server {
    public static final int PORT = 12350;//�����Ķ˿ں�   
    
    public static void main(String[] args) {  
        System.out.println("����������...\n");  
        Server server = new Server();  
        server.init();  
    }  
  
    public void init() {  
        try {  
            ServerSocket serverSocket = new ServerSocket(PORT);  
            while (true) {  
                // һ���ж���, ���ʾ��������ͻ��˻��������  
                Socket client = serverSocket.accept();  
                // �����������  
                new HandlerThread().addClient(client);;  
            }  
        } catch (Exception e) {  
            System.out.println("�������쳣: " + e.getMessage());  
        }  
    }  
  
    private class HandlerThread implements Runnable {  
    	
    	// �ͻ������ӵ�socket�б�
	    private ArrayList<Socket> clients = new ArrayList<Socket>();
	    
//        private Socket socket; 
        public HandlerThread() {   
            new Thread(this).start();  
        }  
//        public HandlerThread(Socket client) {  
//            socket = client;  
//            new Thread(this).start();  
//        }  
        
        // ��ӿͻ�
	    public void addClient(Socket socket) {
	        clients.add(socket);
	    }
	    // ɾ���ͻ�
	    public void removeClient(Socket socket) {
	        clients.remove(socket);
	    }
	    public  void timer3() {
	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	          public void run() {
	            System.out.println("-------�趨Ҫָ������--------");
	          }
	        }, 1000, 2000);
	      }
	       
  
        public void run() {  
        	while (true) {
        		for (Socket socket : clients) {
        			try {  
                        // ��ȡ�ͻ�������  
        				System.out.println(socket+"sssssssssss");
                        DataInputStream input = new DataInputStream(socket.getInputStream());
                        String clientInputStr = input.readUTF();//����Ҫע��Ϳͻ����������д������Ӧ,������� EOFException
                        // ����ͻ�������  
                        System.out.println("�ͻ��˷�����������:" + clientInputStr);  
                        if ("ok".equals(clientInputStr)) {
                        	System.out.println("�ͻ�������ok���Ƴ��˿ͻ������ӵ�socket");
        					this.removeClient(socket);
        					break;
        				}
          
                        // ��ͻ��˻ظ���Ϣ  
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());  
                        System.out.print("������:\t");  
                        // ���ͼ��������һ��  
                        String s = new BufferedReader(new InputStreamReader(System.in)).readLine();  
                        out.writeUTF(s);  
                        
                        out.close();  
                        input.close();  
                    } catch (IOException e) {
    	                e.printStackTrace();
    	                System.out.println("������IOException �쳣: " + e.getMessage()); 
    	            } catch (Exception e) {  
    	            	e.printStackTrace();
                        System.out.println("������ run �쳣: " + e.getMessage());  
                    } 
//        			finally {  
//                        if (socket != null) {  
//                            try {  
//                            	System.out.println("socket��Ϊ�գ��ر��׽���");
//                                socket.close();  
//                            } catch (Exception e) {  
//                                socket = null;  
//                                System.out.println("����� finally �쳣:" + e.getMessage());  
//                            }  
//                        }  
//                    } 
				}
        		
				
			}
            
        }  
    }  
} 
