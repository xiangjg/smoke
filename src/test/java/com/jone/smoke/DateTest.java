package com.jone.smoke;

import org.junit.Test;

import java.text.SimpleDateFormat;

public class DateTest
{
    @Test
    public void test(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            System.out.println(sdf.format(sdf.parse("2018-01-12")));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
