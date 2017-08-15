package ru.lysykh.concurrency.GraphBuilder.ForkJoin;

import java.util.*;
import java.util.concurrent.*;

public class GraphBuilder extends RecursiveTask<Set<GoField>> {

    private final Figure nextFigure;

    private final Figure currentFigure;

    private final GoField currentField;

    private final int deepLevel;

    public GraphBuilder(
            final Figure currentFigure,
            final GoField currentField,
            final int deepLevel) {
        this.currentField = currentField;
        this.nextFigure = currentFigure == Figure.WHITE ? Figure.BLACK : Figure.WHITE;
        this.deepLevel = deepLevel;
        this.currentFigure = currentFigure;
    }

    @Override
    public Set<GoField> compute() {
        // BEGIN (write your solution here)
        final List<ForkJoinTask<Set<GoField>>> tasks=new ArrayList<>();
        final Set<GoField> finalFieldStates= new HashSet<>();
        if (isCurrentFieldFinal()){
            finalFieldStates.add(currentField);
        }
        for (int y = 0; y < GoField.FIELD_SIZE; y++) {
            for (int x = 0; x < GoField.FIELD_SIZE; x++) {
                if (!hasMove(x,y)){
                    continue;
                }
                final GoField newField=new GoField();
                setNextState(newField,x,y);

                final GraphBuilder graphBuilder= new GraphBuilder(
                        nextFigure
                        ,newField
                        ,deepLevel+1);

                if (isAsync()){
                    tasks.add(graphBuilder.fork());
                } else{
                    finalFieldStates.addAll(graphBuilder.compute());
                }
            }
        }

        if (!tasks.isEmpty()){
            for (ForkJoinTask<Set<GoField>> task:
                    tasks){
                finalFieldStates.addAll(task.join());
            }
        }
        return finalFieldStates;
        // END
    }

    private void setNextState(GoField field, int x, int y) {
        for (int i = 0; i < GoField.FIELD_SIZE; i++) {
            field.figures[i]= Arrays.copyOf(currentField.figures[i], GoField.FIELD_SIZE);
        }
        field.figures[x][y]=nextFigure;
    }

    private boolean hasMove(final int x, final int y) {
        if (currentField.figures[x][y]!=null)
            return false;
        return true;
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