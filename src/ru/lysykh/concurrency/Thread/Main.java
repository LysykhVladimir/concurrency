package ru.lysykh.concurrency.Thread;

import java.util.Arrays;

public class Main {

    public static void main(String... args) throws Exception {
        final int[] test1 = {1, 2, 3};
        final int expectedResult1 = 6;
        final int[] test2 = {4, 5, 6};
        final int expectedResult2 = 15;
        validateSummThread(test1, expectedResult1);
        validateSummThread(test2, expectedResult2);
    }

    private static void validateSummThread(
            final int[] input,
            final int expectedResult) throws Exception {

        final SummThread t = new SummThread(input);
        t.start();
        t.join();

        final int actualResult = t.getResult();
        if (actualResult != expectedResult) {
            throw new RuntimeException(String.format("SummThread calculates" +
                            "incorrect summ for the input: %s," +
                            " expected result: %d, actual" +
                            " result: %d",
                    Arrays.toString(input),
                    expectedResult,
                    actualResult));
        } else {
            System.out.println(actualResult);
        }
    }

}