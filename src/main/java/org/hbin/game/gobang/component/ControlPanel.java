package org.hbin.game.gobang.component;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlPanel extends JPanel {
    private static final long serialVersionUID = -4583182024156832465L;
    private JTextField ipField;
    private JButton connectButton, joinButton, lostButton, exitButton;
    
    public ControlPanel() {
        init();
    }
    
    private void init() {
        JLabel ipLabel = new JLabel("服务器IP：", JLabel.LEFT);
        ipField = new JTextField("127.0.0.1", 12);
        connectButton = new JButton("连接主机");
        joinButton = new JButton("加入游戏");
        lostButton = new JButton("认输");
        exitButton = new JButton("退出");
        
        joinButton.setEnabled(false);
        lostButton.setEnabled(false);
        
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(200, 200, 200));
        add(ipLabel);
        add(ipField);
        add(connectButton);
        add(joinButton);
        add(lostButton);
        add(exitButton);
    }

    public JTextField getIpField() {
        return ipField;
    }

    public JButton getConnectButton() {
        return connectButton;
    }

    public JButton getJoinButton() {
        return joinButton;
    }

    public JButton getLostButton() {
        return lostButton;
    }

    public JButton getExitButton() {
        return exitButton;
    }
}
