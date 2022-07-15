package com.game.model.levels;

import com.game.model.cells.*;
import com.game.model.utils.Coordinate;
import com.game.model.cells.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that represents each level of the game.
 *
 * @author David García-Solórzano
 * @version 1.0
 */
public class Level {

    /**
     * Size of the board, i.e. size x size.
     */
    private int size;

    /**
     * Difficulty of the level
     */
    private LevelDifficulty difficulty;

    /**
     * Representation of the board.
     */
    private Cell[][] board;

    /**
     * Number of moves that the player has made so far.
     */
    private int numMoves = 0;

    /**
     * Minimum value that must be assigned to the attribute "size".
     */
    private static final int MINIMUM_BOARD_SIZE = 3;

    /**
     * Constructor
     *
     * @param fileName Name of the file that contains level's data.
     * @throws LevelException When there is any error while parsing the file.
     */
    public Level(String fileName) throws LevelException {
        setNumMoves(0);
        parse(fileName);
    }

    public int getSize() {
        return size;
    }

    private void setSize(int size) throws LevelException {
        if (size < 3) {
            throw new LevelException(LevelException.ERROR_BOARD_SIZE);
        }
        this.size = size;
    }

    public LevelDifficulty getDifficulty() {
        return difficulty;
    }

    private void setDifficulty(LevelDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getNumMoves() {
        return numMoves;
    }

    private void setNumMoves(int numMoves) {
        this.numMoves = numMoves;
    }

    /**
     * Parses/Reads level's data from the given file.<br/>
     * It also checks which the board's requirements are met.
     *
     * @param fileName Name of the file that contains level's data.
     * @throws LevelException When there is any error while parsing the file
     * or some board's requirement is not satisfied.
     */
    private void parse(String fileName) throws LevelException{
        boolean isStarting = false;
        boolean isFinish = false;
        String line;

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = Objects.requireNonNull(classLoader.getResourceAsStream(fileName));

        try(InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader reader = new BufferedReader(streamReader)){

            line = getFirstNonEmptyLine(reader);

            if (line  != null) {
                setSize(Integer.parseInt(line));
            }

            line = getFirstNonEmptyLine(reader);

            if (line != null) {
                setDifficulty(LevelDifficulty.valueOf(line));
            }

            board = new Cell[getSize()][getSize()];

            for (int row = 0; row < getSize(); row++) {
                char[] rowChar = Objects.requireNonNull(getFirstNonEmptyLine(reader)).toCharArray();
                for (int column = 0; column < getSize(); column++) {
                    board[row][column] = CellFactory.getCellInstance(row, column,
                            Objects.requireNonNull(CellType.map2CellType(rowChar[column])));
                }
            }

        }catch (IllegalArgumentException | IOException e){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        //Check if there is one starting cell, one finish cell and, at least, any other type of cell.
        for(var j =0; j<getSize(); j++){

            if(getCell(new Coordinate(getSize()-1,j)).getType() == CellType.START){
                isStarting = true;
            }

            if(getCell(new Coordinate(0,j)).getType() == CellType.FINISH){
                isFinish = true;
            }
        }

        //Checks if there are more than one starting cell
        if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getType() == CellType.START).count()>1){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        //Checks if there are more than one finish cell
        if(Stream.of(board).flatMap(Arrays::stream).filter(x -> x.getType() == CellType.FINISH).count()>1){
            throw new LevelException(LevelException.ERROR_PARSING_LEVEL_FILE);
        }

        if(!isStarting){
            throw new LevelException(LevelException.ERROR_NO_STARTING);
        }

        if(!isFinish){
            throw new LevelException(LevelException.ERROR_NO_FINISH);
        }

        //Checks if there is one road (i.e. movable or rotatable cell) at least.
        if(Stream.of(board).flatMap(Arrays::stream).noneMatch(x -> x.isMovable() || x.isRotatable())){
            throw new LevelException(LevelException.ERROR_NO_ROAD);
        }

    }

    /**
     * This a helper method for {@link #parse(String fileName)} which returns
     * the first non-empty and non-comment line from the reader.
     *
     * @param br BufferedReader object to read from.
     * @return First line that is a parsable line, or {@code null} there are no lines to read.
     * @throws IOException if the reader fails to read a line.
     */
    private String getFirstNonEmptyLine(final BufferedReader br) throws IOException {
        do {

            String s = br.readLine();

            if (s == null) {
                return null;
            }
            if (s.isBlank() || s.startsWith("#")) {
                continue;
            }

            return s;
        } while (true);
    }

    private boolean validatePosition(Coordinate coord) {
        return (coord != null) && (coord.getRow() >= 0) && (coord.getRow() < size)
                && (coord.getColumn() >= 0) && (coord.getColumn() < size);
    }

    public Cell getCell(Coordinate coord) throws LevelException {
        if (!validatePosition(coord)) {
            throw new LevelException(LevelException.ERROR_COORDINATE);
        }
        return board[coord.getRow()][coord.getColumn()];
    }

    private void setCell(Coordinate coord, Cell cell) throws LevelException {
        if (cell == null || !validatePosition(coord)) {
            throw new LevelException(LevelException.ERROR_COORDINATE);
        }
        board[coord.getRow()][coord.getColumn()] = cell;
    }

    public void swapCells(Coordinate firstCoord, Coordinate secondCoord) throws LevelException {
        if (!getCell(firstCoord).isMovable() || !getCell(secondCoord).isMovable()) {
            throw new LevelException(LevelException.ERROR_NO_MOVABLE_CELL);
        }
        Cell cellToMove = getCell(firstCoord);
        ((MovableCell) getCell(firstCoord)).move(secondCoord);
        ((MovableCell) getCell(secondCoord)).move(firstCoord);
        setCell(firstCoord, getCell(secondCoord));
        setCell(secondCoord, cellToMove);
        setNumMoves(getNumMoves() + 1);
    }

    public void rotateCell(Coordinate coord) throws LevelException {
        if (!getCell(coord).isRotatable()) {
            throw new LevelException(LevelException.ERROR_NO_ROTATABLE_CELL);
        }
        ((RotatableCell) getCell(coord)).rotate();
        setNumMoves(getNumMoves() + 1);
    }

    public boolean isSolved() throws LevelException {

        int columnToContinue;
        int rowToContinue;
        Coordinate coordinateToContinue;

        boolean solved = false;

        // First cell will always be start type. Left direction will not interfere as initial value with the checking.
        Direction direction = Direction.LEFT;
        Cell currentCell = null;
        EnumSet<Direction> availableConnections;

        //Find the start cell in the board
        //(It has been verified when loading that it exists, so it can't be null after assignment).
        for (int i = 0; i < getSize(); i++) {
            if (board[getSize() - 1][i].getType() == CellType.START) {
                currentCell = board[getSize() - 1][i];
                break;
            }
        }

        while (!solved) {

            availableConnections = currentCell.getType().getAvailableConnections();
            //Removes previous direction to avoid going back and gets the other one as new direction.
            availableConnections.remove(direction);
            direction = availableConnections.iterator().next();

            //calculates the coordinate of the piece where we can move in the selected direction.
            rowToContinue = currentCell.getCoordinate().getRow() + direction.getDRow();
            columnToContinue = currentCell.getCoordinate().getColumn() + direction.getDColumn();
            coordinateToContinue = new Coordinate(rowToContinue, columnToContinue);

            /*validates the coordinate and if it is valid checks if the opposite direction is available
            in the piece on this coordinate to move.If the cell we move on is the finish one the game is solved.*/
            if (validatePosition(coordinateToContinue)) {
                currentCell = getCell(coordinateToContinue);
                availableConnections = currentCell.getType().getAvailableConnections();
                    if (availableConnections.contains(direction.getOpposite())) {
                        direction = direction.getOpposite();
                        if (currentCell.getType() == CellType.FINISH) {
                            solved = true;
                        }
                    } else {
                        break;
                    }
            } else {
                break;
            }
        }

        return solved;
    }


    @Override
    public String toString() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= size; i++) {
            result.append(String.format("%d", i));
        }
        result.append(System.lineSeparator());
        for (int i = 0; i < size; i++) {
            result.append(alphabet[i]).append("|");
            for (int j = 0; j < size; j++) {
                result.append(board[i][j].toString());
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

}
