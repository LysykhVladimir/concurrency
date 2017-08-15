package ru.lysykh.concurrency.GraphBuilder.ForkJoin;

import java.util.Arrays;

public class GoField {

    public final static int FIELD_SIZE = 3;

    public final Figure[][] figures =  new Figure[FIELD_SIZE][FIELD_SIZE];

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GoField goField = (GoField) o;

        return Arrays.deepEquals(figures, goField.figures);

    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(figures);
    }
}
