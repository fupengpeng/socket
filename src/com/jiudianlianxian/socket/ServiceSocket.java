package com.jiudianlianxian.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServiceSocket {

	
	public static void main(String[] args) throws IOException {
        
        // ����ServerSocket�������˿ںţ�12345
        ServerSocket ss = new ServerSocket(12347);
        
         
        System.out.println("������������");
 
        // �����˿ںţ�12345
        // �ȴ��ͻ����� accept()
        while (true) {
        	// �������ڹ���ͻ��˵��շ����ݵ����߳���
            
        	
            // ��ʼ���տͻ��˵�����
            Socket socket = ss.accept();
            System.out.println("=���¿ͻ�����~");
            
            /** 
             * ���ǵķ���˴���ͻ��˵�����������ͬ�����еģ� ÿ�ν��յ����Կͻ��˵���������� 
             * ��Ҫ�ȸ���ǰ�Ŀͻ���ͨ����֮������ٴ�����һ���������� ���ڲ����Ƚ϶������»�����Ӱ���������ܣ� 
             * Ϊ�ˣ����ǿ��԰�����Ϊ���������첽������ͻ���ͨ�ŵķ�ʽ 
             */  
            // ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������  
            new ClientThread(socket).start();  
        }
    }
}
