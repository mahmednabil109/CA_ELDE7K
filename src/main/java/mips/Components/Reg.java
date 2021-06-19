package mips.Components;


import mips.Utils.Input;
import mips.Utils.Output;
import mips.Utils.Observer;

public class Reg implements Observer{
    
    public Input inData;
    public Input clk;
    
    public Output outData;

    int width;

    public Reg(int width){
        this.width = width;

        this.inData = new Input(this, width);
        this.clk = new Input(this, 1);
        this.outData = new Output(width);
    }

    @Override
    public void update() {
        this.outData.load(this.inData.data & ((1 << this.width) - 1));
    }

    @Override
    public void update(int data) {
        // no need to do any thing here  :0
        ;        
    }

}
