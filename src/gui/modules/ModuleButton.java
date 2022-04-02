//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui.modules;

import compiler.CompilerException;
import gui.Nios2SimPanel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ModuleButton extends Module {
    private JTable table;
    private static final int STATUS = 0;
    private static final int EDGECAPTURE = 1;
    private static final int NB_OF_BUTTONS = 4;
    JToggleButton[] buttons = new JToggleButton[4];

    private void setButtonValue(int var1) {
        var1 &= 15;
        super.setTableValueAt(1, this.getData()[1] | ~var1 & this.getData()[0]);
        super.setTableValueAt(0, var1);
    }

    public ModuleButton(Nios2SimPanel var1, int var2, String var3, int var4) {
        super(var1, var2, 8, var3, var4);
    }

    public ModuleButton(Nios2SimPanel var1, int var2, String var3, int var4, HashMap<String, String> var5) throws CompilerException {
        super(var1, var2, 8, var3, var4);
    }

    public JComponent getPane() {
        this.table = this.getDefaultJTable(new String[]{"Address", "Data"}, new String[]{"0: status", "4: edgecapture"}, (boolean[])null);
        table.setForeground(DARK_MODE_FONT_COLOR);
        this.setDefaultColumnSize(this.table);
        JPanel var1 = new JPanel();
        var1.setBackground(Color.white);

        for(int var2 = 0; var2 < 4; ++var2) {
            var1.add(this.getJToggleButton(var2));
        }

        Box var4 = Box.createVerticalBox();
        var4.setBackground(Color.white);
        var4.setOpaque(true);
        var4.add(Box.createVerticalStrut(5));
        var4.add(this.table.getTableHeader());
        var4.add(this.table);
        var4.add(Box.createVerticalStrut(10));
        var4.add(var1);
        var4.add(Box.createVerticalGlue());
        JScrollPane var3 = new JScrollPane(var4);
        return var3;
    }

    private JToggleButton getJToggleButton(final int var1) {
        if (this.buttons[var1] == null) {
            this.buttons[var1] = new JToggleButton(String.valueOf(var1 + 1));
            this.buttons[var1].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent var1x) {
                    int var2 = 1 << var1;
                    if (ModuleButton.this.buttons[var1].isSelected()) {
                        var2 = ModuleButton.this.getData()[0] & ~var2;
                        ModuleButton.this.setButtonValue(var2);
                    } else {
                        var2 |= ModuleButton.this.getData()[0];
                        ModuleButton.this.setButtonValue(var2);
                    }

                    ModuleButton.this.refresh();
                }
            });
        }

        return this.buttons[var1];
    }

    public void reset() {
        super.reset();

        for(int var1 = 0; var1 < this.buttons.length; ++var1) {
            this.getJToggleButton(var1).setSelected(false);
        }

        this.getData()[0] = 15;
    }

    protected void setTableValueAt(int var1, int var2) {
        if (var1 == 0) {
            var2 &= 15;
            this.setButtonValue(var2);

            for(int var3 = 0; var3 < 4; ++var3) {
                if ((var2 & 1) == 0) {
                    this.getJToggleButton(var3).setSelected(true);
                } else {
                    this.getJToggleButton(var3).setSelected(false);
                }

                var2 >>= 1;
            }
        } else {
            this.write(var1 << 2, var2);
        }

    }

    public boolean write(int var1, int var2) {
        if (var1 == 4) {
            super.write(4, 0);
            return true;
        } else {
            return false;
        }
    }

    public void clockEvent() {
        if (this.getData()[1] == 0) {
            this.clearInterrupt();
        } else {
            this.setInterrupt();
        }

    }

    public void commitEdits() {
        TableCellEditor var1 = this.table.getCellEditor();
        if (var1 != null) {
            var1.stopCellEditing();
        }

    }

    public void refresh() {
        this.table.tableChanged(new TableModelEvent(this.table.getModel()));
    }
}
