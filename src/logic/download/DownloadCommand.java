package logic.download;

import controller.VTMainController;
import javafx.scene.media.AudioClip;
import javafx.util.Pair;
import logic.SaveToLocal;
import logic.ThreadPool;
import org.jetbrains.annotations.NotNull;
import viewModel.VThunderUI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 建立开始下载,传入一个DownloadingFileProperty,并且通过property更新界面
 */
public class DownloadCommand {
    @NotNull
    private static AudioClip audioClip = new AudioClip(DownloadUtil.class.getClassLoader().getResource("fxml/download-complete.wav").toExternalForm());

    static {
        audioClip.setVolume(1.0);
    }

    private DownloadingFileProperty downloadingFileProperty;
    private DownloadUtil downloadUtil;
    private VThunderUI vThunderUI;
    private int byteSize;


    public DownloadCommand(@NotNull DownloadingFileProperty downloadingFileProperty, VThunderUI vThunderUI) throws IOException {
        this.downloadingFileProperty = downloadingFileProperty;
        downloadUtil = new DownloadUtil(new URL(downloadingFileProperty.getLink()), new File(downloadingFileProperty.getSavePath()));
        this.vThunderUI = vThunderUI;

        byteSize = downloadUtil.getByteSize();
    }

    public DownloadCommand(DownloadingFileProperty downloadingFileProperty, @NotNull DownloadUtil downloadUtil, VThunderUI vThunderUI) {
        this.downloadingFileProperty = downloadingFileProperty;
        this.downloadUtil = downloadUtil;

        byteSize = downloadUtil.getByteSize();
        this.vThunderUI = vThunderUI;
    }

    /**
     * 没有显示进度的下载,下载完成更新进度为1.0
     *
     * @throws IOException
     */
    public void download() throws IOException {
        System.out.println("DownloadCommand.download 下载开始");
        byte[] downloadedContent = downloadUtil.downloadAll();
        System.out.println("DownloadCommand.download 下载完成");
        downloadingFileProperty.setTimeRemain(0);
        downloadingFileProperty.setProgress(1);

        saveAllContent(downloadedContent);
    }


    private volatile int nowSize = 0;//这个变量用于计算速度
    private volatile boolean complete = false;

    /**
     * <p>有显示进度的下载,暂时不考虑方法性能</p>
     * <p>
     * <p><i>注意:这个方法不会阻塞</i></p>
     */
    public void downloadBySec() throws IOException {
        //先实现再说,以后再优化
        //TODO:把代码优化得漂亮些
        System.out.println("DownloadCommand.downloadBySec 下载开始");
        Pair<byte[], Integer> pair = downloadUtil.downloadBySegment();
        byte[] content = pair.getKey();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(downloadingFileProperty.getSavePath()));
        int totalSize = downloadUtil.getByteSize();
        nowSize = 0;
        complete = false;



        /*
          速度计算以及剩余时间计算线程,算出来的速度是以KB/s算的
         */
        ThreadPool.getThreadPool().getInterruptablePool().submit(new Runnable() {
            int lastSize = 0;
            int delta = 800;//每隔delta ms计算一次
            double speed = (nowSize - lastSize) / (delta * 0.001);
            //用于取个速度平均值
            @NotNull double[] sp = {speed, speed, speed};
            int pos = 0;

            long secRemain;

            @Override
            public void run() {
                double avg;
                while (!complete) {
                    try {
                        Thread.sleep(delta);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    speed = (nowSize - lastSize) / (delta * 0.001);//B/s
                    sp[(pos++) % 3] = speed;
                    avg = (sp[0] + sp[1] + sp[2]) / 3;
                    secRemain = (long) ((downloadUtil.getByteSize() - nowSize) / avg);//返回的是秒
                    downloadingFileProperty.setTimeRemain(secRemain);

                    lastSize = nowSize;
                    downloadingFileProperty.setSpeed(speed);

                    //更新悬浮窗进度
                    vThunderUI.getFloatWindow().setProgress(downloadingFileProperty.getProgress());
                }
                onDownloadEnded();

//                System.out.println("速度计算线程退出");
            }

            private void onDownloadEnded() {
                //最后完成时将速度设置为0
                downloadingFileProperty.setSpeed(0);

                VTMainController vtMainController = vThunderUI.getVtMainController();
                vtMainController.getDownloading().getItems().remove(downloadingFileProperty);
                vtMainController.getDownloaded().getItems().add(downloadingFileProperty);


                //播放结束音乐
                audioClip.play();
                System.out.println("DownloadCommand.run 速度 时间 计算线程退出");
            }
        });

        while (pair.getValue() != -1) {
            //存储
            bufferedOutputStream.write(content, 0, pair.getValue());

            //更新进度
            nowSize += pair.getValue();
            downloadingFileProperty.setProgress(nowSize * 1.0 / totalSize);

            //下载
            pair = downloadUtil.downloadBySegment();

            content = pair.getKey();
        }
        complete = true;
        bufferedOutputStream.close();
        System.out.println("下载完成");
    }


    /**
     * 将其封装起来,方便使用
     *
     * @throws IOException
     */
    private void saveAllContent(byte[] downloadedContent) throws IOException {
        System.out.println("DownloadCommand.download 开始保存");
        SaveToLocal saveToLocal = new SaveToLocal(downloadedContent, new File(downloadingFileProperty.getSavePath()));
        saveToLocal.save();
        System.out.println("DownloadCommand.download 保存成功");
    }
}
