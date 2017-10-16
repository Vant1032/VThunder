package logic;


import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 线程安全
 * 所有的退出事件都由这里统一管理
 *
 * @author Vant
 * @version 2017/10/11 下午 8:58
 */
public class ExitCommand implements Observerable<Exitable> {
    @NotNull
    private static volatile ExitCommand exitCommand = new ExitCommand();

    private Set<Exitable> exitables;
    private Set<Exitable> marks;

    private ExitCommand() {
        exitables = Collections.synchronizedSet(new HashSet<>());
    }

    @Override
    public boolean addListener(Exitable listener) {
        return exitables.add(listener);
    }

    @Override
    public boolean removeListener(Exitable listener) {
        return exitables.remove(listener);
    }

    /**
     * 通知系统即将关闭,对于每个通知对象,都新开一个线程通知,这个通知只应该通知一次
     */
    @Override
    public void informAll() {
        System.out.println("ExitCommand.informAll 开始通知关闭程序");

        exitables.forEach(exitable -> {
            ThreadPool.getThreadPool().getNoninterruptablePool().submit(() -> exitable.onExit());
        });
        ThreadPool.getThreadPool().getNoninterruptablePool().submit(() -> {
            try {
                checkExit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


    }

    /**
     * 在通知关闭事件后,隔一段时间查询是否所有关闭事件完成,如果完成就调用System.exit(0)
     *
     * @throws InterruptedException
     */
    private void checkExit() throws InterruptedException {
        marks = new HashSet<>(exitables);
        while (true) {
            //考虑到仅仅是退出时轮询是否完成,所以轮询次数多点也没关系
            Thread.sleep(80);
            marks.removeIf(exitable -> exitable.isExited());
            if (marks.isEmpty()) {
                System.exit(0);
            }
        }
    }

    @NotNull
    @Contract(pure = true)
    public static ExitCommand getExitCommand() {
        return exitCommand;
    }
}