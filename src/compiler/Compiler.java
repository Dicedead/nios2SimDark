//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package compiler;

import compiler.Line.ref_type;
import gui.modules.Module;
import nios2sim.MessageListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler {
    private static final Pattern labelPattern = Pattern.compile("^[ \t]*([a-zA-Z_]\\w*)[ \t]*:(.*)$");
    private static final Pattern intPattern = Pattern.compile("^([^ \t]*)[ \t]*(.*)$");
    private static final Pattern dotPattern = Pattern.compile("^([^ \t]*)[ \t]*(.*)$");
    private static final Pattern splitPattern = Pattern.compile("[ \t]*,[ \t]*");
    private static final Pattern immRegPattern = Pattern.compile("^(.*)\\((.*)\\)[ \t]*$");
    private static final Pattern linePattern = Pattern.compile("\r{0,1}\n");
    private static final Pattern commentPattern = Pattern.compile("^([^;#]*)[;#].*$");
    public static HashMap<String, Integer> itype_ra_rb_imm = null;
    public static HashMap<String, Integer> itype_ra_rb_immu = null;
    public static HashMap<String, Integer> itype_ra_rb_relative = null;
    public static HashMap<String, Integer> rtype_rc_ra_rb = null;
    public static HashMap<String, Integer> rtype_rc_ra_imm = null;
    public static final String[] reg_aliases = new String[]{"zero", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", "t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "et", "bt", "gp", "sp", "fp", "ea", "ba", "ra"};
    public static final String[] ctl_reg_aliases = new String[]{"status", "estatus", "bstatus", "ienable", "ipending", "cpuid"};
    public static final String[] OP_STRINGS = new String[]{"call", "jmpi", "?", "ldbu", "addi", "stb", "br", "ldb", "cmpgei", "?", "?", "ldhu", "andi", "sth", "bge", "ldh", "cmplti", "?", "?", "?", "ori", "stw", "blt", "ldw", "cmpnei", "?", "?", "flushda", "xori", "?", "bne", "?", "cmpeqi", "?", "?", "ldbuio", "muli", "stbio", "beq", "ldbio", "cmpgeui", "?", "?", "ldhuio", "andhi", "sthio", "bgeu", "ldhio", "cmpltui", "?", "custom", "initd", "orhi", "stwio", "bltu", "ldwio", "?", "?", "R-Type", "flushd", "xorhi", "?", "?", "?"};
    public static final String[] OPX_STRINGS = new String[]{"?", "eret", "roli", "rol", "flushp", "ret", "nor", "mulxuu", "cmpge", "bret", "?", "ror", "flushi", "jmp", "and", "?", "cmplt", "?", "slli", "sll", "?", "?", "or", "mulxsu", "cmpne", "?", "srli", "srl", "nextpc", "callr", "xor", "mulxss", "cmpeq", "?", "?", "?", "divu", "div", "rdctl", "mul", "cmpgeu", "initi", "?", "?", "?", "trap", "wrctl", "?", "cmpltu", "add", "?", "?", "break", "?", "sync", "?", "?", "sub", "srai", "sra", "?", "?", "?", "?"};
    private ArrayList<Line> lines;
    private int currentLine = 0;
    private HashMap<String, Integer> globalSymbols;
    private HashMap<String, Integer> labels;
    private HashMap<String, Integer> dataLabels;
    private state_type state;
    private int textAddress;
    private int dataAddress;
    private int textBaseAddress;
    private int dataBaseAddress;
    private MessageListener ml;
    private Interpreter interpreter;

    public int getTextAddress() {
        return this.textAddress;
    }

    public int getDataAddress() {
        return this.dataAddress;
    }

    public int getTextBaseAddress() {
        return this.textBaseAddress;
    }

    public int getDataBaseAddress() {
        return this.dataBaseAddress;
    }

    public static int aliasToReg(String var0) {
        for(int var1 = 0; var1 < 32; ++var1) {
            if (reg_aliases[var1].equals(var0)) {
                return var1;
            }
        }

        return -1;
    }

    public static int aliasToCtlReg(String var0) {
        for(int var1 = 0; var1 < 6; ++var1) {
            if (ctl_reg_aliases[var1].equals(var0)) {
                return var1;
            }
        }

        return -1;
    }

    public static String regToAlias(int var0) {
        return reg_aliases[var0];
    }

    public static String ctlRegToAlias(int var0) {
        return ctl_reg_aliases[var0];
    }

    public static boolean isKeyword(String var0) {
        var0 = var0.toLowerCase();
        createMaps();
        if (itype_ra_rb_imm.containsKey(var0)) {
            return true;
        } else if (itype_ra_rb_relative.containsKey(var0)) {
            return true;
        } else if (rtype_rc_ra_rb.containsKey(var0)) {
            return true;
        } else if (rtype_rc_ra_imm.containsKey(var0)) {
            return true;
        } else if (itype_ra_rb_immu.containsKey(var0)) {
            return true;
        } else if (var0.equals("br")) {
            return true;
        } else if (var0.equals("jmp")) {
            return true;
        } else if (var0.equals("callr")) {
            return true;
        } else if (var0.equals("call")) {
            return true;
        } else if (var0.equals("jmpi")) {
            return true;
        } else if (var0.equals("nop")) {
            return true;
        } else if (var0.equals("ret")) {
            return true;
        } else if (var0.equals("stw")) {
            return true;
        } else if (var0.equals("ldw")) {
            return true;
        } else if (var0.equals("break")) {
            return true;
        } else if (var0.equals("eret")) {
            return true;
        } else if (var0.equals("wrctl")) {
            return true;
        } else if (var0.equals("rdctl")) {
            return true;
        } else if (var0.equals(".equ")) {
            return true;
        } else if (var0.equals(".word")) {
            return true;
        } else if (var0.equals(".hword")) {
            return true;
        } else if (var0.equals(".byte")) {
            return true;
        } else if (var0.equals(".text")) {
            return true;
        } else {
            return var0.equals(".data");
        }
    }

    public static boolean isRegister(String var0) {
        var0 = var0.toLowerCase();
        if (aliasToReg(var0) > -1) {
            return true;
        } else if (aliasToCtlReg(var0) > -1) {
            return true;
        } else if (var0.length() > 0 && var0.charAt(0) == 'r') {
            try {
                int var4 = Integer.parseInt(var0.substring(1));
                if (var4 < 0) {
                    return false;
                } else {
                    return var4 <= 31;
                }
            } catch (NumberFormatException var3) {
                return false;
            }
        } else {
            String[] var1 = new String[]{"ctl0", "ctl1", "ctl2", "ctl3", "ctl4", "ctl5"};

            for(int var2 = 0; var2 < 6; ++var2) {
                if (var0.equals(var1[var2])) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isSymbol(String var1) {
        return this.globalSymbols.containsKey(var1) || this.labels.containsKey(var1);
    }

    public Compiler(MessageListener var1, int var2, int var3) {
        this.state = state_type.TEXT;
        this.lines = new ArrayList();
        this.globalSymbols = new HashMap();
        this.labels = new HashMap();
        this.dataLabels = new HashMap();
        this.textBaseAddress = var2;
        this.dataBaseAddress = var3;
        this.interpreter = new Interpreter(this.globalSymbols, this.labels);
        this.textAddress = 0;
        this.dataAddress = 0;
        this.ml = var1;
        this.state = state_type.TEXT;
        createMaps();
    }

    private static void createMaps() {
        if (itype_ra_rb_imm == null) {
            itype_ra_rb_imm = new HashMap(13);
            itype_ra_rb_imm.put("addi", new Integer(4));
            itype_ra_rb_imm.put("cmpgei", new Integer(8));
            itype_ra_rb_imm.put("cmplti", new Integer(16));
            itype_ra_rb_imm.put("cmpnei", new Integer(24));
            itype_ra_rb_imm.put("cmpeqi", new Integer(32));
            itype_ra_rb_imm.put("cmpgeui", new Integer(40));
            itype_ra_rb_imm.put("cmpltui", new Integer(48));
        }

        if (itype_ra_rb_immu == null) {
            itype_ra_rb_immu = new HashMap(6);
            itype_ra_rb_immu.put("andi", new Integer(12));
            itype_ra_rb_immu.put("ori", new Integer(20));
            itype_ra_rb_immu.put("xori", new Integer(28));
            itype_ra_rb_immu.put("andhi", new Integer(44));
            itype_ra_rb_immu.put("orhi", new Integer(52));
            itype_ra_rb_immu.put("xorhi", new Integer(60));
        }

        if (itype_ra_rb_relative == null) {
            itype_ra_rb_relative = new HashMap(6);
            itype_ra_rb_relative.put("bge", new Integer(14));
            itype_ra_rb_relative.put("blt", new Integer(22));
            itype_ra_rb_relative.put("bne", new Integer(30));
            itype_ra_rb_relative.put("beq", new Integer(38));
            itype_ra_rb_relative.put("bgeu", new Integer(46));
            itype_ra_rb_relative.put("bltu", new Integer(54));
        }

        if (rtype_rc_ra_rb == null) {
            rtype_rc_ra_rb = new HashMap(17);
            rtype_rc_ra_rb.put("rol", new Integer(3));
            rtype_rc_ra_rb.put("nor", new Integer(6));
            rtype_rc_ra_rb.put("mulxuu", new Integer(7));
            rtype_rc_ra_rb.put("cmpge", new Integer(8));
            rtype_rc_ra_rb.put("ror", new Integer(11));
            rtype_rc_ra_rb.put("and", new Integer(14));
            rtype_rc_ra_rb.put("cmplt", new Integer(16));
            rtype_rc_ra_rb.put("sll", new Integer(19));
            rtype_rc_ra_rb.put("or", new Integer(22));
            rtype_rc_ra_rb.put("mulxsu", new Integer(23));
            rtype_rc_ra_rb.put("cmpne", new Integer(24));
            rtype_rc_ra_rb.put("srl", new Integer(27));
            rtype_rc_ra_rb.put("xor", new Integer(30));
            rtype_rc_ra_rb.put("mulxss", new Integer(31));
            rtype_rc_ra_rb.put("cmpeq", new Integer(32));
            rtype_rc_ra_rb.put("mul", new Integer(39));
            rtype_rc_ra_rb.put("cmpgeu", new Integer(40));
            rtype_rc_ra_rb.put("cmpltu", new Integer(48));
            rtype_rc_ra_rb.put("add", new Integer(49));
            rtype_rc_ra_rb.put("sub", new Integer(57));
            rtype_rc_ra_rb.put("sra", new Integer(59));
        }

        if (rtype_rc_ra_imm == null) {
            rtype_rc_ra_imm = new HashMap(4);
            rtype_rc_ra_imm.put("roli", new Integer(2));
            rtype_rc_ra_imm.put("slli", new Integer(18));
            rtype_rc_ra_imm.put("srli", new Integer(26));
            rtype_rc_ra_imm.put("srai", new Integer(58));
        }

    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }

    private void throwException(String var1) throws CompilerException {
        throw new CompilerException(var1);
    }

    private void error(String var1) {
        this.error(var1, this.currentLine);
    }

    private void warning(String var1) {
        this.warning(var1, this.currentLine);
    }

    private void error(String var1, int var2) {
        this.ml.error(var1, var2);
    }

    private void warning(String var1, int var2) {
        this.ml.warning(var1, var2);
    }

    private void info(String var1, int var2) {
        this.ml.info(var1, var2);
    }

    public boolean addText(String var1) {
        boolean var2 = true;
        String[] var3 = linePattern.split(var1);

        for(int var4 = 0; var4 < var3.length; ++var4) {
            if (!this.addLine(var3[var4])) {
                var2 = false;
            }
        }

        return var2;
    }

    public boolean addLine(String var1) {
        ++this.currentLine;
        Line var2 = new Line(this.currentLine);
        this.lines.add(var2);

        try {
            boolean var3 = false;
            var1 = var1.replace('\t', ' ');
            var2.text = var1;
            Matcher var4 = commentPattern.matcher(var1);
            if (var4.find()) {
                var1 = var4.group(1);
            }

            var1 = var1.trim();
            var3 = var1.length() == 0;
            if (!var3) {
                var1 = this.parseLabel(var1);
                var1 = var1.trim();
                var3 = var1.length() == 0;
            }

            if (!var3) {
                if (var1.charAt(0) == '.') {
                    if (this.state == state_type.TEXT) {
                        this.textAddress += this.parseDotCommand(var1, var2);
                    }

                    if (this.state == state_type.DATA) {
                        this.dataAddress += this.parseDotCommand(var1, var2);
                    }
                } else if (this.parseCommand(var1, var2)) {
                    if (this.state == state_type.TEXT) {
                        this.textAddress += 4;
                    }

                    if (this.state == state_type.DATA) {
                        this.dataAddress += 4;
                    }
                } else {
                    this.throwException("Undefined command : " + var1.split(" ")[0]);
                }
            }

            return true;
        } catch (CompilerException var5) {
            this.error(var5.getMessage());
            return false;
        }
    }

    private boolean checkKey(String var1) {
        if (this.globalSymbols.containsKey(var1)) {
            return false;
        } else if (this.labels.containsKey(var1)) {
            return false;
        } else {
            return !this.dataLabels.containsKey(var1);
        }
    }

    private void addSymbol(String var1, int var2) throws CompilerException {
        if (this.checkKey(var1)) {
            this.globalSymbols.put(var1, new Integer(var2));
        } else {
            throw new CompilerException("Symbol " + var1 + " is defined twice");
        }
    }

    private void addLabel(String var1, int var2) throws CompilerException {
        if (this.checkKey(var1)) {
            this.labels.put(var1, new Integer(var2));
        } else {
            throw new CompilerException("Label " + var1 + " is defined twice");
        }
    }

    private void addDataLabel(String var1, int var2) throws CompilerException {
        if (this.checkKey(var1)) {
            this.dataLabels.put(var1, new Integer(var2));
        } else {
            throw new CompilerException("Label " + var1 + " is defined twice");
        }
    }

    private boolean isDataAndTextOverlaping() {
        return this.dataBaseAddress < this.textBaseAddress + this.textAddress && this.dataBaseAddress + this.dataAddress > this.textBaseAddress;
    }

    public boolean terminate() {
        int var1 = 0;
        boolean var2 = true;
        this.info("Linking ...", 0);
        if (this.isDataAndTextOverlaping() && this.dataAddress > 0) {
            this.dataBaseAddress = this.textBaseAddress + this.textAddress;
            this.info(String.format("Moving .data section after .text section (address 0x%04X)", this.dataBaseAddress), -1);
        }

        Iterator var3 = this.dataLabels.keySet().iterator();

        while(var3.hasNext()) {
            String var4 = (String)var3.next();
            int var5 = (Integer)this.dataLabels.get(var4) + this.dataBaseAddress;
            this.labels.put(var4, var5);
        }

        this.dataLabels.clear();
        Iterator var10 = this.lines.iterator();

        while(true) {
            Integer var6;
            Line var11;
            label132:
            while(true) {
                while(true) {
                    do {
                        if (!var10.hasNext()) {
                            if (var2) {
                                this.ml.info("Assembly Successful!", -1);
                            }

                            return var2;
                        }

                        ++var1;
                        var11 = (Line)var10.next();
                        if (var11.contextType == state_type.DATA) {
                            var11.address += this.dataBaseAddress;
                        }
                    } while(var11.expression == null);

                    var6 = 0;
                    if (var11.rtype == ref_type.REF_RELATIVE_ADDR) {
                        try {
                            var6 = this.interpreter.resolve(var11.expression, var11.address);
                            break label132;
                        } catch (CompilerException var9) {
                            this.error(var9.getMessage(), var11.lineNumber);
                            var2 = false;
                        }
                    } else {
                        try {
                            var6 = this.interpreter.resolve(var11.expression);
                        } catch (CompilerException var8) {
                            this.error(var8.getMessage(), var11.lineNumber);
                            var2 = false;
                            continue;
                        }

                        if (var11.rtype == ref_type.REF_IMM26) {
                            if (var6 > 268435455 || var6 < 0) {
                                this.error(String.format("0x%08X is not representable in a 28-bit unsigned word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data |= (var6 >> 2 & 67108863) << 6;
                        } else if (var11.rtype == ref_type.REF_IMM16) {
                            if (var6 > 32767 || var6 < -32768) {
                                this.error(String.format("0x%08X is not representable in a 16-bit signed word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data |= (var6 & '\uffff') << 6;
                        } else if (var11.rtype == ref_type.REF_IMM16U) {
                            if (var6 > 65535 || var6 < 0) {
                                this.error(String.format("0x%08X is not representable in a 16-bit unsigned word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data |= (var6 & '\uffff') << 6;
                        } else if (var11.rtype == ref_type.REF_IMM5) {
                            if (var6 > 31 || var6 < 0) {
                                this.error(String.format("0x%04X is not representable in a 5-bit signed word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data |= (var6 & 31) << 6;
                        } else if (var11.rtype == ref_type.DATA32) {
                            var11.data = var6;
                        } else if (var11.rtype == ref_type.DATA16) {
                            if (var6 > 32767 || var6 < -32768) {
                                this.error(String.format("0x%08X is not representable in a 16-bit signed word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data = var6 & '\uffff';
                        } else if (var11.rtype != ref_type.DATA8) {
                            this.error("Unknown reference type", var1);
                            var2 = false;
                        } else {
                            if (var6 > 127 || var6 < -128) {
                                this.error(String.format("0x%04X is not representable in a 16-bit signed word", var6), var11.lineNumber);
                                var2 = false;
                            }

                            var11.data = var6 & 255;
                        }
                    }
                }
            }

            if (var6 > 32767 || var6 < -32768) {
                this.error(String.format("0x%08X is not representable in a 16-bit signed word", var6), var11.lineNumber);
                var2 = false;
            }

            var11.data |= (var6 & '\uffff') << 6;
        }
    }

    private String parseLabel(String var1) throws CompilerException {
        Matcher var2 = labelPattern.matcher(var1);
        if (var2.find()) {
            if (this.state == state_type.TEXT) {
                this.addLabel(var2.group(1), this.textAddress + this.textBaseAddress);
            }

            if (this.state == state_type.DATA) {
                this.addDataLabel(var2.group(1), this.dataAddress);
            }

            return var2.group(2);
        } else {
            return var1;
        }
    }

    private int paramToReg(String var1) throws CompilerException {
        int var2;
        if ((var2 = aliasToReg(var1)) >= 0) {
            return var2;
        } else {
            int var3 = -1;
            if (var1.charAt(0) == 'r') {
                try {
                    var3 = Integer.parseInt(var1.substring(1));
                    if (var3 < 0 || var3 > 31) {
                        var3 = -1;
                    }
                } catch (NumberFormatException var5) {
                    var3 = -1;
                }
            }

            if (var3 < 0) {
                this.throwException(var1 + " is not a register");
            }

            return var3;
        }
    }

    private ImmReg paramToImmReg(String var1) throws CompilerException {
        Matcher var2 = immRegPattern.matcher(var1);
        if (var2.find()) {
            String var3 = var2.group(1).trim();
            return new ImmReg(var3, this.paramToReg(var2.group(2)));
        } else {
            this.throwException("Parameter " + var1 + " is not an imm(reg) value");
            return null;
        }
    }

    private int packI_type(int var1, int var2, int var3, int var4) {
        return var1 | (var4 & '\uffff') << 6 | (var3 & 31) << 22 | (var2 & 31) << 27;
    }

    private int packR_type(int var1, int var2, int var3, int var4) {
        return this.packR_type(var1, var2, var3, var4, 0);
    }

    private int packR_type(int var1, int var2, int var3, int var4, int var5) {
        return 58 | (var5 & 31) << 6 | (var1 & 63) << 11 | (var4 & 31) << 17 | (var3 & 31) << 22 | (var2 & 31) << 27;
    }

    private int packJ_type(int var1, int var2) {
        return var1 & 63 | var2 << 6;
    }

    private boolean parseCommand(String var1, Line var2) throws CompilerException {
        Matcher var3 = intPattern.matcher(var1);
        int var4 = 0;
        if (this.state == state_type.TEXT) {
            var4 = this.textAddress + this.textBaseAddress;
        }

        if (this.state == state_type.DATA) {
            var4 = this.dataAddress;
        }

        if (var3.find()) {
            String var5 = var3.group(1).toLowerCase();
            String[] var6 = splitPattern.split(var3.group(2));
            Integer var7;
            int var8;
            int var9;
            if ((var7 = (Integer)itype_ra_rb_imm.get(var5)) != null) {
                if (var6.length != 3) {
                    this.throwException(var5 + " needs 3 parameters: rB, rA, imm16");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                var2.setData(this.packI_type(var7, var9, var8, 0), var4, var6[2], ref_type.REF_IMM16, this.state);
                return true;
            }

            if ((var7 = (Integer)itype_ra_rb_immu.get(var5)) != null) {
                if (var6.length != 3) {
                    this.throwException(var5 + " needs 3 parameters: rB, rA, imm16u");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                var2.setData(this.packI_type(var7, var9, var8, 0), var4, var6[2], ref_type.REF_IMM16U, this.state);
                return true;
            }

            if ((var7 = (Integer)itype_ra_rb_relative.get(var5)) != null) {
                if (var6.length != 3) {
                    this.throwException(var5 + " needs 3 parameters: rA, rB, label");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                var2.setData(this.packI_type(var7, var8, var9, 0), var4, var6[2], ref_type.REF_RELATIVE_ADDR, this.state);
                return true;
            }

            if ((var7 = (Integer)rtype_rc_ra_rb.get(var5)) != null) {
                if (var6.length != 3) {
                    this.throwException(var5 + " needs 3 parameters: rC, rA, rB");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                int var10 = this.paramToReg(var6[2]);
                var2.setData(this.packR_type(var7, var9, var10, var8), var4, this.state);
                return true;
            }

            if ((var7 = (Integer)rtype_rc_ra_imm.get(var5)) != null) {
                if (var6.length != 3) {
                    this.throwException(var5 + " needs 3 parameters: rC, rA, imm5");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                var2.setData(this.packR_type(var7, var9, 0, var8), var4, var6[2], ref_type.REF_IMM5, this.state);
                return true;
            }

            if (var5.equals("br")) {
                if (var6.length != 1 || var6[0].length() == 0) {
                    this.throwException("br needs 1 parameters: label");
                }

                var2.setData(this.packI_type(6, 0, 0, 0), var4, var6[0], ref_type.REF_RELATIVE_ADDR, this.state);
                return true;
            }

            if (var5.equals("jmp")) {
                if (var6.length != 1 || var6[0].length() == 0) {
                    this.throwException("jmp needs 1 parameters: rA");
                }

                var8 = this.paramToReg(var6[0]);
                var2.setData(this.packR_type(13, var8, 0, 0), var4, this.state);
                return true;
            }

            if (var5.equals("callr")) {
                if (var6.length != 1 || var6[0].length() == 0) {
                    this.throwException("callr needs 1 parameters: rA");
                }

                var8 = this.paramToReg(var6[0]);
                var2.setData(this.packR_type(29, var8, 0, 31), var4, this.state);
                return true;
            }

            if (var5.equals("call")) {
                if (var6.length != 1 || var6[0].length() == 0) {
                    this.throwException("call needs 1 parameter");
                }

                var2.setData(this.packJ_type(0, 0), var4, var6[0], ref_type.REF_IMM26, this.state);
                return true;
            }

            if (var5.equals("jmpi")) {
                if (var6.length != 1 || var6[0].length() == 0) {
                    this.throwException("call needs 1 parameter");
                }

                var2.setData(this.packJ_type(1, 0), var4, var6[0], ref_type.REF_IMM26, this.state);
                return true;
            }

            if (var5.equals("ret")) {
                if (var6.length != 1 && var6[0].length() == 0) {
                    this.throwException("ret needs no parameters");
                }

                var2.setData(this.packR_type(5, 31, 0, 0), var4, this.state);
                return true;
            }

            ImmReg var11;
            if (var5.equals("stw")) {
                if (var6.length != 2) {
                    this.throwException("stw needs 2 parameters");
                }

                var8 = this.paramToReg(var6[0]);
                var11 = this.paramToImmReg(var6[1]);
                var2.setData(this.packI_type(21, var11.reg, var8, 0), var4, var11.expression, ref_type.REF_IMM16, this.state);
                return true;
            }

            if (var5.equals("ldw")) {
                if (var6.length != 2) {
                    this.throwException("ldw needs 2 parameters");
                }

                var8 = this.paramToReg(var6[0]);
                var11 = this.paramToImmReg(var6[1]);
                var2.setData(this.packI_type(23, var11.reg, var8, 0), var4, var11.expression, ref_type.REF_IMM16, this.state);
                return true;
            }

            if (var5.equals("break")) {
                if (var6.length != 1 && var6[0].length() == 0) {
                    this.throwException("break needs no parameters");
                }

                var2.setData(this.packR_type(52, 0, 0, 30), var4, this.state);
                return true;
            }

            if (var5.equals("nop")) {
                if (var6.length != 1 && var6[0].length() == 0) {
                    this.throwException("nop needs no parameters");
                }

                var2.setData(this.packR_type((Integer)rtype_rc_ra_rb.get("add"), 0, 0, 0), var4, this.state);
                return true;
            }

            if (var5.equals("eret")) {
                if (var6.length != 1 && var6[0].length() == 0) {
                    this.throwException("eret needs no parameters");
                }

                var2.setData(this.packR_type(1, 29, 0, 0), var4, this.state);
                return true;
            }

            if (var5.equals("rdctl")) {
                if (var6.length != 2) {
                    this.throwException("rdclt needs 2 parameters");
                }

                var8 = this.paramToReg(var6[0]);
                var9 = this.paramToCtlReg(var6[1]);
                var2.setData(this.packR_type(38, 0, 0, var8, var9), var4, this.state);
                return true;
            }

            if (var5.equals("wrctl")) {
                if (var6.length != 2) {
                    this.throwException("wrctl needs 2 parameters");
                }

                var8 = this.paramToCtlReg(var6[0]);
                var9 = this.paramToReg(var6[1]);
                var2.setData(this.packR_type(46, var9, 0, 0, var8), var4, this.state);
                return true;
            }
        }

        return false;
    }

    private int paramToCtlReg(String var1) throws CompilerException {
        int var2;
        if ((var2 = aliasToCtlReg(var1)) >= 0) {
            return var2;
        } else {
            String[] var3 = new String[]{"ctl0", "ctl1", "ctl2", "ctl3", "ctl4", "ctl5"};

            for(var2 = 0; var2 < 6; ++var2) {
                if (var1.equals(var3[var2])) {
                    return var2;
                }
            }

            this.throwException(var1 + " is not a control register");
            return -1;
        }
    }

    private int parseDotCommand(String var1, Line var2) throws CompilerException {
        int var3 = 0;
        if (this.state == state_type.TEXT) {
            var3 = this.textAddress + this.textBaseAddress;
        }

        if (this.state == state_type.DATA) {
            var3 = this.dataAddress;
        }

        Matcher var4 = dotPattern.matcher(var1);
        if (var4.find()) {
            String var5 = var4.group(1);
            String[] var6 = splitPattern.split(var4.group(2));
            if (var5.equals(".word")) {
                if (var6 != null && var6.length != 0 && var6[0].length() != 0) {
                    var2.setData(0, var3, var6[0], ref_type.DATA32, this.state);

                    for(int var7 = 1; var7 < var6.length; ++var7) {
                        Line var8 = new Line(var2.lineNumber);
                        var8.setData(0, var3 + 4 * var7, var6[var7], ref_type.DATA32, this.state);
                        var8.contextType = this.state;
                        this.lines.add(var8);
                    }

                    return 4 * var6.length;
                }

                return 0;
            }

            if (var5.equals(".hword")) {
                this.throwException(".hword makes no sense for now (ldh/sth not implemented)");
            } else if (var5.equals(".byte")) {
                this.throwException(".byte makes no sense for now (ldb/stb not implemented)");
            } else {
                if (var5.equals(".equ")) {
                    if (var6.length != 2) {
                        this.throwException("(.equ symbol, value) needs 2 parameters");
                    } else {
                        this.addSymbol(var6[0], this.interpreter.resolve(var6[1]));
                    }

                    return 0;
                }

                if (var5.equals(".text")) {
                    this.state = state_type.TEXT;
                } else {
                    if (!var5.equals(".data")) {
                        this.warning("command ignored (" + var5 + ")");
                        return 0;
                    }

                    this.state = state_type.DATA;
                }
            }
        }

        return 0;
    }

    public static String deassemble(int var0) {
        String var1 = "";
        int var2 = var0 & 63;
        int var3;
        int var4;
        int var5;
        int var6;
        int var7;
        String var8;
        if (var2 == 58) {
            var3 = var0 >> 6 & 31;
            var4 = var0 >> 11 & 63;
            var5 = var0 >> 17 & 31;
            var6 = var0 >> 22 & 31;
            var7 = var0 >> 27 & 31;
            var8 = OPX_STRINGS[var4];
            if (rtype_rc_ra_imm.containsKey(var8)) {
                var1 = String.format("%s  %s, %s, %d", var8, regToAlias(var5), regToAlias(var7), var3);
            } else if (rtype_rc_ra_rb.containsKey(var8)) {
                if (var5 == 0) {
                    var1 = "nop";
                } else {
                    var1 = String.format("%s  %s, %s, %s", var8, regToAlias(var5), regToAlias(var7), regToAlias(var6));
                }
            } else if (!var8.equals("jmp") && !var8.equals("callr") && !var8.equals("flushi")) {
                if (var8.equals("rdctl")) {
                    var1 = String.format("%s  %s, %s", var8, regToAlias(var5), ctlRegToAlias(var3 & 7));
                } else if (var8.equals("wrctl")) {
                    var1 = String.format("%s  %s, %s", var8, ctlRegToAlias(var3 & 7), regToAlias(var7));
                } else {
                    var1 = var8;
                }
            } else {
                var1 = String.format("%s  %s", var8, regToAlias(var7));
            }
        } else {
            var3 = var0 >> 6 & '\uffff';
            var4 = (var3 & '耀') == 32768 ? -65536 | var3 : var3;
            var5 = var0 >> 6;
            var6 = var0 >> 22 & 31;
            var7 = var0 >> 27 & 31;
            var8 = OP_STRINGS[var2];
            if (itype_ra_rb_imm.containsKey(var8)) {
                var1 = String.format("%s  %s, %s, %d", var8, regToAlias(var6), regToAlias(var7), var4);
            } else if (itype_ra_rb_immu.containsKey(var8)) {
                var1 = String.format("%s  %s, %s, %d", var8, regToAlias(var6), regToAlias(var7), var3);
            } else if (itype_ra_rb_relative.containsKey(var8)) {
                var1 = String.format("%s  %s, %s, %d", var8, regToAlias(var7), regToAlias(var6), var4);
            } else if (!var8.equals("call") && !var8.equals("jmpi")) {
                if (!var8.equals("stw") && !var8.equals("ldw")) {
                    if (var8.equals("br")) {
                        var1 = String.format("%s  %d", var8, var4);
                    } else {
                        var1 = var8;
                    }
                } else {
                    var1 = String.format("%s  %s, 0x%04X(%s)", var8, regToAlias(var6), var4, regToAlias(var7));
                }
            } else {
                var1 = String.format("%s  0x%04X", var8, var5 << 2);
            }
        }

        return var1;
    }

    public void writeHexFile(File var1, Module var2) throws IOException {
        FileWriter var3 = new FileWriter(var1);
        short var6 = 0;
        int[] var7 = var2.getData();

        for(int var8 = 0; var8 < var7.length; ++var8) {
            char var9 = '￼';
            var9 = (char)(var9 - (var6 & 255));
            var9 = (char)(var9 - (var6 >> 8 & 255));
            var9 = (char)(var9 + 0);
            var9 = (char)(var9 - (var7[var8] & 255));
            var9 = (char)(var9 - (var7[var8] >> 8 & 255));
            var9 = (char)(var9 - (var7[var8] >> 16 & 255));
            var9 = (char)(var9 - (var7[var8] >> 24 & 255));
            var3.write(String.format(":%02X%04X%02X%08X%02X\n", 4, var6 & '\uffff', 0, var7[var8], var9 & 255));
            ++var6;
        }

        var3.write(":00000001FF\n");
        var3.flush();
        var3.close();
    }

    private class ImmReg {
        String expression;
        int reg;

        ImmReg(String var2, int var3) {
            this.expression = var2;
            this.reg = var3;
        }
    }

    public static enum state_type {
        TEXT,
        DATA,
        INVALID;

        private state_type() {
        }
    }
}
