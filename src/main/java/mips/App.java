package mips;

import java.nio.file.*;
import java.util.*;
import mips.Components.*;
import mips.Utils.*;

public class App {
    public static void main(String[] args) throws Exception {

        // ? to change the file name 
        String programName = "program 1.txt";

        Path path = Paths.get(System.getProperty("user.dir"), programName);
        Parser parser = new Parser();

        Vector<Integer> instructions = parser.Parse(path.toString());

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

        // this must be in a speciefic order to mimic the real behaviuor of the clk
        clk.connect(cpu.clk);
        clk.connect(dataMem.clk);
        clk.connect(regFile.clk);
        clk.connect(PC.clk);
        clk.connect(statusReg.clk);
        clk.connect(iDReg.clk);
        clk.connect(instMem.clk);
        clk.connect(buffer.clk);

        loadProgram(instMem, instructions);

        for (int inst : instructions)
            System.out.println(Integer.toBinaryString(inst));

        // this is for printing
        Vector<String> queue = new Vector<>();
        queue.add("NOP");
        queue.add("NOP");
        queue.add("NOP");

        while(PC.outData.data <= (instructions.size() + 2)){
            clk.tick();

            /**
             * this code is for printing the status of the mips
            */
            System.out.println(buffer.outData.data);
            queue.remove(0);
            if(buffer.outData.data <= instructions.size())
                queue.add(Integer.toBinaryString(instructions.get(buffer.outData.data - 1)));
            else
                queue.add("NOP");
            System.out.printf("%16s %16s %16s\n", "IF", "ID", "EX");
            System.out.printf("%16s %16s %16s\n", queue.get(2), queue.get(1), queue.get(0));

        }
        System.out.println("[INSTRUCTION MEMORY CONTENT]");
        for(int i=0; i<instMem.data.length; i++)
            System.out.printf("[INST DATA] @%4d: %d\n", i, instMem.data[i]);
        System.out.println("[==========================]");

        System.out.println("[DATA MEMORY CONTENT]");
        for(int i=0; i<dataMem.data.length; i++)
            System.out.printf("[DATA] @%4d: %d\n", i, dataMem.data[i]);
        System.out.println("[===================]");

        System.out.println("[REGISTER FILE CONTENT]");
        for(int i=0; i<regFile.regs.length; i++)
            System.out.printf("[REG DATA] R%2d: %d\n", i, regFile.regs[i]);
        System.out.println("[=====================]");

    }

    public static void loadProgram(InstMem memo, Vector<Integer> insts) {
        for (int i = 0; i < insts.size(); i++) {
            memo.data[i] = insts.get(i);
        }
    }
}
