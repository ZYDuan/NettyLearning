/**
 * 
 */
package bio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zyd
 * @date 2019年1月16日 下午2:49:27
 * @ClassName: TimeServerHandlerExecutePool
 */
public class TimeServerHandlerExecutePool {
	private ExecutorService executor;

	public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
		this.executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(), maxPoolSize, 120L,
				TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}
}
