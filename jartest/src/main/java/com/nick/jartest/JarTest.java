package com.nick.jartest;

import android.util.Log;


public class JarTest {
    private static final String LOG = "LOG";

    public static final String HAHA = "haha";
    public int testPublic = 1;
    private int testPrivate = 2;

    private JarTest() {
    }

    /**
     * use Log.i to print, TAG = "LOG"
     *
     * @param str the content of the log
     */
    public static void printInfo(String str) {
        Log.i(LOG, str);
    }

    /**
     * return sum of two integers
     *
     * @param a input a
     * @param b input b
     * @return the sum of a and b
     */
    public static int sum(int a, int b) {
        return a + b;
    }

    public void test1() {
        System.out.println("test public method");
    }

    private void test2() {
        System.out.println("test private method");
    }
}
