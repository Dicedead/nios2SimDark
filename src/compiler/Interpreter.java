//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package compiler;

import java.util.HashMap;

public class Interpreter {
    private String expr;
    private char token = 0;
    private int cursor = 0;
    private int currentAddress = 0;
    private HashMap<String, Integer> symbols;
    private HashMap<String, Integer> labels;

    public Interpreter(HashMap<String, Integer> var1, HashMap<String, Integer> var2) {
        this.symbols = var1;
        this.labels = var2;
    }

    public static int resolveExpression(String var0) throws CompilerException {
        Interpreter var1 = new Interpreter(new HashMap(), new HashMap());
        return var1.resolve(var0);
    }

    public Integer resolve(String var1) throws CompilerException {
        if (var1 == null) {
            return 0;
        } else {
            this.expr = var1;
            this.cursor = 0;
            this.nextToken();
            Integer var2 = this.expression();
            if (this.token != 0) {
                throw new CompilerException("Unexpected token: " + this.token);
            } else {
                this.currentAddress = 0;
                return var2;
            }
        }
    }

    public Integer resolve(String var1, int var2) throws CompilerException {
        this.currentAddress = var2 + 4;
        return this.resolve(var1);
    }

    public static boolean isNumeric(char var0) {
        return var0 >= '0' && var0 <= '9';
    }

    public static boolean isAlpha(char var0) {
        return var0 == '_' || var0 >= 'a' && var0 <= 'z' || var0 >= 'A' && var0 <= 'Z';
    }

    public static boolean isAlphaNumeric(char var0) {
        return isNumeric(var0) || isAlpha(var0);
    }

    private void nextToken() {
        if (this.cursor >= this.expr.length()) {
            this.token = 0;
        } else {
            this.token = this.expr.charAt(this.cursor);
            ++this.cursor;
            if (this.token <= ' ') {
                this.nextToken();
            }
        }

    }

    private Integer expression() throws CompilerException {
        Integer var1 = this.term();
        boolean var2 = true;

        while(var2) {
            char var3 = this.token;
            switch(var3) {
                case '+':
                    this.nextToken();
                    var1 = var1 + this.term();
                    break;
                case '-':
                    this.nextToken();
                    var1 = var1 - this.term();
                    break;
                default:
                    var2 = false;
            }
        }

        return var1;
    }

    private Integer term() throws CompilerException {
        Integer var1 = this.factor();
        boolean var2 = true;

        while(var2) {
            char var3 = this.token;
            switch(var3) {
                case '!':
                    this.nextToken();
                    var1 = ~(var1 | this.factor());
                    break;
                case '&':
                    this.nextToken();
                    var1 = var1 & this.factor();
                    break;
                case '^':
                    this.nextToken();
                    var1 = var1 ^ this.factor();
                    break;
                case '|':
                    this.nextToken();
                    var1 = var1 | this.factor();
                    break;
                default:
                    var2 = false;
            }
        }

        return var1;
    }

    private Integer factor() throws CompilerException {
        Integer var1 = this.element();
        boolean var2 = true;

        while(var2) {
            char var3 = this.token;
            switch(var3) {
                case '%':
                    this.nextToken();
                    var1 = var1 % this.element();
                    break;
                case '*':
                    this.nextToken();
                    var1 = var1 * this.element();
                    break;
                case '/':
                    this.nextToken();
                    var1 = var1 / this.element();
                    break;
                case '<':
                    this.nextToken();
                    if (this.token != '<') {
                        throw new CompilerException("Unexpected token: " + this.token);
                    }

                    this.nextToken();
                    var1 = var1 << this.element();
                    break;
                case '>':
                    this.nextToken();
                    if (this.token != '>') {
                        throw new CompilerException("Unexpected token: " + this.token);
                    }

                    this.nextToken();
                    var1 = var1 >> this.element();
                    break;
                default:
                    var2 = false;
            }
        }

        return var1;
    }

    private Integer element() throws CompilerException {
        Integer var1 = null;
        if (isAlpha(this.token)) {
            String var2 = this.parseSymbol();
            var1 = (Integer)this.symbols.get(var2);
            if (var1 == null) {
                var1 = (Integer)this.labels.get(var2);
                if (var1 == null) {
                    throw new CompilerException("Unresolvable symbol: " + var2);
                }

                var1 = var1 - this.currentAddress;
            }
        } else if (isNumeric(this.token)) {
            var1 = this.parseInt();
        } else if (this.token == '-') {
            this.nextToken();
            var1 = -this.element();
        } else if (this.token == '~') {
            this.nextToken();
            var1 = ~this.element();
        } else {
            if (this.token != '(') {
                throw new CompilerException("Illegal start of expression : " + this.token);
            }

            this.nextToken();
            var1 = this.expression();
            if (this.token != ')') {
                throw new CompilerException("expected ')' instead of " + this.token);
            }
        }

        this.nextToken();
        return var1;
    }

    private String parseSymbol() {
        int var1;
        for(var1 = this.cursor - 1; this.cursor < this.expr.length() && isAlphaNumeric(this.expr.charAt(this.cursor)); ++this.cursor) {
        }

        return this.expr.substring(var1, this.cursor);
    }

    private Integer parseInt() throws CompilerException {
        Integer var1 = null;
        Long var2 = null;
        String var3 = this.expr.substring(this.cursor - 1);
        String var4;
        if (var3.startsWith("0x")) {
            this.cursor += 2;
            var4 = this.parseDigit();

            try {
                var2 = Long.valueOf(var4, 16);
            } catch (NumberFormatException var8) {
                throw new CompilerException("0x" + var4 + " is not a valid number.");
            }
        } else if (var3.startsWith("0b")) {
            this.cursor += 2;
            var4 = this.parseDigit();

            try {
                var2 = Long.valueOf(var4, 2);
            } catch (NumberFormatException var7) {
                throw new CompilerException("0b" + var4 + " is not a valid number.");
            }
        } else {
            var4 = this.parseDigit();

            try {
                var2 = Long.valueOf(var4);
            } catch (NumberFormatException var6) {
                throw new CompilerException(var4 + " is not a valid number.");
            }
        }

        if (var2 >> 32 != 0L) {
            throw new CompilerException(var3 + " is not representable on 32 bits.");
        } else {
            var1 = var2.intValue();
            return var1;
        }
    }

    private String parseDigit() {
        int var1;
        for(var1 = this.cursor - 1; this.cursor - 1 < this.expr.length() && isAlphaNumeric(this.expr.charAt(this.cursor - 1)); ++this.cursor) {
        }

        --this.cursor;
        return this.expr.substring(var1, this.cursor);
    }
}
