package mips.Components;

import mips.Utils.Input;
import mips.Utils.Output;

public class ALU implements mips.Utils.Observer {
    public Input input1;
    public Input input2;
    public Input ALUop;
    public Output output;
    public Output SREGoutput;

    public ALU() {
        input1 = new Input(this, 8);
        input2 = new Input(this, 8);
        ALUop = new Input(this, 4);
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
        switch (ALUop.data) {
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
            // BEQZ
            case 3:
                // r2 "imm"
                System.out.println("data : " + data2 + " " + data1);
                res = data2 + 1;
                // change the zFlag to operate on the R1 but ouput R2 "imm"
                zFlag = (data1 == 0) ? 1 : 0;
                cFlag = 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // ANDI
            case 4:
                res = data1 & data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // XOR
            case 5:
                res = data1 ^ data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // SAL
            case 6:
                res = data1 << data2;
                res = res & 255;

                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // SAR
            case 7:
                res = data1 >> data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // MOV
            case 8:
                res = data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
                break;
            // BR
            case 9:
                res = (data1 << 6) | data2;
                res = res & 255;
                zFlag = (res == 0) ? 1 : 0;
                nFlag = 128 & res;
                nFlag = nFlag >> 7;
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

}
