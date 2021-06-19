package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class InstMem implements Observer{

    // this is fixed for this project
    public static final int WORD_SIZE = 16;
    public static final int MEMO_SIZE = 1024;

    // to save the data
    public int data[];

    // to handle the I/O ports
    public Input inWord;
    public Input addr;
    public Input clk;

    public Input writeSignal;
    public Input readSignal;

    public Output outWord;

    public InstMem(){
        data = new int[MEMO_SIZE];

        this.inWord = new Input(this, 16);
        this.clk = new Input(this, 1);
        this.addr = new Input(this, 10);
        this.writeSignal = new Input(this, 1);
        this.readSignal = new Input(this, 1);

        this.outWord = new Output(16);

    }

    @Override
    public void update() {
        // main logic goes here
        if(this.readSignal.data == 1)
            this.outWord.load(this.data[this.addr.data]);
        if(this.writeSignal.data == 1)
            this.data[this.addr.data] = this.inWord.data;
    }

    @Override
    public void update(int data) {
        // no need to compute any thing
        ;
    }

}
