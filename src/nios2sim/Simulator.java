//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package nios2sim;

import compiler.Compiler;
import compiler.CompilerException;
import compiler.Line;
import gui.Nios2SimPanel;
import gui.ProcessorPane.InstructionType;
import gui.modules.Module;

import java.util.ArrayList;

public class Simulator {
    private static final int CTL_STATUS = 0;
    private static final int CTL_ESTATUS = 1;
    private static final int CTL_IENABLE = 3;
    private static final int CTL_IPENDING = 4;
    Module[] modules;
    private ArrayList<Line> lines;
    private int pc = 0;
    private int cc = 0;
    private int currentLine;
    private int[] regs = new int[32];
    private boolean[] regs_modified = new boolean[32];
    private int[] ctlregs = new int[6];
    private boolean[] ctlregs_modified = new boolean[7];
    private Nios2SimPanel nios2SimPanel;
    private int pcStartAddress;
    private int interruptAddress;
    MessageListener ml;
    private int irq32;
    private boolean IRQmodified = false;
    private MulticycleCPU pipeline;
    private boolean multicycleEnabled;
    private boolean pipelineEnabled;

    public Simulator(ArrayList<Line> var1, MessageListener var2, Module[] var3, Nios2SimPanel var4, int var5, int var6, boolean var7, boolean var8, boolean var9, boolean var10, boolean var11, boolean var12, boolean var13) {
        this.lines = var1;
        this.ml = var2;
        this.modules = var3;
        this.nios2SimPanel = var4;
        this.interruptAddress = var5;
        this.pcStartAddress = var6;
        this.multicycleEnabled = var7;
        this.pipelineEnabled = var8;
        this.pipeline = new MulticycleCPU(var8, var9, var10, var11, var12, var13);
    }

    public ArrayList<Line> getLines() {
        return this.lines;
    }

    public int getPC() {
        return this.pc;
    }

    public int getCC() {
        return this.cc;
    }

    public int getCurrentLine() {
        return this.currentLine;
    }

    public int getReg(int var1) {
        return this.regs[var1];
    }

    public boolean getRegModified(int var1) {
        return this.regs_modified[var1];
    }

    public void setReg(int var1, int var2) {
        this.regs[var1] = var2;
        this.regs_modified[var1] = true;
    }

    public int getCtlReg(int var1) {
        return this.ctlregs[var1];
    }

    public boolean getCtlRegModified(int var1) {
        return this.ctlregs_modified[var1];
    }

    public void setCtlReg(int var1, int var2) {
        switch(var1) {
            case 0:
            case 1:
            case 2:
                this.ctlregs[var1] = var2 & 1;
                this.ctlregs_modified[var1] = true;
                break;
            case 3:
                int var3 = var2 & this.irq32;
                if (this.ctlregs[4] != var3) {
                    this.ctlregs[4] = var3;
                    this.ctlregs_modified[4] = true;
                    this.nios2SimPanel.getSimulation().refreshTable();
                }
            default:
                this.ctlregs[var1] = var2;
                this.ctlregs_modified[var1] = true;
        }

    }

    public boolean isMemoryInstr(int var1) {
        int var2 = var1 & 63;
        return var2 == 23 || var2 == 21;
    }

    public void start() throws CompilerException {
        int var1;
        for(var1 = 0; var1 < this.modules.length; ++var1) {
            this.modules[var1].reset();
        }

        this.nios2SimPanel.handleSystem();
        this.pc = this.pcStartAddress;
        this.nios2SimPanel.getSimulation().setPC(this.pc);
        this.cc = 0;
        this.nios2SimPanel.getSimulation().setCC(this.cc);

        for(var1 = 0; var1 < this.regs.length; ++var1) {
            this.regs[var1] = 0;
            this.regs_modified[var1] = false;
        }

        for(var1 = 0; var1 < this.ctlregs.length; ++var1) {
            this.ctlregs[var1] = 0;
            this.ctlregs_modified[var1] = false;
        }

        this.nios2SimPanel.getSimulation().setIRQValue(0);
        this.IRQmodified = false;
        this.nios2SimPanel.getSimulation().refreshTable();
        this.pipeline.flush();
        this.ml.info("Simulator started...", -1);
    }

    public Module[] getModules() {
        return this.modules;
    }

    private int loadMem(int var1) {
        for(int var2 = 0; var2 < this.modules.length; ++var2) {
            int var3 = this.modules[var2].getStartAddress();
            int var4 = var3 + this.modules[var2].getSize();
            if (var1 >= var3 && var1 < var4) {
                return this.modules[var2].read(var1 - var3);
            }
        }

        this.ml.warning(String.format("Invalid load address: %08X", var1), this.currentLine + 1);
        return -1;
    }

    private void storeMem(int var1, int var2) {
        for(int var3 = 0; var3 < this.modules.length; ++var3) {
            int var4 = this.modules[var3].getStartAddress();
            int var5 = var4 + this.modules[var3].getSize();
            if (var1 >= var4 && var1 < var5) {
                this.modules[var3].write(var1 - var4, var2);
                return;
            }
        }

        this.ml.warning(String.format("Invalid store address: %08X", var1), this.currentLine + 1);
    }

    private void findPC() {
        for(int var1 = 0; var1 < this.lines.size(); ++var1) {
            Line var2 = (Line)this.lines.get(var1);
            if (var2.isValid() && var2.address == this.pc) {
                this.currentLine = var1;
                return;
            }
        }

        this.nios2SimPanel.warning(String.format("Instruction at address 0x%04X is undefined!", this.pc), -1);
    }

