//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package compiler;

import compiler.Compiler.state_type;

public class Line {
    public String text;
    public int lineNumber;
    public int data = 0;
    public int address = 0;
    public int length = 0;
    public state_type contextType;
    public boolean breakpoint;
    public String expression;
    public ref_type rtype;

    public Line(int var1) {
        this.contextType = state_type.INVALID;
        this.breakpoint = false;
        this.expression = null;
        this.lineNumber = var1;
    }

    public void setData(int var1, int var2, state_type var3) {
        this.setData(var1, var2, (String)null, ref_type.REF_RELATIVE_ADDR, var3);
    }

    public void setData(int var1, int var2, int var3, state_type var4) {
        this.setData(var1, var2, (String)null, ref_type.REF_RELATIVE_ADDR, var3, var4);
    }

    public void setData(int var1, int var2, String var3, ref_type var4, state_type var5) {
        this.setData(var1, var2, var3, var4, 4, var5);
    }

    public void setData(int var1, int var2, String var3, ref_type var4, int var5, state_type var6) {
        this.data = var1;
        this.address = var2;
        this.expression = var3;
        this.rtype = var4;
        this.length = var5;
        this.contextType = var6;
    }

    public boolean isValid() {
        return this.contextType != state_type.INVALID;
    }

    public static enum ref_type {
        REF_RELATIVE_ADDR,
        REF_IMM26,
        REF_IMM16,
        REF_IMM16U,
        REF_IMM5,
        DATA32,
        DATA16,
        DATA8;

        private ref_type() {
        }
    }
}
