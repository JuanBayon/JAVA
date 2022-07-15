package com.game.model.cells;

import com.game.model.utils.Coordinate;

public class Cell {

    private Coordinate coordinate;
    private CellType type;

    public Cell(int row, int column, CellType type) {
        setCoordinate(row, column);
        setType(type);
    }

    public CellType getType() {
        return type;
    }

    protected void setType(CellType type) {
        this.type = type;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    protected void setCoordinate(int row, int column) {
        coordinate = new Coordinate(row, column);
    }

    public boolean isMovable() {
        return this instanceof Movable;
    }

    public boolean isRotatable() {
        return this instanceof Rotatable;
    }

    @Override
    public String toString() {
        return Character.toString(type.getUnicodeRepresentation());
    }

}
