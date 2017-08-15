package ru.lysykh.concurrency.GraphBuilder.ForkJoin;

import java.util.Set;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ForkJoinPool forkJoinPool= new ForkJoinPool();
        final GraphBuilder gb = new GraphBuilder(Figure.WHITE, new GoField(), 0);
        final ForkJoinTask<Set<GoField>> rootTask = gb.fork();

        try {
            final Set<GoField> fields = rootTask.get(4000, TimeUnit.SECONDS);
            if (fields.size() != 126) {
                throw new RuntimeException("The amount of unique fields is not correct... fields count="+fields.size());
            } else
                System.out.println("All done!");
        } catch (TimeoutException e) {
            e.printStackTrace();
            throw new RuntimeException("Task was not finished withing 4 seconds!");
        } finally {
            forkJoinPool.shutdownNow();
        }
    }

}
