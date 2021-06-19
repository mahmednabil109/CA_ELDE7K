package mips;

import java.util.Arrays;
import java.util.Vector;

import mips.Components.InstMem;
import mips.Components.Mux;

public class App {
    public static void main(String[] args) {
        String programName = "progra.asm";
        String pathToProgram = System.getProperty("user.dir");
        // TODO pass that string to the function of the parser
        System.out.println(pathToProgram);

    }

    public static void loadProgram (InstMem memo, Vector<Integer> insts){
        
    }
}
