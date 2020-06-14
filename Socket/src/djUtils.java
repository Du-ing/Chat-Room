import java.io.Closeable;

/**
 * 工具类
 * 释放所有资源
 * @author dj
 */

public class djUtils {
	public static void close(Closeable... targets) {
		for(Closeable target:targets) {
			try {
				if(target!=null) {
					target.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
