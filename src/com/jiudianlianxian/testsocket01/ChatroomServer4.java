package com.jiudianlianxian.testsocket01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.security.auth.login.LoginContext;

import com.alibaba.fastjson.JSON;
import com.jiudianlianxian.bean.LoginRequestBean;

/**
 * 
 * @Title: ChatroomServer4
 * @Description: 给此类一个描述
 * @Company: 济宁九点连线信息技术有限公司
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017年9月20日 上午9:21:36
 */
public class ChatroomServer4 extends ServerSocket {  
  
    private static final int SERVER_PORT = 12301; // 服务端端口  
    private static final String END_MARK = "quit"; // 断开连接标识  
    private static final String VIEW_USER = "viewuser"; // 查看连接客户成员列表  
  
    private static List<String> portList = new CopyOnWriteArrayList<String>();  
    private static List<Task> threadList = new ArrayList<Task>(); // 服务器已启用线程集合  
    private static Map<String, Task> threadMap = new HashMap<String,Task>(); //服务器已启用线程集合
    private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(500); // 存放消息的队列  
  
    public ChatroomServer4() throws Exception {  
        super(SERVER_PORT);  
    }  
  
    /** 
     * 启动向客户端发送消息的线程，使用线程处理每个客户端发来的消息 
     *  
     * @throws Exception 
     */  
    public void load() throws Exception {  
        new Thread(new PushMsgTask()).start(); // 开启向客户端发送消息的线程  
  
        while (true) {  
            // server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的  
            Socket socket = this.accept();  
            /** 
             * 我们的服务端处理客户端的连接请求是同步进行的， 每次接收到来自客户端的连接请求后， 
             * 都要先跟当前的客户端通信完之后才能再处理下一个连接请求。 这在并发比较多的情况下会严重影响程序的性能， 
             * 为此，我们可以把它改为如下这种异步处理与客户端通信的方式 
             */  
            // 每接收到一个Socket就建立一个新的线程来处理它  
            new Thread(new Task(socket)).start();  
        }  
    }  
  
    /** 
     * 从消息队列中取消息，再发送给聊天室所有成员 
     */  
    class PushMsgTask implements Runnable {  
  
        @Override  
        public void run() {  
            while (true) {  
                String msg = null;  
                try {  
                	System.out.println("001----获取消息队列中的消息");
                    msg = msgQueue.take();  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                if (msg != null) { 
                	
                	//将Map集合中的映射关系取出。存入到Set集合中。
                    Set<Entry<String, Task>> entrySet = threadMap.entrySet();

                    Iterator<Entry<String, Task>> it = entrySet.iterator();

                    while(it.hasNext())
                    {
                        Entry<String, Task> me = it.next();
                        String key = me.getKey();
                        Task task = me.getValue();
                        //发送map集合中的消息
                        task.sendMsg(msg);
                        System.out.println(key+":"+task);

                    }
                    
                    for (Task thread : threadList) {  
                        thread.sendMsg(msg);  //如果获取到的消息不为空，则发送此消息
                    }  
                }  
            }  
        }  
  
    }  
    /** 
     * 从消息队列中取消息，再发送给聊天室所有成员 
     */  
    class PushMsgTaskLogin implements Runnable {  
  
        @Override  
        public void run() {  
            while (true) {  
                String msg = null;  
                try {  
                    msg = msgQueue.take();  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                if (msg != null) { 
                	
                	//将Map集合中的映射关系取出。存入到Set集合中。
                    Set<Entry<String, Task>> entrySet = threadMap.entrySet();

                    Iterator<Entry<String, Task>> it = entrySet.iterator();

                    while(it.hasNext())
                    {
                        Entry<String, Task> me = it.next();
                        String key = me.getKey();
                        Task task = me.getValue();
                        task.sendMsg(msg);
                        System.out.println(key+":"+task);

                    }
                    
                    for (Task thread : threadList) {  
                        thread.sendMsg(msg);  
                    }  
                    
                }  
            }  
        }  
  
    } 
  
    /** 
     * 处理客户端发来的消息线程类 
     */  
    class Task implements Runnable {  
  
        private Socket socket;  
  
        private BufferedReader buff;  
  
        private Writer writer;  
  
        private String port; // 连接客户端口号  
  
        /** 
         * 构造函数<br> 
         * 处理客户端的消息，加入到在线成员列表中 
         *  
         * @throws Exception 
         */  
        public Task(Socket socket) {  
            this.socket = socket;  
            this.port = String.valueOf(socket.getPort());  
            try {  
                this.buff = new BufferedReader(new InputStreamReader(  
                        socket.getInputStream(), "UTF-8"));  
                this.writer = new OutputStreamWriter(socket.getOutputStream(),  
                        "UTF-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            portList.add(this.port);  
            
            
            threadMap.put("login", this);
            threadList.add(this);  
            pushMsg("【" + this.port + "连接成功】");  
            System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                    + this.port + "连接成功");  
        }  
  
        @Override  
        public void run() {  
            try {  
                while (true) {  
                    String msg = buff.readLine();  //读取到客户端发送过来的消息
                    //解析客户端发送过来的消息，例如登录，查询数据库，账号密码是否正确，正确，返回客户端登陆成功的信息
 
                    LoginRequestBean loginRequestBean = JSON.parseObject(msg,LoginRequestBean.class);
                    System.out.println(loginRequestBean.toString());
                    if ("login".equals(loginRequestBean.getTag())) {
						//如果是登录请求，则获取到登录的用户名和密码，进行数据库查询数据，是否用户名和密码争取，如果正确，则返回个用户登录成功的提示
                    	System.out.println("确认是登录请求，到数据库查询数据，验证用户名和密码是否正确");
                    	pushMsg("确认是登录请求，到数据库查询数据，验证用户名和密码是否正确");
					}
                    if (VIEW_USER.equals(msg)) { // 查看连接成员列表  
                        sendMsg(onlineUsers());  
                    } else if (END_MARK.equals(msg)) { // 遇到退出标识时就结束让客户端退出  
                        sendMsg(END_MARK);  
                        break;  
                    } else {  
                        pushMsg(String.format("%1$shehe：%2$s", port, msg));  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally { // 关闭资源，移除连接成员  
                try {  
                    writer.close();  
                    buff.close();  
                    socket.close();  
                } catch (Exception e) {  
  
                }  
                portList.remove(port);  
                
                threadMap.remove(this);
                threadList.remove(this);  
                pushMsg("【" + port + "断开连接】");  
                System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                        + port + "断开连接");  
            }  
        }  
  
        /** 
         * 准备发送的消息存入队列 
         *  
         * @param msg 
         */  
        private void pushMsg(String msg) {  
            try {  
                msgQueue.put(msg);  
            } catch (InterruptedException e) {  
                e.printStackTrace();  
            }  
        }  
  
        /** 
         * 发送消息 
         *  
         * @param msg 
         */  
        private void sendMsg(String msg) {  
            try {  
                writer.write(msg);  
                writer.write("\015\012");  
                writer.flush();  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
  
        /** 
         * 已连接成员列表 
         *  
         * @return 
         */  
        private String onlineUsers() {  
            StringBuffer sbf = new StringBuffer();  
            sbf.append("======== 连接成员成员列表(").append(portList.size())  
                    .append(") ========\015\012");  
            for (int i = 0; i < portList.size(); i++) {  
                sbf.append("[" + portList.get(i) + "]\015\012");  
            }  
            sbf.append("===============================");  
            return sbf.toString();  
        }  
  
    }  
  
    /** 
     * 入口 
     *  
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            ChatroomServer4 server = new ChatroomServer4(); // 启动服务端  
            server.load();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
} 
