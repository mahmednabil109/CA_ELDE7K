package mips.Components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class Parser {

    // vector for instructions as strings , hashtable for labels
    Vector<String[]> instructions = new Vector<String[]>();
    Vector<Integer> binInstructions = new Vector<Integer>();
    Hashtable<String, Integer> labels = new Hashtable<>();

    // parser which gets each line and parses it and calls convertBin to convert it
    public Vector<Integer> Parse(String instructionsPath) {
        try {
            File instructions = new File(instructionsPath);
            FileReader fr = new FileReader(instructions);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int lineNum = 0;
            // reads the file line by line
            while ((line = br.readLine()) != null) {

                line.trim();
                line.toUpperCase();

                String[] splittedLine = line.replaceAll("\\s(\\s)+", "").split(" ");
                System.out.println(Arrays.toString(splittedLine));
                for(int i=0; i<splittedLine.length; i++)
                    splittedLine[i] = splittedLine[i].trim();
                String label;
                // here we have label
                if (splittedLine.length >= 2 && (label = find(splittedLine[0], splittedLine[1])) != null) {
                    labels.put(label, lineNum);
                    String[] splittedInstruction = line.split(":");
                    // checking if we have something in the line with the label
                    if (splittedInstruction.length > 1) {
                        String[] splittedFinal = splittedInstruction[1].trim().split(" ");
                        this.instructions.add(splittedFinal);
                    }
                }
                // here we don't have label
                else {
                    System.out.println(line);
                    this.instructions.add(splittedLine);
                }
                lineNum++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(int i=0; i<this.instructions.size(); i++){
            String[] inst = instructions.get(i);
            if(inst.length < 2) continue;
            if(inst[2].charAt(0) != 'r' && inst[2].charAt(0) != '-' && (inst[2].charAt(0) > '9' || inst[2].charAt(0) < '0')){
                System.out.println(labels + " " + inst[2] + " " + labels.get(inst[2]));
                inst[2] = "" + (labels.get(inst[2]) - i - 1);
            }
        }
        return convertStringtoBin();

    }{}

    // takes the instructions
    private Vector<Integer> convertStringtoBin() {
        for (String[] inst : this.instructions) {
            if(inst.length < 2) continue;
            int Instruction = 0;
            int opcode = 0;

            // opcode switch case

            switch (inst[0].toUpperCase()) {
                case "ADD":
                    opcode = 0;
                    break;
                case "SUB":
                    opcode = 1;
                    break;
                case "MUL":
                    opcode = 2;
                    break;
                case "MOVI":
                    opcode = 3;
                    break;
                case "BEQZ":
                    opcode = 4;
                    break;
                case "ANDI":
                    opcode = 5;
                    break;
                case "EOR":
                    opcode = 6;
                    break;
                case "BR":
                    opcode = 7;
                    break;
                case "SAL":
                    opcode = 8;
                    break;
                case "SAR":
                    opcode = 9;
                    break;
                case "LDR":
                    opcode = 10;
                    break;
                case "STR":
                    opcode = 11;
                    break;
                default:
                    opcode = 15;
                    break;
            }
            // gets R1 code
            String R1 = inst[1];
            int regNum1 = Integer.parseInt(R1.substring(1, R1.length()));
            if (inst[2].toLowerCase().charAt(0) == 'r') {
                inst[2] = inst[2].substring(1, inst[2].length());
            }
            // Gets R2/I code
            int regNum2 = Integer.parseInt(inst[2]);

            // calculates the instruction as 16 bits bin num
            Instruction = (opcode << 12) | (regNum1 << 6) | (regNum2 & ((1 << 6) - 1));
            binInstructions.add(Instruction);
        }
        // For Testing

        // System.out.println(binInstructions);
        // System.out.println(Integer.toBinaryString(binInstructions.get(1)));
        return binInstructions;
    }

    private String find(String word1, String word2) {
        // this can get the line we have label in
        String res = "";
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) == ':') {
                res = word1.substring(0, i);
                return res;
            }
        }
        if (word2.equals(":"))
            return word1;
        return null;
    }

    // public static void main(String[] args) {
    //     Parser p = new Parser();
    //     p.Parse("C://Users/Ismailia Laptop/eclipse-workspace/pipeline/src/test.txt");
    // }
}