package org.hbin.game.gobang.util;

public class StringUtil {
    /**
     * 将时间格式化为%02d:%02d:%02d
     * @param time
     * @return
     */
    public static String formatTime(int time) {
        int h = time / 3600;
        int m = time % 3600 / 60;
        int s = time % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
    
    public static void main(String[] args) {
        System.out.println(StringUtil.formatTime(65));
    }
}
