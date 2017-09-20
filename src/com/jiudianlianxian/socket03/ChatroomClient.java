package com.jiudianlianxian.socket03;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;

import javax.imageio.spi.RegisterableService;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.alibaba.fastjson.JSONObject;
import com.jiudianlianxian.bean.LoginRequestBean;

public class ChatroomClient extends Socket {

	private static final String SERVER_IP = "127.0.0.1"; // �����IP
	private static final int SERVER_PORT = 8990; // ����˶˿�
	private static final String END_MARK = "quit"; // �Ͽ����ӱ�ʶ

	private Socket client;

	private Writer writer;

	// ������Ϣ������
	private BufferedReader in;

	/**
	 * ���캯��<br/>
	 * ���������������
	 * 
	 * @throws Exception
	 */
	public ChatroomClient() throws Exception {
		super(SERVER_IP, SERVER_PORT);
		this.client = this;
		System.out.println("Cliect[port:" + client.getLocalPort() + "] ���ѽ���������");
	}

	/**
	 * ����������ȡ��Ϣ��ѭ�����Բ�ͣ��������Ϣ������Ϣ���ͳ�ȥ
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");
		new Thread(new ReceiveMsgTask()).start(); // ��������

		// while(true) {
		// in = new BufferedReader(new InputStreamReader(System.in));
		// String inputMsg = in.readLine();
		
		if ("login".equals("login")) {    //��¼����
			writer.write(login());
		}else if ("register".equals("register")){    //ע������
			writer.write(register()); 
		}else {
			
		}
		
		
		writer.write("\n");
		writer.flush(); // д���Ҫ�ǵ�flush
		// }
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

					System.out.println("result = " + result);
					
					

					if (END_MARK.equals(result)) { // �����˳���ʶʱ��ʾ����˷���ȷ���˳�
						System.out.println("Cliect[port:" + client.getLocalPort() + "] ���ѶϿ�����");
						break;
					} else { // �������˻ظ�����Ϣ
						
						switch (result) {
						case "dengluchenggong":
							System.out.println(result);
							break;
                        case "zhucechenggong":
							
							break;

						default:
							System.out.println(result);
							System.out.println("������󣬻���������");
							break;
						}
						
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
	 * 
	 * Description: ��¼����
	 * @return
	 */
	private String login() {
		LoginRequestBean loginRequestBean = new LoginRequestBean("login", "username", "password");
		String jsonObject = JSONObject.toJSONString(loginRequestBean);
		return jsonObject;
	}
	/**
	 * 
	 * Description: ע������
	 * @return
	 */
	private String register() {
		LoginRequestBean loginRequestBean = new LoginRequestBean("login", "username", "password");
		String jsonObject = JSONObject.toJSONString(loginRequestBean);
		return jsonObject;
	}

	/**
	 * ���
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChatroomClient client = new ChatroomClient(); // �����ͻ���
			client.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}