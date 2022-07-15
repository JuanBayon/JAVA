package com.game.model.cells;

public class RotatableCell extends Cell implements Rotatable {

    public RotatableCell(int row, int column, CellType type) {
        super(row, column, type);
    }

    @Override
    public boolean isMovable() {
        return false;
    }

    @Override
    public boolean isRotatable() {
        return true;
    }

    @Override
    public void rotate() {
        setType(getType().next());
    }

}
