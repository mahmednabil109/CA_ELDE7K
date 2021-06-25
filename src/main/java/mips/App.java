package mips;

import java.lang.module.ModuleDescriptor.Builder;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Vector;

import mips.Components.ALU;
import mips.Components.Adder;
import mips.Components.CLK;
import mips.Components.CPU;
import mips.Components.DataMem;
import mips.Components.IDReg;
import mips.Components.InstMem;
import mips.Components.Mux;
import mips.Components.Parser;
import mips.Components.Reg;
import mips.Components.RegFile;
import mips.Components.SignExtend;
import mips.Utils.Input;
import mips.Utils.Output;

public class App {
    public static void main(String[] args) throws Exception {

        // testing the parser
        Path path = Paths.get(System.getProperty("user.dir"), "program 1.txt");
        Parser parser = new Parser();

        Vector<Integer> instructions = parser.Parse(path.toString());

        // System.exit(0);

        // test the arch itself and it works just fine yeah
        Reg PC = new Reg(16);
        Reg buffer = new Reg(16);
        Reg statusReg = new Reg(8);
        InstMem instMem = new InstMem();
        RegFile regFile = new RegFile();
        SignExtend se = new SignExtend(6, 16);
        SignExtend se2 = new SignExtend(8, 16);
        Mux aluMux = new Mux(16);
        Mux writeMux = new Mux(16);
        Mux incMux = new Mux(16);
        Mux pcMux = new Mux(16);
        Mux adderMux = new Mux(16);
        Output high = new Output(1);
        high.data = 1;
        Adder adder = new Adder();
        DataMem dataMem = new DataMem();
        CPU cpu = new CPU(1);
        ALU alu = new ALU();
        CLK clk = new CLK();
        IDReg iDReg = new IDReg();

        // connect status reg
        alu.SREGoutput.connect(statusReg.inData);
        statusReg.outData.connect(cpu.statusReg);

        // PC.outData.connect(instMem.addr);
        PC.outData.connect(buffer.inData);
        buffer.outData.connect(instMem.addr);

        // the branch handling
        iDReg.pc.connect(adderMux.i2);
        PC.outData.connect(adderMux.i1);

        adderMux.out.connect(adder.input1);
        incMux.out.connect(adder.input2);

        high.connect(incMux.i1);
        alu.output.connect(se2.inData);

        se2.outData.connect(incMux.i2);

        se2.outData.connect(pcMux.i2);
        adder.output.connect(pcMux.i1);

        pcMux.out.connect(PC.inData);

        cpu.INCsrc.connect(incMux.sel);
        cpu.PCsrc.connect(pcMux.sel);
        cpu.adderSrc.connect(adderMux.sel);

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

        System.out.println("r1: " + regFile.regs[0]);
        // writeback
        writeMux.out.connect(regFile.writeData);

        iDReg.pc.connect(cpu.pc);
        PC.outData.connect(iDReg.pcInput);

        // instMem.opCode.connect(cpu.op);
        instMem.opCode.connect(iDReg.opInput);
        iDReg.op.connect(cpu.op);

        cpu.ALUop.connect(alu.ALUop);
        cpu.ALUsrc.connect(aluMux.sel);
        cpu.memToReg.connect(writeMux.sel);
        cpu.memWrite.connect(dataMem.storeSignal);
        cpu.regWrite.connect(regFile.writeSignal);

        // this must be in a speciefic order to mimic the real behaviuor
        // but that here

        
        clk.connect(cpu.clk);
        clk.connect(dataMem.clk);
        clk.connect(regFile.clk);
        clk.connect(PC.clk);
        clk.connect(statusReg.clk);
        // test this
        clk.connect(iDReg.clk);
        clk.connect(instMem.clk);
        clk.connect(buffer.clk);

        loadProgram(instMem, instructions);
        for (int inst : instructions)
            System.out.println(Integer.toBinaryString(inst));

        while(PC.outData.data <= (instructions.size() + 2)){
            clk.tick();
            System.out.println(PC.outData.data);
            System.out.println(buffer.outData.data);
        }

        System.out.println("a7a ya 3am");
        System.out.println(Arrays.toString(regFile.regs));

        System.exit(0);

        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        System.out.println("op: " + cpu.op.data);
        System.out.println("op: " + iDReg.op.data);
        System.out.println("pcsrc: " + cpu.PCsrc.data);
        System.out.println("incsrc " + cpu.INCsrc.data);
        System.out.println("addersrc : " + cpu.adderSrc.data);
        System.out.println("adder :" +  adder.output.data);
        System.out.println("alu :" +  alu.output.data);
        

        System.out.println("================");
        for(Input in : clk.subs){
            System.out.println("pcin :" +  PC.inData.data);
            System.out.println("pcout :" +  PC.outData.data);
            System.out.println("bufferin :" +  buffer.inData.data);
            System.out.println("bufferout :" +  buffer.outData.data);
            in.tick();
        }
        System.out.println("================");

        // clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data);
        clk.tick();
        System.out.println(buffer.outData.data); 
        clk.tick();
        System.out.println(buffer.outData.data);

        

    }

    public static void loadProgram(InstMem memo, Vector<Integer> insts) {
        for (int i = 0; i < insts.size(); i++) {
            memo.data[i] = insts.get(i);
        }
    }
}
