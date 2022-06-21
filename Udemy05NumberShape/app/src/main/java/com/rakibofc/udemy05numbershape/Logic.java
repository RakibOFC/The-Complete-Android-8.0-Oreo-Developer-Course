package com.rakibofc.udemy05numbershape;

public class Logic {

    public int number;

    public boolean isTriangular(){

        int genNum = 0;

        for(int i = 0; genNum < number; i++){

            genNum += i;

            if(number == genNum){

                return true;
            }
        }
        return false;
    }

    public boolean isSquare(){

        double tempNumber = Math.sqrt(number);

        if (tempNumber == Math.floor(tempNumber)){

            return true;

        } else {

            return false;

        }
    }
}
