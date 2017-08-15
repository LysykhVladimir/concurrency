package ru.lysykh.concurrency.GraphBuilder.Callable;

import java.util.Set;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newWorkStealingPool(3);
        final GraphBuilder gb = new GraphBuilder(executorService, Figure.WHITE, new GoField(), 0);
        final Future<Set<GoField>> future = executorService.submit(gb);
        try {
            final Set<GoField> fields = future.get(4, TimeUnit.SECONDS);
            if (fields.size() != 126) {
                throw new RuntimeException("The amount of unique fields is not correct");
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException("Task was not finished withing 4 seconds!");
        } finally {
            executorService.shutdownNow();
        }
    }

}
