package logic;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 提供定时任务的基础功能
 *
 * @author Vant
 * @version 2017/10/16 下午 11:37
 */
public class MyTimerTask {
    private Timer timer = new Timer(true);

    //定时关机功能
    public void timedShutdown(int seconds) throws IOException {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("shutdown -p");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, seconds * 1000);
    }

    //定时退出本软件功能
    public void timedExit(int seconds) {
        java.util.TimerTask timerTask = new java.util.TimerTask() {
            @Override
            public void run() {
                ExitCommand.getExitCommand().informAll();
            }
        };
        timer.schedule(timerTask, seconds * 1000);
    }

    //定时休眠功能
    public void timedSleep(int seconds) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    Runtime.getRuntime().exec("shutdown -h");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, seconds * 1000);
    }


    //TODO:添加下载完成关机功能

    //TODO:添加下载完成退出本软件功能

    //TODO:添加下载完成休眠功能

    //TODO:添加下载完成睡眠功能


    //添加取消所有任务功能
    public void cancelAll() {
        timer.cancel();
    }
}
