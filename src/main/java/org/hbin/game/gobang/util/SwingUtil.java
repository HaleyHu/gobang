package org.hbin.game.gobang.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class SwingUtil {
    /**
     * 窗口居中显示
     * @param w
     */
    public static void center(Window w) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - w.getWidth()) / 2;
        int y = (screenSize.height - w.getHeight()) / 2;
        w.setLocation(x, y);
    }
}
