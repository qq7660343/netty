package cloudy.keepAlive.netty;

import cloudy.keepAlive.entity.Request;
import cloudy.keepAlive.entity.Response;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 7cc on 2017/9/2
 */
public class ConcurrentResponse {

    static {
        Thread mainThread = new Thread(() -> {
            while(true) {
                Set<Long> ids = ConcurrentResponse.FUTURES.keySet();
                if(ids == null || ids.isEmpty()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                ids.forEach(id -> {
                    ConcurrentResponse concurrentResponse = ConcurrentResponse.FUTURES.get(id);
                    if(concurrentResponse == null) {
                        ConcurrentResponse.FUTURES.remove(id);
                    } else {
                        setTimeOutResponse(concurrentResponse);
                    }
                });
            }
        });
        mainThread.setDaemon(true);
        mainThread.start();
    }

    private static void setTimeOutResponse(ConcurrentResponse concurrentResponse) {
        if(concurrentResponse.timeOut > 0) {
            if((System.currentTimeMillis() - concurrentResponse.startTime) > concurrentResponse.timeOut) {
                Response res = new Response();
                res.setContent(null);
                res.setMsg("请求超时！");
                res.setStatus(1);//响应异常处理
                res.setId(concurrentResponse.id);
                ConcurrentResponse.receive(res);
            }
        }
    }


    private long id;
    private volatile Response response;
    public final static Map<Long, ConcurrentResponse> FUTURES = new ConcurrentHashMap<>();
    private long timeOut;
    private final long startTime = System.currentTimeMillis();
    private volatile Lock lock = new ReentrantLock();
    private volatile Condition condition = lock.newCondition();

    public ConcurrentResponse() {   }

    public ConcurrentResponse(Request request) {
        id = request.getId();
        FUTURES.put(id, this);
    }

    public Response get(long failureTime) {
        timeOut = failureTime;
        lock.lock();
        while (!hasResponse()) {
            try {
                condition.await(failureTime, TimeUnit.SECONDS);
                if (System.currentTimeMillis() - startTime > failureTime) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return response;
    }

    public static void receive(Response response) {
        ConcurrentResponse future = FUTURES.remove(response.getId());
        if(future == null) {
            return;
        }
        Lock lock = future.lock;
        lock.lock();
        try {
            future.response = response;
            Condition condition = future.condition;
            if(condition != null) {
                condition.signal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private boolean hasResponse() {
        return response != null ? true : false;
    }

}
