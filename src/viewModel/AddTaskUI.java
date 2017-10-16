package viewModel;

import controller.AddTaskController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import logic.ConfigUtil;
import logic.DownloadCommand;
import logic.DownloadUtil;
import logic.DownloadingFileProperty;
import logic.ExitCommand;
import logic.Exitable;
import logic.StyleChangeable;
import logic.ThreadPool;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;

public class AddTaskUI implements Exitable, StyleChangeable {
    private AddTaskController addTaskController;
    private Stage parentStage;
    private DownloadingFileProperty downloadingFileProperty;
    private Tooltip pathTooltip, linkTooltip;
    private Stage addTaskStage;
    private boolean exited = false;

    public AddTaskUI(@NotNull VThunderUI vThunderUI) {
        ExitCommand.getExitCommand().addListener(this);

        parentStage = vThunderUI.getvThunderstage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/AddTask.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addTaskController = fxmlLoader.getController();
        Scene addScene = new Scene(parent);
        addTaskStage = new Stage(StageStyle.UNIFIED);

        addTaskStage.setResizable(false);
        addTaskStage.setTitle(" 添加个下载任务吧 ^_^");
        addTaskStage.getIcons().add(new Image("img/迅雷.png"));
        addTaskStage.initModality(Modality.APPLICATION_MODAL);
        addTaskStage.initOwner(parentStage);
        addTaskStage.setScene(addScene);
        addTaskStage.getScene().getStylesheets().add("css/AddTask.css");

        addEvent(vThunderUI);

        addTaskController.getSaveDirectory().setText(ConfigUtil.getConfigUtil(ConfigUtil.Type.DEFAULT).getProperties().getProperty(SettingUI.DEFAULTDIR));

        addTaskStage.show();
    }

    private void addEvent(@NotNull VThunderUI vThunderUI) {
        addFileNameEvent();

        addTaskController.getDownloadAddress().textProperty().addListener(observable -> {
            String text = addTaskController.getDownloadAddress().getText();
            text = DownloadUtil.getFileNameByLink(text);
            Matcher matcher = DownloadUtil.FILENAMEPATTERN.matcher(text);
            text = matcher.replaceAll("");
            addTaskController.getFileName().setText(text);
        });

        addTaskController.getDownload().setOnAction(new OnDownloadEventHandler(vThunderUI));
        pathTooltip = new Tooltip("输入的目录有问题");
        pathTooltip.setAutoHide(true);

        addTaskController.getOpenFileChooser().setOnMouseClicked(new OnChooseFileEventHandler(addTaskStage, addTaskController.getSaveDirectory()));

        //设置链接出错提示
        linkTooltip = new Tooltip();
        linkTooltip.setAutoHide(true);
    }

    /**
     * 给文件名输入框添加时间
     */
    private void addFileNameEvent() {
        //设置输入文件名时的事件
        TextField fileName = addTaskController.getFileName();
        fileName.setOnKeyTyped((KeyEvent event) -> {
            String text = fileName.getText();
            if (!DownloadUtil.checkFileName(text)) {
                fileName.setText(text.substring(0, text.length() - 1));
                Tooltip tooltip = new Tooltip("文件名不合法,包含特殊字符");
                tooltip.setAutoHide(true);
                tooltip.show(addTaskStage);

            }
        });
    }

    public AddTaskController getAddTaskController() {
        return addTaskController;
    }

    @Override
    public void onExit() {
        Platform.runLater(() -> addTaskStage.hide());
        exited = true;
    }

    @Override
    public boolean isExited() {
        return exited;
    }

    @Override
    public void setStyle(String style) {
        addTaskStage.getScene().getRoot().setStyle(style);
    }

    class OnChooseFileEventHandler implements EventHandler<MouseEvent> {
        private Window parent;
        private TextField textField;

        /**
         * @param parent    父窗口
         * @param textField 被改变的文本域
         */
        public OnChooseFileEventHandler(Window parent, TextField textField) {
            this.parent = parent;
            this.textField = textField;
        }

        @Override
        public void handle(MouseEvent event) {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(parent);
            //noinspection StatementWithEmptyBody
            if (file != null && file.canWrite()) {
                textField.setText(file.getAbsolutePath());
            } else {
                //否则就不做任何操作
            }
        }
    }

    class OnDownloadEventHandler implements EventHandler<ActionEvent> {
        VThunderUI vThunderUI;

        public OnDownloadEventHandler(VThunderUI vThunderUI) {
            this.vThunderUI = vThunderUI;
        }

        @Override
        public void handle(ActionEvent event) {
            String link = addTaskController.getDownloadAddress().getText().trim();

            File directory = getDirectory();
            if (directory == null || !directory.isDirectory()) {
                //目录参数不合法,在文件路径框显示悬浮提示
                pathTooltip.show(addTaskStage);
                return;
            }

            DownloadUtil downloadUtil = null;
            TextField fileNameTF = addTaskController.getFileName();
            File position = new File(directory.getAbsolutePath() + File.separator + fileNameTF.getText().trim());

            try {
                downloadUtil = new DownloadUtil(new URL(link), position);
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                //url有问题
                linkTooltip.setText("链接格式有问题");
                linkTooltip.show(addTaskStage);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                //连接有问题
                linkTooltip.setText("连接出错");
                linkTooltip.show(addTaskStage);
                return;
            }

            //当没问题时,开始下载
            TableView<DownloadingFileProperty> downloading = vThunderUI.getVtMainController().getDownloading();
            String fileName = fileNameTF.getText().trim();//假定文件名一定正确
            DownloadingFileProperty property = new DownloadingFileProperty(fileName, link, position.getAbsolutePath(), downloadUtil.getByteSize(), 0, -1, 0);
            downloading.getItems().add(property);

            //为了防止界面假死,新建一个线程下载
            DownloadUtil finalDownloadUtil = downloadUtil;
            ThreadPool.getThreadPool().getInterruptablePool().submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        new DownloadCommand(property, finalDownloadUtil, vThunderUI).downloadBySec();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            addTaskStage.hide();
        }


        @Nullable
        private File getDirectory() {
            //获取下载的目录
            String directoryStr = addTaskController.getSaveDirectory().getText();
            if (directoryStr == null) {
                return null;
            }
            return new File(directoryStr);
        }
    }
}