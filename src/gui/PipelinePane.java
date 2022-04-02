//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.RenderingHints.Key;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class PipelinePane extends JComponent {
    private static final long serialVersionUID = 1L;
    private static final int REG_WIDTH = 5;
    private static final int HEIGHT = 17;
    private static final int WIDTH = 40;
    private final int CHAR_WIDTH;
    private final int CHAR_HEIGHT;
    private static final Font FONT_STAGES = new Font("Arial", 0, 12);
    private static final Font FONT_INSTR = new Font("courier", 0, 12);
    private static final Color BGD_COLOR = new Color(255, 255, 255, 196);
    private HashMap<Key, Object> renderingMap;
    private String[] descriptions;
    private String[] addresses;
    private boolean[] forwarding_paths;
    private boolean forwardingWDEnabled;
    private boolean forwardingEEEnabled;
    private boolean forwardingMEEnabled;
    private int gridStep;
    private int offsetx;
    private Font fontStage;
    private Font fontAddr;

    public PipelinePane() {
        this.CHAR_WIDTH = this.getFontMetrics(FONT_STAGES).charWidth('M');
        this.CHAR_HEIGHT = this.getFontMetrics(FONT_INSTR).getHeight();
        this.forwardingWDEnabled = false;
        this.forwardingEEEnabled = false;
        this.forwardingMEEnabled = false;
        this.setBackground(DARK_COLOR);
        this.setOpaque(true);
        this.renderingMap = new HashMap(1);
        this.renderingMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        this.forwarding_paths = new boolean[]{false, false, false};
        this.setMinimumSize(new Dimension(420, 140));
        this.setMaximumSize(new Dimension(1260, 420));
        this.setPreferredSize(new Dimension(1260, 420));
    }

    public void update(boolean[] var1, String[] var2, String[] var3) {
        this.forwarding_paths = var1;
        this.descriptions = var2;
        this.addresses = var3;
        this.repaint();
    }

    public void setforwardingWDEnabled(boolean var1) {
        this.forwardingWDEnabled = var1;
        this.repaint();
    }

    public void setforwardingEEEnabled(boolean var1) {
        this.forwardingEEEnabled = var1;
        this.repaint();
    }

    public void setforwardingMEEnabled(boolean var1) {
        this.forwardingMEEnabled = var1;
        this.repaint();
    }

    public void setBounds(int var1, int var2, int var3, int var4) {
        super.setBounds(var1, var2, var3, var4);
        int var5 = var3 / 40;
        int var6 = var4 / 17;
        if (var6 < var5) {
            this.gridStep = var6;
        } else {
            this.gridStep = var5;
        }

        this.offsetx = (var3 - this.gridStep * 40) / 2;
        double var7 = (double)(2 * this.gridStep) / (double)this.CHAR_WIDTH;
        this.fontStage = FONT_STAGES.deriveFont(new AffineTransform(new double[]{var7, 0.0D, 0.0D, var7}));
        var7 = (double)this.gridStep / (double)this.CHAR_HEIGHT;
        this.fontAddr = FONT_INSTR.deriveFont(new AffineTransform(new double[]{var7, 0.0D, 0.0D, var7}));
    }

    public void paint(Graphics var1) {
        Graphics2D var2 = (Graphics2D)var1;
        var2.setRenderingHints(this.renderingMap);
        var2.setColor(DARK_COLOR);
        var2.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.forwardingEEEnabled) {
            if (this.forwarding_paths[0]) {
                var2.setColor(Color.green);
            } else {
                var2.setColor(DARK_MODE_FONT_COLOR);
            }

            var2.drawLine(this.offsetx + 24 * this.gridStep, 8 * this.gridStep, this.offsetx + 25 * this.gridStep, 8 * this.gridStep);
            var2.drawLine(this.offsetx + 25 * this.gridStep, 8 * this.gridStep, this.offsetx + 25 * this.gridStep, 6 * this.gridStep);
            var2.drawLine(this.offsetx + 25 * this.gridStep, 6 * this.gridStep, this.offsetx + 17 * this.gridStep, 6 * this.gridStep);
            var2.drawLine(this.offsetx + 17 * this.gridStep, 6 * this.gridStep, this.offsetx + 17 * this.gridStep, 10 * this.gridStep);
            var2.drawLine(this.offsetx + 17 * this.gridStep, 10 * this.gridStep, this.offsetx + 18 * this.gridStep, 10 * this.gridStep);
        }

        if (this.forwardingMEEnabled) {
            if (this.forwarding_paths[1]) {
                var2.setColor(Color.green);
            } else {
                var2.setColor(DARK_MODE_FONT_COLOR);
            }

            var2.drawLine(this.offsetx + 32 * this.gridStep, 14 * this.gridStep, this.offsetx + 33 * this.gridStep, 14 * this.gridStep);
            var2.drawLine(this.offsetx + 33 * this.gridStep, 14 * this.gridStep, this.offsetx + 33 * this.gridStep, 16 * this.gridStep);
            var2.drawLine(this.offsetx + 33 * this.gridStep, 16 * this.gridStep, this.offsetx + 17 * this.gridStep, 16 * this.gridStep);
            var2.drawLine(this.offsetx + 17 * this.gridStep, 16 * this.gridStep, this.offsetx + 17 * this.gridStep, 12 * this.gridStep);
            var2.drawLine(this.offsetx + 17 * this.gridStep, 12 * this.gridStep, this.offsetx + 18 * this.gridStep, 12 * this.gridStep);
        }

        var2.setColor(DARK_MODE_FONT_COLOR);
        if (this.forwardingWDEnabled && this.forwarding_paths[2]) {
            var2.setColor(Color.green);
        }

        var2.drawLine(this.offsetx + 36 * this.gridStep, 9 * this.gridStep, this.offsetx + 36 * this.gridStep, 3 * this.gridStep);
        var2.drawLine(this.offsetx + 36 * this.gridStep, 3 * this.gridStep, this.offsetx + 27 * this.gridStep, 3 * this.gridStep);
        var2.drawLine(this.offsetx + 21 * this.gridStep, 3 * this.gridStep, this.offsetx + 12 * this.gridStep, 3 * this.gridStep);
        var2.drawLine(this.offsetx + 12 * this.gridStep, 3 * this.gridStep, this.offsetx + 12 * this.gridStep, 9 * this.gridStep);
        var2.setColor(DARK_MODE_FONT_COLOR);
        int var3 = this.offsetx + 2 * this.gridStep;
        int var4 = 9 * this.gridStep;
        this.draw_stagebox(var2, 'F', var3, var4);
        this.draw_addresses(var2, 0, var3, var4 - 2 * this.gridStep);
        this.draw_instruction(var2, 0, var3 - 2 * this.gridStep, var4 + 5 * this.gridStep);
        var2.drawLine(var3 + 4 * this.gridStep, var4 + 2 * this.gridStep, var3 + 8 * this.gridStep, var4 + 2 * this.gridStep);
        var2.fillRect(var3 + 6 * this.gridStep - 2, var4 - 2 * this.gridStep, 5, 8 * this.gridStep);
        var3 = this.offsetx + 10 * this.gridStep;
        this.draw_stagebox(var2, 'D', var3, var4);
        this.draw_addresses(var2, 1, var3, var4 - 2 * this.gridStep);
        this.draw_instruction(var2, 1, var3 - 2 * this.gridStep, var4 + 5 * this.gridStep);
        var2.drawLine(var3 + 4 * this.gridStep, var4 + 2 * this.gridStep, var3 + 8 * this.gridStep, var4 + 2 * this.gridStep);
        var2.fillRect(var3 + 6 * this.gridStep - 2, var4 - 2 * this.gridStep, 5, 8 * this.gridStep);
        var3 = this.offsetx + 18 * this.gridStep;
        this.draw_stagebox(var2, 'E', var3, var4);
        this.draw_addresses(var2, 2, var3, var4 - 2 * this.gridStep);
        this.draw_instruction(var2, 2, var3 - 2 * this.gridStep, var4 + 5 * this.gridStep);
        var2.drawLine(var3 + 4 * this.gridStep, var4 + 2 * this.gridStep, var3 + 8 * this.gridStep, var4 + 2 * this.gridStep);
        var2.fillRect(var3 + 6 * this.gridStep - 2, var4 - 2 * this.gridStep, 5, 8 * this.gridStep);
        var2.setFont(this.fontStage);
        var3 = this.offsetx + 26 * this.gridStep;
        this.draw_stagebox(var2, 'M', var3, var4);
        this.draw_addresses(var2, 3, var3, var4 - 2 * this.gridStep);
        this.draw_instruction(var2, 3, var3 - 2 * this.gridStep, var4 + 5 * this.gridStep);
        var2.drawLine(var3 + 4 * this.gridStep, var4 + 2 * this.gridStep, var3 + 8 * this.gridStep, var4 + 2 * this.gridStep);
        var2.fillRect(var3 + 6 * this.gridStep - 2, var4 - 2 * this.gridStep, 5, 8 * this.gridStep);
        var3 = this.offsetx + 34 * this.gridStep;
        this.draw_stagebox(var2, 'W', var3, var4);
        this.draw_addresses(var2, 4, var3, var4 - 2 * this.gridStep);
        this.draw_instruction(var2, 4, var3 - 2 * this.gridStep, var4 + 5 * this.gridStep);
        var3 = this.offsetx + 21 * this.gridStep;
        var4 = this.gridStep;
        var1.setFont(this.fontStage);
        var1.drawRect(var3, var4, 6 * this.gridStep, 4 * this.gridStep);
        Rectangle var5 = var1.getFontMetrics().getStringBounds("RF", var1).getBounds();
        var1.drawChars("RF".toCharArray(), 0, 2, var3 + (6 * this.gridStep - var5.width) / 2, var4 + (4 * this.gridStep - var5.height) / 2 - var5.y);
    }

    private void draw_addresses(Graphics2D var1, int var2, int var3, int var4) {
        if (this.addresses != null) {
            var1.setFont(this.fontAddr);
            Rectangle var5 = var1.getFontMetrics().getStringBounds(this.addresses[var2], var1).getBounds();
            var3 += (4 * this.gridStep - var5.width) / 2;
            var1.setColor(BGD_COLOR);
            var1.fillRect(var3 + 1 + 2, var4, var5.width, this.gridStep);
            var1.setColor(DARK_MODE_FONT_COLOR);
            var1.drawChars(this.addresses[var2].toCharArray(), 0, this.addresses[var2].length(), var3, var4 + (this.gridStep - var5.height) / 2 - var5.y);
        }
    }

    private void draw_instruction(Graphics2D var1, int var2, int var3, int var4) {
        if (this.descriptions != null) {
            Rectangle2D var5 = this.getFontMetrics(FONT_INSTR).getStringBounds(this.descriptions[var2], var1);
            double var6 = (double)this.gridStep / var5.getHeight();
            double var8 = (double)(7 * this.gridStep) / var5.getWidth();
            if (var6 > var8) {
                var6 = var8;
            }

            Font var10 = FONT_INSTR.deriveFont(new AffineTransform(new double[]{var6, 0.0D, 0.0D, var6}));
            GlyphVector var11 = var10.createGlyphVector(var1.getFontRenderContext(), this.descriptions[var2]);
            var5 = var11.getVisualBounds();
            float var12 = (float)var3;
            var12 += ((float)(8 * this.gridStep) - (float)var5.getWidth()) / 2.0F;
            float var13 = (float)var4;
            var13 = var13 + ((float)this.gridStep - (float)var5.getHeight()) / 2.0F - (float)var5.getMinY();
            var1.setColor(BGD_COLOR);
            var1.fillRect((int)var12 + 1 + 2, var4, (int)var5.getWidth(), this.gridStep);
            var1.setColor(DARK_MODE_FONT_COLOR);
            var1.drawGlyphVector(var11, var12, var13);
        }
    }

    private void draw_stagebox(Graphics2D var1, char var2, int var3, int var4) {
        var1.setFont(this.fontStage);
        var1.drawRect(var3, var4, 4 * this.gridStep, 4 * this.gridStep);
        Rectangle var5 = var1.getFontMetrics().getStringBounds("" + var2, var1).getBounds();
        var1.drawChars(new char[]{var2}, 0, 1, var3 + (4 * this.gridStep - var5.width) / 2, var4 + (4 * this.gridStep - var5.height) / 2 - var5.y);
    }
}
