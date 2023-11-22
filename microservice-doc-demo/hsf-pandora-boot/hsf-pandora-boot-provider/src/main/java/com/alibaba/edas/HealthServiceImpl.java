package com.alibaba.edas;

import com.alibaba.boot.hsf.annotation.HSFProvider;
import com.taobao.hsf.threadpool.ThreadPoolService;
import com.taobao.hsf.util.HSFServiceContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@HSFProvider(serviceInterface = HealthService.class, serviceVersion = "1.0.0") public class HealthServiceImpl
    implements HealthService {
    private String state = "UP";

    @Override public String state() {
        if (state.contains("exception")) {
            throw new RuntimeException(state);
        }
        return state;
    }

    @Override public String update(String state) {
        String oldState = this.state;
        this.state = state;
        return oldState;
    }

    @Override public String mockThreadPoolFull(int worktime) {
        long begin = System.currentTimeMillis();
        ThreadPoolService threadPoolService = HSFServiceContainer.getInstance(ThreadPoolService.class);
        ThreadPoolExecutor executor = threadPoolService.getExecutorManager().getDefaultExecutor();
        int i = 0;
        while (true) {
            i++;
            try {
                submitLongWork(worktime, executor);
            } catch (RejectedExecutionException e) {
                System.out.println("happened RejectedExecutionException");
                break;
            }
        }
        long payTime = (System.currentTimeMillis() - begin) / 1000;
        long remaining = (worktime - (System.currentTimeMillis() - begin)) / 1000;
        return "create " + i + " threads, Use " + payTime + " seconds to create a scene with full threads. After "
            + remaining + " seconds, the scene will be restored.";
    }

    private void submitLongWork(int worktime, ThreadPoolExecutor executor) {
        executor.submit(new Runnable() {
            @Override public void run() {
                try {
                    Thread.sleep(worktime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<byte[]> fullHeap = Collections.synchronizedList(new ArrayList<>());

    @Override public String mockOOM(int worktime) {
        long begin = System.currentTimeMillis();
        long expireTime = begin + worktime;
        Thread clearMemoryThread = new Thread(new Runnable() {
            @Override public void run() {
                clearHeap(expireTime);
            }
        });
        clearMemoryThread.start();
        long size = 0;
        while (true) {
            try {
                int kb = 256 * 1024;
                size += kb;
                fullHeap.add(new byte[kb]);
            } catch (OutOfMemoryError e) {
                System.out.println("happened OutOfMemoryError");
                break;
            }
        }
        long payTime = (System.currentTimeMillis() - begin) / 1000;
        long remaining = (worktime - (System.currentTimeMillis() - begin)) / 1000;
        return "create memory " +  size / 1024  +  "KB,Use " + payTime + " seconds to create a scene with OutOfMemoryError. After " + remaining
            + " seconds, the scene will be restored.";
    }

    private void clearHeap(long expireTime) {
        byte[] tmp = null;
        while (System.currentTimeMillis() < expireTime) {
            try {
                try {
                    tmp = new byte[1024 * 1024];
                }catch (OutOfMemoryError e){
                }
                tmp = null;
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fullHeap.clear();
    }
}
