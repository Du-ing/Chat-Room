import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 使用多线程封装接收端
 * @author dj
 */

public class ReceiveThread implements Runnable {
	
	private DataInputStream dis;
	private Socket client;
	private boolean isRunning;
	//构造器
	public ReceiveThread(Socket client) {
		this.client = client;
		this.isRunning = true;
		try {
			dis = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			closeAll();
		}
	}
	//线程体
	public void run() {
		while(isRunning) {
			String msg = receive();
			if(!msg.equals("")) {
				System.out.println(msg);
			}
		}
	}
	//接收从服务器传来的字符串
	private String receive() {
		String msg = "";
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
			closeAll();
		}
		return msg;
	}
	//释放所有资源
	private void closeAll() {
		this.isRunning = false;
		djUtils.close(dis,client);
	}
}
