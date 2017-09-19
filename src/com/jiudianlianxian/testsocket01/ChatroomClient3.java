package com.jiudianlianxian.testsocket01;


import java.io.BufferedReader;
import java.io.InputStreamReader;  
import java.io.OutputStreamWriter;  
import java.io.Writer;  
import java.net.Socket;  
  
/**
 * @Title: ChatroomClient
 * @Description: Socket�ͻ���  ģ�������ң�ʵ�ֶ�������     ����˵������Ϣ����
 * @Company: �����ŵ�������Ϣ�������޹�˾
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017��9��19�� ����2:55:37
 */
public class ChatroomClient3 extends Socket {  
  
    private static final String SERVER_IP = "127.0.0.1"; // �����IP  
    private static final int SERVER_PORT = 12372; // ����˶˿�  
    private static final String END_MARK = "quit"; // �˳������ұ�ʶ  
  
    private Socket client;  
  
    private Writer writer;  
  
    // ������Ϣ������  
    private BufferedReader in;  
  
    /** 
     * ���캯��<br/> 
     * ��������������� 
     * @throws Exception 
     */  
    public ChatroomClient3() throws Exception {  
        super(SERVER_IP, SERVER_PORT);  
        this.client = this;  
        System.out.println("Cliect[port:" + client.getLocalPort() + "] ���ѳɹ�����");  
    }  
    
    
    public void login(){
    	
    }
  
    /** 
     * ����������ȡ��Ϣ��ѭ�����Բ�ͣ��������Ϣ������Ϣ���ͳ�ȥ 
     * @throws Exception 
     */  
    public void load() throws Exception {  
        this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");  
        new Thread(new ReceiveMsgTask()).start(); // �������� 
  
        while(true) {  
            in = new BufferedReader(new InputStreamReader(System.in));  
            System.out.println("������Ϣ������");
            String inputMsg = in.readLine();  
            writer.write(inputMsg);  
            writer.write("\n");  
            writer.flush(); // д���Ҫ�ǵ�flush  
        }  
    }  
  
    /** 
     * ������������������Ϣ�߳��� 
     */  
    class ReceiveMsgTask implements Runnable {  
  
        private BufferedReader buff;  
  
        @Override  
        public void run() {  
            try {  
                this.buff = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));  
                while (true) {  
                    String result = buff.readLine();  
                    //��ȡ���������͹�������Ϣ
                    System.out.println("���յ����������͵���Ϣ�����д���");
                    if (END_MARK.equals(result)) { // �����˳���ʶʱ��ʾ����˷���ȷ���˳�  
                        System.out.println("Cliect[port:" + client.getLocalPort() + "] ���ѶϿ�����");  
                        break;  
                    } else { // �������˻ظ�����Ϣ  
                        System.out.println(result);  
                    }  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {  
                try {  
                    buff.close();  
                    // �ر�����  
                    writer.close();  
                    client.close();  
                    in.close();  
                } catch (Exception e) {  
  
                }  
            }  
        }  
    }  
  
    /** 
     * ��� 
     * @param args 
     */  
    public static void main(String[] args) {  
        try {  
            ChatroomClient3 client = new ChatroomClient3(); // �����ͻ���  
            client.load();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
}  