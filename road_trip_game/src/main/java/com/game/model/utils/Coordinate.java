package com.game.model.utils;

import java.util.Objects;

public class Coordinate {

    private int row;
    private int column;

    public Coordinate(int row, int column) {
        setRow(row);
        setColumn(column);
    }

    public int getRow() {
        return row;
    }

    private void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    private void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Coordinate) && (((Coordinate) obj).getColumn() == column)
                && (((Coordinate) obj).getRow() == row);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", row, column);
    }

}
