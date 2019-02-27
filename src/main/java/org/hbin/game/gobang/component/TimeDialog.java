package org.hbin.game.gobang.component;

import java.awt.Frame;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class TimeDialog {
    private String message;
    private int seconds;
    private JLabel label = new JLabel();
    private JButton confirmButton, cancelButton;
    private JDialog dialog;
    int result = -5;
    
    public int showDialog(Frame parent, String message, int sec) {
        this.message = message;
        seconds = sec;
        label.setText(message);
        label.setBounds(80, 6, 200, 20);
        
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        confirmButton = new JButton("接受");
        confirmButton.setBounds(100, 40, 60, 20);
        confirmButton.addActionListener(e -> {
            result = 0;
            TimeDialog.this.dialog.dispose();
        });
        
        cancelButton = new JButton("拒绝");
        cancelButton.setBounds(190, 40, 60, 20);
        cancelButton.addActionListener(e -> {
            result = 1;
            TimeDialog.this.dialog.dispose();
        });
        
        dialog = new JDialog(parent, true);
        dialog.setTitle("提示：本窗口将在" + seconds + "秒后自动关闭");
        dialog.setLayout(null);
        dialog.add(label);
        dialog.add(confirmButton);
        dialog.add(cancelButton);
        
        s.scheduleAtFixedRate(() -> {
            TimeDialog.this.seconds --;
            if(TimeDialog.this.seconds <= 0) {
                TimeDialog.this.dialog.dispose();
            } else {
                dialog.setTitle("提示：本窗口将在" + seconds + "秒后自动关闭");
            }
        }, 1, 1, TimeUnit.SECONDS);
        
        dialog.pack();
        dialog.setSize(350, 100);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        
        return result;
    }
}
