package com.game.model.levels;

public class LevelException extends Exception {

    public static final String ERROR_PARSING_LEVEL_FILE;
    public static final String ERROR_BOARD_SIZE;
    public static final String ERROR_COORDINATE;
    public static final String ERROR_NO_STARTING;
    public static final String ERROR_NO_FINISH;
    public static final String ERROR_NO_ROAD;
    public static final String ERROR_NO_MOVABLE_CELL;
    public static final String ERROR_NO_ROTATABLE_CELL;

    static {
        ERROR_PARSING_LEVEL_FILE = "[ERROR] There was an error while loading the current level file!!";
        ERROR_BOARD_SIZE = "[ERROR] Board's size must be greater than 2!!";
        ERROR_COORDINATE = "[ERROR] This coordinate is incorrect!!";
        ERROR_NO_STARTING = "[ERROR] This level does not have any starting cell!!";
        ERROR_NO_FINISH = "[ERROR] This level does not have any finish cell!!";
        ERROR_NO_ROAD = "[ERROR] This level does not have any road!!";
        ERROR_NO_MOVABLE_CELL = "[ERROR] You have chosen a static cell!!";
        ERROR_NO_ROTATABLE_CELL = "[ERROR] You have chosen a non-rotatable cell!!";
    }

    public LevelException(String msg) {
        super(msg);
    }
}
