package mips.Components;


import mips.Utils.Input;
import mips.Utils.Output;
import mips.Utils.Observer;

public class Reg implements Observer{
    
    public Input inData;
    public Input clk;
    
    public Output outData;

    // this is special for the pc
    boolean inc;
    int width;


    public Reg(int width){
        this.width = width;
        this.inc = false;

        this.inData = new Input(this, width);
        this.clk = new Input(this, 1);
        this.outData = new Output(width);
    }

    public void setInc(){
        this.inc = true;
    }

    @Override
    public void update() {
        if(this.inc) this.inData.data ++;
        this.outData.load(this.inData.data & ((1 << this.width) - 1));
    }

    @Override
    public void update(int data) {
        // no need to do any thing here  :0
        ;        
    }

}
