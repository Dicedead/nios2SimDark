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
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ModuleTimer extends Module {
    private JTable table;
    private final int STATUS = 3;
    private final int CONTROL = 2;
    private final int PERIOD = 1;
    private final int COUNTER = 0;
    private final int SHIFT_CONT = 0;
    private final int SHIFT_ITO = 1;
    private final int SHIFT_STOP = 2;
    private final int SHIFT_START = 3;
    private final int SHIFT_RUN = 0;
    private final int SHIFT_TO = 1;

    public ModuleTimer(Nios2SimPanel var1, int var2, String var3, int var4) {
        super(var1, var2, 16, var3, var4);
    }

    public ModuleTimer(Nios2SimPanel var1, int var2, String var3, int var4, HashMap<String, String> var5) throws CompilerException {
        super(var1, var2, 16, var3, var4);
    }

    public JComponent getPane() {
        this.table = this.getDefaultJTable(new String[]{"Address", "Data"}, new String[]{"0: counter", "4: period", "8: control", "C: status"}, (boolean[])null);
        table.setForeground(DARK_MODE_FONT_COLOR);
        JPanel var1 = new JPanel(new BorderLayout());
        var1.add(this.table.getTableHeader(), "First");
        var1.add(this.table, "Center");
        JPanel var2 = new JPanel(new FlowLayout());
        var2.add(var1);
        var2.setBackground(Color.white);
        this.setDefaultColumnSize(this.table);
        JScrollPane var3 = new JScrollPane(var2);
        return var3;
    }

    public void clockEvent() {
        int var1 = this.getData()[3] >> 0 & 1;
        int var2 = this.getData()[2] >> 0 & 1;
        if (var1 == 1) {
            if (this.getData()[0] == 0) {
                super.write(0, this.getData()[1]);
                if (var2 == 0) {
                    super.write(12, this.getData()[3] & 2);
                }

                super.write(12, this.getData()[3] | 2);
            } else {
                super.write(0, this.getData()[0] - 1);
            }
        }

        int var3 = this.getData()[3] >> 1 & 1;
        int var4 = this.getData()[2] >> 1 & 1;
        if ((var4 & var3) != 0) {
            this.setInterrupt();
        } else {
            this.clearInterrupt();
        }

    }

    public void commitEdits() {
        TableCellEditor var1 = this.table.getCellEditor();
        if (var1 != null) {
            var1.stopCellEditing();
        }

    }

    protected void setTableValueAt(int var1, int var2) {
        this.write(var1 << 2, var2);
    }

    public boolean write(int var1, int var2) {
        switch(var1) {
            case 4:
                super.write(12, this.getData()[3] & 2);
                super.write(4, var2);
                super.write(0, var2);
                return true;
            case 8:
                int var3 = var2 >> 3 & 1;
                int var4 = var2 >> 2 & 1;
                if (var4 == 1) {
                    super.write(12, this.getData()[3] & 2);
                } else if (var3 == 1) {
                    super.write(12, this.getData()[3] | 1);
                }

                super.write(8, var2 & 3);
                return true;
            case 12:
                super.write(var1, this.getData()[3] & 1);
                return true;
            default:
                return false;
        }
    }

    public void refresh() {
        this.table.tableChanged(new TableModelEvent(this.table.getModel()));
    }
}
