package org.hbin.game.gobang.component;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.hbin.game.gobang.util.StringUtil;

public class TimingPanel extends JPanel {
    private static final long serialVersionUID = -939110794237540686L;
    private JLabel myIcon, opIcon, myName, opName, myTime, opTime;
    private Icon blackIcon, whiteIcon;
    
    public TimingPanel() {
        init();
    }

    private void init() {
        blackIcon = new ImageIcon(getClass().getResource("/images/gobang/black.png"));
        whiteIcon = new ImageIcon(getClass().getResource("/images/gobang/white.png"));

        myIcon = new JLabel(blackIcon, JLabel.CENTER);
        opIcon = new JLabel(whiteIcon, JLabel.CENTER);

        myName = new JLabel("My Name", JLabel.CENTER);
        opName = new JLabel("Op Name", JLabel.CENTER);

        myTime = new JLabel("00:00:00");
        opTime = new JLabel("00:00:00");

        JPanel myInfoPanel = new JPanel(new GridLayout(2, 1));
        myInfoPanel.add(myName);
        myInfoPanel.add(myTime);

        JPanel opInfoPanel = new JPanel(new GridLayout(2, 1));
        opInfoPanel.add(opName);
        opInfoPanel.add(opTime);
        
        add(myIcon);
        add(myInfoPanel);
        add(opIcon);
        add(opInfoPanel);
    }

    public void setMyIcon(Color color) {
        myIcon.setIcon(Color.BLACK == color ? blackIcon : whiteIcon);
    }

    public void setOpIcon(Color color) {
        opIcon.setIcon(Color.BLACK == color ? blackIcon : whiteIcon);
    }

    public void setMyName(String name) {
        myName.setText(name);
    }

    public void setOpName(String name) {
        opName.setText(name);
    }

    public void setMyTime(int time) {
        myTime.setText(StringUtil.formatTime(time));
    }

    public void setOpTime(int time) {
        opTime.setText(StringUtil.formatTime(time));
    }
}
