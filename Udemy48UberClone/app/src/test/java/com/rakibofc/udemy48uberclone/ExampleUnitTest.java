package com.rakibofc.udemy48uberclone;

import org.junit.Test;

public class ExampleUnitTest {

    String line = "------------------------------------------------------------";

    @Test
    public void addition_isCorrect() {

        System.out.println(line);

        String latLanStr = "24.12321321,30.23123444444443";
        String[] latLan = latLanStr.split(",");

        System.out.println(latLan[0]);
        System.out.println(latLan[1]);

        System.out.println(line);
    }
}