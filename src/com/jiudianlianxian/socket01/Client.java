package com.jiudianlianxian.socket01;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @Title: Client
 * @Description: �ͻ���
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��18�� ����2:29:36
 */
public class Client {  
	   
    /** 
     * ��������˷��صĶ��󣬿�ʵ�ָýӿڡ� 
     */ 
    public static interface ObjectAction {
        void doAction(Object obj,Client client);  
    }  
    public static final class DefaultObjectAction implements ObjectAction {
        public void doAction(Object obj,Client client) {  
            System.out.println("������\t"+obj.toString());  
        }  
    }
       
    private String serverIp;  
    private int port;  
    private Socket socket;  
    private boolean running=false;  
    private long lastSendTime;  
    private ConcurrentHashMap<Class, ObjectAction> actionMapping = new ConcurrentHashMap<Class,ObjectAction>();  
       
    public Client(String serverIp, int port) {  
        this.serverIp=serverIp;this.port=port;  
    }  
       
    public void start() throws UnknownHostException, IOException {  
        if(running)return;  
        socket = new Socket(serverIp,port);  
        System.out.println("���ض˿ڣ�"+socket.getLocalPort());  
        lastSendTime=System.currentTimeMillis();  
        running=true;  
        new Thread(new KeepAliveWatchDog()).start();  
        new Thread(new ReceiveWatchDog()).start();  
    }  
       
    public void stop(){  
        if(running)running=false;  
    }  
       
    /** 
     * ���ӽ��ն���Ĵ������� 
     * @param cls �������Ķ������������ࡣ 
     * @param action �������̶��� 
     */ 
    public void addActionMap(Class<Object> cls,ObjectAction action){  
        actionMapping.put(cls, action);  
    }  
   
    public void sendObject(Object obj) throws IOException {  
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  
        oos.writeObject(obj);  
        System.out.println("���ͣ�\t"+obj);  
        oos.flush();  
    }  
       
    class KeepAliveWatchDog implements Runnable {
        long checkDelay = 10;  
        long keepAliveDelay = 2000;  
        public void run() {  
            while(running){  
                if(System.currentTimeMillis()-lastSendTime>keepAliveDelay){  
                    try {  
                        Client.this.sendObject(new KeepAlive());  
                    } catch (IOException e) {  
                        e.printStackTrace();  
                        Client.this.stop();  
                    }  
                    lastSendTime = System.currentTimeMillis();  
                }else{  
                    try {  
                        Thread.sleep(checkDelay);  
                    } catch (InterruptedException e) {  
                        e.printStackTrace();  
                        Client.this.stop();  
                    }  
                }  
            }  
        }  
    }  
       
    class ReceiveWatchDog implements Runnable {
        public void run() {  
            while(running){  
                try {  
                    InputStream in = socket.getInputStream();  
                    if(in.available()>0){  
                        ObjectInputStream ois = new ObjectInputStream(in);  
                        Object obj = ois.readObject();  
                        System.out.println("���գ�\t"+obj);  
                        ObjectAction oa = actionMapping.get(obj.getClass());  
                        oa = oa==null?new DefaultObjectAction():oa;  
                        oa.doAction(obj, Client.this);  
                    }else{  
                        Thread.sleep(10);  
                    }  
                } catch (Exception e) {  
                    e.printStackTrace();  
                    Client.this.stop();  
                }   
            }  
        }  
    }
     
    public static void main(String[] args) throws UnknownHostException, IOException {  
        String serverIp = "127.0.0.1";  
        int port = 65432;  
        Client client = new Client(serverIp,port);  
        client.start();  
    } 
}  