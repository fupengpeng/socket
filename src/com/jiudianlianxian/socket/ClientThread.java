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
	
	    // �ͻ������ӵ�socket�б�
	    private ArrayList<Socket> clients = new ArrayList<Socket>();
	 
	    // ��ӿͻ�
	    public void addClient(Socket socket) {
	        clients.add(socket);
	    }
	    // ɾ���ͻ�
	    public void removeClient(Socket socket) {
	        clients.remove(socket);
	    }
	    // ��ͻ���������
	    public void sendMessage(Socket socket, String data) throws IOException {
	        // ����ҷ�������
	        OutputStream os = socket.getOutputStream();
	        os.write(data.getBytes("UTF-8"));
	    }
	    //��¼
	    
	    //ע��
	    
	    //
	 
	    @Override
	    public void run() {
	        while (true) {
	            try {
	                for (Socket socket : clients) {
	                    // ��ȡ�ͻ��˷���������
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
	                    	  System.out.println("�пͻ��Ͽ�����~");
	                    	  this.removeClient(socket);
	                    	  break;
	                    	}
	                    
	                    // ������յ�������
	                    String read = new String(buff);
	                    System.out.println("�յ����ݣ�" + read);
	 
	                    // ����ҷ�������
	                    String data = "��ϲ�㣬���ӳɹ���~~";
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
