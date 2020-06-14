/**
 * 使用多线程封装发送端
 * @author dj
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SendThread implements Runnable {
	
	private BufferedReader br;
	private DataOutputStream dos;
	private Socket client;
	private boolean isRunning;
	private String name;
	//构造器
	public SendThread(Socket client, String name) {
		this.client = client;
		this.isRunning = true;
		this.name = name;
		br = new BufferedReader(new InputStreamReader(System.in));
		try {
			dos = new DataOutputStream(client.getOutputStream());
			//发送名称
			send(name);
		} catch (IOException e) {
			closeAll();
		}
	}
	//线程体
	public void run() {
		while(isRunning) {
			String msg = getStrFromBr();
			if(!msg.equals("")) {
				send(msg);
			}
		}
	}
	//从控制台获得字符串
	private String getStrFromBr() {
		try {
			return br.readLine();
		} catch (IOException e) {
			closeAll();
		}
		return "";
	}
	//发送字符串
	private void send(String msg) {
		try {
			dos.writeUTF(msg);
			dos.flush();
		} catch (IOException e) {
			closeAll();
		}
	}
	//释放所有资源
	private void closeAll() {
		this.isRunning = false;
		djUtils.close(br,dos,client);
	}
}
