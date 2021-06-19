package mips.Utils;

public class Output extends Observable {
    
    int width;

    public  Output(int width){
        this.width = width;
    }

    public void load(int data){
        this.data = data & ((1 << this.width) - 1);
        this.update();
    }
}
