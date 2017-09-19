package com.jiudianlianxian.testsocket01;


import java.io.BufferedReader;
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.io.Writer;  
import java.net.Socket;  
  
/**
 * @Title: ChatroomClient
 * @Description: Socket客户端  模拟聊天室，实现多人聊天     功能说明：信息共享
 * @Company: 济宁九点连线信息技术有限公司
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017年9月19日 下午2:55:37
 */
public class ChatroomClient3 extends Socket {  
  
    private static final String SERVER_IP = "127.0.0.1"; // 服务端IP  
    private static final int SERVER_PORT = 12372; // 服务端端口  
    private static final String END_MARK = "quit"; // 退出聊天室标识  
  
    private Socket client;  
  
    private Writer writer;  
  
    // 发送消息输入流  
    private BufferedReader in;  
  
    /** 
     * 构造函数<br/> 
     * 与服务器建立连接 
     * @throws Exception 
     */  
    public ChatroomClient3() throws Exception {  
        super(SERVER_IP, SERVER_PORT);  
        this.client = this;  
        System.out.println("Cliect[port:" + client.getLocalPort() + "] 您已成功连接");  
    }  
    
    
    public void login(){
    	
    }
  
    /** 
     * 启动监听收取消息，循环可以不停的输入消息，将消息发送出去 
     * @throws Exception 
     */  
    public void load() throws Exception {  
        this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");  
        new Thread(new ReceiveMsgTask()).start(); // 启动监听 
  
        while(true) {  
            in = new BufferedReader(new InputStreamReader(System.in));  
            System.out.println("输入消息并发送");
            String inputMsg = in.readLine();  
            writer.write(inputMsg);  
            writer.write("\n");  
            writer.flush(); // 写完后要记得flush  
        }  
    }  
  
    /** 
     * 监听服务器发来的消息线程类 
     */  
    class ReceiveMsgTask implements Runnable {  
  
        private BufferedReader buff;  
  
        @Override  
        public void run() {  
            try {  
                this.buff = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));  
                while (true) {  
                    String result = buff.readLine();  
                    //读取服务器发送过来的消息
                    System.out.println("接收到服务器发送的消息，进行处理");
                    if (END_MARK.equals(result)) { // 遇到退出标识时表示服务端返回确认退出  
                        System.out.println("Cliect[port:" + client.getLocalPort() + "] 您已断开连接");  
                        break;  
                    } else { // 输出服务端回复的消息  
                        System.out.println(result);  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    buff.close();  
                    // 关闭连接  
                    writer.close();  
                    client.close();  
                    in.close();  
                } catch (Exception e) {  
  
                }  
            }  
        }  
    }  
  
    /** 
     * 入口 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            ChatroomClient3 client = new ChatroomClient3(); // 启动客户端  
            client.load();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  