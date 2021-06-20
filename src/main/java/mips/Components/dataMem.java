package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class dataMem implements Observer {

    public static final int wordSize = 8;
    public static final int memorySize = 2048;

    public int data[];

    // input, address and clock
    public Input inWord;
    public Input addr;
    public Input clk;

    // load and store signals
    public Input loadSignal;
    public Input storeSignal;

    // outword which will be loaded frome memory address
    public Output outWord;

    public dataMem() {
        data = new int[memorySize];
        this.inWord = new Input(this, 6);
        this.clk = new Input(this, 1);
        this.addr = new Input(this, 6);
        this.loadSignal = new Input(this, 1);
        this.storeSignal = new Input(this, 1);
    }

    @Override
    public void update() {
        if (this.loadSignal.data == 1) {
            this.outWord.load(this.data[this.addr.data]);
        }
        if (this.storeSignal.data == 1) {
            this.data[this.addr.data] = this.inWord.data;
        }

    }

    @Override
    public void update(int data) {

    }

}