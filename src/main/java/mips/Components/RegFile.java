package mips.Components;


import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class RegFile implements Observer{
    
    public Input inReg1;
    public Input inReg2;
    public Input writeReg;
    public Input writeData;
    public Input writeSignal;

    public Output outReg1;
    public Output outReg2;

    public RegFile(){
        inReg1 = new Input(this, 16);
    }

    @Override
    public void update() {
    }

    @Override
    public void update(int data) {
        
    }


}
