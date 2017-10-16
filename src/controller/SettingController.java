package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * @author Vant
 * @version 2017/10/10 上午 8:05
 */
public class SettingController implements Initializable {
    public SettingController() {

    }

    @FXML
    private CheckBox bgImgSwitch;
    @FXML
    private TextField browserResult;
    @FXML
    private Button browseDir;
    @FXML
    private Button saveSetting;
    @FXML
    private Button cancelSetting;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public CheckBox getBgImgSwitch() {
        return bgImgSwitch;
    }

    public TextField getBrowserResult() {
        return browserResult;
    }

    public Button getBrowseDir() {
        return browseDir;
    }

    public Button getSaveSetting() {
        return saveSetting;
    }

    public Button getCancelSetting() {
        return cancelSetting;
    }
}
