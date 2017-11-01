package viewModel;

import controller.VTMainController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.ExitCommand;
import logic.Exitable;
import logic.MainUIBackground;
import logic.StyleChangeable;
import logic.download.DownloadUtil;
import logic.download.DownloadingFileProperty;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * 用这个完成所有界面与核心代码的交互
 */
public class VThunderUI implements Exitable, StyleChangeable {
    private Stage vThunderstage;
    private VTMainController vtMainController;//提供各个界面元素的引用
    private FloatWindow floatWindow;
    private boolean exited = false;
    private double initX, initY;//用于标题栏移动


    public VThunderUI() {
        try {
            Tray tray = new Tray(this);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("＞﹏＜ 遇到一个问题");
            alert.setContentText("加载应用图标失败:找不到应用图标");
            alert.show();
        } catch (AWTException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("＞﹏＜ 遇到一个问题");
            alert.setContentText("似乎无法在本机创建系统图标");
            alert.show();
        }

        makeInterface();//构造界面

        addEvent();

        makeTable();


        vThunderstage.show();

        floatWindow = new FloatWindow();


        MainUIBackground mainUIBackground = MainUIBackground.getMainUIBackground();
        mainUIBackground.add(this);
        mainUIBackground.checkSetting();

        ExitCommand.getExitCommand().addListener(this);
    }

