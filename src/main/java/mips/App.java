package mips;

import java.util.Vector;

import mips.Components.InstMem;
import mips.Components.SignExtend;

public class App {
    public static void main(String[] args) {
        String programName = "progra.asm";
        String pathToProgram = System.getProperty("user.dir");
        // TODO pass that string to the function of the parser
        System.out.println(pathToProgram);

        SignExtend se = new SignExtend(6, 16);
        se.inData.update(0b010000);
        System.out.println(se.outData.data);

    }

    public static void loadProgram (InstMem memo, Vector<Integer> insts){
        
    }
}
