package org.hbin.game.gobang.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.hbin.game.gobang.common.Constant;
import org.hbin.game.gobang.component.ChessBoard;
import org.hbin.game.gobang.component.ControlPanel;
import org.hbin.game.gobang.component.MessagePanel;
import org.hbin.game.gobang.component.TimingPanel;
import org.hbin.game.gobang.component.UserListPanel;
import org.hbin.game.gobang.util.SwingUtil;

public class GobangClient extends JFrame {
    private static final long serialVersionUID = -4841333589150711258L;
    private ChessBoard board;
    
    private TimingPanel timingPanel;
    private UserListPanel userListPanel;
    private MessagePanel messagePanel;
    
    private ControlPanel controlPanel;
    
    private String myName, opName;
    private boolean isConnected;
    
    private Communication c;
    
    public GobangClient() {
        init();
    }
    
    private void init() {
        setTitle("五子棋客户端");
        board = new ChessBoard();
        add(board);
        
        timingPanel = new TimingPanel();
        userListPanel = new UserListPanel();
        messagePanel = new MessagePanel();
        controlPanel = new ControlPanel();
        
        JPanel eastPanel = new JPanel(new BorderLayout());
        eastPanel.add(timingPanel, BorderLayout.NORTH);
        eastPanel.add(userListPanel);
        eastPanel.add(messagePanel, BorderLayout.SOUTH);
        add(eastPanel, BorderLayout.EAST);
        add(controlPanel, BorderLayout.SOUTH);

        ActionMonitor monitor = new ActionMonitor();
        controlPanel.getConnectButton().addActionListener(monitor);
        controlPanel.getJoinButton().addActionListener(monitor);
        controlPanel.getLostButton().addActionListener(monitor);
        controlPanel.getExitButton().addActionListener(monitor);
        controlPanel.getConnectButton().setEnabled(true);
        controlPanel.getJoinButton().setEnabled(false);
        controlPanel.getLostButton().setEnabled(false);
        controlPanel.getExitButton().setEnabled(true);
        
        pack();
        
        setResizable(false);
        SwingUtil.center(this);
    }
    
    private void connect() {
        String ip = controlPanel.getIpField().getText();
        try {
            c = new Communication(this);
            c.connect(ip, Constant.PORT);
            messagePanel.getMessageArea().append("已连接.\n");
            isConnected = true;
            
            controlPanel.getConnectButton().setEnabled(false);
            controlPanel.getJoinButton().setEnabled(true);
            controlPanel.getLostButton().setEnabled(false);
            controlPanel.getExitButton().setEnabled(true);
            
//            DataInputStream in = new DataInputStream(s.getInputStream());
//            String msg = in.readUTF();
//            String[] words = msg.split(":");
//            myName = words[1];
//            userListPanel.userList.add(myName + " ready.");
//            timingPanel.setMyName(myName);
//            messagePanel.getMessageArea().append("My name: " + myName + "\n");                
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public TimingPanel getTimingPanel() {
        return timingPanel;
    }

    public UserListPanel getUserListPanel() {
        return userListPanel;
    }

    public MessagePanel getMessagePanel() {
        return messagePanel;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setOpName(String opName) {
        this.opName = opName;
    }

    class ActionMonitor implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            if(source == controlPanel.getExitButton()) {
                System.exit(0);
            } else if(source == controlPanel.getConnectButton()) {
                connect();
            } else if(source == controlPanel.getLostButton()) {
                
            } else if(source == controlPanel.getJoinButton()) {
                join();
            }
        }
    }
    
    private void join() {
        String select = userListPanel.userList.getSelectedItem();
        if(select == null) {
            messagePanel.getMessageArea().append("请选择一个对手\n");
            return;
        }
        if(!select.endsWith("ready")) {
            messagePanel.getMessageArea().append("请选择ready状态的对手\n");
            return;
        }
        if(select.startsWith(myName)) {
            messagePanel.getMessageArea().append("不能选择自己作为对手\n");
            return;
        }
        int index = select.indexOf(":");
        String name = select.substring(0, index);
        join(name);
    }
    
    private void join(String opName) {
        c.join(opName);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame f = new GobangClient();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
        });
    }
}