    /**
     * 给界面添加各种事件
     */
    private void addEvent() {
        vtMainController.getMinize().setOnMouseClicked(event -> {
            vThunderstage.setIconified(true);
        });
        vtMainController.getMaximize().setOnMouseClicked(event -> {
            vThunderstage.setMaximized(!vThunderstage.isMaximized());
        });
        vtMainController.getAdd().setOnMouseClicked(event -> {
            AddTaskUI addTaskUI = new AddTaskUI(this);
        });

        vtMainController.getTopNav().setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                vThunderstage.setMaximized(!vThunderstage.isMaximized());
            }
        });

        //实现了标题栏拖动---------------
        vtMainController.getTopNav().setOnMousePressed(event -> {
            initX = event.getScreenX() - vThunderstage.getX();
            initY = event.getScreenY() - vThunderstage.getY();
        });
        vtMainController.getTopNav().setOnMouseDragged(event -> {
            vThunderstage.setX(event.getScreenX() - initX);
            vThunderstage.setY(event.getScreenY() - initY);
        });
        //-----------------------------

        vtMainController.getSetting().setOnMouseClicked(event -> {
            try {
                new SettingUI(vThunderstage);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("＞﹏＜ 遇到一个问题");
                alert.setContentText("因为无法加载界面设置文件导致无法打开设置界面");
                alert.show();
            }
        });

        vtMainController.getClose().setOnMouseClicked(event -> {
            ExitCommand.getExitCommand().informAll();
        });

        vThunderstage.setOnCloseRequest(event -> {
            event.consume();
            ExitCommand.getExitCommand().informAll();
        });

        //添加上下文菜单
        vtMainController.getDownloaded().setRowFactory((TableView<DownloadingFileProperty> param) -> {
            TableRow<DownloadingFileProperty> row = new TableRow<>();
            MenuItem menuItem = new MenuItem("打开文件");
            ContextMenu contextMenu = new ContextMenu(menuItem);
            menuItem.setOnAction(event -> {
                System.out.println(row.getItem().getSavePath());
                try {
                    Desktop.getDesktop().open(new File(row.getItem().getSavePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            row.emptyProperty().addListener((observable, oldValue, newValue) -> {
                row.setContextMenu(contextMenu);
            });

            return row;
        });

        vtMainController.getDownloading().setRowFactory(param -> {
            TableRow<DownloadingFileProperty> row = new TableRow<>();
            MenuItem menuItem = new MenuItem("打开目录");
            ContextMenu contextMenu = new ContextMenu(menuItem);
            menuItem.setOnAction(event -> {
                try {
                    Desktop.getDesktop().open(new File(row.getItem().getSavePath()).getParentFile());
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("＞﹏＜ 遇到一个问题");
                    alert.setContentText("无法打开文件夹");
                    alert.show();
                }
            });
            row.emptyProperty().addListener((observable, oldValue, newValue) -> {
                row.setContextMenu(contextMenu);
            });

            return row;
        });
    }

    public FloatWindow getFloatWindow() {
        return floatWindow;
    }

    /**
     * 构造窗口的基本元素
     */
    private void makeInterface() {
        vThunderstage = new Stage(StageStyle.UNDECORATED);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/VTMain.fxml"));
        Parent parent = null;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        vtMainController = fxmlLoader.getController();
        assert parent != null;
        Scene vThunderScene = new Scene(parent);

        vThunderstage.setScene(vThunderScene);
        vThunderstage.getIcons().add(new Image("img/迅雷.png"));
        vThunderScene.getStylesheets().add("css/VTMain.css");

    }


    /**
     * 构造表格相关的东西
     */
    private void makeTable() {
        //downloading------------------------
        vtMainController.getDingFileName().setCellValueFactory(new PropertyValueFactory<>("fileName"));

        //显示文件大小
        vtMainController.getDingSize().setCellValueFactory(new PropertyValueFactory<>("size"));
        vtMainController.getDingSize().setCellFactory(VThunderUI::sizeCellFactory);


        //速度
        vtMainController.getDingSpeed().setCellValueFactory(new PropertyValueFactory<>("speed"));
        vtMainController.getDingSpeed().setCellFactory((TableColumn<DownloadingFileProperty, Double> param) -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    item = item / 1024;//转换成kB/s
                    if (item > 1000) {
                        item = item / 1024;
                        setText(String.format("%.2f MB/s", item));
                    } else {
                        setText(String.format("%.2f KB/s", item));
                    }
                }
            }
        });

        //显示剩余时间
        vtMainController.getDingTimeRemain().setCellValueFactory(new PropertyValueFactory<>("timeRemain"));
        vtMainController.getDingTimeRemain().setCellFactory((TableColumn<DownloadingFileProperty, Long> param) -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Long item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Long sec = item;
                        //搞成迅雷那样 00:00:00
                        long s = sec % 60;
                        sec /= 60;
                        long min = sec % 60;
                        sec /= 60;
                        long hour = sec;
                        setText(String.format("%02d:%02d:%02d", hour, min, s));
                    }
                }
            };
        });

        //处理进度条部分
        vtMainController.getDingProgress().setCellValueFactory(new PropertyValueFactory<>("progress"));
        vtMainController.getDingProgress().setCellFactory(ProgressBarTableCell.forTableColumn());
        //------------------------------

        //downloaded-------------------------
        vtMainController.getDedSize().setCellValueFactory(new PropertyValueFactory<>("size"));
        vtMainController.getDedSize().setCellFactory(VThunderUI::sizeCellFactory);
        vtMainController.getDedFileName().setCellValueFactory(new PropertyValueFactory<>("fileName"));
        //------------------------------------

    }

    /**
     * 这是作为两个表格处理文件大小的共用方法
     *
     * @param param
     * @return
     */
    @NotNull
    @Contract(pure = true)
    private static TableCell<DownloadingFileProperty, Double> sizeCellFactory(TableColumn<DownloadingFileProperty, Double> param) {
        return new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.format("%.2f MB", DownloadUtil.byteToMegabyte(item)));
                }
            }
        };
    }

    public Stage getvThunderstage() {
        return vThunderstage;
    }

    public VTMainController getVtMainController() {
        return vtMainController;
    }

    @Override
    public void onExit() {
        System.out.println("VThunderUI.onExit start");
        Platform.runLater(() -> {
            vThunderstage.hide();
            exited = true;
        });
        System.out.println("VThunderUI.onExit end");
    }

    @Override
    public boolean isExited() {
        return exited;
    }

    @Override
    public void setStyle(String style) {
        vThunderstage.getScene().getRoot().setStyle(style);
    }
}
