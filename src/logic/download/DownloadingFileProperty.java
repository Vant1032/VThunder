package logic.download;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

/**
 * 为列表定制的数据类型
 */
public class DownloadingFileProperty {
    @NotNull
    private SimpleStringProperty fileName = new SimpleStringProperty("");
    @NotNull
    private SimpleDoubleProperty size = new SimpleDoubleProperty();
    @NotNull
    private SimpleDoubleProperty speed = new SimpleDoubleProperty();
    @NotNull
    private SimpleLongProperty timeRemain = new SimpleLongProperty();
    @NotNull
    private SimpleDoubleProperty progress = new SimpleDoubleProperty();
    @NotNull
    private SimpleStringProperty link = new SimpleStringProperty();
    @NotNull
    private SimpleStringProperty savePath = new SimpleStringProperty();

    public DownloadingFileProperty() {
    }

    public DownloadingFileProperty(String fileName, String link, String savePath, double size, double speed, long timeRemain,
                                   double progress
    ) {
        this.fileName = new SimpleStringProperty(fileName);
        this.link = new SimpleStringProperty(link);
        this.savePath = new SimpleStringProperty(savePath);
        this.size = new SimpleDoubleProperty(size);
        this.speed = new SimpleDoubleProperty(speed);
        this.timeRemain = new SimpleLongProperty(timeRemain);
        this.progress = new SimpleDoubleProperty(progress);
    }

    public String getLink() {
        return link.get();
    }

    @NotNull
    public SimpleStringProperty linkProperty() {
        return link;
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public String getSavePath() {
        return savePath.get();
    }

    @NotNull
    public SimpleStringProperty savePathProperty() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath.set(savePath);
    }

    @NotNull
    public SimpleStringProperty fileNameProperty() {
        return this.fileName;
    }

    public String getFileName() {
        return this.fileNameProperty().get();
    }

    public void setFileName(final String fileName) {
        this.fileNameProperty().set(fileName);
    }

    @NotNull
    public SimpleDoubleProperty sizeProperty() {
        return this.size;
    }

    public double getSize() {
        return this.sizeProperty().get();
    }

    public void setSize(final double size) {
        this.sizeProperty().set(size);
    }

    @NotNull
    public SimpleDoubleProperty speedProperty() {
        return this.speed;
    }

    public double getSpeed() {
        return this.speedProperty().get();
    }

    public void setSpeed(final double speed) {
        this.speedProperty().set(speed);
    }

    @NotNull
    public SimpleLongProperty timeRemainProperty() {
        return this.timeRemain;
    }

    public long getTimeRemain() {
        return this.timeRemainProperty().get();
    }

    public void setTimeRemain(final long timeRemain) {
        this.timeRemainProperty().set(timeRemain);
    }

    @NotNull
    public SimpleDoubleProperty progressProperty() {
        return this.progress;
    }

    /**
     * @return 这个值范围从0-1
     */
    public double getProgress() {
        return this.progressProperty().get();
    }

    /**
     * @param progress 这个值范围从0-1
     */
    public void setProgress(final double progress) {
        this.progressProperty().set(progress);
    }
}