import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 聊天室用户端
 * 发送和接收同时进行，用两个线程解决
 * @author dj
 */

public class ChartClient {
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("===client===");
		//读取输入的用户名
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("请输入用户名：");
		String name = br.readLine();
		
		Socket client = new Socket("localhost",8866);
		
		new Thread(new SendThread(client, name)).start();
		new Thread(new ReceiveThread(client)).start();
		//client.close();此句千万不能加！！！会导致客户端什么都没做就直接关闭了
	}
}
