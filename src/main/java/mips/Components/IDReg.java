package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class IDReg implements Observer{
    
    Input op;
    Input pc;
    Input regOp1;
    Input regOp2;
    Input imme;

    Output pcOut;
    Output opOut;
    Output regOp1Out;
    Output regOp2Out;
    Output immeOut;


    public IDReg (){
        this.pc = new Input(this, 16);
        this.op = new Input(this, 4);
        this.regOp1 = new Input(this, 16);
        this.regOp2 = new Input(this, 16);
        this.imme = new Input(this, 6);

        this.pcOut = new Output(16);
        this.opOut = new Output(4);
        this.regOp1Out = new Output(16);
        this.regOp2Out = new Output(16);
        this.immeOut = new Output(6);
    }


    @Override
    public void update() {
        this.pcOut.load(this.pc.data);
        this.opOut.load(this.op.data);
        this.regOp1Out.load(this.regOp1.data);
        this.regOp2Out.load(this.regOp2.data);
        this.immeOut.load(this.imme.data);
    }


    @Override
    public void update(int data) {
        // no need to do any thing here
        ;
    }
}
