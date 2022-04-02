//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import gui.Nios2SimPanelMessage.message_type;

import javax.swing.*;
import java.awt.*;

class MessageCellRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 1L;

    MessageCellRenderer() {
    }

    public Component getListCellRendererComponent(JList var1, Object var2, int var3, boolean var4, boolean var5) {
        String var6 = var2.toString();
        this.setText(var6);
        if (var4) {
            this.setBackground(var1.getSelectionBackground());
        } else {
            this.setBackground(var1.getBackground());
        }

        MessageListModel var7 = (MessageListModel)var1.getModel();
        if (var7.getType(var3) == message_type.ERROR) {
            this.setForeground(new Color(200, 0, 0));
        } else if (var7.getType(var3) == message_type.WARNING) {
            this.setForeground(new Color(100, 100, 0));
        } else if (var7.getType(var3) == message_type.INFO) {
            this.setForeground(var1.getForeground());
        }

        this.setEnabled(var1.isEnabled());
        this.setFont(var1.getFont());
        this.setOpaque(true);
        return this;
    }
}