    private int rol(int var1, int var2) {
        var2 &= 63;
        return var1 << var2 | var1 >> 32 - var2 & (1 << var2) - 1;
    }

    private boolean parseRtype(int var1) throws SimulatorException {
        int var2 = var1 >> 6 & 31;
        int var3 = var1 >> 11 & 63;
        int var4 = var1 >> 17 & 31;
        int var5 = var1 >> 22 & 31;
        int var6 = var1 >> 27 & 31;
        int var7 = this.pc + 4;
        switch(var3) {
            case 1:
                this.ctlregs[0] = this.ctlregs[1];
                var7 = this.regs[var6];
                break;
            case 2:
                this.setReg(var4, this.rol(this.regs[var6], var2));
                break;
            case 3:
                this.setReg(var4, this.rol(this.regs[var6], this.regs[var5]));
                break;
            case 4:
            case 9:
            case 10:
            case 12:
            case 15:
            case 17:
            case 20:
            case 21:
            case 25:
            case 28:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 47:
            case 50:
            case 51:
            case 53:
            case 54:
            case 55:
            case 56:
            default:
                this.ml.error(String.format("Invalid opx: %02X...%08X", var3, var1), this.currentLine + 1);
                throw new SimulatorException();
            case 5:
                var7 = this.regs[31];
                break;
            case 6:
                this.setReg(var4, ~(this.regs[var6] | this.regs[var5]));
                break;
            case 7:
                this.setReg(var4, (int)(this.unsigned(this.regs[var6]) * this.unsigned(this.regs[var5]) >> 32));
                break;
            case 8:
                this.setReg(var4, this.regs[var6] >= this.regs[var5] ? 1 : 0);
                break;
            case 11:
                this.setReg(var4, this.rol(this.regs[var6], 32 - this.regs[var5]));
                break;
            case 13:
                var7 = this.regs[var6];
                break;
            case 14:
                this.setReg(var4, this.regs[var6] & this.regs[var5]);
                break;
            case 16:
                this.setReg(var4, this.regs[var6] < this.regs[var5] ? 1 : 0);
                break;
            case 18:
                this.setReg(var4, this.regs[var6] << (var2 & 31));
                break;
            case 19:
                this.setReg(var4, this.regs[var6] << (this.regs[var5] & 31));
                break;
            case 22:
                this.setReg(var4, this.regs[var6] | this.regs[var5]);
                break;
            case 23:
                this.setReg(var4, (int)((long)this.regs[var6] * this.unsigned(this.regs[var5]) >> 32));
                break;
            case 24:
                this.setReg(var4, this.regs[var6] != this.regs[var5] ? 1 : 0);
                break;
            case 26:
                this.setReg(var4, (int)(this.unsigned(this.regs[var6]) >> (var2 & 31)));
                break;
            case 27:
                this.setReg(var4, (int)(this.unsigned(this.regs[var6]) >> (this.regs[var5] & 31)));
                break;
            case 29:
                this.regs[31] = var7;
                var7 = this.regs[var6];
                break;
            case 30:
                this.setReg(var4, this.regs[var6] ^ this.regs[var5]);
                break;
            case 31:
                this.setReg(var4, (int)((long)this.regs[var6] * (long)this.regs[var6] >> 32));
                break;
            case 32:
                this.setReg(var4, this.regs[var6] == this.regs[var5] ? 1 : 0);
                break;
            case 38:
                this.setReg(var4, this.ctlregs[var2]);
                break;
            case 39:
                this.setReg(var4, this.regs[var6] * this.regs[var5]);
                break;
            case 40:
                this.setReg(var4, this.unsigned(this.regs[var6]) >= this.unsigned(this.regs[var5]) ? 1 : 0);
                break;
            case 46:
                this.setCtlReg(var2, this.regs[var6]);
                break;
            case 48:
                this.setReg(var4, this.unsigned(this.regs[var6]) < this.unsigned(this.regs[var5]) ? 1 : 0);
                break;
            case 49:
                this.setReg(var4, this.regs[var6] + this.regs[var5]);
                break;
            case 52:
                this.ml.info("Break!", this.currentLine + 1);
                return false;
            case 57:
                this.setReg(var4, this.regs[var6] - this.regs[var5]);
                break;
            case 58:
                this.setReg(var4, this.regs[var6] >> (var2 & 31));
                break;
            case 59:
                this.setReg(var4, this.regs[var6] >> (this.regs[var5] & 31));
        }

        this.pc = var7;
        return true;
    }

    private long unsigned(int var1) {
        return (long)var1 & 4294967295L;
    }

