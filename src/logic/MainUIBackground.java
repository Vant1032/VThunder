package logic;

import javafx.application.Platform;
import viewModel.SettingUI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO:先写一个能用的
 *
 * @author Vant
 * @version 2017/10/15 下午 5:35
 */
public class MainUIBackground {
    private static volatile MainUIBackground mainUIBackground = null;

    public static synchronized MainUIBackground getMainUIBackground() {
        if (mainUIBackground == null) {
            mainUIBackground = new MainUIBackground();
        }
        return mainUIBackground;
    }

    private ArrayList<String> paths;
    private Set<StyleChangeable> styleChangeables;
    private volatile boolean running;//控制切换壁纸线程的关闭
    private volatile int interval;//控制每隔多少毫秒切换一次壁纸
    private volatile int now;


    private MainUIBackground() {
        paths = getBackgroudImgs();
        styleChangeables = new HashSet<>();
        running = false;
        interval = 10 * 1000;
        now = 0;
    }

    public void checkSetting() {
        System.out.println("MainUIBackground.checkSetting");
        String property = ConfigUtil.getConfigUtil(ConfigUtil.Type.DEFAULT).getProperty(SettingUI.ISBGSWITCH);
        if (property.equals(SettingUI.TRUR)) {//如果设置是要切换
            if (running) {
                return;
            }
            running = true;
            createThread();
        } else {
            if (!running) {
                return;
            }
            running = false;
        }
    }

    private void createThread() {
        ThreadPool.getThreadPool().getInterruptablePool().submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("启动切换壁纸线程");
                System.out.println("running = " + running);
                while (running) {
                    try {
                        System.out.println("开始切换壁纸");
                        for (StyleChangeable s : styleChangeables) {
                            Platform.runLater(() -> {
                                s.setStyle("-fx-background-image: url(\"file:///" + paths.get((now++) % paths.size()) + "\");");
                            });
                        }
                        Thread.sleep(interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("壁纸切换线程结束");
            }
        });
    }

    /**
     * 再次扫描背景图片目录,只会添加图片,不会删除原有的
     */
    public void updateBackgroundImgs() {
        ArrayList<String> backgroudImgs = getBackgroudImgs();
        boolean flag;
        int len = paths.size();
        for (String s : backgroudImgs) {
            flag = true;
            for (int i = 0; i < len; i++) {
                if (paths.get(i).equals(s)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                paths.add(s.replaceAll("\\\\", "/"));
            }
        }
    }

    public void clearBackgrounImgs() {
        paths.clear();
    }

    /**
     * @return 背景文件夹下所有背景图片的路径
     */
    public static ArrayList<String> getBackgroudImgs() {
        ArrayList<String> imgs = new ArrayList<>();
        File bgDir = new File("src/img/background");
        for (File file : bgDir.listFiles()) {
            if (file.getAbsolutePath().toLowerCase().endsWith(".jpg") || file.getAbsolutePath().toLowerCase().endsWith(".png")) {
                imgs.add(file.getAbsolutePath().replaceAll("\\\\", "/"));
            }
        }
        return imgs;
    }

    public boolean add(StyleChangeable styleChangeable) {
        return styleChangeables.add(styleChangeable);
    }
}