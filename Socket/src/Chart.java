import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 聊天室服务端
 * 支持多用户同时连接服务器并进行收发操作，用多线程处理
 * 注意：1个client的消息接收和转发是先后关系，故不需要使用两个线程处理
 * @author dj
 */

public class Chart {
	//使用一个容器储存登录的用户
	private static CopyOnWriteArrayList<Channel> all = new CopyOnWriteArrayList<Channel>();
	
	public static void main(String[] args) throws IOException {
		System.out.println("===server===");
		ServerSocket server = new ServerSocket(8866);
		
		while(true) {
			Socket client = server.accept();
			Channel c = new Channel(client);
			all.add(c);//管理所有的成员
			new Thread(c).start();
		}
	}
	
	static class Channel implements Runnable{
		private Socket client;
		private DataInputStream dis;
		private DataOutputStream dos;
		private boolean isRunning; 
		private String name;
		
		public Channel(Socket client) {
			this.client = client;
			this.isRunning = true;
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				//获取名称
				this.name = receive();
				System.out.println(this.name+"进入群聊");
				send("欢迎进入群聊");
				sendOthers("欢迎" +this.name+ "进入群聊",false);//系统消息
			} catch (IOException e) {
				closeAll();
			}
		}
		
		public void run() {
			while(isRunning) {
				String msg = receive();
				if(!msg.equals("")) {
					sendOthers(msg, true);//用户消息
					System.out.println(name+":"+msg);
				}
			}
		}
		
		private String receive() {
			String msg = "";
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				closeAll();
			}
			return msg;
		}
		
		private void send(String msg) {
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
				closeAll();
			}
		}
		//群聊，发的消息发给其他人
		private void sendOthers(String msg, boolean isSys) {
			for(Channel other:all) {
				if(other==this) {
					continue;
				}
				if(isSys) {
					other.send(this.name +":"+ msg);
				} else {
					other.send("系统："+msg);
				}
			}
		}
		
		private void closeAll() {
			this.isRunning = false;
			djUtils.close(dis,dos,client);
			all.remove(this);//从容器中移除
			sendOthers(this.name +"离开了群聊", false);//系统消息
			System.out.println(this.name+"离开了群聊");
		}
	}
}
