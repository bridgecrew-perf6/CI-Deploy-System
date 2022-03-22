package com.deploymentsys.utils;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SocketIOUtils {
	private static final ExecutorService execService = Executors.newCachedThreadPool();

	/**
	 * 测试连接，相当于模拟telnet
	 * 
	 * @param ip
	 * @param port
	 * @return
	 * @throws IOException
	 */
	public static boolean testConnect(String ip, int port, int timeoutSeconds) {
		 
		//try {
			
			Future<Boolean> f1 = execService.submit(new Callable<Boolean>() {
				Socket s = null;
				@Override
				public Boolean call() throws Exception {
					try {
						s = new Socket(ip, port);
						return s.isConnected();
					} catch (IOException e) {
						return false;
					} catch (Exception e) {
						return false;
					} finally {
						if (s != null && !s.isClosed()) {
							s.close();
						}
					}
					
				}
				
			});
			
			
			try {
//		        if (f1.get(retry, TimeUnit.SECONDS)) { // future将在retry秒之后取结果
//		            System.out.println("one complete successfully");
//		        }
		        
		        return f1.get(timeoutSeconds, TimeUnit.SECONDS);
		    } catch (InterruptedException e) {
		        return false;
		    } catch (ExecutionException e) {
		    	return false;
		    } catch (TimeoutException e) {
		        f1.cancel(true);
		        return false;
		    } catch (Exception e) {
		        return false;
		    }
			
			
//			s = new Socket(ip, port);
//			boolean connectResult = false;
//			retry = retry > 0 ? retry : 10;
//			while (retry > 0 && !connectResult) {
//				connectResult = s.isConnected();
//				Thread.sleep(1000);
//				retry--;
//			}
//			return connectResult;
//		} catch (IOException e) {
//			return false;
//		} catch (Exception e) {
//			return false;
//		} finally {
//			if (s != null && !s.isClosed()) {
//				s.close();
//			}
//		}
	}

}
