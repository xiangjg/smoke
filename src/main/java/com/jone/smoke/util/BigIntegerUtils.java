package com.jone.smoke.util;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class BigIntegerUtils {

    public static BigInteger sumRights(List<Integer> rights){
        BigInteger num = new BigInteger("0");
        for(int i=0; i<rights.size(); i++){
            num = num.setBit(rights.get(i));
        }
        return num;
    }

    public static boolean testRights(BigInteger sum,int targetRights){
        if(sum==null)
            sum = new BigInteger("0");
        return sum.testBit(targetRights);
    }

    public  static void main(String[] args){
        List<Integer> rights = new ArrayList<>();
//        for(int i=1;i<8;i++){
//            rights.add(i);
//        }
        rights.add(1);
        System.out.println(sumRights(rights));
    }
}
