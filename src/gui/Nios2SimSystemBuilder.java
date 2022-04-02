//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Nios2SimSystemBuilder extends JDialog implements ActionListener {
    private static final long serialVersionUID = 1L;

    public Nios2SimSystemBuilder(JFrame var1, String var2, String var3) {
        super(var1, var2, true);
        if (var1 != null) {
            Dimension var4 = var1.getSize();
            Point var5 = var1.getLocation();
            this.setLocation(var5.x + var4.width / 4, var5.y + var4.height / 4);
        }

        String[] var10 = new String[]{"ROM", "RAM", "Buttons", "PIO"};
        JList var11 = new JList(var10);
        JPanel var6 = new JPanel();
        var6.add(new JLabel(var3));
        var6.add(var11, "West");
        this.getContentPane().add(var6);
        JPanel var7 = new JPanel();
        JButton var8 = new JButton("OK");
        var7.add(var8);
        JButton var9 = new JButton("Cancel");
        var7.add(var8, "East");
        var7.add(var9, "West");
        var8.addActionListener(this);
        this.getContentPane().add(var7, "South");
        this.setDefaultCloseOperation(2);
        this.pack();
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent var1) {
        this.setVisible(false);
        this.dispose();
    }
}
