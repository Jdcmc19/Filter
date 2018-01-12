package sample;

import javafx.beans.property.SimpleIntegerProperty;

public class Combinacion {
    private final SimpleIntegerProperty n0;
    private final SimpleIntegerProperty n1;
    private final SimpleIntegerProperty n2;
    private final SimpleIntegerProperty n3;
    private final SimpleIntegerProperty n4;

    public Combinacion(int nn0,int nn1,int nn2,int nn3,int nn4){
        super();
        n0= new SimpleIntegerProperty(nn0);
        n1= new SimpleIntegerProperty(nn1);
        n2= new SimpleIntegerProperty(nn2);
        n3= new SimpleIntegerProperty(nn3);
        n4= new SimpleIntegerProperty(nn4);
    }

    public boolean pares(){return (n0.get()%2==0 && n1.get()%2==0 && n2.get()%2==0 && n3.get()%2==0 && n4.get()%2==0)?true:false;}
    public boolean impares(){return (n0.get()%2!=0 && n1.get()%2!=0 && n2.get()%2!=0 && n3.get()%2!=0 && n4.get()%2!=0)?true:false;}
    public boolean consecutivos(){return (n0.get()+1==n1.get() && n1.get()+1==n2.get() ||
            n1.get()+1==n2.get() && n2.get()+1==n3.get() ||
            n2.get()+1==n3.get() && n3.get()+1==n4.get())?true:false;}
    public boolean consecutivos2(){
        for(int i = 2; i<10; i++){
            if(n0.get()+i==n1.get() && n1.get()+i==n2.get() && n2.get()+i==n3.get() && n3.get()+i==n4.get()){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return n0.get() + ", " + n1.get() + ", " + n2.get() + ", " + n3.get() + ", " + n4.get();
    }
    public int hash(){
        String s = n0.get() + "" + n1.get() + "" + n2.get() + "" + n3.get() + "" + n4.get();
        return s.hashCode();
    }
    public int getN0() {
        return n0.get();
    }

    public int getN1() {
        return n1.get();
    }

    public int getN2() {
        return n2.get();
    }

    public int getN3() {
        return n3.get();
    }

    public int getN4() {
        return n4.get();
    }

}
