//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DialogFindReplace extends JDialog {
    private static final long serialVersionUID = 1L;
    JTextPane jTextPane;
    JLabel jLabel;
    JTextField jTextFieldFind;
    JTextField jTextFieldReplace;
    JButton jButtonFind;
    JButton jButtonReplaceFind;
    JButton jButtonReplace;
    JButton jButtonReplaceAll;

    public DialogFindReplace(Frame var1, JTextPane var2) {
        super(var1, "Find/Replace", false);
        this.jTextPane = var2;
        this.init();
    }

    private void init() {
        JPanel var1 = new JPanel();
        var1.setLayout(new BoxLayout(var1, 1));
        this.setContentPane(var1);
        Box var2 = Box.createHorizontalBox();
        Box var3 = Box.createVerticalBox();
        Box var4 = Box.createVerticalBox();
        var2.add(Box.createHorizontalStrut(10));
        var2.add(var3);
        var2.add(Box.createHorizontalStrut(10));
        var2.add(var4);
        var2.add(Box.createHorizontalStrut(10));
        JLabel var5 = new JLabel("Find:");
        var3.add(var5);
        var3.add(Box.createVerticalStrut(5));
        var5 = new JLabel("Replace With:");
        var3.add(var5);
        this.jTextFieldFind = new JTextField();
        this.jTextFieldFind.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent var1) {
                super.keyPressed(var1);
                DialogFindReplace.this.updateButtons();
            }
        });
        this.jTextFieldReplace = new JTextField();
        var4.add(this.jTextFieldFind);
        var4.add(Box.createVerticalStrut(5));
        var4.add(this.jTextFieldReplace);
        Box var6 = Box.createHorizontalBox();
        Box var7 = Box.createVerticalBox();
        Box var8 = Box.createVerticalBox();
        var6.add(Box.createHorizontalStrut(10));
        var6.add(var7);
        var6.add(Box.createHorizontalStrut(10));
        var6.add(var8);
        var6.add(Box.createHorizontalStrut(10));
        this.jButtonFind = new JButton("Find");
        this.jButtonFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                DialogFindReplace.this.find(true);
            }
        });
        this.jButtonReplaceFind = new JButton("Replace/Find");
        this.jButtonReplaceFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                DialogFindReplace.this.replaceFind();
            }
        });
        var7.add(this.jButtonFind);
        var7.add(Box.createVerticalStrut(10));
        var7.add(this.jButtonReplaceFind);
        this.jButtonReplace = new JButton("Replace");
        this.jButtonReplaceFind.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                DialogFindReplace.this.replace();
            }
        });
        this.jButtonReplaceAll = new JButton("Replace All");
        this.jButtonReplaceAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                DialogFindReplace.this.replaceAll();
            }
        });
        var8.add(this.jButtonReplace);
        var8.add(Box.createVerticalStrut(10));
        var8.add(this.jButtonReplaceAll);
        Dimension var9 = this.jButtonReplaceFind.getPreferredSize();
        this.jButtonFind.setPreferredSize(var9);
        this.jButtonFind.setMaximumSize(var9);
        this.jButtonReplaceFind.setPreferredSize(var9);
        this.jButtonReplaceFind.setMaximumSize(var9);
        this.jButtonReplace.setPreferredSize(var9);
        this.jButtonReplace.setMaximumSize(var9);
        this.jButtonReplaceAll.setPreferredSize(var9);
        this.jButtonReplaceAll.setMaximumSize(var9);
        Box var10 = Box.createHorizontalBox();
        this.jLabel = new JLabel();
        JButton var11 = new JButton("Close");
        var11.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent var1) {
                DialogFindReplace.this.close();
            }
        });
        var11.setAlignmentX(0.5F);
        var10.add(Box.createHorizontalStrut(10));
        var10.add(this.jLabel);
        var10.add(Box.createHorizontalGlue());
        var10.add(Box.createHorizontalStrut(10));
        var10.add(var11);
        var10.add(Box.createHorizontalStrut(10));
        var1.add(Box.createVerticalStrut(10));
        var1.add(var2);
        var1.add(Box.createVerticalStrut(20));
        var1.add(var6);
        var1.add(Box.createVerticalStrut(20));
        var1.add(var10);
        var1.add(Box.createVerticalStrut(10));
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(this.getParent());
    }

    public void showDialog() {
        String var1 = this.jTextPane.getSelectedText();
        if (var1 != null && var1.length() > 0) {
            int var2 = var1.indexOf(10);
            if (var2 > 0) {
                var1 = var1.substring(0, var2);
            }

            this.jTextFieldFind.setText(var1);
        }

        this.jTextFieldFind.setSelectionStart(0);
        this.jTextFieldFind.setSelectionEnd(this.jTextFieldFind.getText().length());
        this.updateButtons();
        this.setVisible(true);
    }

    private void close() {
        this.dispose();
    }

    private void find(boolean var1) {
        String var2 = this.jTextFieldFind.getText();
        String var3 = "";

        try {
            var3 = this.jTextPane.getDocument().getText(0, this.jTextPane.getDocument().getLength());
        } catch (BadLocationException var5) {
            var5.printStackTrace();
        }

        int var4 = var3.indexOf(var2, this.jTextPane.getSelectionEnd());
        if (var4 < 0 && var1) {
            var4 = var3.indexOf(var2);
        }

        if (var4 > -1) {
            this.jTextPane.setSelectionStart(var4);
            this.jTextPane.setSelectionEnd(var4 + var2.length());
            this.updateButtons();
        } else {
            this.jLabel.setText("String not found.");
        }

    }

    private void replaceFind() {
        this.replace();
        this.find(true);
    }

    private void replace() {
        if (this.jTextPane.getSelectedText() != null && this.jTextPane.getSelectedText().equals(this.jTextFieldFind.getText())) {
            try {
                int var1 = this.jTextPane.getSelectionStart();
                this.jTextPane.getDocument().remove(var1, this.jTextPane.getSelectionEnd() - var1);
                this.jTextPane.getDocument().insertString(this.jTextPane.getSelectionStart(), this.jTextFieldReplace.getText(), this.jTextPane.getParagraphAttributes());
                this.jTextPane.setSelectionStart(var1);
                int var2 = var1 + this.jTextFieldReplace.getText().length();
                this.jTextPane.setCaretPosition(var2);
                this.jTextPane.setSelectionEnd(var2);
            } catch (BadLocationException var3) {
                var3.printStackTrace();
            }
        }

    }

    private void replaceAll() {
        int var1 = 0;
        String var2 = this.jTextFieldFind.getText();
        String var3 = "";

        try {
            var3 = this.jTextPane.getDocument().getText(0, this.jTextPane.getDocument().getLength());

            for(int var4 = var3.indexOf(var2); var4 > -1; var4 = var3.indexOf(var2, this.jTextPane.getSelectionEnd())) {
                this.jTextPane.setSelectionStart(var4);
                this.jTextPane.setSelectionEnd(var4 + var2.length());
                this.replace();
                ++var1;
                var3 = this.jTextPane.getDocument().getText(0, this.jTextPane.getDocument().getLength());
            }
        } catch (BadLocationException var5) {
            var5.printStackTrace();
        }

        this.jLabel.setText(var1 + " matches replaced.");
    }

    private void updateButtons() {
        this.jLabel.setText("");
        boolean var1 = this.jTextFieldFind.getText().length() > 0;
        boolean var2 = var1 && this.jTextPane.getSelectedText() != null && this.jTextPane.getSelectedText().equals(this.jTextFieldFind.getText());
        this.jButtonFind.setEnabled(var1);
        this.jButtonReplaceAll.setEnabled(var1);
        this.jButtonReplace.setEnabled(var2);
        this.jButtonReplaceFind.setEnabled(var2);
    }
}
