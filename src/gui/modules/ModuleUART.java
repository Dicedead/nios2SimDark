//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui.modules;

import compiler.CompilerException;
import compiler.Interpreter;
import gui.Nios2SimPanel;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ModuleUART extends Module {
    private static final int REG_RECIEVE_DATA = 0;
    private static final int REG_TRANSMIT_DATA = 1;
    private static final int REG_STATUS = 2;
    private static final int REG_CONTROL = 3;
    private JTable table;
    private int delayPeriod;
    private int currentDelay;
    private JTextArea jTextAreaTransmitted;

    public ModuleUART(Nios2SimPanel var1, int var2, String var3, int var4, int var5) {
        super(var1, var2, 16, var3, var4);
        this.delayPeriod = var5;
    }

    public ModuleUART(Nios2SimPanel var1, int var2, String var3, int var4, HashMap<String, String> var5) throws CompilerException {
        super(var1, var2, 16, var3, var4);

        try {
            this.delayPeriod = Interpreter.resolveExpression((String)var5.get("delay"));
        } catch (CompilerException var7) {
        }

    }

    public JComponent getPane() {
        this.table = this.getDefaultJTable(new String[]{"Address", "Data"}, new String[]{"0: receive", "4: transmit", "8: status", "C: control"}, (boolean[])null);
        table.setForeground(DARK_MODE_FONT_COLOR);
        this.jTextAreaTransmitted = new JTextArea();
        JScrollPane var1 = new JScrollPane(this.jTextAreaTransmitted);
        Box var2 = Box.createVerticalBox();
        var2.setBackground(Color.white);
        var2.setOpaque(true);
        var2.add(Box.createVerticalStrut(5));
        var2.add(this.table.getTableHeader());
        var2.add(this.table);
        var2.add(Box.createVerticalStrut(10));
        var2.add(var1);
        this.setDefaultColumnSize(this.table);
        return var2;
    }

    public void clockEvent() {
        if (this.currentDelay < this.delayPeriod) {
            if (this.currentDelay == 0) {
                this.currentDelay = this.delayPeriod;
                super.setTableValueAt(2, this.getData()[2] | 2);
            } else {
                --this.currentDelay;
            }
        }

        if ((this.getData()[2] & this.getData()[3]) == 0) {
            this.clearInterrupt();
        } else {
            this.setInterrupt();
        }

    }

    public int read(int var1) {
        if (var1 == 0) {
            super.setTableValueAt(2, this.getData()[2] & -2);
        }

        return super.read(var1);
    }

    protected void setTableValueAt(int var1, int var2) {
        switch(var1) {
            case 0:
                super.setTableValueAt(var1, var2 & 255);
                super.setTableValueAt(2, this.getData()[2] | 1);
                break;
            default:
                this.write(var1 << 2, var2);
        }

    }

    public boolean write(int var1, int var2) {
        switch(var1 >> 2) {
            case 1:
                if ((this.getData()[2] & 2) > 0) {
                    super.write(8, this.getData()[2] & 1);
                    --this.currentDelay;
                    this.jTextAreaTransmitted.append(String.format("\n0x%02X", var2 & 255));
                    return true;
                }

                return false;
            case 3:
                super.write(var1, var2 & 3);
                return true;
            default:
                return false;
        }
    }

    public void reset() {
        for(int var1 = 0; var1 < this.getData().length; ++var1) {
            this.getData()[var1] = 0;
        }

        this.getData()[2] = 2;
        this.currentDelay = this.delayPeriod;
        this.jTextAreaTransmitted.setText("Data transmitted:");
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