    private boolean parseItype(int var1, int var2) throws SimulatorException {
        int var3 = var1 >> 6 & '\uffff';
        int var4 = (var3 & '耀') == 32768 ? var3 | -65536 : var3;
        int var5 = var1 >> 6;
        int var6 = var1 >> 22 & 31;
        int var7 = var1 >> 27 & 31;
        int var8 = this.pc + 4;
        switch(var2) {
            case 0:
                this.regs[31] = var8;
                var8 = var5 << 2;
                break;
            case 1:
                var8 = var5 << 2;
                break;
            case 2:
            case 3:
            case 5:
            case 7:
            case 9:
            case 10:
            case 11:
            case 13:
            case 15:
            case 17:
            case 18:
            case 19:
            case 25:
            case 26:
            case 27:
            case 29:
            case 31:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 39:
            case 41:
            case 42:
            case 43:
            case 45:
            case 47:
            case 49:
            case 50:
            case 51:
            case 53:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            default:
                this.ml.error(String.format("Invalid op: %02X...%08X", var2, var1), this.currentLine + 1);
                throw new SimulatorException();
            case 4:
                this.setReg(var6, this.regs[var7] + var4);
                break;
            case 6:
                var8 = this.pc + 4 + var4;
                break;
            case 8:
                this.setReg(var6, this.regs[var7] >= var4 ? 1 : 0);
                break;
            case 12:
                this.setReg(var6, this.regs[var7] & var3);
                break;
            case 14:
                if (this.regs[var7] >= this.regs[var6]) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 16:
                this.setReg(var6, this.regs[var7] < var4 ? 1 : 0);
                break;
            case 20:
                this.setReg(var6, this.regs[var7] | var3);
                break;
            case 21:
                this.storeMem(this.regs[var7] + var4, this.regs[var6]);
                break;
            case 22:
                if (this.regs[var7] < this.regs[var6]) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 23:
                this.setReg(var6, this.loadMem(this.regs[var7] + var4));
                break;
            case 24:
                this.setReg(var6, this.regs[var7] != var4 ? 1 : 0);
                break;
            case 28:
                this.setReg(var6, this.regs[var7] ^ var3);
                break;
            case 30:
                if (this.regs[var7] != this.regs[var6]) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 32:
                this.setReg(var6, this.regs[var7] == var4 ? 1 : 0);
                break;
            case 38:
                if (this.regs[var7] == this.regs[var6]) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 40:
                this.setReg(var6, this.unsigned(this.regs[var7]) >= (long)var3 ? 1 : 0);
                break;
            case 44:
                this.setReg(var6, this.regs[var7] & var3 << 16);
                break;
            case 46:
                if (this.unsigned(this.regs[var7]) >= this.unsigned(this.regs[var6])) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 48:
                this.setReg(var6, this.unsigned(this.regs[var7]) < (long)var3 ? 1 : 0);
                break;
            case 52:
                this.setReg(var6, this.regs[var7] | var3 << 16);
                break;
            case 54:
                if (this.unsigned(this.regs[var7]) < this.unsigned(this.regs[var6])) {
                    var8 = this.pc + 4 + var4;
                }
                break;
            case 60:
                this.setReg(var6, this.regs[var7] ^ var3 << 16);
        }

        this.pc = var8;
        return true;
    }

    public boolean run() {
        int var1;
        for(var1 = 0; var1 < 32; ++var1) {
            this.regs_modified[var1] = false;
        }

        for(var1 = 0; var1 < 7; ++var1) {
            this.ctlregs_modified[var1] = false;
        }

        for(var1 = 0; var1 < this.modules.length; ++var1) {
            this.modules[var1].clearModificationHistory();
        }

        this.IRQmodified = false;
        short var5 = 500;

        for(int var2 = 0; var2 < var5; ++var2) {
            try {
                if (!this.istep(var2 > 0)) {
                    return true;
                }
            } catch (SimulatorException var4) {
                var4.printStackTrace();
                return false;
            }
        }

        this.ml.warning("Reached maximum number of steps (" + var5 + ") while running.", -1);
        return false;
    }

    public void step() {
        int var1;
        for(var1 = 0; var1 < 32; ++var1) {
            this.regs_modified[var1] = false;
        }

        for(var1 = 0; var1 < 7; ++var1) {
            this.ctlregs_modified[var1] = false;
        }

        for(var1 = 0; var1 < this.modules.length; ++var1) {
            this.modules[var1].clearModificationHistory();
        }

        this.IRQmodified = false;

        try {
            this.istep(false);
        } catch (SimulatorException var2) {
        }

    }

    private boolean istep(boolean var1) throws SimulatorException {
        if (var1 && ((Line)this.lines.get(this.currentLine)).breakpoint) {
            this.findPC();
            return false;
        } else {
            ++this.cc;

            for(int var2 = 0; var2 < this.modules.length; ++var2) {
                this.modules[var2].clockEvent();
            }

            boolean var5 = false;
            if (!this.pipelineEnabled && !this.multicycleEnabled) {
                if (this.isInterruptPending()) {
                    this.ctlregs[1] = this.ctlregs[0];
                    this.ctlregs[0] = 0;
                    this.setReg(29, this.pc + 4);
                    this.pc = this.interruptAddress;
                    this.findPC();
                    return true;
                }

                int var3 = this.loadMem(this.pc);
                int var4 = var3 & 63;
                if (var4 == 58) {
                    var5 = this.parseRtype(var3);
                } else {
                    var5 = this.parseItype(var3, var4);
                }
            } else {
                var5 = this.pipeline.step();
            }

            this.regs[0] = 0;
            this.regs_modified[0] = false;
            this.findPC();
            return var5;
        }
    }

    private boolean isInterruptPending() {
        this.ctlregs[4] = this.ctlregs[3] & this.getIRQ32();
        return this.ctlregs[0] == 1 && this.ctlregs[4] != 0;
    }

    public void setInterrupt(int var1) {
        this.irq32 |= 1 << var1;
        this.IRQmodified = true;
        this.nios2SimPanel.getSimulation().setIRQValue(this.irq32);
        this.nios2SimPanel.getSimulation().refreshTable();
    }

