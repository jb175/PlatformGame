package com.jbmo60927;

public class BbbTest{
    public static void main(String[] args) {
        int i = 0;
        int max = 5;
        for (int j = 0; j < 20; j++) {
            i--;
            i%=max;
            System.out.println(i);
        }
    }
}