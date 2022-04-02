//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class Nios2SimPanelMessage extends JList {
    private static final long serialVersionUID = 1L;
    private MessageListModel messagesModel;
    private JScrollPane messagesScroll;
    private Nios2SimPanel panel;

    public Nios2SimPanelMessage(Nios2SimPanel var1) {
        this.panel = var1;
        this.messagesModel = new MessageListModel();
        this.setModel(this.messagesModel);
        this.setCellRenderer(new MessageCellRenderer());
        this.messagesScroll = new JScrollPane(this);
        this.setAutoscrolls(true);
        this.addMouseListener(new MessageMouseListener());
        this.setBackground(DARK_COLOR);
        this.setForeground(DARK_MODE_FONT_COLOR);
    }

    public JScrollPane getScrollPane() {
        return this.messagesScroll;
    }

    public void addMessage(int var1, message_type var2, String var3) {
        this.messagesModel.add(var1, var2, var3);
        int var4 = (int)this.getBounds().getHeight() + 40;
        this.scrollRectToVisible(new Rectangle(new Point(0, var4)));
    }

    public void clear() {
        this.messagesModel.clear();
    }

    private class MessageMouseListener extends MouseAdapter {
        private MessageMouseListener() {
        }

        public void mouseClicked(MouseEvent var1) {
            if (var1.getClickCount() == 2 && !Nios2SimPanelMessage.this.panel.isSimulating()) {
                int var2 = Nios2SimPanelMessage.this.messagesModel.getLine(Nios2SimPanelMessage.this.locationToIndex(var1.getPoint()));
                if (var2 >= 0) {
                    Nios2SimPanelMessage.this.panel.showLine(var2);
                }
            }

        }
    }

    public static enum message_type {
        INFO,
        WARNING,
        ERROR;

        private message_type() {
        }
    }
}
