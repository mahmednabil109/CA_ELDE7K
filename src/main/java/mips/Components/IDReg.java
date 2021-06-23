package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class IDReg implements Observer{
    
    public Input pcInput;
    public Input opInput;
    public Input R1Input;
    public Input R2Input;
    public Input outReg1Input;
    public Input outReg2Input;

    public Input clk;

    public Output pc;
    public Output op;
    public Output R1;
    public Output R2;
    public Output outReg1;
    public Output outReg2;


    public IDReg (){
        this.pcInput = new Input(this, 16);
        this.opInput = new Input(this, 4);
        this.R1Input = new Input(this, 6);
        this.R2Input = new Input(this, 6);
        this.outReg1Input = new Input(this, 8);
        this.outReg2Input = new Input(this, 8);
        this.clk = new Input(this, 1);

        this.pc = new Output(16);
        this.op = new Output(4);
        this.R1 = new Output(6);
        this.R2 = new Output(6);
        this.outReg1 = new Output(8);
        this.outReg2 = new Output(8);
    }


    @Override
    public void update() {
        this.pc.load(this.pcInput.data);
        this.op.load(this.opInput.data);
        this.R1.load(this.R1Input.data);
        this.R2.load(this.R2Input.data);
        this.outReg1.load(this.outReg1Input.data);
        this.outReg2.load(this.outReg2Input.data);
    }


    @Override
    public void update(int data) {
        // no need to do any thing here
        ;
    }
}
