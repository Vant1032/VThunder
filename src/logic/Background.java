package logic;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Vant
 * @version 2017/10/15 下午 5:35
 */
public class Background {
    public static volatile Background background = null;

    public static Background getBackground() {
        synchronized (background) {
            if (background == null) {
                background = new Background();
            }
        }
        return background;
    }


    private ConcurrentMap<StyleChangeable, ArrayList<File>> concurrentMap;

    public Background() {
        concurrentMap = new ConcurrentHashMap<>();
    }



    /**
     * @return 背景文件夹下所有背景图片的路径
     */
    public static ArrayList<String> getBackgroudImgs() {
        ArrayList<String> imgs = new ArrayList<>(4);
        File bgDir = new File("src/img/background");
        for (File file : bgDir.listFiles()) {
            if (file.getAbsolutePath().toLowerCase().endsWith(".jpg") || file.getAbsolutePath().toLowerCase().endsWith(".png")) {
                imgs.add(file.getAbsolutePath());
            }
        }
        return imgs;
    }
}