package com.zhi;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description
 * 必做）思考有多少种方式，在main函数启动一个新线程或线程池，
 * 异步运行一个方法，拿到这个方法的返回值后，退出主线程？
 * 写出你的方法，越多越好，提交到github。
 * @Author WenZhiLuo
 * @Date 2020-11-10 13:37
 */
public class HomeWork_7_2 {
    //CPU密集型
    ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);


    private static int sum() {
        System.out.println("非Main线程执行计算");
        return fibo(36);
    }

    private static int fibo(int a) {
        //f(0) = 0, f(1) = 1, f(2) = 1
        if ( a == 0) return 0;
        if ( a <= 2)  return 1;
        int f = 0;
        int f_1 = 1;
        int f_2 = 0;
        for(int i = 2; i <=a; i++){
            f = f_1 + f_2;
            f_2 = f_1;
            f_1 = f;
        }
        return f;
    }

    /**
     * 该法是同步，不算
     *
     * 非Main线程执行计算
     * 异步计算结果为：14930352
     * 使用时间：0 ms
     */
    @Test
    public void SyncMethod1() {

        long start=System.currentTimeMillis();
        // 在这里创建一个线程或线程池，
        // 异步执行 下面方法

        int result = sum(); //这是得到的返回值

        // 确保  拿到result 并输出
        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        // 然后退出main线程
    }

    /**
     * 通过Future,阻塞获取
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：2 ms
     */
    @Test
    public void AsyncMethod1() throws Exception {
        long start=System.currentTimeMillis();
        Future<Integer> future = threadPool.submit(HomeWork_7_2::sum);
        //会阻塞直到获取结果
        Integer result = future.get();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过futureTask
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：2 ms
     */
    @Test
    public void AsyncMethod2() throws Exception {
        long start=System.currentTimeMillis();
        FutureTask<Integer> futureTask = new FutureTask<>(HomeWork_7_2::sum);
        threadPool.submit(futureTask);
        Integer result = futureTask.get();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过join方法, Main线程等待
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod3() throws Exception {
        long start=System.currentTimeMillis();
        //Variable used in lambda expression should be final or effectively final
        //Integer result = null;
        AtomicInteger result = new AtomicInteger();
        Thread thread = new Thread(()->{
            result.set(sum());
        });
        thread.start();
        thread.join();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过CountDownLatch,Main线程等待CountLatch为0，
     *
     *非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod4() throws Exception {
        long start=System.currentTimeMillis();
        //Variable used in lambda expression should be final or effectively final
        //Integer result = null;
        AtomicInteger result = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        threadPool.execute(()->{
            result.set(sum());
            countDownLatch.countDown();
        });
        countDownLatch.await();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过Semaphore, 当semaphore为1的时候，Main线程才能acquire
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod5() throws Exception {
        long start=System.currentTimeMillis();
        //Variable used in lambda expression should be final or effectively final
        //Integer result = null;
        AtomicInteger result = new AtomicInteger();
        Semaphore semaphore = new Semaphore(0);
        threadPool.execute(()->{
            result.set(sum());
            semaphore.release();
        });
        semaphore.acquire();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过CyclicBarrier,两者同时互相等待
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod6() throws Exception {
        long start=System.currentTimeMillis();
        //Variable used in lambda expression should be final or effectively final
        //Integer result = null;
        AtomicInteger result = new AtomicInteger();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        threadPool.execute(()->{
            result.set(sum());
            try {
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cyclicBarrier.await();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过CompletableFuture异步执行，Main线程等待
     * 默认情况下 CompletableFuture 会使用公共的 ForkJoinPool 线程池，这个线程池默认创建的线程数是 CPU 的核数
     * 如果所有 CompletableFuture 共享一个线程池，那么一旦有任务执行一些很慢的 I/O 操作，就会导致线程池中所有线程都阻塞在 I/O 操作上，
     * 从而造成线程饥饿，进而影响整个系统的性能。所以，强烈建议你要根据不同的业务类型创建不同的线程池，以避免互相干扰。
     * runAsync(Runnable runnable)和supplyAsync(Supplier<U> supplier)，
     * 它们之间的区别是：Runnable 接口的 run() 方法没有返回值，而 Supplier 接口的 get() 方法是有返回值的。
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：4 ms
     */
    @Test
    public void AsyncMethod7() throws Exception {
        long start=System.currentTimeMillis();
        //Variable used in lambda expression should be final or effectively final
        //Integer result = null;
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(HomeWork_7_2::sum);
        //等待子线程执行完
        completableFuture.join();
        System.out.println("Main线程得到异步计算结果为："+completableFuture.get());
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        //非Main线程执行计算
        //Main线程得到异步计算结果为：14930352
        //使用时间：4 ms
    }

    /**
     * 通过阻塞队列
     * @throws Exception
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod8() throws Exception {
        long start=System.currentTimeMillis();
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        threadPool.execute(()->{
            try {
                queue.put(sum());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Integer result = queue.take();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * 通过Main线程Sleep,让出CPU资源
     * @throws Exception
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：3 ms
     */
    @Test
    public void AsyncMethod9() throws Exception {
        long start=System.currentTimeMillis();
        AtomicInteger result = new AtomicInteger();
        threadPool.execute(()->{
            result.set(sum());
        });
        Thread.sleep(1);
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * LockSupport方法先锁住Main线程
     * @return
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：1 ms
     */
    @Test
    public void AsyncMethod10(){
        long start=System.currentTimeMillis();
        Thread mainThread = Thread.currentThread();
        AtomicInteger result = new AtomicInteger();
        threadPool.execute(()->{
            result.set(sum());
            LockSupport.unpark(mainThread);
        });
        LockSupport.park();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        // 然后退出main线程
    }

    /**
     * 使用Condition，类似于信号量
     *
     * 非Main线程执行计算
     * Main线程得到异步计算结果为：14930352
     * 使用时间：2 ms
     */
    @Test
    public void AsyncMethod11() throws InterruptedException {
        long start=System.currentTimeMillis();
        Lock lock = new ReentrantLock();
        Condition notComplete = lock.newCondition();
        AtomicInteger result = new AtomicInteger();
        threadPool.execute(()->{
            lock.lock();
            result.set(sum());
            notComplete.signal();
            lock.unlock();
        });
        lock.lock();
        //主动释放🔒资源
        notComplete.await();
        lock.unlock();
        System.out.println("Main线程得到异步计算结果为："+result);
        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
        // 然后退出main线程
    }
}
