package mips.Components;

import mips.Utils.Input;
import mips.Utils.Output;

public class ALU implements mips.Utils.Observer {
    Input input1;
    Input input2;
    Input Opcode;
    Output output;
    Output SREGoutput;

    public ALU() {
        input1 = new Input(this, 8);
        input2 = new Input(this, 8);
        Opcode = new Input(this, 3);
        output = new Output(8);
        SREGoutput = new Output(8);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(int data) {
        int res = 0;
        int data1 = (input1.data >= 128) ? input1.data - 256 : input1.data;
        int data2 = (input2.data >= 128) ? input2.data - 256 : input2.data;
        int SREG = 0;
        int cFlag = 0;
        int vFlag = 0;
        int nFlag = 0;
        int sFlag = 0;
        int zFlag = 0;
        switch (Opcode.data) {
            // Add
            case 0:
                res = data1 + data2;
                cFlag = (res > 127 || res <= -128) ? 1 : 0;
                res = res & 255;
                vFlag = ((data1 > 0 && data2 > 0) && res > 127 || (data1 < 0 && data2 < 0) && res < 128) ? 1 : 0;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                sFlag = nFlag ^ vFlag;
                break;
            // sub
            case 1:
                res = data1 - data2;
                cFlag = (res > 127 || res <= -128) ? 1 : 0;
                res = res & 255;
                vFlag = ((data1 > 0 && data2 < 0) && res > 127 || (data1 < 0 && data2 > 0) && res < 128) ? 1 : 0;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                sFlag = nFlag ^ vFlag;
                break;
            // mul
            case 2:
                res = data1 * data2;
                cFlag = (res > 127 || res <= -128) ? 1 : 0;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // ANDI
            case 3:
                res = data1 & data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // XOR
            case 4:
                res = data1 ^ data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // SAR
            case 5:
                res = data1 << data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // SAL
            case 6:
                res = data1 >> data2;
                res = res & 255;
                System.out.println(data1 + " " + data2);
                System.out.println(res);
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            default:
                break;
        }
        cFlag = cFlag & 1;
        zFlag = zFlag & 1;
        sFlag = sFlag & 1;
        vFlag = vFlag & 1;
        SREG = cFlag << 4 | vFlag << 3 | nFlag << 2 | sFlag << 1 | zFlag;
        output.load(res);
        SREGoutput.load(SREG);
    }

    public static void main(String[] args) {
        ALU alu = new ALU();
        alu.input1.update(128);
        alu.input2.update(7);
        alu.Opcode.update(6);
        String res = Integer.toBinaryString(alu.output.data);
        while (res.length() < 8) {
            res = "0" + res;
        }
        System.out.println(res);
        String SREG = Integer.toBinaryString(alu.SREGoutput.data);
        while (SREG.length() < 8) {
            SREG = "0" + SREG;
        }
        System.out.println(SREG);
    }

}
