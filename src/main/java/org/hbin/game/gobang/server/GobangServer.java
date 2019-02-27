package org.hbin.game.gobang.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.hbin.game.gobang.common.Command;
import org.hbin.game.gobang.common.Constant;
import org.hbin.game.gobang.util.SwingUtil;

public class GobangServer extends JFrame {
    private static final long serialVersionUID = 2548528394555301344L;
    
    private static int clientNumber = 0;
    private static int clientIndex = 0;
    
    private JLabel statusLabel;
    private JTextArea messageArea;
    private JButton closeButton;
    
    private ServerSocket server;
    private List<Client> clients = new ArrayList<>();
    
    public GobangServer() {
        init();
    }
    
    private void init() {
        setTitle("Java五子棋服务器");
        
        statusLabel = new JLabel("当前连接数：");
        messageArea = new JTextArea(22, 50);
        messageArea.setEditable(false);
        closeButton = new JButton("关闭服务器");
        
        add(statusLabel, BorderLayout.NORTH);
        add(new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
        add(closeButton, BorderLayout.SOUTH);
        
        closeButton.addActionListener(e -> {
            if(e.getSource() == closeButton) {
                System.exit(0);
            }
        });
        
//        messageArea.append("服务器初始化 ……\n");
        
        pack();
        setResizable(false);
        SwingUtil.center(this);
    }
    
    public void start() {
        messageArea.append("服务器启动中 ……\n");
        new ServerThread(Constant.PORT).start();
    }
    
    private void addAllUser2me(Client c) {
        DataOutputStream out = null;
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i) != c) {
                try {
                    String msg = Command.ADD + ":" + clients.get(i).name + ":" + clients.get(i).gaming;
                    out = new DataOutputStream(c.s.getOutputStream());
                    out.writeUTF(msg);
                    out.flush();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void addMe2AllUser(Client c) {
        DataOutputStream out = null;
        for (int i = 0; i < clients.size(); i++) {
            if(clients.get(i) != c) {
                try {
                    String msg = Command.ADD + ":" + c.name + ":" + c.gaming;
                    out = new DataOutputStream(clients.get(i).s.getOutputStream());
                    out.writeUTF(msg);
                    out.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private void tellName(Client c) {
        DataOutputStream out = null;
        try {
            String msg = Command.TELL_NAME + ":" + c.name;
            out = new DataOutputStream(c.s.getOutputStream());
            out.writeUTF(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    class Client {
        String name;
        Socket s;
        boolean gaming;
        Client opponent;
        
        public Client(String name, Socket s) {
            this.name = name;
            this.s = s;
            gaming = false;
            opponent = null;
        }
    }
    
    class ServerThread extends Thread {
        private int port;

        public ServerThread(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                server = new ServerSocket(port);
                messageArea.append("服务器已就绪 ……\n");
                while(true) {
                    Socket s = server.accept();
                    System.out.println("Socket connecting ……");
                    clientNumber ++;
                    
                    Client client = new Client("Player" + clientIndex ++, s);
                    clients.add(client);
                    
                    statusLabel.setText("当前连接数：" + clientNumber);
                    String msg = s.getInetAddress().getHostAddress() + " " + client.name + "\n";
                    messageArea.append(msg);
                    
                    tellName(client);
                    addAllUser2me(client);
                    addMe2AllUser(client);
                    
                    new ClientThread(client).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    class ClientThread extends Thread {
        private Client c;
        private DataInputStream in;
        private DataOutputStream out;
        
        public ClientThread(Client c) {
            this.c = c;
        }
        
        @Override
        public void run() {
            while(true) {
                try {
                    in = new DataInputStream(c.s.getInputStream());
                    String msg = in.readUTF();
                    String[] words = msg.split(":");
                    switch(words[0]) {
                    case Command.JOIN:
                        String opName = words[1];
                        for (int i = 0; i < clients.size(); i++) {
                            if(clients.get(i).name.equals(opName)) {
                                out = new DataOutputStream(clients.get(i).s.getOutputStream());
                                out.writeUTF(Command.JOIN + ":" + c.name);
                                break;
                            }
                        }
                        break;
                    case Command.REFUSE:
                        opName = words[1];
                        for (int i = 0; i < clients.size(); i++) {
                            if(clients.get(i).name.equals(opName)) {
                                out = new DataOutputStream(clients.get(i).s.getOutputStream());
                                out.writeUTF(Command.REFUSE + ":" + c.name);
                                break;
                            }
                        }
                        break;
                    case Command.AGREE:
                        c.gaming = true;
                        opName = words[1];
                        for (int i = 0; i < clients.size(); i++) {
                            if(clients.get(i).name.equals(opName)) {
                                clients.get(i).gaming = true;
                                clients.get(i).opponent = c;
                                c.opponent = clients.get(i);
                                break;
                            }
                        }
                        
                        //改变所有客户端中这两个用户状态为游戏中
                        for (int i = 0; i < clients.size(); i++) {
                            out = new DataOutputStream(clients.get(i).s.getOutputStream());
                            out.writeUTF(Command.CHANGE + ":" + c.name + ":playing");
                            out.writeUTF(Command.CHANGE + ":" + opName + ":playing");
                        }
                        
                        //随机分配黑棋、白棋
                        int r = (int) (Math.random() * 2);
                        if(r == 0) {
                            out = new DataOutputStream(c.s.getOutputStream());
                            out.writeUTF(Command.GUESS_COLOR + ":black:" + opName);
                            out = new DataOutputStream(c.opponent.s.getOutputStream());
                            out.writeUTF(Command.GUESS_COLOR + ":white:" + c.name);
                        } else {
                            out = new DataOutputStream(c.s.getOutputStream());
                            out.writeUTF(Command.GUESS_COLOR + ":white:" + opName);
                            out = new DataOutputStream(c.opponent.s.getOutputStream());
                            out.writeUTF(Command.GUESS_COLOR + ":black:" + c.name);
                        }
                        messageArea.append(c.name + " playing\n");
                        messageArea.append(opName + " playing\n");
                        break;
                    case Command.GO:
                        out = new DataOutputStream(c.opponent.s.getOutputStream());
                        out.writeUTF(msg);
                        messageArea.append(c.name + " " + msg + "\n");
                        break;
                    case Command.WIN:
                        //更新所有客户端的客户列表中，这两个客户的状态
                        for (int i = 0; i < clients.size(); i++) {
                            out = new DataOutputStream(clients.get(i).s.getOutputStream());
                            out.writeUTF(Command.CHANGE + ":" + c.name + ":false");
                            out.writeUTF(Command.CHANGE + ":" + c.opponent.name + ":false");
                        }
                        out = new DataOutputStream(c.s.getOutputStream());
                        out.writeUTF(Command.TELL_RESULT + ":win");
                        out = new DataOutputStream(c.opponent.s.getOutputStream());
                        out.writeUTF(Command.TELL_RESULT + ":lose");
                        
                        c.gaming = false;
                        c.opponent.gaming = false;
                        messageArea.append(c.name + " win\n");
                        messageArea.append(c.opponent.name + " lose\n");
                        break;
                    case Command.GIVEUP:
                        for (int i = 0; i < clients.size(); i++) {
                            out = new DataOutputStream(clients.get(i).s.getOutputStream());
                            out.writeUTF(Command.CHANGE + ":" + c.name + ":false");
                            out.writeUTF(Command.CHANGE + ":" + c.opponent.name + ":false");
                        }
                        
                        out = new DataOutputStream(c.s.getOutputStream());
                        out.writeUTF(Command.TELL_RESULT + ":lose");
                        out = new DataOutputStream(c.opponent.s.getOutputStream());
                        out.writeUTF(Command.TELL_RESULT + ":win");
                        c.gaming = false;
                        c.opponent.gaming = false;
                        messageArea.append(c.name + " loss\n");
                        messageArea.append(c.opponent.name + " win\n");
                        break;
                    case Command.QUIT:
                        for (int i = 0; i < clients.size(); i++) {
                            if(clients.get(i) != c) {
                                out = new DataOutputStream(clients.get(i).s.getOutputStream());
                                out.writeUTF(Command.DELETE + ":" + c.name);
                            }
                        }
                        clients.remove(c);
                        messageArea.append(c.name + " quit\n");
                        clientNumber --;
                        statusLabel.setText("当前连接数：" + clientNumber);
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GobangServer f = new GobangServer();
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setVisible(true);
            
            f.start();
        });
    }
}
