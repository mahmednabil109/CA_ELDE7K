package mips;

import java.util.Vector;

import mips.Components.ALU;
import mips.Components.CLK;
import mips.Components.CPU;
import mips.Components.DataMem;
import mips.Components.IDReg;
import mips.Components.InstMem;
import mips.Components.Mux;
import mips.Components.Reg;
import mips.Components.RegFile;
import mips.Components.SignExtend;

public class App {
    public static void main(String[] args) throws Exception {
        Reg PC = new Reg(16);
        InstMem instMem = new InstMem();
        RegFile regFile = new RegFile();
        SignExtend se = new SignExtend(6, 16);
        Mux aluMux = new Mux(16);
        Mux writeMux = new Mux(16);
        DataMem dataMem = new DataMem();
        CPU cpu = new CPU(1);
        ALU alu = new ALU();
        CLK clk = new CLK();
        IDReg iDReg = new IDReg();

        //
        PC.outData.connect(instMem.addr);

        instMem.R1.connect(regFile.inReg1);
        instMem.R1.connect(regFile.writeReg);
        instMem.R2.connect(regFile.inReg2);

        instMem.R2.connect(se.inData);

        instMem.R2.connect(dataMem.addr);

        se.outData.connect(aluMux.i2);
        regFile.outReg2.connect(aluMux.i1);

        regFile.outReg1.connect(alu.input1);
        aluMux.out.connect(alu.input2);

        alu.output.connect(writeMux.i1);
        dataMem.outWord.connect(writeMux.i2);

        regFile.outReg1.connect(dataMem.inWord);

        // writeback
        writeMux.out.connect(regFile.writeData);

        PC.outData.connect(cpu.pc);
        instMem.opCode.connect(cpu.op);

        cpu.ALUop.connect(alu.ALUop);
        cpu.ALUsrc.connect(aluMux.sel);
        cpu.memToReg.connect(writeMux.sel);
        cpu.memWrite.connect(dataMem.storeSignal);
        cpu.regWrite.connect(regFile.writeSignal);

        clk.connect(cpu.clk);
        clk.connect(dataMem.clk);
        clk.connect(regFile.clk);
        clk.connect(instMem.clk);
        clk.connect(PC.clk);


        // andi r1 2
        instMem.data[0] = 0b1011_000001_000010;
        regFile.regs[1] = 0b11;
        // pc
        PC.update(0);

        // first clk
        clk.tick();
        // second clk
        clk.tick();

        // third
        clk.tick();



        System.out.println(instMem.opCode.data);
        System.out.println(regFile.outReg1.data);
        System.out.println(cpu.memToReg.data);
        System.out.println(cpu.memWrite.data);
        System.out.println(writeMux.out.data);
        System.out.println(dataMem.outWord.data);
        System.out.println(regFile.outReg1.data);
        System.out.println(dataMem.data[2]);

    }

    public static void loadProgram(InstMem memo, Vector<Integer> insts) {

    }
}
