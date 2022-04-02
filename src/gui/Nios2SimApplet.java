//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import nios2sim.Nios2Sim;

import javax.swing.*;

public class Nios2SimApplet extends JApplet {
    private static final long serialVersionUID = 1L;
    Nios2Sim nios2Sim;

    public Nios2SimApplet() {
    }

    public void init() {
        super.init();
        this.setSize(400, 400);
        this.nios2Sim = new Nios2Sim(this);
        this.setContentPane(this.nios2Sim.getPanel());
        this.setJMenuBar(this.nios2Sim.getMenuBar());
        this.repaint();
    }
}
