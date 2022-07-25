package com.test.SimplePFC;

public class Flag {
    private static  boolean finishing;
    public static boolean getFinishing(){
        return finishing;
    }
    public static void setFinishing(boolean flag){
        finishing = flag;
    }
}
