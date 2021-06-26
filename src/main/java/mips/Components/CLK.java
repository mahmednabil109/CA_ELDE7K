package mips.Components;

import java.util.Vector;

import mips.Utils.Input;

public class CLK {

    public Vector<Input> subs;

    public CLK(){
        this.subs = new Vector<>();
    }

    public void connect(Input in){
        this.subs.add(in);
    }

    public void tick(){
        for(Input in : this.subs)
            in.tick();
    }
    
}
