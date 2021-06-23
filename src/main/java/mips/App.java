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
        PC.setInc();
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

    
        PC.outData.connect(instMem.addr);

        instMem.R1.connect(regFile.inReg1);
        
        // instMem.R1.connect(regFile.writeReg);
        instMem.R1.connect(iDReg.R1Input);
        iDReg.R1.connect(regFile.writeReg);
        
        instMem.R2.connect(regFile.inReg2);

        // instMem.R2.connect(se.inData);
        instMem.R2.connect(iDReg.R2Input);
        iDReg.R2.connect(se.inData);

        // instMem.R2.connect(dataMem.addr);
        iDReg.R2.connect(dataMem.addr);

        se.outData.connect(aluMux.i2);

        // regFile.outReg2.connect(aluMux.i1);
        regFile.outReg2.connect(iDReg.outReg2Input);
        iDReg.outReg2.connect(aluMux.i1);

        // regFile.outReg1.connect(alu.input1);
        regFile.outReg1.connect(iDReg.outReg1Input);
        iDReg.outReg1.connect(alu.input1);

        aluMux.out.connect(alu.input2);

        alu.output.connect(writeMux.i1);
        dataMem.outWord.connect(writeMux.i2);

        // regFile.outReg1.connect(dataMem.inWord);
        iDReg.outReg1.connect(dataMem.inWord);

        // writeback
        writeMux.out.connect(regFile.writeData);

        PC.outData.connect(cpu.pc);
        
        // instMem.opCode.connect(cpu.op);
        instMem.opCode.connect(iDReg.opInput);
        iDReg.op.connect(cpu.op);

        cpu.ALUop.connect(alu.ALUop);
        cpu.ALUsrc.connect(aluMux.sel);
        cpu.memToReg.connect(writeMux.sel);
        cpu.memWrite.connect(dataMem.storeSignal);
        cpu.regWrite.connect(regFile.writeSignal);

        // this must be in a speciefic order to mimic the real behaviuor
        clk.connect(cpu.clk);
        clk.connect(dataMem.clk);
        clk.connect(regFile.clk);
        // test this 
        clk.connect(iDReg.clk);
        //
        clk.connect(instMem.clk);
        clk.connect(PC.clk);

        // piplined test
        // movi r1 20
        instMem.data[0] = 0b0011_000000_010100;
        // lw r2 @12
        instMem.data[1] = 0b1010_000001_001100;
        dataMem.data[12] = 10;
        // add r1 r2
        instMem.data[2] = 0b0000_000000_000001;
        // expected result r1 -> 30, r2 -> 10

        clk.tick();
        clk.tick();
        clk.tick();
        // first result here expected  --> r1 = 20 [x]
        System.out.println("r1: " + regFile.regs[0]);
        System.out.println("r2: " + regFile.regs[1]);
        clk.tick();
        // second result here expected --> r2 = 10 [x]
        System.out.println("r1: " + regFile.regs[0]);
        System.out.println("r2: " + regFile.regs[1]);
        clk.tick();
        System.out.println("r1: " + regFile.regs[0]);
        System.out.println("r2: " + regFile.regs[1]);
        // final result here expected  --> r1 = 30
    
    }

    public static void loadProgram(InstMem memo, Vector<Integer> insts) {

    }
}
