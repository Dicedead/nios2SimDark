//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import compiler.Compiler;
import compiler.Compiler.state_type;
import compiler.CompilerException;
import compiler.Line;
import gui.Nios2SimPanelMessage.message_type;
import gui.modules.Module;
import nios2sim.MessageListener;
import nios2sim.Nios2Sim;
import nios2sim.Simulator;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class Nios2SimPanel extends JPanel implements MessageListener, DropTargetListener {
    private static final long serialVersionUID = 1L;
    public static final Font TABLE_FONT = new Font("Monospaced", 0, 12);
    public static final Font TABLE_FONT_EDIT = new Font("Monospaced", 0, 11);
    public static final Font TABLE_FONT_BOLD = new Font("Monospaced", 1, 12);
    public int CHAR_WIDTH = 7;
    public int LINE_HEIGHT = 0;
    public int LINE_ASCENT = 0;
    public static final int SPACE_PER_TAB = 4;
    private Nios2Sim nios2sim;
    private Nios2SimTextPane textPane = null;
    private DialogFindReplace dialogFindReplace = null;
    static final String EDITORVIEW = "Editor";
    static final String SIMVIEW = "Sim";
    private JPanel topView;
    private JScrollPane textScroll;
    private Compiler compiler = null;
    private Simulator simulator = null;
    XMLparser parser = null;
    private int textBaseAddress = 0;
    private int dataBaseAddress = 0;
    private int interruptAddress = 0;
    private Module textModule = null;
    private Module dataModule = null;
    private Module[] modules;
    private boolean simulating = false;
    Nios2SimPanelMessage messages;
    Nios2SimPanelSimulation simulation;
    private LinkedList<Integer> warningLines;
    private LinkedList<Integer> errorLines;
    private int flashLine = -1;
    static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss : ");
    boolean firstPaint = true;

    public Nios2SimPanel(Nios2Sim var1) {
        this.nios2sim = var1;
        this.messages = new Nios2SimPanelMessage(this);
        this.messages.addMessage(-1, message_type.INFO, "Welcome to NIOS II simulator 0.6.4!");
        this.warningLines = new LinkedList();
        this.errorLines = new LinkedList();
        this.setLayout(new BorderLayout());
        this.topView = new JPanel();
        CardLayout var2 = new CardLayout();
        this.topView.setLayout(var2);
        this.textPane = new Nios2SimTextPane(this);
        this.textScroll = new JScrollPane(this.textPane);
        new DropTarget(this.textPane, this);
        this.simulation = new Nios2SimPanelSimulation(this);
        this.topView.add(this.textScroll, "Editor");
        this.topView.add(this.simulation.getSplitPane(), "Sim");
        JSplitPane var3 = new JSplitPane(0, this.topView, this.messages.getScrollPane());
        Dimension var4 = new Dimension(0, 100);
        this.topView.setMinimumSize(var4);
        this.messages.getScrollPane().setMinimumSize(var4);
        this.add(var3);
        var3.setResizeWeight(1.0D);
    }

    public Module getTextModule() {
        return this.textModule;
    }

    public Module getDataModule() {
        return this.dataModule;
    }

    public Nios2SimPanelSimulation getSimulation() {
        return this.simulation;
    }

    public void loadSystem(InputStream var1) throws Exception {
        this.parser = new XMLparser(this, var1);

        try {
            this.parser.extractInfo();
            this.parser.checkInfo();
            this.defineSystem(this.parser.getModules(), this.parser.getInterruptOffset(), this.parser.getTextModule(), this.parser.getDataModule(), this.parser.getTextOffset(), this.parser.getDataOffset());
        } catch (CompilerException var3) {
            throw new CompilerException(var3.getMessage());
        }
    }

    public void paint(Graphics var1) {
        super.paint(var1);
        var1.setColor(DARK_COLOR);
        if (this.firstPaint) {
            FontMetrics var2 = var1.getFontMetrics(TABLE_FONT);
            FontMetrics var3 = var1.getFontMetrics(TABLE_FONT_BOLD);
            this.LINE_HEIGHT = var2.getHeight();
            this.LINE_ASCENT = var2.getAscent();
            this.CHAR_WIDTH = var2.charWidth(' ');
            this.simulation.updateTableColumnWidth(var2, var3);
            this.textPane.updateTabSet();
            this.textPane.repaint();
            this.firstPaint = false;
            this.setBackground(DARK_COLOR);
            this.setForeground(DARK_MODE_FONT_COLOR);
        }

    }

    public boolean isErrorLine(int var1) {
        return this.errorLines.contains(new Integer(var1));
    }

    public boolean isWarningLine(int var1) {
        return this.warningLines.contains(new Integer(var1));
    }

    public boolean isFlashLine(int var1) {
        return var1 == this.flashLine;
    }

    public Compiler getCompiler() {
        return this.compiler;
    }

    public Module[] getModules() {
        return this.modules;
    }

    public Simulator getSimulator() {
        return this.simulator;
    }

    public JTextPane getTextPane() {
        return this.textPane;
    }

    public void showLine(int var1) {
        if (!this.isSimulating()) {
            this.textPane.showLine(var1);
            this.flashLine = var1;
        }
    }

    public DialogFindReplace getDialogfindReplace() {
        if (this.dialogFindReplace == null) {
            this.dialogFindReplace = new DialogFindReplace(JOptionPane.getFrameForComponent(this), this.textPane);
        }

        return this.dialogFindReplace;
    }

    public UndoManager getUndoManager() {
        return this.textPane.getUndoManager();
    }

    public void open(File var1) throws Exception {
        this.textPane.open(var1);
    }

    public void clearDocument() {
        this.textPane.clearDocument();
    }

    public void save(File var1) throws Exception {
        this.textPane.save(var1);
        this.info("Saved", -1);
    }

    public void saveHex(File var1, Module var2) throws Exception {
        if (this.compile()) {
            this.dataBaseAddress = this.compiler.getDataBaseAddress();
            this.handleSystem();
            this.compiler.writeHexFile(var1, var2);
            this.info("Hex file saved.", -1);
        } else {
            this.error("Cannot save hex file: error while compiling.", -1);
        }

    }

    public void undo() {
        if (this.getUndoManager().canUndo()) {
            this.getUndoManager().undo();
        }

    }

    public void redo() {
        if (this.getUndoManager().canRedo()) {
            this.getUndoManager().redo();
        }

    }

    public void find() {
        this.getDialogfindReplace().showDialog();
    }

    private void setTopView(String var1) {
        CardLayout var2 = (CardLayout)((CardLayout)this.topView.getLayout());
        var2.show(this.topView, var1);
        if (var1 == "Sim") {
            this.simulation.requestFocus();
        }

    }

    private boolean defineSystem(Module[] var1, int var2, Module var3, Module var4, int var5, int var6) {
        if (var3 != null) {
            this.interruptAddress = var3.getStartAddress() + var5 + var2;
            this.modules = var1;
            this.textModule = var3;
            this.dataModule = var4;
            this.textBaseAddress = var3.getStartAddress() + var5;
            this.dataBaseAddress = var4.getStartAddress() + var6;
            return true;
        } else {
            return false;
        }
    }

    public void handleSystem() throws CompilerException {
        this.dataBaseAddress = this.compiler.getDataBaseAddress();
        if (this.textModule.getSize() < this.compiler.getTextAddress()) {
            throw new CompilerException(String.format("Memory module %s is too small to contain the .text section (0x%04X Bytes)", this.textModule.getName(), this.compiler.getTextAddress()));
        } else if (this.dataModule.getSize() < this.compiler.getDataAddress()) {
            throw new CompilerException(String.format("Memory module %s is too small to contain the .data section (0x%04X Bytes)", this.dataModule.getName(), this.compiler.getDataAddress()));
        } else {
            Iterator var1 = this.compiler.getLines().iterator();
            int var2 = (this.textBaseAddress - this.textModule.getStartAddress()) / 4;
            int var3 = (this.dataBaseAddress - this.dataModule.getStartAddress()) / 4;

            while(var1.hasNext()) {
                Line var4 = (Line)var1.next();
                if (var4.contextType == state_type.TEXT) {
                    this.textModule.getData()[var2] = var4.data;
                    ++var2;
                } else if (var4.contextType == state_type.DATA) {
                    this.dataModule.getData()[var3] = var4.data;
                    ++var3;
                }
            }

        }
    }

    public boolean compile() {
        boolean var1 = true;
        this.errorLines.clear();
        this.warningLines.clear();
        this.messages.clear();
        this.compiler = new Compiler(this, this.textBaseAddress, this.dataBaseAddress);
        if (!this.compiler.addText(this.textPane.getText())) {
            var1 = false;
        } else if (!this.compiler.terminate()) {
            var1 = false;
        }

        this.textPane.refreshHighlight();
        return var1;
    }

    public void simulate() {
        if (this.compile()) {
            if (this.compiler.getLines().size() != 0) {
                this.simulator = new Simulator(this.compiler.getLines(), this, this.modules, this, this.interruptAddress, this.textBaseAddress, this.nios2sim.getMenuBar().isMulticycleEnabled(), this.nios2sim.getMenuBar().isPipelineEnabled(), this.nios2sim.getMenuBar().isStallsEnabled(), this.nios2sim.getMenuBar().isFlushesEnabled(), this.nios2sim.getMenuBar().isForwardEEEnabled(), this.nios2sim.getMenuBar().isForwardMEEnabled(), this.nios2sim.getMenuBar().isForwardWDEnabled());

                try {
                    this.simulator.start();
                    this.simulating = true;
                    this.setTopView("Sim");
                    this.nios2sim.getMenuBar().updateMenuState();
                    this.simulation.refreshTable();
                } catch (CompilerException var2) {
                    this.error(var2.getMessage(), -1);
                }

            }
        }
    }

    public void restartSimulation() {
        if (this.isSimulating()) {
            try {
                this.simulator.start();
            } catch (CompilerException var2) {
                this.error(var2.getMessage(), -1);
            }

        }
    }

    public void endSimulation() {
        this.simulating = false;
        this.setTopView("Editor");
        this.nios2sim.getMenuBar().updateMenuState();
        this.simulator = null;
    }

    public void setPipelineEnabled(boolean var1) {
        this.simulator.setPipelineEnabled(var1);
        this.simulation.setPipelineEnabled(var1);
    }

    public void setProcessorEnabled(boolean var1) {
        this.simulator.setMulticycleEnabled(var1);
        this.simulation.setMulticycleEnabled(var1);
    }

    public boolean isSimulating() {
        return this.simulating;
    }

    public void step() {
        if (this.isSimulating()) {
            this.simulation.commitEdits();
            this.simulator.step();
            this.simulation.refreshTable();
        }
    }

    public void run() {
        if (this.isSimulating()) {
            this.simulation.commitEdits();
            this.simulator.run();
            this.simulation.refreshTable();
        }
    }

    public void error(String var1, int var2) {
        if (var2 > 0) {
            this.messages.addMessage(var2, message_type.ERROR, sdf.format(new Date()) + "Error at line " + var2 + ": " + var1);
            if (!this.isSimulating()) {
                this.errorLines.add(new Integer(var2));
            }
        } else {
            this.messages.addMessage(-1, message_type.ERROR, sdf.format(new Date()) + "Error : " + var1);
        }

    }

    public void info(String var1, int var2) {
        if (var2 > 0) {
            this.messages.addMessage(var2, message_type.INFO, sdf.format(new Date()) + "Info at line " + var2 + ": " + var1);
        } else {
            this.messages.addMessage(-1, message_type.INFO, sdf.format(new Date()) + "Info : " + var1);
        }

    }

    public void warning(String var1, int var2) {
        if (var2 > 0) {
            this.messages.addMessage(var2, message_type.WARNING, sdf.format(new Date()) + "Warning at line " + var2 + ": " + var1);
            if (!this.isSimulating()) {
                this.warningLines.add(new Integer(var2));
            }
        } else {
            this.messages.addMessage(-1, message_type.WARNING, sdf.format(new Date()) + "Warning : " + var1);
        }

    }

    public void dragEnter(DropTargetDragEvent var1) {
    }

    public void dragExit(DropTargetEvent var1) {
    }

    public void dragOver(DropTargetDragEvent var1) {
    }

    public void drop(DropTargetDropEvent var1) {
        try {
            Transferable var2 = var1.getTransferable();
            DataFlavor[] var3 = var2.getTransferDataFlavors();
            if (var3.length > 0 && var3[0].isFlavorJavaFileListType()) {
                var1.acceptDrop(1);
                Object var4 = var2.getTransferData(var3[0]);
                if (var4 instanceof List) {
                    List var5 = (List)var4;
                    if (var5.size() > 0 && var5.get(0) instanceof File) {
                        this.nios2sim.open((File)var5.get(0));
                        var1.dropComplete(true);
                        return;
                    }
                }
            }

            var1.rejectDrop();
        } catch (Exception var6) {
            this.nios2sim.error(var6.getMessage());
            var1.rejectDrop();
        }

    }

    public void dropActionChanged(DropTargetDragEvent var1) {
    }
}
