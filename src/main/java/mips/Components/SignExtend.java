package mips.Components;


import mips.Utils.Input;
import mips.Utils.Output;
import mips.Utils.Observer;

public class SignExtend implements Observer{

    public Input inData;
    public Output outData;

    int from, to;

    public SignExtend(int from, int to){
        this.from = from;
        this.to = to;

        this.inData = new Input(this, this.from);
        this.outData = new Output(this.to);
    }

    @Override
    public void update() {
        // no need to do any thing here ;)
        ;        
    }

    @Override
    public void update(int data) {
        if(((this.inData.data >> this.from - 1) & 1 ) == 1){
            // this input must be negative
            this.outData.load(
                (((1 << (this.to - this.from)) - 1) << this.from) | this.inData.data
            );
        }else{
            this.outData.load(this.inData.data);
        }
    }

    
    
}
