package com.jiudianlianxian.socket03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.fastjson.JSON;
import com.jiudianlianxian.bean.LoginRequestBean;

public class ChatroomServer extends ServerSocket {  
	  
    private static final int SERVER_PORT = 8990; // ����˶˿�  
    private static final String END_MARK = "quit"; // �Ͽ����ӱ�ʶ  
    private static final String VIEW_USER = "viewuser"; // �鿴���ӿͻ����б�  
  
    private static List<String> userList = new CopyOnWriteArrayList<String>();  
    private static List<Task> threadList = new ArrayList<Task>(); // �������������̼߳���  
    private static BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(  
            20); // �����Ϣ�Ķ���  
  
    public ChatroomServer() throws Exception {  
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
     * ����Ϣ������ȡ��Ϣ���ٷ��͸������ӵ����пͻ��˳�Ա 
     */  
    class PushMsgTask implements Runnable {  
  
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
  
        private String userName; // ��Ա����  
  
        /** 
         * ���캯��<br> 
         * ����ͻ��˵���Ϣ�����뵽���߳�Ա�б��� 
         *  
         * @throws Exception 
         */  
        public Task(Socket socket) {  
            this.socket = socket;  
            this.userName = String.valueOf(socket.getPort());  
            try {  
                this.buff = new BufferedReader(new InputStreamReader(  
                        socket.getInputStream(), "UTF-8"));  
                this.writer = new OutputStreamWriter(socket.getOutputStream(),  
                        "UTF-8");  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            userList.add(this.userName);  
            threadList.add(this);  
            pushMsg("��" + this.userName + "�����ӳɹ���");  
            System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                    + this.userName + "�ѽ��뷿����Կ�ʼ��Ϸ");  
        }  
  
        @Override  
        public void run() {  
            try {  
                while (true) {  
                    String msg = buff.readLine();  
                    
                    //�����ͻ��˷��͹�������Ϣ�������¼����ѯ���ݿ⣬�˺������Ƿ���ȷ����ȷ�����ؿͻ��˵�½�ɹ�����Ϣ
                    LoginRequestBean loginRequestBean = JSON.parseObject(msg,LoginRequestBean.class);
                    System.out.println(loginRequestBean.toString());
                    if ("login".equals(loginRequestBean.getTag())) {
        				//����ǵ�¼�������ȡ����¼���û��������룬�������ݿ��ѯ���ݣ��Ƿ��û�����������ȡ�������ȷ���򷵻ظ��û���¼�ɹ�����ʾ
                    	System.out.println("ȷ���ǵ�¼���󣬵����ݿ��ѯ���ݣ���֤�û����������Ƿ���ȷ");
                    	if ("usernam".equals(loginRequestBean.getUsername()) && "password".equals(loginRequestBean.getPassword())) {
        					System.out.println("��ѯ���ݿ����֤�û�����������ȷ�������¼�����ص�¼�ɹ���Ϣ");
        					String msg01 = "dengluchenggong";
        					pushMsg(msg01);
        				}else {
        					String msg02 = "denglushibai";
        					pushMsg(msg02);
        				}
        			}else {
        				String msg03 = "feidengluqingqiu";
                    	pushMsg(msg03);
					}
                    
                    
                    
                    if (VIEW_USER.equals(msg)) { // �鿴�����ӿͻ��� 
                        sendMsg(onlineUsers());  
                    } else if (END_MARK.equals(msg)) { // �����˳���ʶʱ�ͽ����ÿͻ����˳�  
                        sendMsg(END_MARK);  
                        break;  
                    } else {  
//                        pushMsg(String.format("%1$s˵��%2$s", userName, msg)); // ���ڸ��������û�����������Ϣ  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally { // �ر���Դ�������Ƴ����пͻ��˳�Ա  
                try {  
                    writer.close();  
                    buff.close();  
                    socket.close();  
                } catch (Exception e) {  
  
                }  
                userList.remove(userName);  
                threadList.remove(this);  
                pushMsg("��" + userName + "�Ͽ����ӡ�");  
                System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                        + userName + "�Ͽ�����");  
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
         * ���������߳�Ա�б� 
         *  
         * @return 
         */  
        private String onlineUsers() {  
            StringBuffer sbf = new StringBuffer();  
            sbf.append("======== �����ӿͻ����б�(").append(userList.size())  
                    .append(") ========\015\012");  
            for (int i = 0; i < userList.size(); i++) {  
                sbf.append("[" + userList.get(i) + "]\015\012");  
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
            ChatroomServer server = new ChatroomServer(); // ���������  
            server.load();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  
