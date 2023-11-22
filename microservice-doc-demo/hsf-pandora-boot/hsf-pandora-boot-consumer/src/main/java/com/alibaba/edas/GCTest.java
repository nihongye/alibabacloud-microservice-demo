package com.alibaba.edas;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GCTest {
    private static List<byte[]> fullHeap = Collections.synchronizedList(new ArrayList<>());
    private static byte[] bytes;

    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                run0();
            }
        });
        thread.start();
        long size = 0;
        while (true) {
            try {
                int kb = 1024 * 1024;
                size += kb;
                fullHeap.add(new byte[kb]);
            } catch (OutOfMemoryError e) {
                System.out.println("happened OutOfMemoryError");
                trigGC();
                break;
            }
        }
        System.out.println("size=" + size / 1024 / 1024);
    }

    private static void trigGC() {
        Thread thread = new Thread(new Runnable() {
            @Override public void run() {
                while(true) {
                    try {
                        bytes = new byte[1024 * 1024];
                    }catch (OutOfMemoryError e) {

                    }
                }
            }
        });
        thread.start();
    }

    public static void run0() {
        while (true) {
            long count = 0;
            long time = 0;
            // 遍历所有的GC beans
            System.out.println("============");
            // 获取GarbageCollectorMXBean列表
            List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
            for (GarbageCollectorMXBean gcBean : gcBeans) {
                // 获取GC名称
                String name = gcBean.getName();
                // 获取GC执行的次数
                long collectionCount = gcBean.getCollectionCount();
                count = count + collectionCount;
                // 获取GC执行所花费的总时间（单位毫秒）
                long collectionTime = gcBean.getCollectionTime();
                time = time + collectionTime;
                System.out.println(name + ":" + " count=" + collectionCount + ",time=" + collectionTime);
            }
            System.out.println("aa count=" + count + ",time=" + time);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
