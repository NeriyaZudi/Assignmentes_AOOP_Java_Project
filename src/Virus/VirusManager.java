package Virus;

import java.util.Random;

public class VirusManager {

    public enum Variants {
        ChineseVariant(new ChineseVariant(),"Chinese Variant"),
        BritishVariant(new BritishVariant(),"British Variant"),
        SouthAfricanVariant(new SouthAfricanVariant(), "SouthAfricanVariant");
        private final String type;
        private final IVirus virus;

        Variants(IVirus virus,String type){
            this.virus=virus;
            this.type=type;

        }
        public Variants[] getVariants(){
            return Variants.values();
        }
        public static int findVirus(IVirus v) {
            for (int i = 0; i < Variants.values().length; i++) {
                if (Variants.values()[i].virus.isEqual(v) )
                    return i;

            }
            return -1;

        }
        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return this.type;
        }
        public IVirus getVirus()
        {
            return this.virus;
        }


    }

    private static VirusManager virusManager=null;
    private static Boolean [][] data;

    //Boolean table initialization for virus development
    static {
        data=new Boolean[Variants.values().length][];
        for(int i=0;i<data.length;i++)
        {
            data[i]=new Boolean[Variants.values().length];
            for(int j=0;j<data.length;j++)
            {
                data[i][j]= i == j;
            }
        }
    }

    //method for changing a value in a cell in a table
    public static void Change(int i, int j)
    {
        data[i][j]=!data[i][j];
    }

    //method that receives a source virus and returns a random virus to which it can develop
    public static IVirus randomContagion(IVirus virus)
    {
        int index = Variants.findVirus(virus);
        if(index==-1) {
            return null;
        }
        IVirus v=findMutation(data[index]);
        return v;
    }
    //Method for finding a random virus for a particular virus
    public static IVirus findMutation(Boolean[] data)
    {
        int size=0;
        int[] indexes=null;
        //Counting the amount of variants to which a virus develops
        for (Boolean datum : data) {
            if (datum) {
                size++;
            }
        }
        //An array of indexes for developments
        indexes=new int[size];
        int j=0;
        for(int i=0;i< data.length;i++)
        {
            if(data[i])
            {
                indexes[j]=i;
                j++;
            }
        }
        if(size==0)
            return null;
        Random r=new Random();
        int x=r.nextInt(size);
        //Random virus return
        return Variants.values()[indexes[x]].getVirus();

    }

    //************** getters **************
    public static boolean getValue(int i, int j)
    {
        return data[i][j];
    }

    public static Boolean[][] getData() {
        return data;
    }

}
