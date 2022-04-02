//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ProcessorPane extends JPanel {
    private static final Font FONT_COMPONENT = new Font("Arial", 0, 12);
    private static final Font FONT_DATA = new Font("courier", 0, 12);
    private static final String MEMORY_LABEL = "Memory";
    private static final String CONTROLER_LABEL = "Controler";
    private static final String CPU_LABEL = "CPU";
    private static final String PC_LABEL = "PC";
    private static final String ALU_LABEL = "ALU";
    private static final String RF_LABEL = "Register File";
    private static final double LABEL_HEIGHT_COEFF = 0.05D;
    private int width;
    private int height;
    private int boxHeight;
    private int memoryX;
    private int memoryWidth;
    private int memoryY;
    private int memoryHeight;
    private int cpuX;
    private int cpuWidth;
    private int cpuY;
    private int cpuHeight;
    private int controlerX;
    private int controlerWidth;
    private int controlerY;
    private int controlerHeight;
    private int registerX;
    private int registerWidth;
    private int registerY;
    private int registerHeight;
    private int pcX;
    private int pcWidth;
    private int pcY;
    private int pcHeight;
    private int aluX1;
    private int aluY1;
    private int aluX2;
    private int aluY2;
    private int aluX3;
    private int aluY3;
    private int aluX4;
    private int aluY4;
    private int aluX5;
    private int aluY5;
    private int aluX6;
    private int aluY6;
    private int aluX7;
    private int aluY7;
    private int memIntZone1X;
    private int memIntZone1Y;
    private int memIntZone1Width;
    private int memIntZone1Height;
    private int memZone1X;
    private int memZone1Y;
    private int memZone1Width;
    private int memZone1Height;
    private int memZone2X;
    private int memZone2Y;
    private int memZone2Width;
    private int memZone2Height;
    private int pcBoxX;
    private int pcBoxY;
    private int pcBoxWidth;
    private int pcBoxHeight;
    private int rfInputBoxX;
    private int rfInputBoxY;
    private int rfInputBoxWidth;
    private int rfInputBoxHeight;
    private int rfOutputBox1X;
    private int rfOutputBox1Y;
    private int rfOutputBox1Width;
    private int rfOutputBox1Height;
    private int rfOutputBox2X;
    private int rfOutputBox2Y;
    private int rfOutputBox2Width;
    private int rfOutputBox2Height;
    private int aluBoxX;
    private int aluBoxY;
    private int aluBoxWidth;
    private int aluBoxHeight;
    private Line2D memToContro1;
    private Line2D memToContro2;
    private Line2D memToRf;
    private Line2D rfOut1ToAlu;
    private Line2D rfOut2ToAlu;
    private Line2D rfOut1ToPC;
    private Line2D rfOut2ToPC;
    private Line2D pcToMem;
    private String currentInstruction;
    private String pcValue;
    private String aluOperation;
    private String rfInput;
    private String rfOutput1;
    private String rfOutput2;
    private String storedValue;
    private static final long serialVersionUID = 1L;

    public ProcessorPane() {
        this.setBackground(DARK_COLOR);
        this.setMinimumSize(new Dimension(420, 140));
        this.setMaximumSize(new Dimension(1260, 420));
        this.setPreferredSize(new Dimension(1260, 420));
        this.setLayout(new FlowLayout());
    }

    public void paintComponent(Graphics var1) {
        this.computeCoordinates();
        this.drawProcessor(var1);
    }

    public void updateFetch(String var1, String var2) {
        System.out.println("Fetch:\n  " + var1 + "\n  " + var2);
    }

    public void updateDecode(String var1, String var2, String var3, String var4, String[] var5) {
        System.out.println("Decode:\n  " + var1 + "\n  " + var2 + "\n  " + var3 + "\n  " + var4 + "\n  " + var5[0] + ", " + var5[1] + ", " + var5[2]);
    }

    public void updateExecute(InstructionType var1, String var2, String var3, boolean var4, String[] var5) {
        System.out.println("Execute:\n  " + var1 + "\n  " + var2 + "\n  " + var3 + "\n  " + var4 + "\n  " + var5[0] + ", " + var5[1] + ", " + var5[2]);
    }

    public void updateMemory(boolean var1, String var2, String var3) {
        System.out.println("Memory:\n  " + var1 + "\n  " + var2 + "\n  " + var3);
    }

    private void computeCoordinates() {
        this.width = this.getWidth();
        this.height = this.getHeight();
        this.boxHeight = (int)(0.035D * (double)this.height);
        this.memoryX = (int)(0.05D * (double)this.width);
        this.memoryWidth = (int)(0.15D * (double)this.width);
        this.memoryY = (int)(0.1D * (double)this.height);
        this.memoryHeight = (int)(0.8D * (double)this.height);
        this.cpuX = (int)(0.35D * (double)this.width);
        this.cpuWidth = (int)(0.6D * (double)this.width);
        this.cpuY = (int)(0.05D * (double)this.height);
        this.cpuHeight = (int)(0.9D * (double)this.height);
        this.controlerX = (int)((double)this.cpuX + 0.2D * (double)this.cpuWidth);
        this.controlerWidth = (int)(0.3D * (double)this.cpuWidth);
        this.controlerY = (int)((double)this.cpuY + 0.1D * (double)this.cpuHeight);
        this.controlerHeight = (int)(0.35D * (double)this.cpuHeight);
        this.registerX = (int)((double)this.cpuX + 0.2D * (double)this.cpuWidth);
        this.registerWidth = (int)(0.3D * (double)this.cpuWidth);
        this.registerY = (int)((double)this.cpuY + 0.5D * (double)this.cpuHeight);
        this.registerHeight = (int)(0.4D * (double)this.cpuHeight);
        this.pcX = (int)((double)this.cpuX + 0.7D * (double)this.cpuWidth);
        this.pcWidth = (int)(0.2D * (double)this.cpuWidth);
        this.pcY = (int)((double)this.cpuY + 0.05D * (double)this.cpuHeight);
        this.pcHeight = (int)(0.2D * (double)this.cpuHeight);
        this.aluX1 = (int)((double)this.cpuX + 0.7D * (double)this.cpuWidth);
        this.aluY1 = (int)((double)this.cpuY + 0.5D * (double)this.cpuHeight);
        this.aluX2 = (int)((double)this.cpuX + 0.85D * (double)this.cpuWidth);
        this.aluY2 = (int)((double)this.cpuY + 0.65D * (double)this.cpuHeight);
        this.aluX3 = (int)((double)this.cpuX + 0.85D * (double)this.cpuWidth);
        this.aluY3 = (int)((double)this.cpuY + 0.75D * (double)this.cpuHeight);
        this.aluX4 = (int)((double)this.cpuX + 0.7D * (double)this.cpuWidth);
        this.aluY4 = (int)((double)this.cpuY + 0.9D * (double)this.cpuHeight);
        this.aluX5 = (int)((double)this.cpuX + 0.7D * (double)this.cpuWidth);
        this.aluY5 = (int)((double)this.cpuY + 0.72D * (double)this.cpuHeight);
        this.aluX6 = (int)((double)this.cpuX + 0.73D * (double)this.cpuWidth);
        this.aluY6 = (int)((double)this.cpuY + 0.7D * (double)this.cpuHeight);
        this.aluX7 = (int)((double)this.cpuX + 0.7D * (double)this.cpuWidth);
        this.aluY7 = (int)((double)this.cpuY + 0.68D * (double)this.cpuHeight);
        int var1 = this.pcX - (this.controlerWidth + this.controlerX);
        this.pcBoxX = (int)(0.2D * (double)var1 + (double)this.controlerX + (double)this.controlerWidth);
        this.pcBoxWidth = var1;
        this.pcBoxY = (int)((double)this.pcY + 0.05D * (double)this.pcHeight);
        this.pcBoxHeight = this.boxHeight;
        this.rfInputBoxWidth = (int)(0.5D * (double)this.registerWidth);
        this.rfInputBoxX = (int)((double)this.registerX - 0.5D * (double)this.rfInputBoxWidth);
        this.rfInputBoxY = (int)((double)this.registerY + 0.1D * (double)this.registerHeight);
        this.rfInputBoxHeight = this.boxHeight;
        this.rfOutputBox1X = (int)((double)(this.registerX + this.registerWidth) - 0.5D * (double)this.rfInputBoxWidth);
        this.rfOutputBox1Y = this.rfInputBoxY;
        this.rfOutputBox1Width = this.rfInputBoxWidth;
        this.rfOutputBox1Height = this.boxHeight;
        this.rfOutputBox2X = this.rfOutputBox1X;
        this.rfOutputBox2Y = (int)((double)this.registerY + 0.9D * (double)this.registerHeight - (double)this.boxHeight);
        this.rfOutputBox2Width = this.rfOutputBox1Width;
        this.rfOutputBox2Height = this.boxHeight;
        this.aluBoxX = (int)((double)this.aluX5 + 0.05D * (double)(this.aluX3 - this.aluX5));
        this.aluBoxY = (int)((double)this.aluY5 + 0.05D * (double)(this.aluY4 - this.aluY5));
        this.aluBoxWidth = (int)(1.7D * (double)(this.aluX3 - this.aluX5));
        this.aluBoxHeight = this.boxHeight;
        this.memIntZone1X = (int)(0.05D * (double)this.width);
        this.memIntZone1Y = (int)((double)this.pcBoxY + (double)this.pcBoxHeight * 0.5D);
        this.memIntZone1Width = (int)(0.15D * (double)this.width);
        this.memIntZone1Height = (int)(0.4D * (double)this.boxHeight);
        this.memZone1X = (int)(0.125D * (double)this.width);
        this.memZone1Y = (int)(0.25D * (double)this.height);
        this.memZone1Width = (int)(0.2D * (double)this.width);
        this.memZone1Height = this.boxHeight;
        this.memZone2X = (int)(0.125D * (double)this.width);
        this.memZone2Y = this.rfInputBoxY;
        this.memZone2Width = (int)(0.2D * (double)this.width);
        this.memZone2Height = this.boxHeight;
        float var2 = (float)((double)this.memZone1X + 0.8D * (double)this.memZone1Width);
        float var3 = (float)(this.memZone1Y + this.memZone1Height);
        float var5 = (float)(this.memZone1Y + this.memZone1Height + this.boxHeight);
        this.memToContro1 = new Float(var2, var3, var2, var5);
        float var8 = (float)this.controlerX;
        this.memToContro2 = new Float(var2, var5, var8, var5);
        float var10 = (float)(this.memZone2X + this.memZone2Width);
        float var11 = (float)((double)this.memZone2Y + 0.5D * (double)this.memZone2Height);
        float var12 = (float)this.rfInputBoxX;
        this.memToRf = new Float(var10, var11, var12, var11);
        float var14 = (float)(this.rfOutputBox1X + this.rfOutputBox1Width);
        float var15 = (float)((double)this.rfOutputBox1Y + 0.5D * (double)this.rfOutputBox1Height);
        float var16 = (float)this.aluX1;
        this.rfOut1ToAlu = new Float(var14, var15, var16, var15);
        float var19 = (float)((double)this.rfOutputBox2Y + 0.5D * (double)this.rfOutputBox2Height);
        float var20 = (float)this.aluX1;
        this.rfOut2ToAlu = new Float(var14, var19, var20, var19);
        float var22 = (float)((double)var14 + 0.8D * (double)(var16 - var14));
        float var25 = (float)(this.pcBoxY + this.pcBoxHeight);
        this.rfOut1ToPC = new Float(var22, var15, var22, var25);
        float var26 = (float)((double)var14 + 0.5D * (double)(var20 - var14));
        float var29 = (float)(this.pcBoxY + this.pcBoxHeight);
        this.rfOut2ToPC = new Float(var26, var19, var26, var29);
        float var30 = (float)this.pcBoxX;
        float var31 = (float)((double)this.pcBoxY + (double)this.pcBoxHeight * 0.5D);
        float var32 = (float)(this.memIntZone1X + this.memIntZone1Width);
        this.pcToMem = new Float(var30, var31, var32, var31);
    }

    private void drawProcessor(Graphics var1) {
        Graphics2D var2 = (Graphics2D)var1;
        var2.setColor(DARK_MODE_FONT_COLOR);
        var2.drawRect(this.memoryX, this.memoryY, this.memoryWidth, this.memoryHeight);
        var2.drawRect(this.cpuX, this.cpuY, this.cpuWidth, this.cpuHeight);
        var2.drawRect(this.controlerX, this.controlerY, this.controlerWidth, this.controlerHeight);
        var2.drawRect(this.registerX, this.registerY, this.registerWidth, this.registerHeight);
        var2.drawRect(this.pcX, this.pcY, this.pcWidth, this.pcHeight);
        var2.drawLine(this.aluX1, this.aluY1, this.aluX2, this.aluY2);
        var2.drawLine(this.aluX2, this.aluY2, this.aluX3, this.aluY3);
        var2.drawLine(this.aluX3, this.aluY3, this.aluX4, this.aluY4);
        var2.drawLine(this.aluX4, this.aluY4, this.aluX5, this.aluY5);
        var2.drawLine(this.aluX5, this.aluY5, this.aluX6, this.aluY6);
        var2.drawLine(this.aluX6, this.aluY6, this.aluX7, this.aluY7);
        var2.drawLine(this.aluX7, this.aluY7, this.aluX1, this.aluY1);
        var2.fillRect(this.memIntZone1X, this.memIntZone1Y, this.memIntZone1Width, this.memIntZone1Height);
        var2.drawLine(this.memIntZone1X, this.memIntZone1Y, this.memZone1X, this.memZone1Y);
        var2.drawLine(this.memIntZone1X, this.memIntZone1Y + this.memIntZone1Height, this.memZone1X, this.memZone1Y + this.memZone1Height);
        var2.drawLine(this.memIntZone1X + this.memIntZone1Width, this.memIntZone1Y, this.memZone1X + this.memZone1Width, this.memZone1Y);
        var2.drawLine(this.memIntZone1X + this.memIntZone1Width, this.memIntZone1Y + this.memIntZone1Height, this.memZone1X + this.memZone1Width, this.memZone1Y + this.memZone1Height);
        this.drawBox(var2, this.memZone1X, this.memZone1Y, this.memZone1Width, this.memZone1Height, true, FONT_DATA, this.currentInstruction);
        this.drawBox(var2, this.memZone2X, this.memZone2Y, this.memZone2Width, this.memZone2Height, true, FONT_DATA, this.storedValue);
        this.drawBox(var2, this.pcBoxX, this.pcBoxY, this.pcBoxWidth, this.pcBoxHeight, true, FONT_DATA, this.pcValue);
        this.drawBox(var2, this.rfInputBoxX, this.rfInputBoxY, this.rfInputBoxWidth, this.rfInputBoxHeight, true, FONT_DATA, this.rfInput);
        this.drawBox(var2, this.rfOutputBox1X, this.rfOutputBox1Y, this.rfOutputBox1Width, this.rfOutputBox1Height, true, FONT_DATA, this.rfOutput1);
        this.drawBox(var2, this.rfOutputBox2X, this.rfOutputBox2Y, this.rfOutputBox2Width, this.rfOutputBox2Height, true, FONT_DATA, this.rfOutput2);
        this.drawBox(var2, this.aluBoxX, this.aluBoxY, this.aluBoxWidth, this.aluBoxHeight, true, FONT_DATA, this.aluOperation);
        FontMetrics var3 = var2.getFontMetrics(FONT_COMPONENT);
        int var4 = var3.getAscent() + var3.getDescent();
        int var5 = (int)(0.05D * (double)this.height);
        double var6 = (double)var5 / (double)var4;
        AffineTransform var8 = new AffineTransform();
        var8.scale(var6, var6);
        Font var9 = FONT_COMPONENT.deriveFont(var8);
        FontMetrics var10 = var2.getFontMetrics(var9);
        int var11 = var10.getDescent();
        int var12 = var10.getAscent();
        var2.setFont(var9);
        var2.drawString("Memory", this.memoryX + 2, this.memoryY - var11);
        var2.drawString("CPU", this.cpuX + 2, this.cpuY + var12);
        var2.drawString("Controler", this.controlerX + 2, this.controlerY + var12);
        var2.drawString("Register File", this.registerX + 2, this.registerY + this.registerHeight / 2 + var12 / 2);
        var2.drawString("PC", this.pcX + 2, this.pcY + this.pcHeight / 2 + var12 / 2);
        var2.drawString("ALU", this.aluX6 + 2, this.aluY6 + var12 / 2);
        var2.setColor(Color.LIGHT_GRAY);
        var2.draw(this.memToContro1);
        this.drawArrow(var2, this.memToContro2);
        this.drawArrow(var2, this.memToRf);
        this.drawArrow(var2, this.rfOut1ToAlu);
        this.drawArrow(var2, this.rfOut2ToAlu);
        this.drawArrow(var2, this.rfOut1ToPC);
        this.drawArrow(var2, this.rfOut2ToPC);
        this.drawArrow(var2, this.pcToMem);
        var2.setColor(DARK_MODE_FONT_COLOR);
    }

    private void drawBox(Graphics2D var1, int var2, int var3, int var4, int var5, boolean var6, Font var7, String var8) {
        var1.setColor(DARK_COLOR);
        var1.fillRect(var2, var3, var4, var5);
        var1.setColor(DARK_MODE_FONT_COLOR);
        if (var6) {
            var1.drawRect(var2, var3, var4, var5);
        }

        if (var8 != null) {
            int var9 = (int)((double)var2 + 0.05D * (double)var4);
            int var10 = (int)((double)var2 + 0.95D * (double)var4);
            FontMetrics var11 = var1.getFontMetrics(var7);
            int var12 = var11.getAscent() + var11.getDescent();
            int var13 = var11.stringWidth(var8);
            double var14 = (double)var5 / (double)var12;
            double var16 = (double)(var10 - var9) / (double)var13;
            AffineTransform var18 = new AffineTransform();
            var18.scale(var16, var14);
            Font var19 = var7.deriveFont(var18);
            FontMetrics var20 = var1.getFontMetrics(var19);
            var1.setFont(var19);
            var1.drawString(var8, var9, var3 + var20.getAscent());
        }

    }

    private void drawArrow(Graphics2D var1, Line2D var2) {
        var1.draw(var2);
    }

    public void setCurrentInstruction(String var1) {
        this.currentInstruction = var1;
        this.repaint();
    }

    public void setPcValue(String var1) {
        this.pcValue = var1;
        this.repaint();
    }

    public void setAluOperation(String var1) {
        this.aluOperation = var1;
        this.repaint();
    }

    public void setRfInput(String var1) {
        this.rfInput = var1;
        this.repaint();
    }

    public void setRfOutput1(String var1) {
        this.rfOutput1 = var1;
        this.repaint();
    }

    public void setRfOutput2(String var1) {
        this.rfOutput2 = var1;
        this.repaint();
    }

    public void setStoredValue(String var1) {
        this.storedValue = var1;
        this.repaint();
    }

    public static enum InstructionType {
        ALU,
        BRANCH,
        JUMP,
        NOP;

        private InstructionType() {
        }
    }
}
