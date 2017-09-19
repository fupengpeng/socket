package com.jiudianlianxian.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServiceSocket {

	
	public static void main(String[] args) throws IOException {
        
        // 创建ServerSocket，监听端口号：12345
        ServerSocket ss = new ServerSocket(12347);
        
         
        System.out.println("服务器开启啦");
 
        // 监听端口号：12345
        // 等待客户连接 accept()
        while (true) {
        	// 创建用于管理客户端的收发数据的子线程类
            
        	
            // 开始接收客户端的连接
            Socket socket = ss.accept();
            System.out.println("=有新客户连接~");
            
            /** 
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后， 
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能， 
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式 
             */  
            // 每接收到一个Socket就建立一个新的线程来处理它  
            new ClientThread(socket).start();  
        }
    }
}
