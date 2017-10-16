package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import logic.DownloadingFileProperty;

import java.net.URL;
import java.util.ResourceBundle;

public class VTMainController implements Initializable {
    @FXML
    private BorderPane all;
    @FXML
    private TableView<DownloadingFileProperty> downloading;
    @FXML
    private TableColumn<DownloadingFileProperty, String> dingFileName;
    @FXML
    private TableColumn<DownloadingFileProperty, Double> dingSize;
    @FXML
    private TableColumn<DownloadingFileProperty, Double> dingSpeed;
    @FXML
    private TableColumn<DownloadingFileProperty, Long> dingTimeRemain;
    @FXML
    private TableColumn<DownloadingFileProperty, Double> dingProgress;
    @FXML
    private TableView<DownloadingFileProperty> downloaded;
    @FXML
    private TableColumn<DownloadingFileProperty, String> dedFileName;
    @FXML
    private TableColumn<DownloadingFileProperty, Double> dedSize;
    @FXML
    private GridPane topNav;
    @FXML
    private HBox topRight;
    @FXML
    private ImageView minize;
    @FXML
    private ImageView maximize;
    @FXML
    private ImageView close;
    @FXML
    private Text title;
    @FXML
    private ImageView add;
    @FXML
    private ImageView setting;


    public ImageView getAdd() {
        return add;
    }

    /**
     * 用户一般不应该调用此方法
     *
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        downloading.setOpacity(0.7);
        downloaded.setOpacity(0.7);
        topNav.setOpacity(0.7);
    }


    public BorderPane getAll() {
        return all;
    }

    public TableView<DownloadingFileProperty> getDownloading() {
        return downloading;
    }

    public TableColumn<DownloadingFileProperty, String> getDingFileName() {
        return dingFileName;
    }

    public TableColumn<DownloadingFileProperty, Double> getDingSize() {
        return dingSize;
    }

    public TableColumn<DownloadingFileProperty, Double> getDingSpeed() {
        return dingSpeed;
    }

    public TableColumn<DownloadingFileProperty, Long> getDingTimeRemain() {
        return dingTimeRemain;
    }

    public TableColumn<DownloadingFileProperty, Double> getDingProgress() {
        return dingProgress;
    }

    public TableView<DownloadingFileProperty> getDownloaded() {
        return downloaded;
    }

    public TableColumn<DownloadingFileProperty, String> getDedFileName() {
        return dedFileName;
    }

    public TableColumn<DownloadingFileProperty, Double> getDedSize() {
        return dedSize;
    }

    public GridPane getTopNav() {
        return topNav;
    }

    public HBox getTopRight() {
        return topRight;
    }

    public ImageView getMinize() {
        return minize;
    }

    public ImageView getMaximize() {
        return maximize;
    }

    public ImageView getClose() {
        return close;
    }

    public Text getTitle() {
        return title;
    }

    public ImageView getSetting() {
        return setting;
    }
}
