package sa.ssSA;

import java.io.Serializable;

public class SolutionStructure implements Serializable {
    private byte [] items;
    private int L;

    public SolutionStructure(int lenght){
        L = lenght;
        items = new byte[L];
        for (int i = 0; i < L; i++){
            items[i] = (byte) (Exe.rand.nextDouble() > 0.5 ? 1 : 0);
        }
    }

    public void set_item(int index, byte value)
    {
        items[index] = value;
    }

    public byte get_item(int index)
    {
        return items[index];
    }

    public void print()
    {
        for(int i=0; i<L; i++) {
            System.out.print(items[i]);
        }
    }
}
