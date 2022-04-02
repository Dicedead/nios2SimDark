//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import compiler.Compiler;
import compiler.Interpreter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;
import javax.swing.text.TextAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static gui.Nios2SimFrame.DARK_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_INSTRUCTIONS_COLOR;
import static gui.Nios2SimFrame.DARK_MODE_REGISTERS_COLOR;

public class Nios2SimTextPane extends JTextPane {
    private static final long serialVersionUID = 1L;
    private SimpleAttributeSet keywordStyle;
    private SimpleAttributeSet labelStyle;
    private SimpleAttributeSet regStyle;
    private SimpleAttributeSet immStyle;
    private SimpleAttributeSet commentStyle;
    private Style style;
    private TextPaneDocumentListener docListener;
    private Nios2SimPanel panel;
    private UndoManager undo;
    private boolean initUndoAtNextRefresh = true;
    private boolean locked = false;

    public Nios2SimTextPane(Nios2SimPanel var1) {
        this.panel = var1;
        this.initUndoAtNextRefresh = true;
        Border var2 = this.getBorder();
        TextPaneBorder var3 = new TextPaneBorder();
        this.setBorder(new CompoundBorder(var2, var3));
        this.refreshStyle();
        this.docListener = new TextPaneDocumentListener();
        this.getDocument().addDocumentListener(this.docListener);
        this.refreshHighlight();
        Keymap var4 = this.getKeymap();
        KeyStroke var5 = KeyStroke.getKeyStroke(90, 2);
        TextAction var6 = new TextAction("Ctrl-Z") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent var1) {
                Nios2SimTextPane.this.panel.undo();
            }
        };
        var4.addActionForKeyStroke(var5, var6);
        var5 = KeyStroke.getKeyStroke(89, 2);
        var6 = new TextAction("Ctrl-Y") {
            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent var1) {
                Nios2SimTextPane.this.panel.redo();
            }
        };
        var4.addActionForKeyStroke(var5, var6);
    }

    public void open(File var1) throws IOException {
        this.getDocument().removeDocumentListener(this.docListener);
        this.getDocument().removeUndoableEditListener(this.undo);
        this.undo.die();
        this.getDocument().putProperty("stream", (Object)null);
        this.setPage(var1.toURI().toURL());
        this.getDocument().addDocumentListener(this.docListener);
        this.refreshStyle();
        this.refreshHighlight();
        this.initUndoAtNextRefresh = true;
    }

    public void clearDocument() {
        this.getDocument().removeUndoableEditListener(this.undo);
        this.undo.die();
        this.getDocument().putProperty("stream", (Object)null);
        this.setText("");
        this.refreshStyle();
        this.getDocument().addDocumentListener(this.docListener);
        this.undo = new StyledUndoManager(this);
    }

    public void save(File var1) throws Exception {
        File var2 = File.createTempFile(var1.getName(), ".tmp", var1.getParentFile());
        FileWriter var3 = new FileWriter(var2);
        var3.write(this.getText());
        var3.close();
        if (var1.exists()) {
            File var4 = new File(var1.getAbsolutePath() + ".bak");
            if (var4.exists()) {
                var4.delete();
            }

            var1.renameTo(var4);
        }

        var2.renameTo(var1);
        this.undo.die();
        this.getDocument().removeUndoableEditListener(this.undo);
        this.undo = new StyledUndoManager(this);
    }

    public void refreshHighlight() {
        this.highlight(0, this.getText().length() - 1);
    }

    public UndoManager getUndoManager() {
        return this.undo;
    }

    private void lock() {
        this.locked = true;
    }

    private void unlock() {
        this.locked = false;
    }

    private int nextDelim(String var1, int var2) {
        for(int var3 = var2; var3 < var1.length(); ++var3) {
            char var4 = var1.charAt(var3);
            if (!Interpreter.isAlphaNumeric(var4) && var4 != '.') {
                return var3;
            }
        }

        return var1.length();
    }

    private int startOfLine(String var1, int var2) {
        int var3 = var1.lastIndexOf("\n", var2);
        return var3 > -1 ? var3 : 0;
    }

    private int endOfLine(String var1, int var2) {
        int var3 = var1.indexOf("\n", var2);
        return var3 > -1 ? var3 : var1.length();
    }

    private void refreshStyle() {
        this.style = this.getLogicalStyle();
        this.setBackground(DARK_COLOR);
        StyleConstants.setFontFamily(this.style, Nios2SimPanel.TABLE_FONT.getFamily());
        StyleConstants.setFontSize(this.style, Nios2SimPanel.TABLE_FONT.getSize());
        StyleConstants.setBackground(this.style, DARK_COLOR);
        StyleConstants.setForeground(this.style, DARK_MODE_FONT_COLOR);
        StyleConstants.setBold(this.style, Nios2SimPanel.TABLE_FONT.isBold());
        StyleConstants.setItalic(this.style, Nios2SimPanel.TABLE_FONT.isItalic());
        StyleConstants.setTabSet(this.style, this.getTabSet());
        this.keywordStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(this.keywordStyle, Nios2SimPanel.TABLE_FONT_BOLD.getFamily());
        StyleConstants.setFontSize(this.keywordStyle, Nios2SimPanel.TABLE_FONT_BOLD.getSize());
        StyleConstants.setBackground(this.keywordStyle, DARK_COLOR);
        StyleConstants.setForeground(this.keywordStyle, DARK_MODE_INSTRUCTIONS_COLOR); //COULEUR INSTRUCTIONS
        StyleConstants.setBold(this.keywordStyle, Nios2SimPanel.TABLE_FONT_BOLD.isBold());
        StyleConstants.setItalic(this.keywordStyle, Nios2SimPanel.TABLE_FONT_BOLD.isItalic());
        StyleConstants.setTabSet(this.keywordStyle, this.getTabSet());
        this.labelStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(this.labelStyle, Nios2SimPanel.TABLE_FONT.getFamily());
        StyleConstants.setFontSize(this.labelStyle, Nios2SimPanel.TABLE_FONT.getSize());
        StyleConstants.setBackground(this.labelStyle, DARK_COLOR);
        StyleConstants.setForeground(this.labelStyle, new Color(128, 128, 255));
        StyleConstants.setBold(this.labelStyle, Nios2SimPanel.TABLE_FONT.isBold());
        StyleConstants.setItalic(this.labelStyle, true);
        StyleConstants.setTabSet(this.labelStyle, this.getTabSet());
        this.regStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(this.regStyle, Nios2SimPanel.TABLE_FONT.getFamily());
        StyleConstants.setFontSize(this.regStyle, Nios2SimPanel.TABLE_FONT.getSize());
        StyleConstants.setBackground(this.regStyle, DARK_COLOR);
        StyleConstants.setForeground(this.regStyle, DARK_MODE_REGISTERS_COLOR);
        StyleConstants.setBold(this.regStyle, false);
        StyleConstants.setItalic(this.regStyle, false);
        StyleConstants.setTabSet(this.regStyle, this.getTabSet());
        this.immStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(this.immStyle, Nios2SimPanel.TABLE_FONT.getFamily());
        StyleConstants.setFontSize(this.immStyle, Nios2SimPanel.TABLE_FONT.getSize());
        StyleConstants.setBackground(this.immStyle, DARK_COLOR);
        StyleConstants.setForeground(this.immStyle, DARK_MODE_FONT_COLOR);
        StyleConstants.setBold(this.immStyle, false);
        StyleConstants.setItalic(this.immStyle, false);
        StyleConstants.setTabSet(this.immStyle, this.getTabSet());
        this.commentStyle = new SimpleAttributeSet();
        StyleConstants.setFontFamily(this.commentStyle, Nios2SimPanel.TABLE_FONT.getFamily());
        StyleConstants.setFontSize(this.commentStyle, Nios2SimPanel.TABLE_FONT.getSize());
        StyleConstants.setBackground(this.commentStyle, DARK_COLOR);
        StyleConstants.setForeground(this.commentStyle, new Color(0, 128, 0));
        StyleConstants.setBold(this.commentStyle, Nios2SimPanel.TABLE_FONT.isBold());
        StyleConstants.setItalic(this.commentStyle, false);
        StyleConstants.setTabSet(this.commentStyle, this.getTabSet());
    }

    public void updateTabSet() {
        StyleConstants.setTabSet(this.getLogicalStyle(), this.getTabSet());
    }

    private TabSet getTabSet() {
        TabStop[] var2 = new TabStop[50];

        for(int var3 = 0; var3 < 50; ++var3) {
            var2[var3] = new TabStop((float)(this.panel.CHAR_WIDTH * 4 * (var3 + 1)));
        }

        return new TabSet(var2);
    }

    private void highlight(int var1, int var2) {
        this.lock();
        String var3 = null;

        try {
            var3 = this.getDocument().getText(0, this.getDocument().getLength());
        } catch (BadLocationException var10) {
            var10.printStackTrace();
            this.unlock();
        }

        int var4 = var3.length();
        int var5 = var1;
        boolean var6 = false;

        while(var5 < var2) {
            String var7 = null;
            int var11 = this.nextDelim(var3, var5);
            if (var11 < var4) {
                char var8 = var3.charAt(var11);
                if (var8 == ';' || var8 == '#') {
                    var5 = var11;
                    var11 = this.endOfLine(var3, var11);
                    ((StyledDocument)this.getDocument()).setCharacterAttributes(var5, var11 - var5, this.commentStyle, false);
                    var5 = var11;
                    continue;
                }

                if (var11 == var5) {
                    ((StyledDocument)this.getDocument()).setCharacterAttributes(var11, 1, this.style, false);
                    ++var5;
                    continue;
                }

                if (var8 == ':') {
                    var5 = this.startOfLine(var3, var11);
                    if (var5 != var11) {
                        var7 = var3.substring(var5, var11 + 1);
                        String var9 = var3.substring(var5, var11).trim();
                        if (!var9.contains(" ") && !var9.contains("\t") && !Character.isDigit(var9.charAt(0))) {
                            ((StyledDocument)this.getDocument()).setCharacterAttributes(var5, var7.length(), this.labelStyle, false);
                        } else {
                            ((StyledDocument)this.getDocument()).setCharacterAttributes(var5, var7.length(), this.style, false);
                        }

                        var5 = var11 + 1;
                        continue;
                    }
                }
            } else if (var11 == var5) {
                break;
            }

            var7 = var3.substring(var5, var11);
            SimpleAttributeSet var12 = null;
            if (Compiler.isKeyword(var7)) {
                var12 = this.keywordStyle;
            } else if (Compiler.isRegister(var7)) {
                var12 = this.regStyle;
            } else if (this.panel.getCompiler() != null && this.panel.getCompiler().isSymbol(var7)) {
                var12 = this.labelStyle;
            }

            if (var12 == null) {
                ((StyledDocument)this.getDocument()).setCharacterAttributes(var5, var7.length(), this.style, false);
            } else {
                ((StyledDocument)this.getDocument()).setCharacterAttributes(var5, var7.length(), var12, false);
            }

            var5 = var11;
        }

        this.unlock();
        if (this.initUndoAtNextRefresh) {
            if (this.undo != null) {
                this.getDocument().removeUndoableEditListener(this.undo);
                this.undo.die();
            }

            this.undo = new StyledUndoManager(this);
            this.initUndoAtNextRefresh = false;
        }

    }

    public void showLine(int var1) {
        String var2 = null;

        try {
            var2 = this.getDocument().getText(0, this.getDocument().getLength());
        } catch (BadLocationException var5) {
            var5.printStackTrace();
            return;
        }

        int var3 = -1;

        for(int var4 = 0; var4 < var1 - 1; ++var4) {
            var3 = var2.indexOf("\n", var3 + 1);
        }

        this.select(var3 + 1, var2.indexOf("\n", var3 + 1));
        this.requestFocus();
        this.repaint();
    }

    private class TextPaneBorder implements Border {
        int borderWidth;

        TextPaneBorder() {
            this.borderWidth = Nios2SimTextPane.this.panel.CHAR_WIDTH * 5 + 20;
        }

        public void paintBorder(Component var1, Graphics var2, int var3, int var4, int var5, int var6) {
            if (Nios2SimTextPane.this.panel.LINE_HEIGHT != 0) {
                var2.setFont(Nios2SimPanel.TABLE_FONT_BOLD);
                int var7 = var4 + Nios2SimTextPane.this.panel.LINE_ASCENT;
                int var8 = 2 * Nios2SimTextPane.this.panel.CHAR_WIDTH;
                Rectangle var9 = new Rectangle(this.borderWidth - var8 + 4, -Nios2SimTextPane.this.panel.LINE_ASCENT + 4, var8 - 8, Nios2SimTextPane.this.panel.LINE_HEIGHT - 8);
                var2.setColor(DARK_COLOR);
                var2.fillRect(0, 0, this.borderWidth, var6 + 10);

                for(int var10 = 1; (double)var7 < Nios2SimTextPane.this.getPreferredSize().getHeight(); ++var10) {
                    if (Nios2SimTextPane.this.panel.isErrorLine(var10)) {
                        var2.setColor(new Color(255, 0, 0, 128));
                        var2.fillRect(var9.x, var9.y + var7, var9.width, var9.height);
                        var2.setColor(new Color(255, 0, 0));
                        var2.drawRect(var9.x, var9.y + var7, var9.width, var9.height);
                        if (Nios2SimTextPane.this.panel.isFlashLine(var10)) {
                            var2.setColor(new Color(255, 0, 0, 64));
                            var2.fillRect(this.borderWidth, var7 - Nios2SimTextPane.this.panel.LINE_ASCENT, var5, Nios2SimTextPane.this.panel.LINE_HEIGHT);
                        }
                    } else if (Nios2SimTextPane.this.panel.isWarningLine(var10)) {
                        var2.setColor(new Color(255, 255, 0, 128));
                        var2.fillRect(var9.x, var9.y + var7, var9.width, var9.height);
                        var2.setColor(new Color(255, 255, 0));
                        var2.drawRect(var9.x, var9.y + var7, var9.width, var9.height);
                        if (Nios2SimTextPane.this.panel.isFlashLine(var10)) {
                            var2.setColor(new Color(255, 255, 0, 64));
                            var2.fillRect(this.borderWidth, var7 - Nios2SimTextPane.this.panel.LINE_ASCENT, var5, Nios2SimTextPane.this.panel.LINE_HEIGHT);
                        }
                    }

                    var2.setColor(new Color(100, 100, 100));
                    String var11 = String.valueOf(var10);
                    var2.drawString(var11, this.borderWidth - (var11.length() + 2) * Nios2SimTextPane.this.panel.CHAR_WIDTH, var7);
                    var7 += Nios2SimTextPane.this.panel.LINE_HEIGHT;
                }

            }
        }

        public Insets getBorderInsets(Component var1) {
            return new Insets(0, this.borderWidth, 0, 0);
        }

        public boolean isBorderOpaque() {
            return true;
        }
    }

    private class TextPaneDocumentListener implements DocumentListener {
        private TextPaneDocumentListener() {
        }

        public void insertUpdate(DocumentEvent var1) {
            this.updateHighlight(var1);
        }

        public void removeUpdate(DocumentEvent var1) {
            this.updateHighlight(var1);
        }

        public void changedUpdate(DocumentEvent var1) {
            this.updateHighlight(var1);
        }

        private void updateHighlight(DocumentEvent var1) {
            if (!Nios2SimTextPane.this.locked) {
                int var2 = var1.getOffset();
                int var3 = var1.getLength();
                String var4 = null;

                try {
                    var4 = Nios2SimTextPane.this.getDocument().getText(0, Nios2SimTextPane.this.getDocument().getLength());
                } catch (BadLocationException var7) {
                    var7.printStackTrace();
                }

                final int var5 = Nios2SimTextPane.this.startOfLine(var4, var2 - 2);
                final int var6 = Nios2SimTextPane.this.endOfLine(var4, var2 + var3);
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        Nios2SimTextPane.this.highlight(var5, var6);
                    }
                });
            }

        }
    }

    private class StyledUndoManager extends UndoManager implements UndoableEditListener {
        private static final long serialVersionUID = 1L;
        public MyCompoundEdit compoundEdit = null;
        private JTextComponent editor;

        public StyledUndoManager(JTextComponent var2) {
            this.editor = var2;
            var2.getDocument().addUndoableEditListener(this);
        }

        public void undo() {
            Nios2SimTextPane.this.lock();
            super.undo();
            Nios2SimTextPane.this.unlock();
        }

        public void redo() {
            Nios2SimTextPane.this.lock();
            super.redo();
            Nios2SimTextPane.this.unlock();
        }

        public void undoableEditHappened(UndoableEditEvent var1) {
            if (this.compoundEdit == null) {
                this.compoundEdit = this.startCompoundEdit(var1.getEdit());
            } else {
                UndoableEdit var2 = var1.getEdit();
                int var3 = this.editor.getCaretPosition();
                int var4 = var3 - this.compoundEdit.lastOffset;
                if (Math.abs(var4) <= 1) {
                    this.compoundEdit.addEdit(var1.getEdit());
                    this.compoundEdit.lastOffset = var3;
                } else if (Nios2SimTextPane.this.locked) {
                    this.compoundEdit.addEdit(var1.getEdit());
                } else {
                    this.compoundEdit.end();
                    this.compoundEdit = this.startCompoundEdit(var1.getEdit());
                }
            }
        }

        private MyCompoundEdit startCompoundEdit(UndoableEdit var1) {
            this.compoundEdit = new MyCompoundEdit();
            this.compoundEdit.lastOffset = this.editor.getCaretPosition();
            this.compoundEdit.addEdit(var1);
            this.addEdit(this.compoundEdit);
            return this.compoundEdit;
        }

        class MyCompoundEdit extends CompoundEdit {
            private static final long serialVersionUID = 1L;
            public int lastOffset;

            MyCompoundEdit() {
            }

            public boolean isInProgress() {
                return false;
            }

            public void undo() throws CannotUndoException {
                if (StyledUndoManager.this.compoundEdit != null) {
                    StyledUndoManager.this.compoundEdit.end();
                }

                super.undo();
                StyledUndoManager.this.compoundEdit = null;
            }

            public void redo() throws CannotRedoException {
                super.redo();
                StyledUndoManager.this.editor.setCaretPosition(this.lastOffset);
            }
        }
    }
}
