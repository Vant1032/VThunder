package viewModel;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import logic.ExitCommand;
import logic.Exitable;
import logic.StyleChangeable;
import org.jetbrains.annotations.NotNull;

import java.awt.*;


/**
 * 先实现为采用扇形表示进度
 *
 * @author Vant
 * @version 2017/10/9 下午 10:15
 */
public class FloatWindow implements Exitable, StyleChangeable {
    private Circle circle;
    private Stage floatStage;
    private Text pgr;
    private Group rootGroup;
    private double radius = 40;
    private boolean exited = false;
    @NotNull
    private Arc cover = new Arc(radius, radius, radius, radius, 180, 0);

    private double initX, initY;

    public FloatWindow() {
        ExitCommand.getExitCommand().addListener(this);

        floatStage = new Stage(StageStyle.TRANSPARENT);
        VBox vBox = new VBox(0);
        vBox.setLayoutX(0);
        vBox.setLayoutY(0);
        vBox.setPrefWidth(radius * 2);
        vBox.setPrefHeight(radius * 2);

        circle = new Circle(radius, radius, radius);
        circle.setFill(Color.SNOW);
        circle.setOpacity(0.9);
        pgr = new Text("0.0%");
        pgr.setTextAlignment(TextAlignment.CENTER);


        rootGroup = new Group();
        rootGroup.getChildren().addAll(circle, cover, vBox);
        cover.setType(ArcType.CHORD);
        cover.setFill(Color.rgb(55, 169, 254, 1));

        vBox.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(pgr);


        floatStage.setX(Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 300);
        floatStage.setY(50);

        addEvent();

        Scene scene = new Scene(rootGroup, Color.TRANSPARENT);
        floatStage.setScene(scene);
        floatStage.setAlwaysOnTop(true);

        floatStage.show();

        //测试线程
//        new Thread(){
//            @Override
//            public void run() {
//                for(double i = 0; i <= 1.1; i += 0.01){
//                    try {
//                        Thread.sleep(200);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    setProgress(i);
//                }
//            }
//        }.start();
    }

    private void addEvent() {
        //添加移动代码
        rootGroup.setOnMousePressed(event -> {
            initX = event.getScreenX() - floatStage.getX();
            initY = event.getScreenY() - floatStage.getY();
        });
        rootGroup.setOnMouseDragged(event -> {
            floatStage.setX(event.getScreenX() - initX);
            floatStage.setY(event.getScreenY() - initY);
        });
        //-----------------------


    }

    /**
     * 此函数只管如何显示进度
     *
     * @param progress 范围从0 到 1
     */
    public void setProgress(double progress) {
        assert progress <= 1 && progress >= 0;

        //覆盖的弧------------------------
        double k = radius * progress * 2;
        double length = 2 * Math.acos((radius - k) / radius) * (180 / Math.PI);
        cover.setLength(length);
        double start = (540 - length) / 2;
        cover.setStartAngle(start);
        //-------------------------------
        if (progress < 1) {
            pgr.setText(String.format("%.1f%%", progress * 100));
        } else {
            pgr.setText("下载完成");
        }
    }

    /**
     *
     */
    public void runEndEvent() {

    }

    public Stage getFloatStage() {
        return floatStage;
    }

    @Override
    public void onExit() {
        System.out.println("FloatWindow.onExit 111");
        Platform.runLater(() -> floatStage.hide());
        System.out.println("FloatWindow.onExit");
        exited = true;
    }

    @Override
    public boolean isExited() {
        return exited;
    }

    @Override
    public void setStyle(String style) {
        floatStage.getScene().getRoot().setStyle(style);
    }
}
