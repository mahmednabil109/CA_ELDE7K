package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class CPU implements Observer {

    public Input op;
    public Input pc;
    public Input statusReg;

    // ?note for piplining
    public Input clk;

    public Output ALUop;
    // write signals
    public Output regWrite;
    public Output memWrite;

    // pc write
    public Output PCsrc;
    public Output INCsrc;
    public Output adderSrc;


    // to the two muxes
    // 0 -> alu , 1 -> mem
    public Output memToReg;
    // 0 -> regfile , 1 -> signextend
    public Output ALUsrc;

    public int lastAddr;

    int clks = 0;
    // number of instruction to cancel
    int cancel = 0;
    int tempMemWrite, tempRegWrite;

    public CPU(int lastAddr) {
        this.tempMemWrite = this.tempRegWrite = 0;
        this.clk = new Input(this, 1);
        this.op = new Input(this, 4);
        this.pc = new Input(this, 16);

        // TODO for testing
        this.statusReg = new Input(this, 8);

        this.ALUop = new Output(4);
        this.regWrite = new Output(1);
        this.memWrite = new Output(1);

        this.PCsrc = new Output(1);
        this.INCsrc = new Output(1);
        this.adderSrc = new Output(1);


        this.memToReg = new Output(1);
        this.ALUsrc = new Output(1);

        //
        this.lastAddr = lastAddr;
    }

    @Override
    public void update() {
        // clk
        System.out.println("clk: " + this.clks + " " +  this.ALUop.data);
        // (++clks) % 3 == 0 --> this is valid but for the non piplined arch
        // decrease the cancel flag each clk
        if(this.cancel > 0) this.cancel --;

        if(++clks >= 3){
            System.out.println("hey i am the helw");
            this.regWrite.load(this.tempRegWrite);
            this.memWrite.load(this.tempMemWrite);
        }
    }

    @Override
    public void update(int data) {
        this.regWrite.load(0);
        this.memWrite.load(0);
        
        // for the pc to handle the branching
        this.PCsrc.load(0);
        this.INCsrc.load(0);
        this.adderSrc.load(0);



        this.tempRegWrite = 0;
        this.tempMemWrite = 0;

        // don't do any change
        if(this.cancel > 0) return;

        if (this.pc.data == lastAddr) {
            // TODO cancel any next instructions
        }

        switch (this.op.data) {
            // ADD
            case 0:
                this.ALUsrc.load(0);
                this.ALUop.load(0);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // SUB
            case 1:
                this.ALUsrc.load(0);
                this.ALUop.load(1);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // MUL
            case 2:
                this.ALUsrc.load(0);
                this.ALUop.load(2);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // MOV
            case 3:
                this.ALUsrc.load(1);
                // 
                this.ALUop.load(8);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // BEQZ
            case 4:
                this.ALUsrc.load(1);
                this.ALUop.load(3);
                // check for zero flag
                System.out.println("in cpu status: " + this.statusReg.data);
                if((this.statusReg.data & 1) == 1){
                    this.adderSrc.load(1);
                    this.cancel = 3;
                    this.PCsrc.load(0);
                    this.INCsrc.load(1);
                }else{
                    this.adderSrc.load(0);
                    this.PCsrc.load(0);
                    this.INCsrc.load(0);
                }
                break;
            // ANDI
            case 5:
                this.ALUsrc.load(1);
                this.ALUop.load(4);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // EOR
            case 6:
                this.ALUsrc.load(0);
                this.ALUop.load(5);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // BR
            case 7:
                this.PCsrc.load(1);
                // this is don't care
                this.INCsrc.load(0);
                this.ALUsrc.load(0);
                this.ALUop.load(9);
                this.cancel = 3;
                break;
            // SAL
            case 8:
                this.ALUsrc.load(1);
                this.ALUop.load(6);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // SAR
            case 9:
                this.ALUsrc.load(1);
                this.ALUop.load(7);
                this.memToReg.load(0);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // LDR
            case 10:
                this.memToReg.load(1);
                // this.regWrite.load(1);
                this.tempRegWrite = 1;
                break;
            // STR
            case 11:
                // this.memWrite.load(1);
                this.tempMemWrite = 1;
                break;

        }

    }

}

/*
 * 3 * n + 1 <- <-
 */
