//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import compiler.Compiler;
import compiler.Line;
import gui.modules.Module;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class Nios2SimPanelSimulation {
    private static final int DIVIDER_SIZE = 5;
    private JScrollPane simScroll;
    private JTable simtable;
    private JTable regtable;
    private PipelinePane pipelinePane;
    private ProcessorPane multicyclePane;
    private JTable interruptTable;
    private Nios2SimPanel panel;
    private JSplitPane simSplitPane;
    private JSplitPane pipelineSplitPane;
    private JTabbedPane tabbedPane;
    private JLabel irqLabel;
    private String[] moduleNames = new String[32];
    private JLabel pcLabel;
    private JLabel ccLabel;
    String[] columnNames = new String[]{"", "Address", "Instr", "Assembly Code"};

    public Nios2SimPanelSimulation(Nios2SimPanel var1) {
        this.panel = var1;
        this.simScroll = new JScrollPane(this.createSimTable());
        this.pipelinePane = new PipelinePane();
        this.multicyclePane = new ProcessorPane();
        this.pipelineSplitPane = new JSplitPane(0);
        this.pipelineSplitPane.setBackground(DARK_COLOR);
        this.pipelineSplitPane.setOpaque(true);
        this.pipelineSplitPane.setResizeWeight(1.0D);
        this.pipelineSplitPane.setDividerSize(0);
        this.pipelineSplitPane.add(this.simScroll);
        this.pipelineSplitPane.setForeground(DARK_MODE_FONT_COLOR);
        JPanel var2 = new JPanel();
        var2.setLayout(new BorderLayout());
        this.tabbedPane = new JTabbedPane();
        var2.add(this.tabbedPane, "Center");
        JTabbedPane var3 = new JTabbedPane();
        var3.addTab("Registers", this.createRegTable());
        var3.addTab("Control", this.createControlTable());
        var2.add(var3, "North");
        this.simSplitPane = new JSplitPane(1, this.pipelineSplitPane, var2);
        this.simSplitPane.setResizeWeight(1.0D);
        this.simSplitPane.setDividerSize(5);
    }

    public void reloadSystem() {
        this.tabbedPane.removeAll();
        Module[] var1 = this.panel.getModules();
        if (var1 != null && var1.length != 0) {
            int var2;
            for(var2 = 0; var2 < this.moduleNames.length; ++var2) {
                this.moduleNames[var2] = null;
            }

            for(var2 = 0; var2 < var1.length; ++var2) {
                this.tabbedPane.addTab(var1[var2].getName(), var1[var2].getPane());
                int var3 = var1[var2].getIrqID();
                if (var3 >= 0) {
                    this.moduleNames[var3] = var1[var2].getName();
                }
            }
        } else {
            this.panel.error("No system is defined", -1);
        }

    }

    public JSplitPane getSplitPane() {
        return this.simSplitPane;
    }

    public PipelinePane getPipelinePane() {
        return this.pipelinePane;
    }

    public ProcessorPane getProcessorPane() {
        return this.multicyclePane;
    }

    public void setPipelineEnabled(boolean var1) {
        this.pipelineSplitPane.remove(this.multicyclePane);
        if (var1) {
            this.pipelineSplitPane.add(this.pipelinePane);
            this.pipelineSplitPane.setDividerSize(5);
            this.pipelineSplitPane.setResizeWeight(0.66D);
        } else {
            this.pipelineSplitPane.remove(this.pipelinePane);
            this.pipelineSplitPane.setDividerSize(0);
            this.pipelineSplitPane.setResizeWeight(1.0D);
        }

    }

    public void setMulticycleEnabled(boolean var1) {
        this.pipelineSplitPane.remove(this.pipelinePane);
        if (var1) {
            this.pipelineSplitPane.add(this.multicyclePane);
            this.pipelineSplitPane.setDividerSize(5);
            this.pipelineSplitPane.setResizeWeight(0.66D);
        } else {
            this.pipelineSplitPane.remove(this.multicyclePane);
            this.pipelineSplitPane.setDividerSize(0);
            this.pipelineSplitPane.setResizeWeight(1.0D);
        }

    }

    public void commitEdits() {
        Module[] var1 = this.panel.getModules();

        for(int var2 = 0; var2 < var1.length; ++var2) {
            var1[var2].commitEdits();
        }

    }

    public void requestFocus() {
        this.simtable.requestFocus();
    }

    public void refreshTable() {
        this.simtable.tableChanged(new TableModelEvent(this.simtable.getModel()));
        this.regtable.tableChanged(new TableModelEvent(this.regtable.getModel()));
        this.interruptTable.tableChanged(new TableModelEvent(this.interruptTable.getModel()));
        Rectangle var1 = this.simtable.getCellRect(this.panel.getSimulator().getCurrentLine(), 0, true);
        var1.y -= var1.height;
        var1.height *= 3;
        this.simtable.scrollRectToVisible(var1);
        Module[] var2 = this.panel.getModules();

        for(int var3 = 0; var3 < var2.length; ++var3) {
            var2[var3].refresh();
        }

        if (this.panel.getSimulator().isIRQmodified()) {
            this.getIRQLabel().setBackground(new Color(255, 150, 150));
        } else {
            this.getIRQLabel().setBackground(DARK_COLOR);
        }

        this.setCC(this.panel.getSimulator().getCC());
        this.setPC(this.panel.getSimulator().getPC());
    }

    public void updateTableColumnWidth(FontMetrics var1, FontMetrics var2) {
        int var3 = var1.stringWidth("00000000") + 4;
        this.simtable.getColumnModel().getColumn(0).setMinWidth(20);
        this.simtable.getColumnModel().getColumn(0).setMaxWidth(20);
        this.simtable.getColumnModel().getColumn(1).setMinWidth(var3);
        this.simtable.getColumnModel().getColumn(1).setMaxWidth(var3);
        this.simtable.getColumnModel().getColumn(2).setMinWidth(var3);
        this.simtable.getColumnModel().getColumn(2).setMaxWidth(var3);
        this.simtable.getColumnModel().getColumn(3).setMinWidth(120);

        int var4;
        int var5;
        int var6;
        for(var4 = 0; var4 < 4; ++var4) {
            var5 = 0;

            for(var6 = var4 * 8; var6 < (var4 + 1) * 8; ++var6) {
                int var7 = var2.stringWidth(Compiler.regToAlias(var4)) + 4;
                if (var7 > var5) {
                    var5 = var7;
                }
            }

            this.regtable.getColumnModel().getColumn(2 * var4).setMinWidth(var5);
            this.regtable.getColumnModel().getColumn(2 * var4).setMaxWidth(var5);
            this.regtable.getColumnModel().getColumn(2 * var4 + 1).setMinWidth(var3);
        }

        this.regtable.setPreferredSize(this.regtable.getMinimumSize());
        var4 = 0;

        for(var5 = 0; var5 < 6; ++var5) {
            var6 = var2.stringWidth(Compiler.ctlRegToAlias(var5)) + 4;
            if (var6 > var4) {
                var4 = var6;
            }
        }

        this.interruptTable.getColumnModel().getColumn(0).setMinWidth(var4);
        this.interruptTable.getColumnModel().getColumn(0).setMaxWidth(var4);
        this.interruptTable.getColumnModel().getColumn(1).setMinWidth(var3);
        this.interruptTable.getColumnModel().getColumn(1).setMaxWidth(var3);
        this.interruptTable.setPreferredSize(this.interruptTable.getMinimumSize());
    }

    public void setIRQValue(int var1) {
        this.getIRQLabel().setText(String.format("%08X", var1));
        String var2 = "<html><b>Interrupt source vector:</b><table cellpadding=2><tr><th>irqN</th><th>Source</th><th>value</th></tr>";

        for(int var3 = 0; var3 < 32; ++var3) {
            if (this.moduleNames[var3] != null) {
                var2 = var2 + "<tr><th>" + var3 + "</th><td>" + this.moduleNames[var3] + "</td><td>" + (var1 & 1) + "</td></tr>";
            }

            var1 >>= 1;
        }

        var2 = var2 + "</table></html>";
        this.getIRQLabel().setToolTipText(var2);
    }

    private JLabel getIRQLabel() {
        if (this.irqLabel == null) {
            this.irqLabel = new JLabel();
            this.irqLabel.setBackground(DARK_COLOR);
            this.irqLabel.setOpaque(true);
            this.irqLabel.setFont(Nios2SimPanel.TABLE_FONT);
            this.irqLabel.setForeground(DARK_MODE_FONT_COLOR);
        }

        return this.irqLabel;
    }

    public void setPC(int var1) {
        this.getPCLabel().setText(String.format("%08X", var1));
    }

    private JLabel getPCLabel() {
        if (this.pcLabel == null) {
            this.pcLabel = new JLabel();
            this.pcLabel.setBackground(DARK_COLOR);
            this.pcLabel.setOpaque(true);
            this.pcLabel.setFont(Nios2SimPanel.TABLE_FONT);
            this.pcLabel.setForeground(DARK_MODE_FONT_COLOR);
        }

        return this.pcLabel;
    }

    public void setCC(int var1) {
        this.getCCLabel().setText(String.format("%d", var1));
    }

    private JLabel getCCLabel() {
        if (this.ccLabel == null) {
            this.ccLabel = new JLabel();
            this.ccLabel.setBackground(DARK_COLOR);
            this.ccLabel.setOpaque(true);
            this.ccLabel.setFont(Nios2SimPanel.TABLE_FONT);
            this.ccLabel.setForeground(DARK_MODE_FONT_COLOR);
        }

        return this.ccLabel;
    }

    private JTable createSimTable() {
        TableModel var1 = new TableModel() {
            public String getColumnName(int var1) {
                return Nios2SimPanelSimulation.this.columnNames[var1].toString();
            }

            public int getColumnCount() {
                return 4;
            }

            public int getRowCount() {
                return Nios2SimPanelSimulation.this.panel.getSimulator() == null ? 0 : Nios2SimPanelSimulation.this.panel.getSimulator().getLines().size();
            }

            public Object getValueAt(int var1, int var2) {
                if (Nios2SimPanelSimulation.this.panel.getSimulator() == null) {
                    return null;
                } else {
                    Line var3 = (Line)Nios2SimPanelSimulation.this.panel.getSimulator().getLines().get(var1);
                    switch(var2) {
                        case 0:
                            return "";
                        case 1:
                            return var3.isValid() ? String.format("%08X", var3.address) : "";
                        case 2:
                            return var3.isValid() ? String.format("%08X", var3.data) : "";
                        case 3:
                            return var3.text;
                        default:
                            return null;
                    }
                }
            }

            public boolean isCellEditable(int var1, int var2) {
                return false;
            }

            public void addTableModelListener(TableModelListener var1) {
            }

            public Class<?> getColumnClass(int var1) {
                return String.class;
            }

            public void removeTableModelListener(TableModelListener var1) {
            }

            public void setValueAt(Object var1, int var2, int var3) {
            }
        };
        DefaultTableCellRenderer var2 = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
                if (var6 == 0) {
                    return Nios2SimPanelSimulation.this.new StatusCellComponent(var5, var6);
                } else {
                    Component var7 = super.getTableCellRendererComponent(var1, var2, false, false, var5, var6);
                    var7.setFont(Nios2SimPanel.TABLE_FONT);
                    var7.setForeground(DARK_MODE_FONT_COLOR);
                    var7.setBackground(DARK_COLOR);
                    Line var8 = (Line)Nios2SimPanelSimulation.this.panel.getSimulator().getLines().get(var5);
                    if (var8.isValid() && var8.address == Nios2SimPanelSimulation.this.panel.getSimulator().getPC()) {
                        var7.setBackground(Color.DARK_GRAY);
                    }

                    return var7;
                }
            }
        };
        this.simtable = new JTable(var1);
        this.simtable.setDefaultRenderer(String.class, var2);
        this.simtable.setSelectionMode(0);
        this.simtable.setBackground(DARK_COLOR);
        this.panel.setBackground(DARK_COLOR);
        this.simtable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent var1) {
                if (var1.getClickCount() == 2) {
                    ListSelectionModel var2 = Nios2SimPanelSimulation.this.simtable.getSelectionModel();
                    if (!var2.isSelectionEmpty()) {
                        int var3 = var2.getMinSelectionIndex();
                        Line var4 = (Line)Nios2SimPanelSimulation.this.panel.getSimulator().getLines().get(var3);
                        if (var4.isValid()) {
                            var4.breakpoint = !var4.breakpoint;
                            Nios2SimPanelSimulation.this.simtable.tableChanged(new TableModelEvent(Nios2SimPanelSimulation.this.simtable.getModel(), var3, var3, 0));
                        }
                    }
                }

            }
        });
        this.simtable.getTableHeader().setReorderingAllowed(false);
        return this.simtable;
    }

    public static void makeToolTip(JComponent var0, String var1, int var2) {
        StringBuilder var3 = new StringBuilder();
        var3.append("<html>");
        var3.append(var1);
        var3.append(":<br>");
        if (var2 < 0) {
            var3.append("<b>Decimal (signed)</b>: ");
            var3.append(var2);
            var3.append("<br>");
            var3.append("<b>Decimal (unsigned)</b>: ");
            var3.append((long)var2 & 4294967295L);
            var3.append("<br>");
        } else {
            var3.append("<b>Decimal</b>: ");
            var3.append(var2);
            var3.append("<br>");
        }

        var3.append("<b>Binary</b>: ");
        int var4 = var2;

        for(int var5 = 0; var5 < 32; ++var5) {
            var3.append((char)((var4 & -2147483648) == 0 ? '0' : '1'));
            if (var5 % 8 == 7 && var5 != 31) {
                var3.append('\'');
            }

            var4 <<= 1;
        }

        var3.append("<br>");
        var3.append("</html>");
        var0.setToolTipText(var3.toString());
    }

    private JComponent createRegTable() {
        TableModel var1 = new TableModel() {
            public String getColumnName(int var1) {
                return "";
            }

            public int getColumnCount() {
                return 8;
            }

            public int getRowCount() {
                return 8;
            }

            public Object getValueAt(int var1, int var2) {
                if (var2 % 2 == 0) {
                    return Compiler.regToAlias(var1 + var2 * 4);
                } else {
                    return Nios2SimPanelSimulation.this.panel.getSimulator() == null ? null : String.format("%08X", Nios2SimPanelSimulation.this.panel.getSimulator().getReg(var1 + (var2 - 1) * 4));
                }
            }

            public boolean isCellEditable(int var1, int var2) {
                if (var2 % 2 == 0) {
                    return false;
                } else {
                    return var1 != 0 || var2 != 1;
                }
            }

            public void addTableModelListener(TableModelListener var1) {
            }

            public Class<?> getColumnClass(int var1) {
                return String.class;
            }

            public void removeTableModelListener(TableModelListener var1) {
            }

            public void setValueAt(Object var1, int var2, int var3) {
                try {
                    long var4 = Long.parseLong(var1.toString(), 16);
                    if ((var4 & -4294967296L) != 0L) {
                        return;
                    }

                    Nios2SimPanelSimulation.this.panel.getSimulator().setReg(var2 + (var3 - 1) * 4, (int)var4);
                } catch (NumberFormatException var7) {
                }

            }
        };
        DefaultTableCellRenderer var2 = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
                Component var7 = super.getTableCellRendererComponent(var1, var2, false, false, var5, var6);
                if (var6 % 2 == 0) {
                    var7.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
                    var7.setBackground(DARK_COLOR);
                    this.setToolTipText("r" + (var5 + var6 * 4));
                } else {
                    var7.setFont(Nios2SimPanel.TABLE_FONT);
                    var7.setForeground(DARK_MODE_FONT_COLOR);
                    int var8 = var5 + (var6 - 1) * 4;
                    if (Nios2SimPanelSimulation.this.panel.getSimulator().getRegModified(var8)) {
                        var7.setBackground(new Color(255, 150, 150));
                    } else {
                        var7.setBackground(DARK_COLOR);
                    }

                    Nios2SimPanelSimulation.makeToolTip(this, "<b>" + Compiler.regToAlias(var8) + "</b>/r" + var8, Nios2SimPanelSimulation.this.panel.getSimulator().getReg(var8));
                }

                return var7;
            }
        };
        JPanel var3 = new JPanel();
        var3.setBorder(BorderFactory.createTitledBorder("General Purpose Registers Content"));
        var3.setOpaque(true);
        var3.setBackground(DARK_COLOR);
        this.regtable = new JTable(var1);
        this.regtable.setDefaultRenderer(String.class, var2);
        this.regtable.getTableHeader().setReorderingAllowed(false);
        JTextField var4 = new JTextField();
        var4.setFont(Nios2SimPanel.TABLE_FONT);
        var4.setForeground(DARK_MODE_FONT_COLOR);
        var4.setBorder(new EmptyBorder(0, 1, 0, 0));
        var4.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
                if (Nios2SimPanelSimulation.this.regtable.getCellEditor() != null) {
                    Nios2SimPanelSimulation.this.regtable.getCellEditor().stopCellEditing();
                }

            }
        });
        this.regtable.setDefaultEditor(String.class, new DefaultCellEditor(var4));
        var3.add(this.regtable);
        return var3;
    }

    private JComponent createControlTable() {
        TableModel var1 = new TableModel() {
            public String getColumnName(int var1) {
                return "";
            }

            public int getColumnCount() {
                return 2;
            }

            public int getRowCount() {
                return 6;
            }

            public Object getValueAt(int var1, int var2) {
                if (var2 == 0) {
                    return Compiler.ctlRegToAlias(var1);
                } else {
                    return Nios2SimPanelSimulation.this.panel.getSimulator() == null ? null : String.format("%08X", Nios2SimPanelSimulation.this.panel.getSimulator().getCtlReg(var1));
                }
            }

            public boolean isCellEditable(int var1, int var2) {
                return var2 != 0 && var1 != 4;
            }

            public void addTableModelListener(TableModelListener var1) {
            }

            public Class<?> getColumnClass(int var1) {
                return String.class;
            }

            public void removeTableModelListener(TableModelListener var1) {
            }

            public void setValueAt(Object var1, int var2, int var3) {
                try {
                    long var4 = Long.parseLong(var1.toString(), 16);
                    if ((var4 & -4294967296L) != 0L) {
                        return;
                    }

                    Nios2SimPanelSimulation.this.panel.getSimulator().setCtlReg(var2, (int)var4);
                } catch (NumberFormatException var7) {
                }

            }
        };
        DefaultTableCellRenderer var2 = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
                Component var7 = super.getTableCellRendererComponent(var1, var2, false, false, var5, var6);
                if (var6 == 0) {
                    var7.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
                    var7.setBackground(DARK_COLOR);
                    this.setToolTipText("ctl" + var5);
                } else {
                    var7.setFont(Nios2SimPanel.TABLE_FONT);
                    var7.setForeground(DARK_MODE_FONT_COLOR);
                    if (Nios2SimPanelSimulation.this.panel.getSimulator().getCtlRegModified(var5)) {
                        var7.setBackground(new Color(255, 150, 150));
                    } else {
                        var7.setBackground(DARK_COLOR);
                    }

                    Nios2SimPanelSimulation.makeToolTip(this, "<b>" + Compiler.ctlRegToAlias(var5) + "</b>/ctl" + var5, Nios2SimPanelSimulation.this.panel.getSimulator().getCtlReg(var5));
                }

                return var7;
            }
        };
        this.interruptTable = new JTable(var1);
        this.interruptTable.setDefaultRenderer(String.class, var2);
        this.interruptTable.getTableHeader().setReorderingAllowed(false);
        JTextField var3 = new JTextField();
        var3.setFont(Nios2SimPanel.TABLE_FONT);
        var3.setForeground(DARK_MODE_FONT_COLOR);
        var3.setBorder(new EmptyBorder(0, 1, 0, 0));
        var3.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent var1) {
            }

            public void focusLost(FocusEvent var1) {
                if (Nios2SimPanelSimulation.this.interruptTable.getCellEditor() != null) {
                    Nios2SimPanelSimulation.this.interruptTable.getCellEditor().stopCellEditing();
                }

            }
        });
        this.interruptTable.setDefaultEditor(String.class, new DefaultCellEditor(var3));
        JPanel var4 = new JPanel();
        var4.setBorder(BorderFactory.createTitledBorder("Control Registers Content"));
        var4.add(this.interruptTable);
        var4.setOpaque(true);
        var4.setBackground(DARK_COLOR);
        Box var5 = Box.createHorizontalBox();
        var5.setBorder(BorderFactory.createTitledBorder("Interrupt Request Vector"));
        var5.setOpaque(true);
        var5.setBackground(DARK_COLOR);
        var5.add(Box.createGlue());
        JLabel var6 = new JLabel("IRQ: ");
        var6.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
        var6.setToolTipText("Interrupt resquest vector");
        var5.add(var6);
        var5.add(this.getIRQLabel());
        var5.add(Box.createGlue());
        Box var7 = Box.createHorizontalBox();
        var7.setBorder(BorderFactory.createTitledBorder("Program Counter"));
        var7.setOpaque(true);
        var7.setBackground(DARK_COLOR);
        var7.add(Box.createGlue());
        var6 = new JLabel("PC: ");
        var6.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
        var6.setToolTipText("Program Counter");
        var7.add(var6);
        var7.add(this.getPCLabel());
        var7.add(Box.createGlue());
        Box var8 = Box.createHorizontalBox();
        var8.setBorder(BorderFactory.createTitledBorder("Cycles Count"));
        var8.setOpaque(true);
        var8.setBackground(DARK_COLOR);
        var8.add(Box.createGlue());
        var6 = new JLabel("CC: ");
        var6.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
        var6.setToolTipText("Cycles Count");
        var8.add(var6);
        var8.add(this.getCCLabel());
        var8.add(Box.createGlue());
        Box var9 = Box.createVerticalBox();
        var9.add(var7);
        var9.add(var8);
        var9.add(var5);
        Box var10 = Box.createHorizontalBox();
        var10.setOpaque(true);
        var10.setBackground(DARK_COLOR);
        var10.add(var9);
        var10.add(var4);
        return var10;
    }

    private class StatusCellComponent extends JComponent {
        private static final long serialVersionUID = 1L;
        int row;

        public StatusCellComponent(int var2, int var3) {
            this.row = var2;
        }

        public void paint(Graphics var1) {
            int var2 = this.getWidth();
            int var3 = this.getHeight();
            Line var4 = (Line)Nios2SimPanelSimulation.this.panel.getSimulator().getLines().get(this.row);
            if (var4.isValid()) {
                if (var4.address == Nios2SimPanelSimulation.this.panel.getSimulator().getPC()) {
                    var1.setColor(new Color(0, 0, 255));
                    var1.fillRect(0, 0, var2, var3);
                }

                if (var4.breakpoint) {
                    int var5 = Math.min(var2, var3) - 7;
                    int var6 = (var2 - var5) / 2;
                    int var7 = (var3 - var5) / 2;
                    var1.setColor(new Color(255, 128, 128));
                    var1.fillOval(var6, var7, var5, var5);
                    var1.setColor(new Color(255, 0, 0));
                    var1.drawOval(var6, var7, var5, var5);
                }
            } else {
                var1.setColor(DARK_COLOR);
                var1.fillRect(0, 0, var2, var3);
            }

        }
    }
}
