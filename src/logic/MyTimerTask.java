package logic;

import java.io.IOException;
import java.util.Timer;

/**
 * 提供定时任务的基础功能
 *
 * @author Vant
 * @version 2017/10/16 下午 11:37
 */
public class MyTimerTask {
    Timer timer = new Timer(true);

    //TODO:添加定时关机功能
    public void timedShutdown(int seconds) throws IOException {
        Runtime.getRuntime().exec("shutdown -s -t " + seconds);
    }

    //TODO:添加定时退出本软件功能
    public void timedExit(int seconds) {
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @Override
            public void run() {
                
            }
        }
    }

    //TODO:添加定时休眠功能

    //TODO:添加定时睡眠功能

    //TODO:添加定时执行cmd命令功能

    //TODO:添加下载完成关机功能

    //TODO:添加下载完成退出本软件功能

    //TODO:添加下载完成休眠功能

    //TODO:添加下载完成睡眠功能


    //TODO:添加下载完成执行cmd命令功能


    //添加取消关机功能
}
