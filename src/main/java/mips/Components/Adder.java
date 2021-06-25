package mips.Components;

import mips.Utils.Input;
import mips.Utils.Observer;
import mips.Utils.Output;

public class Adder implements Observer{
	public Input input1;
	public Input input2;
	public Output output;

	public Adder() {
		input1 = new Input(this, 16);
		input2 = new Input(this, 16);
		output = new Output(16);
	}

	@Override
	public void update() {
		
	}

	@Override
	public void update(int data) {
		output.load(input1.data + input2.data);
	}
}