    public void clearInterrupt(int var1) {
        this.irq32 &= ~(1 << var1);
        this.IRQmodified = true;
        this.nios2SimPanel.getSimulation().setIRQValue(this.irq32);
        this.nios2SimPanel.getSimulation().refreshTable();
    }

    public int getIRQ32() {
        return this.irq32;
    }

    public boolean isIRQmodified() {
        return this.IRQmodified;
    }

    public void setPipelineEnabled(boolean var1) {
        this.pipelineEnabled = var1;
        this.pipeline.pipelined = var1;
    }

    public void setMulticycleEnabled(boolean var1) {
        this.multicycleEnabled = var1;
        this.pipeline.pipelined = false;
    }

    public void setStallEnabled(boolean var1) {
        this.pipeline.stallEnabled = var1;
    }

    public void setForwardingWDEnabled(boolean var1) {
        this.pipeline.forwardWDEnabled = var1;
        this.nios2SimPanel.getSimulation().getPipelinePane().setforwardingWDEnabled(var1);
    }

    public void setForwardingEEEnabled(boolean var1) {
        this.pipeline.forwardEEEnabled = var1;
        this.nios2SimPanel.getSimulation().getPipelinePane().setforwardingEEEnabled(var1);
    }

    public void setForwardingMEEnabled(boolean var1) {
        this.pipeline.forwardMEEnabled = var1;
        this.nios2SimPanel.getSimulation().getPipelinePane().setforwardingMEEnabled(var1);
    }

    public void setFlushesEnabled(boolean var1) {
        this.pipeline.flushEnabled = var1;
    }

    private class MulticycleCPU {
        private static final int INVALID_ADDRESS = -1;
        State state;
        State nextstate;
        int nextpc;
        private boolean pipelined;
        private boolean stallEnabled;
        private boolean flushEnabled;
        private boolean forwardWDEnabled;
        private boolean forwardEEEnabled;
        private boolean forwardMEEnabled;
        private boolean[] forwarding_paths;
        private Stage[] stages;
        private Stage fetch;
        private DecodeStage decode;
        private ExecuteStage execute;
        private MemoryStage memory;
        private WritebackStage writeback;
        private ForwardUnit forward_unit;

        private MulticycleCPU(boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
            this.forwarding_paths = new boolean[]{false, false, false};
            this.pipelined = var2;
            this.stallEnabled = var3;
            this.forwardWDEnabled = var7;
            this.forwardEEEnabled = var5;
            this.forwardMEEnabled = var6;
            this.flushEnabled = var4;
            this.state = State.FETCH;
            this.nextstate = State.FETCH;
            this.fetch = new Stage();
            this.decode = new DecodeStage();
            this.execute = new ExecuteStage();
            this.memory = new MemoryStage();
            this.writeback = new WritebackStage();
            this.forward_unit = new ForwardUnit();
            this.stages = new Stage[]{this.fetch, this.decode, this.execute, this.memory, this.writeback};
            this.refresh();
        }

        public void refresh() {
            String[] var1 = new String[this.stages.length];
            String[] var2 = new String[this.stages.length];

            for(int var3 = 0; var3 < this.stages.length; ++var3) {
                var1[var3] = this.stages[var3].description;
                var2[var3] = this.stages[var3].addr < 0 ? "-" : String.format("0x%04X", this.stages[var3].addr);
            }

            Simulator.this.nios2SimPanel.getSimulation().getPipelinePane().update(this.forwarding_paths, var1, var2);
        }

        private boolean isAddrValid(int var1) {
            return var1 >= 0;
        }

        public void flush() {
            this.nextpc = Simulator.this.pcStartAddress;
            this.fetch.flush();
            this.decode.flush();
            this.execute.flush();
            this.memory.flush();
            this.writeback.flush();
            this.forward_unit.getDependency();

            for(int var1 = 0; var1 < 3; ++var1) {
                this.forwarding_paths[var1] = false;
            }

            this.refresh();
        }

