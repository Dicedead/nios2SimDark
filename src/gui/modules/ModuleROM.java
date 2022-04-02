//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package gui.modules;

import compiler.CompilerException;
import compiler.Interpreter;
import gui.Nios2SimPanel;

import javax.swing.*;
import java.util.HashMap;

import static gui.Nios2SimFrame.DARK_MODE_FONT_COLOR;

public class ModuleROM extends Module {
    private JTable table;

    public ModuleROM(Nios2SimPanel var1, int var2, int var3, String var4) {
        super(var1, var2, var3, var4);
    }

    public ModuleROM(Nios2SimPanel var1, int var2, String var3, int var4, HashMap<String, String> var5) throws CompilerException {
        super(var1, var2, var3);
        int var6 = 0;

        try {
            var6 = Interpreter.resolveExpression((String)var5.get("size"));
        } catch (CompilerException var8) {
        }

        this.setSize(var6);
        this.resizeData(var6);
    }

    public JComponent getPane() {
        this.table = this.getDefaultJTable(new String[]{"Address", "+0", "+4", "+8", "+C"}, (String[])null, new boolean[]{false, false, false, false, false});
        JScrollPane var1 = new JScrollPane(this.table);
        table.setForeground(DARK_MODE_FONT_COLOR);
        return var1;
    }

    public boolean write(int var1, int var2) {
        return false;
    }
}
