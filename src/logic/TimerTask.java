package logic;

import java.io.IOException;

/**
 * 提供定时任务的基础功能
 *
 * @author Vant
 * @version 2017/10/16 下午 11:37
 */
public class TimerTask {

    //TODO:添加定时关机功能
    public void timedShutdown(int seconds) throws IOException {
        execCMD("shutdown -s -t " + seconds);
    }
    //TODO:添加定时退出本软件功能

    //TODO:添加定时休眠功能

    //TODO:添加定时睡眠功能

    //TODO:添加定时执行cmd命令功能

    //TODO:添加下载完成关机功能

    //TODO:添加下载完成退出本软件功能

    //TODO:添加下载完成休眠功能

    //TODO:添加下载完成睡眠功能


    //TODO:添加下载完成执行cmd命令功能

    /**
     * @param command 要执行的cmd命令
     * @throws IOException
     */
    public void execCMD(String command) throws IOException {
        Runtime.getRuntime().exec(command);
    }

    //添加取消关机功能
}
