package ru.lysykh.concurrency.Runnable;

import java.util.stream.*;

public class MaxRunnable implements Runnable {

    private final int[] target;
    private int result;

    public MaxRunnable(final int[] target) {
        this.target = target;
    }

    @Override
    public void run() {
        result = IntStream.of(target).max().getAsInt();
//        int max=0;
//        for(int i=0; i<target.length;i++){
//            if(target[i]>max) max=target[i];
//        }
//        result = max;

    }

    public int getResult() {
        return result;
    }

}