        private boolean step() throws SimulatorException {
            boolean var1 = true;
            if (this.pipelined) {
                this.forwarding_paths[0] = this.forward_unit.forwarding_paths[0];
                this.forwarding_paths[1] = this.forward_unit.forwarding_paths[1];
                if (this.isAddrValid(this.execute.jumpAddress) && this.flushEnabled) {
                    this.decode.flush();
                    this.fetch.flush();
                }

                if (this.writeback.rW > 0) {
                    Simulator.this.setReg(this.writeback.rW, this.writeback.writebackData);
                }

                this.writeback.writeback();
                this.memory.memory();
                if (this.forward_unit.stall) {
                    this.execute.flush();
                } else {
                    var1 = this.execute.execute();
                    this.decode.decode();
                    this.fetch.addr = Simulator.this.pc;
                    this.fetch.instr = Simulator.this.loadMem(Simulator.this.pc);
                    this.fetch.description = Compiler.deassemble(this.fetch.instr);
                    if (this.isAddrValid(this.execute.jumpAddress)) {
                        Simulator.this.pc = this.execute.jumpAddress;
                    } else {
                        Simulator.this.pc = Simulator.this.pc + 4;
                    }
                }

                this.forward_unit.getDependency();
                this.forwarding_paths[2] = this.forward_unit.forwarding_paths[2];
            } else {
                this.state = this.nextstate;
                switch(this.state) {
                    case FETCH:
                        Simulator.this.pc = this.nextpc;
                        this.fetch.addr = Simulator.this.pc;
                        this.fetch.instr = Simulator.this.loadMem(Simulator.this.pc);
                        this.fetch.description = Compiler.deassemble(this.fetch.instr);
                        this.decode.decode();
                        this.execute.execute();
                        if (this.isAddrValid(this.execute.jumpAddress)) {
                            this.nextpc = this.execute.jumpAddress;
                        } else {
                            this.nextpc = Simulator.this.pc + 4;
                        }

                        Simulator.this.nios2SimPanel.getSimulation().getProcessorPane().updateFetch(String.format("0x%04X", Simulator.this.pc), String.format("0x%08X", this.fetch.instr));
                        this.nextstate = State.DECODE;
                        break;
                    case DECODE:
                        Simulator.this.nios2SimPanel.getSimulation().getProcessorPane().updateDecode(String.format("0x%04X", Simulator.this.pc + 4), this.fetch.description, this.execute.isIMMused ? String.format("0x%08X", this.execute.immediate) : null, this.execute.op, new String[]{this.decode.A >= 0 ? String.format("r%d", this.decode.A) : null, this.decode.B >= 0 ? String.format("r%d", this.decode.B) : null, this.execute.rW > 0 ? String.format("r%d", this.execute.rW) : null});
                        this.nextstate = State.EXECUTE;
                        break;
                    case EXECUTE:
                        Simulator.this.nios2SimPanel.getSimulation().getProcessorPane().updateExecute(this.execute.instrType, this.execute.instrType != InstructionType.ALU && this.execute.instrType != InstructionType.BRANCH ? null : String.format("%d%s%d=%d", Simulator.this.regs[this.decode.A], this.execute.op, this.execute.instrType == InstructionType.ALU && this.execute.isIMMused ? this.execute.immediate : Simulator.this.regs[this.decode.A], this.execute.executeData), this.isAddrValid(this.execute.jumpAddress) ? String.format("0x%04X", this.execute.jumpAddress) : null, false, new String[]{this.decode.A >= 0 ? String.format("%d", Simulator.this.regs[this.decode.A]) : null, this.decode.B >= 0 ? String.format("%d", Simulator.this.regs[this.decode.B]) : null, this.execute.rW > 0 ? String.format("%d", this.execute.executeData) : null});
                        if (Simulator.this.isMemoryInstr(this.execute.instr)) {
                            this.nextstate = State.MEMORY;
                        } else {
                            this.memory.memory();
                            this.writeback.writeback();
                            if (this.writeback.rW > 0) {
                                Simulator.this.setReg(this.writeback.rW, this.writeback.writebackData);
                            }

                            this.nextstate = State.FETCH;
                        }
                        break;
                    case MEMORY:
                        this.memory.memory();
                        this.writeback.writeback();
                        if (this.writeback.rW > 0) {
                            Simulator.this.setReg(this.writeback.rW, this.writeback.writebackData);
                        }

                        this.nextstate = State.FETCH;
                        Simulator.this.nios2SimPanel.getSimulation().getProcessorPane().updateMemory((this.execute.instr & 63) == 21, String.format("0x%04X", this.execute.executeData), String.format("%d", this.execute.memoryData));
                }
            }

            this.refresh();
            return var1;
        }

        private class WritebackStage extends Stage {
            public int writebackData;
            public int rW;

            private WritebackStage() {
                super(null);
                this.writebackData = 0;
                this.rW = 0;
            }

            public void flush() {
                super.flush();
                this.writebackData = 0;
                this.rW = 0;
            }

            public void writeback() {
                this.copy(MulticycleCPU.this.memory);
                this.writebackData = MulticycleCPU.this.memory.memoryData;
                this.rW = MulticycleCPU.this.memory.rW;
            }
        }

        private class MemoryStage extends Stage {
            private int data;
            private int executeData;
            public int memoryData;
            public int rW;

            private MemoryStage() {
                super(null);
                this.data = 0;
                this.executeData = 0;
                this.memoryData = 0;
                this.rW = 0;
            }

            public void flush() {
                super.flush();
                this.data = 0;
                this.executeData = 0;
                this.memoryData = 0;
                this.rW = 0;
            }

            public void memory() throws SimulatorException {
                this.copy(MulticycleCPU.this.execute);
                this.data = MulticycleCPU.this.execute.memoryData;
                this.executeData = MulticycleCPU.this.execute.executeData;
                this.rW = MulticycleCPU.this.execute.rW;
                this.memoryData = this.executeData;
                int var1 = this.instr & 63;
                switch(var1) {
                    case 21:
                        Simulator.this.storeMem(this.executeData, this.data);
                        break;
                    case 23:
                        this.memoryData = Simulator.this.loadMem(this.executeData);
                }

            }
        }

        private class ExecuteStage extends Stage {
            public int jumpAddress;
            public int executeData;
            public int rW;
            public int memoryData;
            public int immediate;
            public boolean isIMMused;
            public String op;
            InstructionType instrType;

            private ExecuteStage() {
                super(null);
                this.jumpAddress = -1;
                this.executeData = 0;
                this.rW = 0;
                this.memoryData = 0;
                this.immediate = 0;
                this.isIMMused = false;
                this.op = null;
                this.instrType = InstructionType.NOP;
            }

            public void flush() {
                super.flush();
                this.rW = 0;
                this.executeData = 0;
                this.jumpAddress = -1;
                this.isIMMused = false;
                this.memoryData = 0;
                this.op = null;
                this.instrType = InstructionType.NOP;
            }

