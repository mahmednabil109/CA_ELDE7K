package mips.Components;

import mips.Utils.Input;
import mips.Utils.Output;
import mips.Utils.Observer;

public class Mux implements Observer{
    
    public Input i1;
    public Input i2;
    public Input sel;

    public Output out;

    public Mux(int width){
        this.i1 = new Input(this, width);
        this.i2 = new Input(this, width);

        this.out = new Output(width);

        this.sel = new Input(this, 1);
    }

    @Override
    public void update() {
        // no need to do any thing here
        ;        
    }

    @Override
    public void update(int data) {
        if(this.sel.data == 1)
            this.out.load(this.i2.data);
        else
            this.out.load(this.i1.data);        
    }   
    
}
