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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ModuleLED extends Module {
    private static final int PUSLEWIDTH = 3;
    private JTable table;
    private LEDPanel ledPanel;

    public ModuleLED(Nios2SimPanel var1, String var2, int var3) {
        super(var1, var3, 16, var2);
    }

    public ModuleLED(Nios2SimPanel var1, int var2, String var3, int var4, HashMap<String, String> var5) throws CompilerException {
        super(var1, var2, 16, var3);
    }

    public JComponent getPane() {
        this.table = this.getDefaultJTable(new String[]{"Address", "Data"}, new String[]{"0: leds0", "4: leds1", "8: leds2", "C: pulsewidth"}, (boolean[])null);
        table.setForeground(DARK_MODE_FONT_COLOR);
        JPanel var1 = new JPanel(new BorderLayout());
        var1.add(this.table.getTableHeader(), "First");
        var1.add(this.table, "Center");
        this.ledPanel = new LEDPanel();
        JPanel var2 = new JPanel(new FlowLayout());
        var2.add(var1);
        var2.add(this.ledPanel);
        var2.setBackground(Color.white);
        this.setDefaultColumnSize(this.table);
        JScrollPane var3 = new JScrollPane(var2);
        return var3;
    }

    public void commitEdits() {
        TableCellEditor var1 = this.table.getCellEditor();
        if (var1 != null) {
            var1.stopCellEditing();
        }

    }

    public void refresh() {
        this.table.tableChanged(new TableModelEvent(this.table.getModel()));
        this.ledPanel.repaint();
    }

    protected void setTableValueAt(int var1, int var2) {
        this.write(var1 << 2, var2);
    }

    public boolean write(int var1, int var2) {
        if (var1 >> 2 == 3) {
            var2 &= 255;
        }

        return super.write(var1, var2);
    }

    public void reset() {
        super.reset();
        this.getData()[3] = 15;
    }

    public class LEDPanel extends JComponent {
        private static final long serialVersionUID = 1L;
        private final int MARGE = 1;
        private final int LED_SIZE = 10;
        private final int OFFSET = 5;

        public LEDPanel() {
            this.setPreferredSize(new Dimension(138, 94));
            this.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent var1) {
                    if (var1.getClickCount() == 2) {
                        LEDPanel.this.toggleLED(var1.getX(), var1.getY());
                    }

                    super.mouseClicked(var1);
                }
            });
        }

        private void toggleLED(int var1, int var2) {
            var1 = var1 - 5 - 1;
            var1 /= 11;
            var2 = var2 - 5 - 1;
            var2 /= 11;
            if (var1 >= 0 && var1 <= 11 && var2 >= 0 && var2 <= 7) {
                int var3 = var1 >> 2;
                int var4 = ModuleLED.this.getData()[var3];
                int var5 = (var1 % 4 << 3) + var2;
                var5 = 1 << var5;
                ModuleLED.this.write(var3 << 2, var4 ^ var5);
                ModuleLED.this.refresh();
            }
        }

        public void paint(Graphics var1) {
            super.paint(var1);
            var1.setColor(new Color(0, 128, 0));
            var1.fillRect(5, 5, 133, 89);
            int var2 = ModuleLED.this.getData()[3] << 4;
            if (var2 > 255) {
                var2 = 255;
            }

            for(int var3 = 0; var3 < 3; ++var3) {
                int var4 = ModuleLED.this.getData()[var3];

                for(int var5 = 0; var5 < 32; ++var5) {
                    if ((var4 & 1) == 0) {
                        var1.setColor(new Color(0, 0, 0));
                    } else {
                        var1.setColor(new Color(var2, var2, 0));
                    }

                    var1.fillRect(6 + 11 * ((var5 >> 3) + var3 * 4), 6 + 11 * (var5 & 7), 10, 10);
                    var4 >>= 1;
                }
            }

        }
    }
}