            public boolean execute() throws SimulatorException {
                this.copy(MulticycleCPU.this.decode);
                this.jumpAddress = -1;
                this.isIMMused = false;
                this.executeData = 0;
                this.rW = 0;
                this.op = null;
                this.instrType = InstructionType.NOP;
                if (Simulator.this.isInterruptPending() && MulticycleCPU.this.isAddrValid(this.addr)) {
                    this.rW = 29;
                    this.executeData = this.addr + 4;
                    this.description = "interrupt!";
                    this.addr = -1;
                    this.instr = 0;
                    this.jumpAddress = Simulator.this.interruptAddress;
                    Simulator.this.ctlregs[1] = Simulator.this.ctlregs[0];
                    Simulator.this.ctlregs[0] = 0;
                    return false;
                } else {
                    int var1 = this.instr & 63;
                    boolean var2 = false;
                    boolean var3 = false;
                    int var7;
                    int var8;
                    if (MulticycleCPU.this.pipelined) {
                        var7 = MulticycleCPU.this.forward_unit.rA;
                        var8 = MulticycleCPU.this.forward_unit.rB;
                    } else {
                        var7 = MulticycleCPU.this.decode.A > 0 ? Simulator.this.regs[MulticycleCPU.this.decode.A] : 0;
                        var8 = MulticycleCPU.this.decode.B > 0 ? Simulator.this.regs[MulticycleCPU.this.decode.B] : 0;
                    }

                    this.memoryData = var8;
                    int var4;
                    if (var1 == 58) {
                        this.immediate = this.instr >> 6 & 31;
                        var4 = this.instr >> 11 & 63;
                        this.rW = this.instr >> 17 & 31;
                        switch(var4) {
                            case 1:
                                Simulator.this.ctlregs[0] = Simulator.this.ctlregs[1];
                                this.jumpAddress = var7;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 2:
                                this.executeData = Simulator.this.rol(var7, this.immediate);
                                this.op = "<<";
                                this.isIMMused = true;
                                this.instrType = InstructionType.ALU;
                                break;
                            case 3:
                                this.executeData = Simulator.this.rol(var7, var8);
                                this.op = "<<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 4:
                            case 9:
                            case 10:
                            case 12:
                            case 15:
                            case 17:
                            case 20:
                            case 21:
                            case 25:
                            case 28:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                            case 47:
                            case 50:
                            case 51:
                            case 53:
                            case 54:
                            case 55:
                            case 56:
                            default:
                                Simulator.this.ml.error(String.format("Invalid opx: %02X...%08X", var4, this.instr), Simulator.this.currentLine + 1);
                                throw Simulator.this.new SimulatorException();
                            case 5:
                                this.jumpAddress = var7;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 6:
                                this.executeData = ~(var7 | var8);
                                this.op = "nor";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 7:
                                this.executeData = (int)(Simulator.this.unsigned(var7) * Simulator.this.unsigned(var8) >> 32);
                                this.op = "*";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 8:
                                this.executeData = var7 >= var8 ? 1 : 0;
                                this.op = ">=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 11:
                                this.executeData = Simulator.this.rol(var7, 32 - var8);
                                this.op = ">>";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 13:
                                this.jumpAddress = var7;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 14:
                                this.executeData = var7 & var8;
                                this.op = "&";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 16:
                                this.executeData = var7 < var8 ? 1 : 0;
                                this.op = "<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 18:
                                this.executeData = var7 << (this.immediate & 31);
                                this.op = "<<";
                                this.isIMMused = true;
                                this.instrType = InstructionType.ALU;
                                break;
                            case 19:
                                this.executeData = var7 << (var8 & 31);
                                this.op = "<<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 22:
                                this.executeData = var7 | var8;
                                this.op = "|";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 23:
                                this.executeData = (int)((long)var7 * Simulator.this.unsigned(var8) >> 32);
                                this.op = "*";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 24:
                                this.executeData = var7 != var8 ? 1 : 0;
                                this.op = "!=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 26:
                                this.executeData = (int)(Simulator.this.unsigned(var7) >> (this.immediate & 31));
                                this.op = ">>";
                                this.isIMMused = true;
                                this.instrType = InstructionType.ALU;
                                break;
                            case 27:
                                this.executeData = (int)(Simulator.this.unsigned(var7) >> (var8 & 31));
                                this.op = ">>";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 29:
                                this.executeData = this.addr + 4;
                                this.jumpAddress = var7;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 30:
                                this.executeData = var7 ^ var8;
                                this.op = "^";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 31:
                                this.executeData = (int)((long)var7 * (long)var7 >> 32);
                                this.op = "*";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 32:
                                this.executeData = var7 == var8 ? 1 : 0;
                                this.op = "==";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 38:
                                this.executeData = Simulator.this.ctlregs[this.immediate];
                                break;
                            case 39:
                                this.executeData = var7 * var8;
                                this.op = "*";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 40:
                                this.executeData = Simulator.this.unsigned(var7) >= Simulator.this.unsigned(var8) ? 1 : 0;
                                this.op = ">=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 46:
                                Simulator.this.setCtlReg(this.immediate, var7);
                                break;
                            case 48:
                                this.executeData = Simulator.this.unsigned(var7) < Simulator.this.unsigned(var8) ? 1 : 0;
                                this.op = "<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 49:
                                this.executeData = var7 + var8;
                                this.op = "+";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 52:
                                this.rW = 0;
                                this.jumpAddress = this.addr;
                                Simulator.this.ml.info("Break!", Simulator.this.currentLine + 1);
                                return false;
                            case 57:
                                this.executeData = var7 - var8;
                                this.op = "-";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 58:
                                this.executeData = var7 >> (this.immediate & 31);
                                this.isIMMused = true;
                                this.op = ">>";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 59:
                                this.executeData = var7 >> (var8 & 31);
                                this.op = ">>";
                                this.instrType = InstructionType.ALU;
                        }
                    } else {
                        this.isIMMused = true;
                        var4 = this.instr >> 6 & '\uffff';
                        int var5 = (var4 & '耀') == 32768 ? -65536 | var4 : var4;
                        int var6 = this.instr >> 6 << 2;
                        this.rW = this.instr >> 22 & 31;
                        switch(var1) {
                            case 0:
                                this.rW = 31;
                                this.executeData = this.addr + 4;
                                this.jumpAddress = var6;
                                this.immediate = var6;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 1:
                                this.rW = 0;
                                this.jumpAddress = var6;
                                this.immediate = var6;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 2:
                            case 3:
                            case 5:
                            case 7:
                            case 9:
                            case 10:
                            case 11:
                            case 13:
                            case 15:
                            case 17:
                            case 18:
                            case 19:
                            case 25:
                            case 26:
                            case 27:
                            case 29:
                            case 31:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 39:
                            case 41:
                            case 42:
                            case 43:
                            case 45:
                            case 47:
                            case 49:
                            case 50:
                            case 51:
                            case 53:
                            case 55:
                            case 56:
                            case 57:
                            case 58:
                            case 59:
                            default:
                                Simulator.this.ml.error(String.format("Invalid op: %02X...%08X", var1, this.instr), Simulator.this.currentLine + 1);
                                throw Simulator.this.new SimulatorException();
                            case 4:
                                this.executeData = var7 + var5;
                                this.immediate = var5;
                                this.op = "+";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 6:
                                this.rW = 0;
                                this.jumpAddress = this.addr + 4 + var5;
                                this.immediate = var5;
                                this.instrType = InstructionType.JUMP;
                                break;
                            case 8:
                                this.executeData = var7 >= var5 ? 1 : 0;
                                this.immediate = var5;
                                this.op = ">=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 12:
                                this.executeData = var7 & var4;
                                this.immediate = var4;
                                this.op = "&";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 14:
                                this.rW = 0;
                                if (var7 >= var8) {
                                    this.jumpAddress = this.addr + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = ">=";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 16:
                                this.executeData = var7 < var5 ? 1 : 0;
                                this.immediate = var5;
                                this.op = "<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 20:
                                this.executeData = var7 | var4;
                                this.immediate = var4;
                                this.op = "|";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 21:
                                this.rW = 0;
                                this.executeData = var7 + var5;
                                this.immediate = var5;
                                this.op = "+";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 22:
                                this.rW = 0;
                                if (var7 < var8) {
                                    this.jumpAddress = Simulator.this.pc + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = "<";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 23:
                                this.executeData = var7 + var5;
                                this.immediate = var5;
                                this.op = "+";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 24:
                                this.executeData = var7 != var5 ? 1 : 0;
                                this.immediate = var5;
                                this.op = "!=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 28:
                                this.executeData = var7 ^ var4;
                                this.immediate = var4;
                                this.op = "^";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 30:
                                this.rW = 0;
                                if (var7 != var8) {
                                    this.jumpAddress = this.addr + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = "!=";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 32:
                                this.executeData = var7 == var5 ? 1 : 0;
                                this.immediate = var5;
                                this.op = "==";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 38:
                                this.rW = 0;
                                if (var7 == var8) {
                                    this.jumpAddress = this.addr + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = "==";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 40:
                                this.executeData = Simulator.this.unsigned(var7) >= (long)var4 ? 1 : 0;
                                this.immediate = var4;
                                this.op = ">=";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 44:
                                this.executeData = var7 & var4 << 16;
                                this.immediate = var4 << 16;
                                this.op = "<<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 46:
                                this.rW = 0;
                                if (Simulator.this.unsigned(var7) >= Simulator.this.unsigned(var8)) {
                                    this.jumpAddress = this.addr + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = ">=";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 48:
                                this.executeData = Simulator.this.unsigned(var7) < (long)var4 ? 1 : 0;
                                this.immediate = var4;
                                this.op = "<";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 52:
                                this.executeData = var7 | var4 << 16;
                                this.immediate = var4 << 16;
                                this.op = "|";
                                this.instrType = InstructionType.ALU;
                                break;
                            case 54:
                                this.rW = 0;
                                if (Simulator.this.unsigned(var7) < Simulator.this.unsigned(var8)) {
                                    this.jumpAddress = this.addr + 4 + var5;
                                }

                                this.immediate = var5;
                                this.op = "<";
                                this.instrType = InstructionType.BRANCH;
                                break;
                            case 60:
                                this.executeData = var7 ^ var4 << 16;
                                this.immediate = var4 << 16;
                                this.op = "^";
                                this.instrType = InstructionType.ALU;
                        }
                    }

                    return true;
                }
            }
        }

        private class DecodeStage extends Stage {
            public int A;
            public int B;

            private DecodeStage() {
                super(null);
                this.A = -1;
                this.B = -1;
            }

            public void decode() {
                this.copy(MulticycleCPU.this.fetch);
                this.A = -1;
                this.B = -1;
                int var1 = this.instr & 63;
                int var2;
                int var3;
                if (var1 == 58) {
                    var2 = this.instr >> 27 & 31;
                    var3 = this.instr >> 22 & 31;
                    int var4 = this.instr >> 11 & 63;
                    String var5 = Compiler.OPX_STRINGS[var4];
                    if (Compiler.rtype_rc_ra_rb.containsKey(var5)) {
                        this.A = var2;
                        this.B = var3;
                    } else if (Compiler.rtype_rc_ra_imm.containsKey(var5) || var5.equals("jmp") || var5.equals("callr") || var5.equals("flushi") || var5.equals("ret") || var5.equals("eret") || var5.equals("wrctl")) {
                        this.A = var2;
                    }
                } else {
                    var2 = this.instr >> 27 & 31;
                    var3 = this.instr >> 22 & 31;
                    String var6 = Compiler.OP_STRINGS[var1];
                    if (!Compiler.itype_ra_rb_imm.containsKey(var6) && !Compiler.itype_ra_rb_immu.containsKey(var6) && !var6.equals("ldw")) {
                        if (Compiler.itype_ra_rb_relative.containsKey(var6) || var6.equals("stw")) {
                            this.A = var2;
                            this.B = var3;
                        }
                    } else {
                        this.A = var2;
                    }
                }

            }
        }

        private class Stage {
            public static final int NOP = 100410;
            public int instr;
            public int addr;
            public String description;

            private Stage() {
                this.instr = 100410;
                this.addr = -1;
                this.description = "nop";
            }

            private Stage(Object o) {
                this();
            }

            public void flush() {
                this.instr = 100410;
                this.addr = -1;
                this.description = "nop";
            }

            public void copy(Stage var1) {
                this.instr = var1.instr;
                this.addr = var1.addr;
                this.description = var1.description;
            }
        }

        private class ForwardUnit {
            public boolean stall;
            public boolean[] forwarding_paths;
            public int rA;
            public int rB;

            private ForwardUnit() {
                this.stall = false;
                this.forwarding_paths = new boolean[]{false, false, false};
                this.rA = 0;
                this.rB = 0;
            }

            private void getDependency() {
                this.stall = false;

                int var1;
                for(var1 = 0; var1 < 3; ++var1) {
                    this.forwarding_paths[var1] = false;
                }

                this.rA = 0;
                this.rB = 0;
                if (MulticycleCPU.this.decode.A > 0) {
                    this.rA = Simulator.this.regs[MulticycleCPU.this.decode.A];
                    if (MulticycleCPU.this.execute.rW == MulticycleCPU.this.decode.A) {
                        if (MulticycleCPU.this.forwardEEEnabled) {
                            if (MulticycleCPU.this.stallEnabled && (MulticycleCPU.this.execute.instr & 63) == 23) {
                                this.stall = true;
                            } else {
                                this.rA = MulticycleCPU.this.execute.executeData;
                                this.forwarding_paths[0] = true;
                            }
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    } else if (MulticycleCPU.this.memory.rW == MulticycleCPU.this.decode.A) {
                        if (MulticycleCPU.this.forwardMEEnabled) {
                            this.rA = MulticycleCPU.this.memory.memoryData;
                            this.forwarding_paths[1] = true;
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    } else if (MulticycleCPU.this.writeback.rW == MulticycleCPU.this.decode.A) {
                        if (MulticycleCPU.this.forwardWDEnabled) {
                            this.rA = MulticycleCPU.this.writeback.writebackData;
                            this.forwarding_paths[2] = true;
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    }
                }

                if (MulticycleCPU.this.decode.B > 0 && !this.stall) {
                    this.rB = Simulator.this.regs[MulticycleCPU.this.decode.B];
                    if (MulticycleCPU.this.execute.rW == MulticycleCPU.this.decode.B) {
                        if (MulticycleCPU.this.forwardEEEnabled) {
                            if (MulticycleCPU.this.stallEnabled && (MulticycleCPU.this.execute.instr & 63) == 23) {
                                this.stall = true;
                            } else {
                                this.rB = MulticycleCPU.this.execute.executeData;
                                this.forwarding_paths[0] = true;
                            }
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    } else if (MulticycleCPU.this.memory.rW == MulticycleCPU.this.decode.B) {
                        if (MulticycleCPU.this.forwardMEEnabled) {
                            this.rB = MulticycleCPU.this.memory.memoryData;
                            this.forwarding_paths[1] = true;
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    } else if (MulticycleCPU.this.writeback.rW == MulticycleCPU.this.decode.B) {
                        if (MulticycleCPU.this.forwardWDEnabled) {
                            this.rB = MulticycleCPU.this.writeback.writebackData;
                            this.forwarding_paths[2] = true;
                        } else if (MulticycleCPU.this.stallEnabled) {
                            this.stall = true;
                        }
                    }
                }

                if (this.stall) {
                    for(var1 = 0; var1 < 3; ++var1) {
                        this.forwarding_paths[var1] = false;
                    }
                }

            }
        }
    }

    public static enum State {
        FETCH,
        DECODE,
        EXECUTE,
        MEMORY,
        WRITEBACK;

        private State() {
        }
    }

    private class SimulatorException extends Exception {
        private static final long serialVersionUID = 1L;

        private SimulatorException() {
        }
    }
}
