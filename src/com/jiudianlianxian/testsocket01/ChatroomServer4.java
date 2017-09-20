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
 * @Description: ������һ������
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��20�� ����9:21:36
 */
public class ChatroomServer4 extends ServerSocket {  
  
    private static final int SERVER_PORT = 12301; // ����˶˿�  
    private static final String END_MARK = "quit"; // �Ͽ����ӱ�ʶ  
    private static final String VIEW_USER = "viewuser"; // �鿴���ӿͻ���Ա�б�  
  
    private static List<String> portList = new CopyOnWriteArrayList<String>();  
    private static List<Task> threadList = new ArrayList<Task>(); // �������������̼߳���  
    private static Map<String, Task> threadMap = new HashMap<String,Task>(); //�������������̼߳���
    private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(500); // �����Ϣ�Ķ���  
  
    public ChatroomServer4() throws Exception {  
        super(SERVER_PORT);  
    }  
  
    /** 
     * ������ͻ��˷�����Ϣ���̣߳�ʹ���̴߳���ÿ���ͻ��˷�������Ϣ 
     *  
     * @throws Exception 
     */  
    public void load() throws Exception {  
        new Thread(new PushMsgTask()).start(); // ������ͻ��˷�����Ϣ���߳�  
  
        while (true) {  
            // server���Խ�������Socket����������server��accept����������ʽ��  
            Socket socket = this.accept();  
            /** 
             * ���ǵķ���˴���ͻ��˵�����������ͬ�����еģ� ÿ�ν��յ����Կͻ��˵���������� 
             * ��Ҫ�ȸ���ǰ�Ŀͻ���ͨ����֮������ٴ�����һ���������� ���ڲ����Ƚ϶������»�����Ӱ���������ܣ� 
             * Ϊ�ˣ����ǿ��԰�����Ϊ���������첽������ͻ���ͨ�ŵķ�ʽ 
             */  
            // ÿ���յ�һ��Socket�ͽ���һ���µ��߳���������  
            new Thread(new Task(socket)).start();  
        }  
    }  
  
    /** 
     * ����Ϣ������ȡ��Ϣ���ٷ��͸����������г�Ա 
     */  
    class PushMsgTask implements Runnable {  
  
        @Override  
        public void run() {  
            while (true) {  
                String msg = null;  
                try {  
                	System.out.println("001----��ȡ��Ϣ�����е���Ϣ");
                    msg = msgQueue.take();  
                } catch (InterruptedException e) {  
                    e.printStackTrace();  
                }  
                if (msg != null) { 
                	
                	//��Map�����е�ӳ���ϵȡ�������뵽Set�����С�
                    Set<Entry<String, Task>> entrySet = threadMap.entrySet();

                    Iterator<Entry<String, Task>> it = entrySet.iterator();

                    while(it.hasNext())
                    {
                        Entry<String, Task> me = it.next();
                        String key = me.getKey();
                        Task task = me.getValue();
                        //����map�����е���Ϣ
                        task.sendMsg(msg);
                        System.out.println(key+":"+task);

                    }
                    
                    for (Task thread : threadList) {  
                        thread.sendMsg(msg);  //�����ȡ������Ϣ��Ϊ�գ����ʹ���Ϣ
                    }  
                }  
            }  
        }  
  
    }  
    /** 
     * ����Ϣ������ȡ��Ϣ���ٷ��͸����������г�Ա 
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
                	
                	//��Map�����е�ӳ���ϵȡ�������뵽Set�����С�
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
     * ����ͻ��˷�������Ϣ�߳��� 
     */  
    class Task implements Runnable {  
  
        private Socket socket;  
  
        private BufferedReader buff;  
  
        private Writer writer;  
  
        private String port; // ���ӿͻ��˿ں�  
  
        /** 
         * ���캯��<br> 
         * ����ͻ��˵���Ϣ�����뵽���߳�Ա�б��� 
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
            pushMsg("��" + this.port + "���ӳɹ���");  
            System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                    + this.port + "���ӳɹ�");  
        }  
  
        @Override  
        public void run() {  
            try {  
                while (true) {  
                    String msg = buff.readLine();  //��ȡ���ͻ��˷��͹�������Ϣ
                    //�����ͻ��˷��͹�������Ϣ�������¼����ѯ���ݿ⣬�˺������Ƿ���ȷ����ȷ�����ؿͻ��˵�½�ɹ�����Ϣ
 
                    LoginRequestBean loginRequestBean = JSON.parseObject(msg,LoginRequestBean.class);
                    System.out.println(loginRequestBean.toString());
                    if ("login".equals(loginRequestBean.getTag())) {
						//����ǵ�¼�������ȡ����¼���û��������룬�������ݿ��ѯ���ݣ��Ƿ��û�����������ȡ�������ȷ���򷵻ظ��û���¼�ɹ�����ʾ
                    	System.out.println("ȷ���ǵ�¼���󣬵����ݿ��ѯ���ݣ���֤�û����������Ƿ���ȷ");
                    	pushMsg("ȷ���ǵ�¼���󣬵����ݿ��ѯ���ݣ���֤�û����������Ƿ���ȷ");
					}
                    if (VIEW_USER.equals(msg)) { // �鿴���ӳ�Ա�б�  
                        sendMsg(onlineUsers());  
                    } else if (END_MARK.equals(msg)) { // �����˳���ʶʱ�ͽ����ÿͻ����˳�  
                        sendMsg(END_MARK);  
                        break;  
                    } else {  
                        pushMsg(String.format("%1$shehe��%2$s", port, msg));  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally { // �ر���Դ���Ƴ����ӳ�Ա  
                try {  
                    writer.close();  
                    buff.close();  
                    socket.close();  
                } catch (Exception e) {  
  
                }  
                portList.remove(port);  
                
                threadMap.remove(this);
                threadList.remove(this);  
                pushMsg("��" + port + "�Ͽ����ӡ�");  
                System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                        + port + "�Ͽ�����");  
            }  
        }  
  
        /** 
         * ׼�����͵���Ϣ������� 
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
         * ������Ϣ 
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
         * �����ӳ�Ա�б� 
         *  
         * @return 
         */  
        private String onlineUsers() {  
            StringBuffer sbf = new StringBuffer();  
            sbf.append("======== ���ӳ�Ա��Ա�б�(").append(portList.size())  
                    .append(") ========\015\012");  
            for (int i = 0; i < portList.size(); i++) {  
                sbf.append("[" + portList.get(i) + "]\015\012");  
            }  
            sbf.append("===============================");  
            return sbf.toString();  
        }  
  
    }  
  
    /** 
     * ��� 
     *  
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            ChatroomServer4 server = new ChatroomServer4(); // ���������  
            server.load();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
} 
