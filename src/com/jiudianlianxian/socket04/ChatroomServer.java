package com.jiudianlianxian.socket04;

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

public class ChatroomServer extends ServerSocket {  
	  
    private static final int SERVER_PORT = 8899; // ����˶˿�  
    private static final String END_MARK = "quit"; // �˳������ұ�ʶ  
    private static final String VIEW_USER = "viewuser"; // �鿴���߳�Ա�б�  
  
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
     * ����Ϣ������ȡ��Ϣ���ٷ��͸����������г�Ա 
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
            pushMsg("��" + this.userName + "�����������ҡ�");  
            System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                    + this.userName + "������������");  
        }  
  
        @Override  
        public void run() {  
            try {  
                while (true) {  
                    String msg = buff.readLine();  
  
                    if (VIEW_USER.equals(msg)) { // �鿴���������߳�Ա  
                        sendMsg(onlineUsers());  
                    } else if (END_MARK.equals(msg)) { // �����˳���ʶʱ�ͽ����ÿͻ����˳�  
                        sendMsg(END_MARK);  
                        break;  
                    } else {  
                        pushMsg(String.format("%1$s˵��%2$s", userName, msg));  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally { // �ر���Դ���������Ƴ���Ա  
                try {  
                    writer.close();  
                    buff.close();  
                    socket.close();  
                } catch (Exception e) {  
  
                }  
                userList.remove(userName);  
                threadList.remove(this);  
                pushMsg("��" + userName + "�˳��������ҡ�");  
                System.out.println("Form Cliect[port:" + socket.getPort() + "] "  
                        + userName + "�˳���������");  
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
            sbf.append("======== ���߳�Ա�б�(").append(userList.size())  
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
