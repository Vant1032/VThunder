package logic;

import javafx.scene.control.Alert;
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
 * <p>3.会自动保存设置,如果更改设置而忘记保存,则会自动间隔一定时间保存设置,注意:这应该是作为最后手段</p>
 * TODO:做个优化,如果设置没有改变,则无需保存,并且可以每隔一段时间保存下设置,不过最好是立即保存
 * 所有的设置都应该立即保存,不应该为了那点保存文件的耗时而犹豫
 * @author Vant
 * @version 2017/10/9 下午 6:11
 */
public class ConfigUtil {
    public static final String BASEDIR = ".";//定义程序保存数据的基本目录,并不是文件路径
    public static final String CONFFILENAME = "config.vml";
    @NotNull
    private Properties properties = new Properties();
    private File file;
    private volatile boolean modified = false;

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

    /**
     * @param key
     * @param value
     * @return the previous value of the specified key in this propertylist, or {@code null} if it did not have one.
     */
    public Object setProperty(String key, String value) {
        if (properties.getProperty(key).equals(value)) {
            return value;
        }
        modified = true;
        return properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public void save() {
        if (!modified) return;
        try {
            //因为本身创建时会检查是否有这个文件,有就不创建,所以不需要额外的处理
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("创建文件异常");
        }

        try (FileOutputStream os = new FileOutputStream(file)) {
            properties.storeToXML(os, "Don't Modify this");
            modified = false;//保存好了设置之后清除设置已更改的状态
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("保存设置文件异常");
            alert.setTitle("＞﹏＜ 遇到一个问题");
            alert.show();
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
        DEFAULT;
    }
}
