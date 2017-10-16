package logic;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <p>1.用于界面的保存以及恢复</p>
 * <p>2.用于配置相关</p>
 * <p></p>
 *
 * @author Vant
 * @version 2017/10/9 下午 6:11
 */
public class ConfigUtil {
    public static final String BASEDIR = ".";//定义程序保存数据的基本目录,并不是文件路径
    public static final String CONFFILENAME = "config.vml";
    @NotNull
    private Properties properties = new Properties();
    File file;

    /**
     * 新建的时候会自动获取本地设置
     */
    public ConfigUtil(@NotNull File file) {
        this.file = file;
        try {
            properties.loadFromXML(new FileInputStream(file));
        } catch (IOException e) {
            //文件没找到,就不做任何事情
            System.err.println("没找到文件,无需处理");
        }
    }

    @NotNull
    public Properties getProperties() {
        return properties;
    }

    public void save() {
        if (file.exists() == false) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream os = new FileOutputStream(file)) {
            properties.storeToXML(os, "Don't Modify this");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Contract(pure = true)
    public static ConfigUtil getConfigUtil(@NotNull Type type) {
        ConfigUtil configUtil = null;
        switch (type) {
            case DEFAULT: {
                configUtil = DefaultConfig.getDefaultConfig();
            }
        }
        return configUtil;
    }

    /**
     * 方便工厂方法调用,扩展设置类的时候添加一个枚举就行了
     */
    public enum Type {
        DEFAULT
    }
}
//TODO:写根据导入的设置进行软件行为改动