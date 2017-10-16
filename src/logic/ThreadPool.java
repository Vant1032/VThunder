package logic;

import org.jetbrains.annotations.Contract;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
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
        ExitCommand.getExitCommand().addListener(this);
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
    }

    @Override
    public boolean isExited() {
        return exited;
    }
}