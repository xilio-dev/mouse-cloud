package cn.xilio.mouse.cloud.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import java.util.concurrent.TimeUnit;

public class CuratorDistributedLockExample {
    private static final String ZK_ADDRESS = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
    private static final String LOCK_PATH = "/distributed-lock";

    public static void main(String[] args) throws Exception {
        // 1. 创建 Curator 客户端
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ZK_ADDRESS)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)) // 重试策略
                .build();
        client.start();

        // 2. 创建分布式锁（可重入锁）
        InterProcessMutex lock = new InterProcessMutex(client, LOCK_PATH);
        try {
            // 3. 获取锁（阻塞等待，支持超时设置）
            if (lock.acquire(10, TimeUnit.SECONDS)) {
                System.out.println("获取到锁了");
                // 模拟业务处理
                Thread.sleep(1000);
            }
        } finally {
            // 4. 释放锁
            if (lock.isAcquiredInThisProcess()) {
                lock.release();
                System.out.println("Lock released.");
            }
            client.close();
        }
    }
}
