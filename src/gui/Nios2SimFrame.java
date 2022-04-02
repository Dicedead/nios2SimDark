//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import nios2sim.Nios2Sim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;

public class Nios2SimFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    Nios2Sim nios2Sim;

    public static final Color DARK_COLOR = Color.decode("#06011e"); //background color
    public static final Color DARK_MODE_FONT_COLOR = Color.WHITE; //general font color
    public static final Color DARK_MODE_INSTRUCTIONS_COLOR = Color.decode("#e75350"); //addi, sti, etc
    public static final Color DARK_MODE_REGISTERS_COLOR = Color.decode("#1cd6f2"); //t1, zero, etc

    public Nios2SimFrame(String[] var1) {
        this.setSize(800, 600);
        this.nios2Sim = new Nios2Sim(this);
        this.setContentPane(this.nios2Sim.getPanel());
        this.setJMenuBar(this.nios2Sim.getMenuBar());

        var setToBlack = List.of(this.getContentPane(), this.getJMenuBar(), this);
        setToBlack.forEach(x -> { x.setBackground(DARK_COLOR);
        x.setForeground(DARK_MODE_FONT_COLOR);});

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent var1) {
                Nios2SimFrame.this.nios2Sim.exit();
            }
        });
        this.setDefaultCloseOperation(0);
        this.setVisible(true);
        if (var1.length > 0) {
            try {
                this.nios2Sim.open(new File(var1[0]));
            } catch (Exception var3) {
            }
        }

    }

    public static void main(String[] var0) {
        String var1 = System.getProperty("os.name").toLowerCase();
        boolean var2 = var1.startsWith("mac os x");
        if (var2) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        }

        new Nios2SimFrame(var0);
    }
}
