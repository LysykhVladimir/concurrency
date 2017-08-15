package ru.lysykh.concurrency.GraphBuilder.Callable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class GraphBuilder implements Callable<Set<GoField>> {

    private final ExecutorService executorService;

    private final Figure nextFigure;

    private final GoField currentField;

    private final int deepLevel;

    public GraphBuilder(
            final ExecutorService executorService,
            final Figure currentFigure,
            final GoField currentField,
            final int deepLevel) {
        this.executorService = executorService;
        this.currentField = currentField;
        this.nextFigure = currentFigure == Figure.WHITE ? Figure.BLACK : Figure.WHITE;
        this.deepLevel = deepLevel;
    }

    @Override
    public Set<GoField> call() throws Exception {
        if (isCurrentFieldFinal()) {
            return new HashSet<GoField>(){{add(currentField);}};
        }
        final List<Future<Set<GoField>>> futures = new ArrayList<>();
        final Set<GoField> result = new HashSet<>();
        for (int y = 0; y < GoField.FIELD_SIZE; y++) {
            for (int x = 0; x < GoField.FIELD_SIZE; x++) {
                if (currentField.figures[x][y] != null) {
                    continue;
                }
                final GoField newField = new GoField();
                for (int i = 0; i < GoField.FIELD_SIZE; i++) {
                    for (int j = 0; j < GoField.FIELD_SIZE; j++) {
                        newField.figures[i][j] = currentField.figures[i][j];
                    }
                }
                newField.figures[x][y] = nextFigure;
                final GraphBuilder graphBuilder
                        = new GraphBuilder(
                        executorService,
                        nextFigure,
                        newField,
                        deepLevel + 1);
                if (isAsync()) {
                    final Future<Set<GoField>> future
                            = executorService.submit(graphBuilder);
                    futures.add(future);
                } else {
                    result.addAll(graphBuilder.call());
                }
            }
        }

        if (!futures.isEmpty()) {
            for (Future<Set<GoField>> future : futures) {
                try {
                    result.addAll(future.get());
                } catch (final InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return result;
    }
    private boolean isAsync() {
        if (deepLevel > 1) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isCurrentFieldFinal() {
        for (Figure[] line : currentField.figures) {
            for (Figure f : line) {
                if (f == null) {
                    return false;
                }
            }
        }
        return true;
    }
}