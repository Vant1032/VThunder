package logic;

import javafx.util.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadUtil {
    private final URL url;
    private File localSave;
    private int byteSize = 0;//以字节为单位
    private InputStream inputStream;

    public DownloadUtil(URL url, File localSave) throws IOException {
        this.url = url;
        this.localSave = localSave;
        init();
    }

    /**
     * 初始化连接,为通过流进行下载做好所有准备
     *
     * @throws IOException 当连接出现问题时
     */
    private void init() throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.connect();
        inputStream = httpURLConnection.getInputStream();
        byteSize = httpURLConnection.getContentLength();
    }

    /**
     * 直接将所有文件都一次性下载过来,并且关闭流,不可再次下载了
     *
     * @return
     */
    public byte[] downloadAll() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] bytes = bufferedInputStream.readAllBytes();
        bufferedInputStream.close();
        return bytes;
    }

    /**
     * 这个方法会阻塞
     *
     * @return 一个下载内容的一部分的字节数组和一个读取长度的值
     */
    @NotNull
    public Pair<byte[], Integer> downloadBySegment() throws IOException {
        byte[] bytes = new byte[1024];
        int read = inputStream.read(bytes);
        return new Pair<>(bytes, read);
    }

    public URL getUrl() {
        return url;
    }


    public File getLocalSave() {
        return localSave;
    }

    public int getByteSize() {
        return byteSize;
    }

    /**
     * 传入一个下载链接,它试图找出链接中要下载的文件的名字
     *
     * @param link 如果链接以文件分隔符结尾,则返回"";
     * @return
     */
    @NotNull
    public static String getFileNameByLink(@NotNull String link) {
        int separatorPos;
        for (separatorPos = link.length() - 1; separatorPos >= 0; separatorPos--) {
            if ('/' == link.charAt(separatorPos)) {
                break;
            }
        }
        if (separatorPos + 1 != link.length()) {
            return link.substring(separatorPos + 1);
        } else {
            return "";
        }
    }

    //>|/|:|\*|\||\?|\\|<|"
    //[></:\*\|\?\\]
    public static final Pattern FILENAMEPATTERN = Pattern.compile("[></:\\*\\|\\?\\\\]");

    /**
     * @param fileName 只是文件的名字,不包括路径
     * @return true代表是个合法文件名, 即不包含 特殊字符的
     */
    public static boolean checkFileName(@NotNull String fileName) {
        Matcher matcher = FILENAMEPATTERN.matcher(fileName);
        return !matcher.find();
    }

    public static boolean checkLinkFormat(@NotNull String link) {
        try {
            URL url = new URL(link);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

    /**
     * @param n 字节数
     * @return 兆字节数
     */
    @Contract(pure = true)
    public static double byteToMegabyte(double n) {
        return n / 1024 / 1024;
    }
}