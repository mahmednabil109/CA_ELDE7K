package mips.Utils;

public class Input implements Observer {
    public int data, width;
    Observer cnxt;

    public Input(Observer cnxt, int width){
        this.width = width;
        this.cnxt = cnxt;
        data = 0;
    }

    // special for the clk 
    public void tick(){
        this.update();
    }

    @Override
    public void update() {
        this.cnxt.update();
    }

    @Override
    public void update(int data) {
        this.data = data & ((1 << this.width) - 1);
        this.cnxt.update(this.data);        
    }
}
