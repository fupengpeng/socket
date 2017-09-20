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

	private static final String SERVER_IP = "127.0.0.1"; // 服务端IP
	private static final int SERVER_PORT = 8990; // 服务端端口
	private static final String END_MARK = "quit"; // 断开连接标识

	private Socket client;

	private Writer writer;

	// 发送消息输入流
	private BufferedReader in;

	/**
	 * 构造函数<br/>
	 * 与服务器建立连接
	 * 
	 * @throws Exception
	 */
	public ChatroomClient() throws Exception {
		super(SERVER_IP, SERVER_PORT);
		this.client = this;
		System.out.println("Cliect[port:" + client.getLocalPort() + "] 您已进入聊天室");
	}

	/**
	 * 启动监听收取消息，循环可以不停的输入消息，将消息发送出去
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
		this.writer = new OutputStreamWriter(this.getOutputStream(), "UTF-8");
		new Thread(new ReceiveMsgTask()).start(); // 启动监听

		// while(true) {
		// in = new BufferedReader(new InputStreamReader(System.in));
		// String inputMsg = in.readLine();
		
		if ("login".equals("login")) {    //登录请求
			writer.write(login());
		}else if ("register".equals("register")){    //注册请求
			writer.write(register()); 
		}else {
			
		}
		
		
		writer.write("\n");
		writer.flush(); // 写完后要记得flush
		// }
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

					System.out.println("result = " + result);
					
					

					if (END_MARK.equals(result)) { // 遇到退出标识时表示服务端返回确认退出
						System.out.println("Cliect[port:" + client.getLocalPort() + "] 您已断开连接");
						break;
					} else { // 输出服务端回复的消息
						
						switch (result) {
						case "dengluchenggong":
							System.out.println(result);
							break;
                        case "zhucechenggong":
							
							break;

						default:
							System.out.println(result);
							System.out.println("密码错误，或其他请求");
							break;
						}
						
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
	 * 
	 * Description: 登录请求
	 * @return
	 */
	private String login() {
		LoginRequestBean loginRequestBean = new LoginRequestBean("login", "username", "password");
		String jsonObject = JSONObject.toJSONString(loginRequestBean);
		return jsonObject;
	}
	/**
	 * 
	 * Description: 注册请求
	 * @return
	 */
	private String register() {
		LoginRequestBean loginRequestBean = new LoginRequestBean("login", "username", "password");
		String jsonObject = JSONObject.toJSONString(loginRequestBean);
		return jsonObject;
	}

	/**
	 * 入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ChatroomClient client = new ChatroomClient(); // 启动客户端
			client.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}