package logic;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>这个线程池虽然实现了Exitable,但是并没有监听关闭事件,因为一旦监听关闭事件就会产生bug,ExitCommand依赖线程池,线程池依赖ExitCommand
 * 线程池的关闭需要特殊处理</p>
 * <p>这个线程池不用于关闭事件,防止形成循环依赖,ExitCommand自己内部使用线程池来控制,这个作为事务线程池</p>
 * @author Vant
 * @version 2017/10/15 下午 3:30
 */
public class ThreadPool implements Exitable{
    private static ThreadPool threadPool = new ThreadPool();
    private volatile boolean exited = false;

    //此线程池的线程都是可中断的,仅仅作为守护线程一样的存在,就是说如果遇到突然关闭软件,里面的线程可能还未做完事情就关闭了
    private ExecutorService interruptablePool;

    //此线程池的线程是不可中断的,如果碰到关闭程序指令,会等待其完成任务再关闭
    private ExecutorService noninterruptablePool;

    private ThreadPool() {
        noninterruptablePool = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
        interruptablePool = new ThreadPoolExecutor(2, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue<>());
    }

    /**
     * @return 此线程池的线程都是可中断的,仅仅作为守护线程一样的存在,就是说如果遇到突然关闭软件,里面的线程可能还未做完事情就关闭了
     */
    public ExecutorService getInterruptablePool() {
        return interruptablePool;
    }

    @Contract(pure = true)
    public static ThreadPool getThreadPool() {
        return threadPool;
    }

    /**
     *
     * @return 此线程池的线程是不可中断的,如果碰到关闭程序指令,会等待其完成任务再关闭,但是最长等待时间为60s
     */
    public ExecutorService getNoninterruptablePool() {
        return noninterruptablePool;
    }

    @Override
    public void onExit() {
        System.out.println("ThreadPool.onExit start");
        try {
            noninterruptablePool.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {
            exited = noninterruptablePool.isShutdown();
            if (exited){
                break;
            }
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("ThreadPool.onExit end");
    }

    @Override
    public boolean isExited() {
        return exited;
    }
}