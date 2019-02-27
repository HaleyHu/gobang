package org.hbin.game.gobang.component;

import java.awt.BorderLayout;
import java.awt.List;
import javax.swing.JPanel;

public class UserListPanel extends JPanel {
    private static final long serialVersionUID = 28792197545844077L;
    public List userList;
    
    public UserListPanel() {
        init();
    }

    private void init() {
        userList = new List(8);
        setLayout(new BorderLayout());
        add(userList);
    }
}
