package mips.Utils;

import java.util.Vector;

public class Observable {
    Vector<Observer> observers;
    public int data;

    public Observable(){
        this.data = 0;
        this.observers = new Vector<>();
    }

    public void connect(Observer obs){
        this.observers.add(obs);
    }

    public void update(){
        for(Observer obs : this.observers)
            obs.update(this.data);
    }
    
}
