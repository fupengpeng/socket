package com.jiudianlianxian.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;


/**
 * 
 * @Title: ClientSocket
 * @Description: 客户端
 * @Company: 济宁九点连线信息技术有限公司
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017年9月18日 下午3:23:37
 */
public class ClientSocket extends Socket{
	
    private Socket client;  
    
    private Writer writer;  
	
    /** 
     * 入口 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
        	ClientSocket client = new ClientSocket(); // 启动客户端  
             
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
 
	
}
