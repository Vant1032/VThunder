package viewModel;

import controller.SettingController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import logic.ConfigUtil;
import logic.StyleChangeable;
import logic.ThreadPool;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

/**
 * @author Vant
 * @version 2017/10/9 下午 10:55
 */
public class SettingUI implements StyleChangeable {
    private Stage settingStage;
    private SettingController settingController;
    @Nullable
    private ConfigUtil configUtil;
    public static final String DEFAULTDIR = "DEFAULTDIR", ISBGSWITCH = "ISBGSWITCH", TRUR = "true", FALSE = "false";

    public SettingUI(Window parentWindow) throws IOException {
        makeInterface(parentWindow);

        configUtil = ConfigUtil.getConfigUtil(ConfigUtil.Type.DEFAULT);

        addEvent(parentWindow);

        loadSettingFromXML();

        settingStage.show();
    }

    private void loadSettingFromXML() {
        String dir = configUtil.getProperties().getProperty(DEFAULTDIR);
        if (dir != null) {
            settingController.getBrowserResult().setText(dir);
        }


        String value = configUtil.getProperties().getProperty(ISBGSWITCH);


        if (value != null) {
            if (value.equals(TRUR)) {
                settingController.getBgImgSwitch().setSelected(true);
            } else {
                settingController.getBgImgSwitch().setSelected(false);
            }
        }
    }

    private void addEvent(Window parentWindow) {
        settingController.getBrowseDir().setOnAction(event -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("选择一个默认下载目录");
            File dir = directoryChooser.showDialog(parentWindow);
            if (dir != null) {
                settingController.getBrowserResult().setText(dir.getAbsolutePath());
            }
        });

        //保存设置
        settingController.getSaveSetting().setOnAction(event -> {
            String defaultDir = settingController.getBrowserResult().getText().trim();
            Boolean changeBg = settingController.getBgImgSwitch().isSelected();

            configUtil.getProperties().setProperty(DEFAULTDIR, defaultDir);

            String tmp;
            if (changeBg) {
                tmp = TRUR;
            } else {
                tmp = FALSE;
            }
            configUtil.getProperties().setProperty(ISBGSWITCH, tmp);

            //新建线程保存设置
            ThreadPool.getThreadPool().getNoninterruptablePool().submit(() -> configUtil.save());

            settingStage.hide();
        });

        settingController.getCancelSetting().setOnAction(event -> {
            settingStage.hide();
        });
    }


    private void makeInterface(Window parentWindow) throws IOException {
        settingStage = new Stage(StageStyle.UNIFIED);
        settingStage.setTitle("设置");
        settingStage.initOwner(parentWindow);
        settingStage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/Setting.fxml"));
        Parent parent = fxmlLoader.load();
        settingController = fxmlLoader.getController();

        Scene scene = new Scene(parent);
        settingStage.setScene(scene);
    }


    @Override
    public void setStyle(String style) {
        settingStage.getScene().getRoot().setStyle(style);
    }
}
