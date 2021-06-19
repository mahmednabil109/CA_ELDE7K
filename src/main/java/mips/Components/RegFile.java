package mips.Components;


import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class RegFile implements Observer{

    public static int REGFILE_SIZE = 64; 
    
    public Input inReg1;
    public Input inReg2;
    public Input writeReg;
    public Input writeData;
    public Input writeSignal;
    public Input clk;

    public Output outReg1;
    public Output outReg2;

    int regs[];

    public RegFile(){
        this.regs = new int[REGFILE_SIZE];

        inReg1 = new Input(this, 16);
        inReg2 = new Input(this, 16);
        writeReg = new Input(this, 16);
        writeSignal = new Input(this, 1);
        clk = new Input(this, 1);

        outReg1 = new Output(16);
        outReg2 = new Output(16);
    }

    @Override
    public void update() {
        if(this.writeSignal.data == 1){
            this.regs[this.writeReg.data & ((1 << 6) - 1)] = this.writeData.data;
        }
        this.outReg1.load(this.regs[this.inReg1.data & ((1 << 6) - 1)]);
        this.outReg2.load(this.regs[this.inReg2.data & (((1 << 6) - 1) << 6)]);
    }

    @Override
    public void update(int data) {
        // no need to do any thing here
    }


}
