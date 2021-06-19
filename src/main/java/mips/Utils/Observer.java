package mips.Utils;

public interface Observer {
    // this is for the clk triggerd components
    public void update();
    // this is for the compientional components
    public void update(int data);
}
