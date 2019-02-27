package org.hbin.game.gobang.client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;
import org.hbin.game.gobang.common.Command;
import org.hbin.game.gobang.component.TimeDialog;

public class Communication {
    private GobangClient client;
    private DataOutputStream out;
    private Socket s;

    public Communication(GobangClient client) {
        this.client = client;
    }

    public void connect(String ip, int port) {
        try {
            s = new Socket(ip, port);
            out = new DataOutputStream(s.getOutputStream());
            new ReceiveThread(s).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void join(String opName) {
        try {
            out.writeUTF(Command.JOIN + ":" + opName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ReceiveThread extends Thread {
        private Socket s;
        private DataInputStream in;
        private DataOutputStream out;
        private String msg;

        public ReceiveThread(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    in = new DataInputStream(s.getInputStream());
                    out = new DataOutputStream(s.getOutputStream());
                    msg = in.readUTF();
                    if(msg != null) {
                        String[] words = msg.split(":");
                        switch (words[0]) {
                            case Command.TELL_NAME:
                                client.setMyName(words[1]);
                                client.getUserListPanel().userList
                                        .add(client.getMyName() + ":ready");
                                client.getTimingPanel().setMyName(client.getMyName());
                                client.getMessagePanel().getMessageArea()
                                        .append("My Name: " + client.getMyName() + "\n");
                                break;
                            case Command.ADD:
                                client.getUserListPanel().userList.add(words[1] + ":"
                                        + ("true".equals(words[2]) ? "playing" : "ready"));
                                client.getMessagePanel().getMessageArea()
                                        .append(words[1] + ":" + words[2] + "\n");
                                break;
                            case Command.JOIN:
                                String name = words[1];
                                TimeDialog d = new TimeDialog();
                                int select = d.showDialog(client, name + "邀请您下棋，是否接受？", 100);
                                String msg =
                                        (select == 0 ? Command.AGREE : Command.REFUSE) + ":" + name;
                                out.writeUTF(msg);
                                break;
                            case Command.REFUSE:
                                name = words[1];
                                JOptionPane.showMessageDialog(client, name + "拒绝了您的邀请！");
                                break;
                            case Command.CHANGE:
                                for (int i = 0; i < client.getUserListPanel().userList.getItemCount(); i++) {
                                    if(client.getUserListPanel().userList.getItem(i).startsWith(words[1])) {
                                        client.getUserListPanel().userList.replaceItem(words[1] + ":" + words[2], i);
                                    }
                                }
                                client.getMessagePanel().getMessageArea().append(words[1] + " " + words[2] + "\n");
                                break;
                            case Command.GUESS_COLOR:
                                client.getBoard().setGamming(true);
                                client.setOpName(words[2]);
                                client.getTimingPanel().setOpName(words[2]);

                                switch (words[1]) {
                                    case "black":
                                        client.getTimingPanel().setMyIcon(Color.BLACK);
                                        client.getTimingPanel().setOpIcon(Color.WHITE);
                                        client.getBoard().setBlack(true);
                                        client.getBoard().setGamming(true);
                                        break;
                                    case "white":
                                        client.getTimingPanel().setMyIcon(Color.WHITE);
                                        client.getTimingPanel().setOpIcon(Color.BLACK);
                                        client.getBoard().setBlack(false);
                                        client.getBoard().setGamming(false);
                                        break;
                                }
                                
                                client.getControlPanel().getJoinButton().setEnabled(false);
                                client.getControlPanel().getLostButton().setEnabled(true);
                                client.getControlPanel().getExitButton().setEnabled(false);
                                client.getMessagePanel().getMessageArea().append("My color is " + words[1] + "\n");
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
