//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import nios2sim.Nios2Sim;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class Nios2SimMenuBar extends JMenuBar {
    private static final long serialVersionUID = 1L;
    private Nios2Sim nios2Sim;
    private JMenu menuEdit;
    private JMenu menuSystem;
    private JMenu menuNiosIIEdit;
    private JMenu menuNiosIISim;
    private JMenu pipelineMenu;
    private JMenu menuForwarding;
    private JMenuItem menuItemOpen;
    private JMenuItem menuItemNew;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemSaveAs;
    private JMenuItem menuItemSaveHex;
    private JMenuItem menuItemUndo;
    private JMenuItem menuItemRedo;
    private JMenuItem menuItemCompile;
    private JMenuItem menuItemSimulate;
    private JMenuItem menuItemStep;
    private JMenuItem menuItemRun;
    private JMenuItem menuItemEndSimulation;
    private JMenuItem menuItemResimulate;
    private JMenuItem menuItemLoadSystem;
    private JMenuItem menuItemReloadSystem;
    private JCheckBoxMenuItem pipelineItem;
    private JCheckBoxMenuItem menuItemMulticycle;
    private JCheckBoxMenuItem menuItemStalls;
    private JCheckBoxMenuItem menuItemFlushes;
    private JCheckBoxMenuItem menuItemForwardWD;
    private JCheckBoxMenuItem menuItemForwardEE;
    private JCheckBoxMenuItem menuItemForwardME;

    public Nios2SimMenuBar(Nios2Sim var1) {
        this.nios2Sim = var1;
        JMenu var2 = new JMenu("File");
        var2.setForeground(DARK_MODE_FONT_COLOR);
        var2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
                Nios2SimMenuBar.this.updateMenuState();
            }
        });
        this.menuItemNew = new JMenuItem("New");
        this.menuItemNew.setAccelerator(KeyStroke.getKeyStroke(78, 2));
        this.menuItemNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.newFile();
            }
        });
        var2.add(this.menuItemNew);
        this.menuItemOpen = new JMenuItem("Open...");
        this.menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        this.menuItemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.open();
            }
        });
        var2.add(this.menuItemOpen);
        this.menuItemSave = new JMenuItem("Save");
        this.menuItemSave.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        this.menuItemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.save();
            }
        });
        var2.add(this.menuItemSave);
        this.menuItemSaveAs = new JMenuItem("Save As...");
        this.menuItemSaveAs.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.saveAs();
            }
        });
        var2.add(this.menuItemSaveAs);
        this.menuItemSaveHex = new JMenuItem("Export to Hex File...");
        this.menuItemSaveHex.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.saveHex();
            }
        });
        var2.add(this.menuItemSaveHex);
        JMenuItem var3 = new JMenuItem("Exit");
        var3.setAccelerator(KeyStroke.getKeyStroke(81, 2));
        var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.exit();
            }
        });
        var2.add(var3);
        this.add(var2);
        this.menuEdit = new JMenu("Edit");
        this.menuEdit.setForeground(DARK_MODE_FONT_COLOR);
        this.menuEdit.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
                Nios2SimMenuBar.this.updateMenuState();
            }
        });
        this.menuItemUndo = new JMenuItem("Undo");
        this.menuItemUndo.setAccelerator(KeyStroke.getKeyStroke(90, 2));
        this.menuItemUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().undo();
            }
        });
        this.menuEdit.add(this.menuItemUndo);
        this.menuItemRedo = new JMenuItem("Redo");
        this.menuItemRedo.setAccelerator(KeyStroke.getKeyStroke(89, 2));
        this.menuItemRedo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().redo();
            }
        });
        this.menuEdit.add(this.menuItemRedo);
        this.menuEdit.addSeparator();
        var3 = new JMenuItem("Find/Replace...");
        var3.setAccelerator(KeyStroke.getKeyStroke(70, 2));
        var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().find();
            }
        });
        this.menuEdit.add(var3);
        this.add(this.menuEdit);
        this.menuSystem = new JMenu("System");
        this.menuSystem.setForeground(DARK_MODE_FONT_COLOR);
        this.menuSystem.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent var1) {
                Nios2SimMenuBar.this.updateMenuState();
            }
        });
        this.menuItemLoadSystem = new JMenuItem("Load...");
        this.menuItemLoadSystem.setAccelerator(KeyStroke.getKeyStroke(76, 2));
        this.menuItemLoadSystem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.load();
            }
        });
        this.menuSystem.add(this.menuItemLoadSystem);
        this.menuItemReloadSystem = new JMenuItem("Reload");
        this.menuItemReloadSystem.setAccelerator(KeyStroke.getKeyStroke(82, 2));
        this.menuItemReloadSystem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.reload();
            }
        });
        this.menuSystem.add(this.menuItemReloadSystem);
        this.add(this.menuSystem);
        this.menuNiosIIEdit = new JMenu("Nios II");
        this.menuNiosIIEdit.setForeground(DARK_MODE_FONT_COLOR);
        this.menuItemCompile = new JMenuItem("Assemble");
        this.menuItemCompile.setAccelerator(KeyStroke.getKeyStroke(49, 2));
        this.menuItemCompile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().compile();
            }
        });
        this.menuNiosIIEdit.add(this.menuItemCompile);
        this.menuItemSimulate = new JMenuItem("Start Simulation");
        this.menuItemSimulate.setAccelerator(KeyStroke.getKeyStroke(50, 2));
        this.menuItemSimulate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().simulate();
            }
        });
        this.menuNiosIIEdit.add(this.menuItemSimulate);
        this.add(this.menuNiosIIEdit);
        this.menuNiosIISim = new JMenu("Nios II");
        this.menuItemResimulate = new JMenuItem("Restart Simulation");
        this.menuItemResimulate.setAccelerator(KeyStroke.getKeyStroke(50, 2));
        this.menuItemResimulate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().restartSimulation();
            }
        });
        this.menuNiosIISim.add(this.menuItemResimulate);
        this.menuItemEndSimulation = new JMenuItem("End simulation");
        this.menuItemEndSimulation.setAccelerator(KeyStroke.getKeyStroke(51, 2));
        this.menuItemEndSimulation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().endSimulation();
                Nios2SimMenuBar.this.repaint();
            }
        });
        this.menuNiosIISim.add(this.menuItemEndSimulation);
        this.menuNiosIISim.addSeparator();
        this.pipelineMenu = new JMenu("Pipeline Simulator");
        this.pipelineItem = new JCheckBoxMenuItem("5-stage pipeline");
        this.pipelineItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                int var2 = JOptionPane.showConfirmDialog(Nios2SimMenuBar.this.nios2Sim.getPanel(), "This operation will restart the simulation. Do you want to continue?", Nios2SimMenuBar.this.pipelineItem.isSelected() ? "Enabling the pipeline" : "Disabling the pipeline", 0, 2);
                if (var2 == 0) {
                    Nios2SimMenuBar.this.menuItemMulticycle.setSelected(false);
                    Nios2SimMenuBar.this.nios2Sim.getPanel().setPipelineEnabled(Nios2SimMenuBar.this.pipelineItem.isSelected());
                    Nios2SimMenuBar.this.nios2Sim.getPanel().restartSimulation();
                } else {
                    Nios2SimMenuBar.this.pipelineItem.setSelected(!Nios2SimMenuBar.this.pipelineItem.isSelected());
                }

                Nios2SimMenuBar.this.updateMenuState();
            }
        });
        this.pipelineMenu.add(this.pipelineItem);
        this.pipelineMenu.addSeparator();
        this.menuForwarding = new JMenu("Enable forwarding paths");
        this.pipelineMenu.add(this.menuForwarding);
        this.menuItemForwardEE = new JCheckBoxMenuItem("E->E");
        this.menuItemForwardEE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().getSimulator().setForwardingEEEnabled(Nios2SimMenuBar.this.menuItemForwardEE.isSelected());
                Nios2SimMenuBar.this.nios2Sim.getPanel().repaint();
            }
        });
        this.menuForwarding.add(this.menuItemForwardEE);
        this.menuItemForwardME = new JCheckBoxMenuItem("M->E");
        this.menuItemForwardME.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().getSimulator().setForwardingMEEnabled(Nios2SimMenuBar.this.menuItemForwardME.isSelected());
                Nios2SimMenuBar.this.nios2Sim.getPanel().repaint();
            }
        });
        this.menuForwarding.add(this.menuItemForwardME);
        this.menuItemForwardWD = new JCheckBoxMenuItem("W->D");
        this.menuItemForwardWD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().getSimulator().setForwardingWDEnabled(Nios2SimMenuBar.this.menuItemForwardWD.isSelected());
                Nios2SimMenuBar.this.nios2Sim.getPanel().repaint();
            }
        });
        this.menuForwarding.add(this.menuItemForwardWD);
        this.menuItemStalls = new JCheckBoxMenuItem("Enable stalls");
        this.menuItemStalls.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().getSimulator().setStallEnabled(Nios2SimMenuBar.this.menuItemStalls.isSelected());
                Nios2SimMenuBar.this.nios2Sim.getPanel().repaint();
            }
        });
        this.pipelineMenu.add(this.menuItemStalls);
        this.menuItemFlushes = new JCheckBoxMenuItem("Enable flushes");
        this.menuItemFlushes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().getSimulator().setFlushesEnabled(Nios2SimMenuBar.this.menuItemFlushes.isSelected());
                Nios2SimMenuBar.this.nios2Sim.getPanel().repaint();
            }
        });
        this.pipelineMenu.add(this.menuItemFlushes);
        this.menuNiosIISim.add(this.pipelineMenu);
        this.menuItemMulticycle = new JCheckBoxMenuItem("Processor simulator");
        this.menuNiosIISim.addSeparator();
        this.menuItemStep = new JMenuItem("Execute a Step");
        this.menuItemStep.setAccelerator(KeyStroke.getKeyStroke(69, 2));
        this.menuItemStep.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().step();
            }
        });
        this.menuNiosIISim.add(this.menuItemStep);
        this.menuItemRun = new JMenuItem("Run");
        this.menuItemRun.setAccelerator(KeyStroke.getKeyStroke(82, 2));
        this.menuItemRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                Nios2SimMenuBar.this.nios2Sim.getPanel().run();
            }
        });
        this.menuNiosIISim.add(this.menuItemRun);
        this.add(this.menuNiosIISim);
        var2 = new JMenu("Help");
        var2.setForeground(DARK_MODE_FONT_COLOR);
        this.add(var2);
        var3 = new JMenuItem("About");
        var2.add(var3);
        var3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                JOptionPane.showMessageDialog(Nios2SimMenuBar.this.nios2Sim.getPanel(), "Nios2Sim 2020\nVersion: 0.6.4\nDeveloppers: Benda Lukas, Blanc Régis, Boichat Nicolas,\nJimenez Xavier, Panagiotis Athanasopoulos\n© 2007-2009 LAP-EPFL", "About Nios2Sim 2020 0.6.4", 1);
            }
        });
        this.updateMenuState();
    }

    public boolean isPipelineEnabled() {
        return this.pipelineItem.isSelected();
    }

    public boolean isMulticycleEnabled() {
        return this.menuItemMulticycle.isSelected();
    }

    public boolean isStallsEnabled() {
        return this.menuItemStalls.isSelected();
    }

    public boolean isForwardEEEnabled() {
        return this.menuItemForwardEE.isSelected();
    }

    public boolean isForwardMEEnabled() {
        return this.menuItemForwardME.isSelected();
    }

    public boolean isForwardWDEnabled() {
        return this.menuItemForwardWD.isSelected();
    }

    public boolean isFlushesEnabled() {
        return this.menuItemFlushes.isSelected();
    }

    public void updateMenuState() {
        boolean var1 = this.nios2Sim.getPanel().isSimulating();
        this.menuItemNew.setVisible(!var1);
        this.menuItemOpen.setVisible(!var1);
        this.menuItemSave.setVisible(!var1);
        this.menuItemSaveAs.setVisible(!var1);
        this.menuItemSaveHex.setVisible(!var1);
        this.menuItemCompile.setVisible(!var1);
        this.menuNiosIIEdit.setVisible(!var1);
        this.menuNiosIISim.setForeground(DARK_MODE_FONT_COLOR);
        this.menuNiosIISim.setVisible(var1);
        this.menuEdit.setEnabled(!var1);
        UndoManager var2 = this.nios2Sim.getPanel().getUndoManager();
        this.menuItemUndo.setEnabled(var2.canUndo());
        this.menuItemRedo.setEnabled(var2.canRedo());
        this.menuSystem.setEnabled(!var1);
        this.menuItemStalls.setEnabled(this.pipelineItem.isSelected());
        this.menuForwarding.setEnabled(this.pipelineItem.isSelected());
        this.menuItemFlushes.setEnabled(this.pipelineItem.isSelected());
    }
}
