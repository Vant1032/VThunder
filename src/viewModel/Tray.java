package viewModel;

import logic.ExitCommand;
import logic.Exitable;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

/**
 * 用于显示状态栏以及相关事件
 *
 * @author Vant
 * @version 2017/10/9 上午 8:11
 */
public class Tray implements Exitable {
    private PopupMenu popupMenu;
    private volatile boolean exited = false;
    @NotNull
    private TrayIcon trayIcon = new TrayIcon(ImageIO.read(this.getClass().getClassLoader().getResource("img/迅雷.png")));

    /**
     * @throws IOException  获取图标失败
     * @throws AWTException if the desktop system tray is missing
     */
    public Tray() throws AWTException, IOException {
        ExitCommand.getExitCommand().addListener(this);
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("vThunder正在运行");

        popupMenu = new PopupMenu();
        addItem(popupMenu);
        trayIcon.setPopupMenu(popupMenu);
        SystemTray systemTray = SystemTray.getSystemTray();
        systemTray.add(trayIcon);
    }

    private void addItem(@NotNull PopupMenu popupMenu) {
        MenuItem exit = new MenuItem("退出");//退出
        exit.addActionListener(e -> {
            ExitCommand.getExitCommand().informAll();
        });

        popupMenu.add(exit);
    }


    public void addTrayItem(String name, @NotNull Runnable onClickEvent) {
        MenuItem item = new MenuItem(name);
        item.addActionListener((ActionEvent e) -> {
            onClickEvent.run();
        });
        popupMenu.insert(item, 0);
    }

    public void setTooltip(String text) {
        trayIcon.setToolTip(text);
    }

    public void removeTrayIcon() {
        SystemTray.getSystemTray().remove(trayIcon);
    }

    @Override
    public void onExit() {
        exited = true;
        removeTrayIcon();
        System.out.println("Tray.onExit");
    }

    @Override
    public boolean isExited() {
        return exited;
    }
}