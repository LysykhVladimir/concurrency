package ru.lysykh.concurrency.fibCalculator.withExecutorService;

import java.util.concurrent.*;

public class ThreadHelper {

    public static final ExecutorService EXECUTOR_SERVICE = Executors.newWorkStealingPool(3);


    public static int fib(final int numberToCalculate) throws Exception {
        final FibCalculator calc = new FibCalculator(numberToCalculate);
        final Future f = EXECUTOR_SERVICE.submit(calc);
        f.get();
        return calc.getResult();
    }

    private static class FibCalculator implements Runnable {

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
            final Future leftF =  EXECUTOR_SERVICE.submit(left);
            final Future rightF =  EXECUTOR_SERVICE.submit(right);
            try {
                leftF.get();
                rightF.get();
            } catch (final InterruptedException | ExecutionException e) {}
            result = left.getResult() + right.getResult();
        }

        public int getResult() {
            return result;
        }

    }

}