package logic;

import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Vant
 * @version 2017/10/12 下午 10:30
 */
public class DefaultConfig extends ConfigUtil {
    @Nullable
    public static DefaultConfig defaultConfig = null;

    public DefaultConfig() {
        super(new File(BASEDIR + "/" + CONFFILENAME));
    }

    @Nullable
    public static synchronized DefaultConfig getDefaultConfig() {
        if (defaultConfig == null) {
            defaultConfig = new DefaultConfig();
        }
        return defaultConfig;
    }
}