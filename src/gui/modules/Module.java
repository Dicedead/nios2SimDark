//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui.modules;

import gui.Nios2SimPanel;
import gui.Nios2SimPanelSimulation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public abstract class Module {
    public static final Color COLOR_MOD = new Color(255, 150, 150);
    private Nios2SimPanel nios2SimPanel;
    private String name;
    private int startAddress;
    private int size;
    private int[] data;
    protected ArrayList<Integer> modifiedAddresses;
    private int irqID;
    private boolean irqPending = false;

    public Module(Nios2SimPanel var1, int var2, int var3, String var4) {
        this.nios2SimPanel = var1;
        this.startAddress = var2;
        this.size = var3;
        this.name = var4;
        this.data = new int[var3 >> 2];
        this.modifiedAddresses = new ArrayList();
        this.irqID = -1;
    }

    public Module(Nios2SimPanel var1, int var2, String var3) {
        this.nios2SimPanel = var1;
        this.nios2SimPanel.setForeground(DARK_MODE_FONT_COLOR);
        this.startAddress = var2;
        this.name = var3;
        this.modifiedAddresses = new ArrayList();
        this.irqID = -1;
    }

    public Module(Nios2SimPanel var1, int var2, int var3, String var4, int var5) {
        this.nios2SimPanel = var1;
        this.startAddress = var2;
        this.size = var3;
        this.name = var4;
        this.data = new int[var3 >> 2];
        this.modifiedAddresses = new ArrayList();
        this.irqID = var5;
    }

    public Module(Nios2SimPanel var1, int var2, String var3, int var4) {
        this.nios2SimPanel = var1;
        this.startAddress = var2;
        this.name = var3;
        this.modifiedAddresses = new ArrayList();
        this.irqID = var4;
    }

    public int getIrqID() {
        return this.irqID;
    }

    public void clockEvent() {
    }

    public String toString() {
        return this.name;
    }

    public void clearModificationHistory() {
        this.modifiedAddresses.clear();
    }

    public abstract JComponent getPane();

    protected void setInterrupt() {
        if (!this.irqPending) {
            this.irqPending = true;
            this.nios2SimPanel.getSimulator().setInterrupt(this.irqID);
        }

    }

    protected void clearInterrupt() {
        if (this.irqPending) {
            this.irqPending = false;
            this.nios2SimPanel.getSimulator().clearInterrupt(this.irqID);
        }

    }

    public int[] getData() {
        return this.data;
    }

    public void setData(int var1, int var2) {
        this.data[var1] = var2;
    }

    public void resizeData(int var1) {
        this.data = new int[var1 >> 2];
    }

    public int read(int var1) {
        return this.data[var1 >> 2];
    }

    public void reset() {
        for(int var1 = 0; var1 < this.data.length; ++var1) {
            this.data[var1] = 0;
        }

        this.modifiedAddresses.clear();
        this.irqPending = false;
    }

    public boolean write(int var1, int var2) {
        if (this.data[var1 >> 2] != var2) {
            this.data[var1 >> 2] = var2;
            this.modifiedAddresses.add(new Integer(var1 >> 2));
        }

        return true;
    }

    public boolean isDataModified(int var1) {
        return this.modifiedAddresses.contains(new Integer(var1));
    }

    public int getDataIndexFromCell(int var1, int var2, int var3) {
        return (var3 - 1) * var1 + var2 - 1;
    }

    public void commitEdits() {
    }

    public void refresh() {
    }

    public String getName() {
        return this.name;
    }

    public int getAbsoluteByteAddressFromCell(int var1, int var2, int var3) {
        return this.getStartAddress() + ((var3 - 1) * var1 + var2 - 1) * 4;
    }

    public int getStartAddress() {
        return this.startAddress;
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int var1) {
        this.size = var1;
    }

    protected void setTableValueAt(int var1, int var2) {
        if (this.data[var1] != var2) {
            this.data[var1] = var2;
            this.modifiedAddresses.add(var1);
        }

    }

    protected int getTableValueAt(int var1) {
        return this.data[var1];
    }

    public static DefaultCellEditor getDefaultCellEditor(final JTable var0) {
        JTextField var1 = new JTextField();
        var1.setFont(Nios2SimPanel.TABLE_FONT);
        var1.setBorder(new EmptyBorder(0, 1, 0, 0));
        var1.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
                if (var0.getCellEditor() != null) {
                    var0.getCellEditor().stopCellEditing();
                }

            }
        });
        return new DefaultCellEditor(var1);
    }

    public JTable getDefaultJTable(final String[] var1, String[] var2, boolean[] var3) {
        TableModel var4 = this.getDefaultTableModel(var1, var2, this.data, this.startAddress, var3);
        DefaultTableCellRenderer var5 = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable var1x, Object var2, boolean var3, boolean var4, int var5, int var6) {
                Component var7 = super.getTableCellRendererComponent(var1x, var2, false, false, var5, var6);
                if (var6 == 0) {
                    var7.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
                    var7.setBackground(DARK_COLOR);
                } else {
                    var7.setFont(Nios2SimPanel.TABLE_FONT);
                    int var8 = Module.this.getDataIndexFromCell(var5, var6, var1.length);
                    if (Module.this.modifiedAddresses.contains(new Integer(var8))) {
                        var7.setBackground(Module.COLOR_MOD);
                    } else {
                        var7.setBackground(DARK_COLOR);
                    }

                    Nios2SimPanelSimulation.makeToolTip(this, "<b>" + Module.this.getName() + " at " + String.format("%08X", Module.this.getAbsoluteByteAddressFromCell(var5, var6, var1.length)) + "</b>", Module.this.data[var8]);
                }

                return var7;
            }
        };
        JTable var6 = new JTable(var4);
        var6.setDefaultRenderer(String.class, var5);
        var6.getTableHeader().setReorderingAllowed(false);
        var6.setDefaultEditor(String.class, getDefaultCellEditor(var6));
        this.setDefaultColumnSize(var6);
        return var6;
    }

    public TableModel getDefaultTableModel(String[] var1, String[] var2, int[] var3, int var4, boolean[] var5) {
        DefaultTableModel var6 = new DefaultTableModel(var1, var2, var3, var4, var5);
        return var6;
    }

    public void setDefaultColumnSize(JTable var1) {
        for(int var2 = 0; var2 < var1.getColumnCount(); ++var2) {
            int var3 = 0;

            for(int var4 = 0; var4 < var1.getRowCount(); ++var4) {
                if (((String)var1.getValueAt(var4, var2)).length() > var3) {
                    var3 = ((String)var1.getValueAt(var4, var2)).length();
                }
            }

            if (var1.getColumnName(var2).length() > var3) {
                var3 = var1.getColumnName(var2).length();
            }

            String var6 = "";

            for(int var5 = 0; var5 < var3; ++var5) {
                var6 = var6 + "0";
            }

            var3 = 4 + (int)Nios2SimPanel.TABLE_FONT.getStringBounds(var6, new FontRenderContext(new AffineTransform(), false, false)).getWidth();
            var1.getColumnModel().getColumn(var2).setMinWidth(var3);
            var1.getColumnModel().getColumn(var2).setMaxWidth(var3);
        }

    }

    public class DefaultTableModel implements TableModel {
        private String[] colNames = null;
        private String[] rowNames = null;
        private int[] data = null;
        private int startAddress = 0;
        private boolean[] editable = null;

        public DefaultTableModel(String[] var2, String[] var3, int[] var4, int var5, boolean[] var6) {
            this.colNames = var2;
            this.rowNames = var3;
            this.data = var4;
            this.startAddress = var5;
            this.editable = var6;
            if (var6 == null) {
                this.editable = new boolean[var2.length];
                this.editable[0] = false;

                for(int var7 = 1; var7 < this.editable.length; ++var7) {
                    this.editable[var7] = true;
                }
            }

        }

        public void addTableModelListener(TableModelListener var1) {
        }

        public Class<?> getColumnClass(int var1) {
            return String.class;
        }

        public int getColumnCount() {
            return this.colNames.length;
        }

        public String getColumnName(int var1) {
            return var1 < this.colNames.length ? this.colNames[var1] : "";
        }

        public int getRowCount() {
            return this.data.length / (this.colNames.length - 1);
        }

        public Object getValueAt(int var1, int var2) {
            String var3 = "";
            if (var2 == 0) {
                if (this.rowNames != null && this.rowNames.length > var1) {
                    var3 = this.rowNames[var1];
                } else {
                    var3 = String.format("%08X", this.startAddress + 4 * var1 * (this.colNames.length - 1));
                }
            } else {
                var3 = String.format("%08X", Module.this.getTableValueAt((this.colNames.length - 1) * var1 + var2 - 1));
            }

            return var3;
        }

        public boolean isCellEditable(int var1, int var2) {
            return this.editable[var2];
        }

        public void removeTableModelListener(TableModelListener var1) {
        }

        public void setValueAt(Object var1, int var2, int var3) {
            try {
                long var4 = Long.parseLong(var1.toString(), 16);
                if ((var4 & -4294967296L) != 0L) {
                    return;
                }

                Module.this.setTableValueAt((this.colNames.length - 1) * var2 + var3 - 1, (int)var4);
                Module.this.refresh();
            } catch (NumberFormatException var7) {
            }

        }
    }
}
