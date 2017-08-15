package ru.lysykh.concurrency.fibCalculator.thread;

public class ThreadHelper {

    public static int fib(final int numberToCalculate) throws Exception {
        final FibCalculator calculator = new FibCalculator(numberToCalculate);
        calculator.start();
        calculator.join();
        return calculator.getResult();
    }

    private static class FibCalculator extends Thread {
        private final int currentNum;

        private int result;

        public FibCalculator(final int numberToCalculate) {
            this.currentNum = numberToCalculate;
        }

        @Override
        public void run() {
            if (currentNum == 1 || currentNum == 2) {
                result = 1;
                return;
            }
            final FibCalculator left = new FibCalculator(currentNum - 1);
            final FibCalculator right = new FibCalculator(currentNum - 2);
            left.start();
            right.start();
            try {
                left.join();
                right.join();
            } catch (final InterruptedException e) {}
            result = left.getResult() + right.getResult();
        }

        public int getResult() {
            return result;
        }
    }

}