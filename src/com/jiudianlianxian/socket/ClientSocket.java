package com.jiudianlianxian.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;


/**
 * 
 * @Title: ClientSocket
 * @Description: �ͻ���
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��18�� ����3:23:37
 */
public class ClientSocket extends Socket{
	
    private Socket client;  
    
    private Writer writer;  
	
    /** 
     * ��� 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
        	ClientSocket client = new ClientSocket(); // �����ͻ���  
             
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
 
	
}
