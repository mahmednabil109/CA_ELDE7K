package mips.Utils;

import java.util.Vector;

public class Observable {
    Vector<Observer> observers;
    public int data;

    public Observable(){
        this.data = 0;
        this.observers = new Vector<>();
    }

    public void connect(Observer obs) throws Exception{
        if(obs == null) throw new Exception();
        this.observers.add(obs);
        this.update();
    }

    public void update(){
        for(Observer obs : this.observers){
            if(obs == null) {System.out.println("NULL ??");}
            else{
                obs.update(this.data);
            }
        }
    }
    
}
