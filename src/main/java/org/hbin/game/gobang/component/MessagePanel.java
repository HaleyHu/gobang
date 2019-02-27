package org.hbin.game.gobang.component;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MessagePanel extends JPanel {
    private static final long serialVersionUID = -6298537952001527227L;
    private JTextArea messageArea;
    
    public MessagePanel() {
        init();
    }
    
    private void init() {
        messageArea = new JTextArea(12, 20);
        setLayout(new BorderLayout());
        add(new JScrollPane(messageArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
    }

    public JTextArea getMessageArea() {
        return messageArea;
    }
}
