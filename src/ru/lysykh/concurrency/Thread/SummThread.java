package ru.lysykh.concurrency.Thread;

import java.util.stream.*;

public class SummThread extends Thread {
    private final int[] toSumm;
    int result = 0;

    public SummThread (final int[] toSumm){
        this.toSumm = toSumm;
    }

    @Override public void run(){
    result = IntStream.of(toSumm).sum();
        /*for (int i=0; i<toSumm.length; i++) {
            result = result + toSumm[i];
        }*/
    }

    public int getResult(){
        return result;
    }
}
