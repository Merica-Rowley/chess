package chess.Calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    /**
     * Note: this basically combines the Bishop and Rook move calculator methods.
     */
    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPosition positionToCheck;

        boolean arm1 = true; // Diagonal in quadrant 1
        boolean arm2 = true; // Diagonal in quadrant 2
        boolean arm3 = true; // Diagonal in quadrant 3
        boolean arm4 = true; // Diagonal in quadrant 4
        boolean arm5 = true; // Positive vertical
        boolean arm6 = true; // Negative vertical
        boolean arm7 = true; // Positive horizontal
        boolean arm8 = true; // Negative horizontal

        int rowInc = 0;
        int colInc = 0;

        while (arm1 || arm2 || arm3 || arm4 || arm5 || arm6 || arm7 || arm8) {
            rowInc++;
            colInc++;

            if (arm1){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm1 = setArmBool(positionToCheck);
            }

            if (arm2){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm2 = setArmBool(positionToCheck);
            }

            if (arm3){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm3 = setArmBool(positionToCheck);
            }

            if (arm4){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm4 = setArmBool(positionToCheck);
            }

            if (arm5){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), myPosition.getColumn());
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm5 = setArmBool(positionToCheck);
            }

            if (arm6){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), myPosition.getColumn());
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm6 = setArmBool(positionToCheck);
            }

            if (arm7){
                positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm7 = setArmBool(positionToCheck);
            }

            if (arm8){
                positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)){
                    moveList = addMoves(positionToCheck, moveList);
                }
                arm8 = setArmBool(positionToCheck);
            }
        }

        return moveList;
    }

    public boolean setArmBool(ChessPosition positionToCheck) {
        return checkPositionOnBoard(positionToCheck) && checkUnoccupation(positionToCheck);
    }

    public ArrayList<ChessMove> addMoves(ChessPosition positionToAdd, ArrayList<ChessMove> moveList) {
        if (checkUnoccupation(positionToAdd) || checkEnemyOccupation(positionToAdd)) moveList.add(new ChessMove(myPosition, positionToAdd, null));
        return moveList;
    }

    /**
     * Returns true if the positionToCheck is unoccupied.
     */
    public boolean checkUnoccupation(ChessPosition positionToCheck) {
        return board.getPiece(positionToCheck) == null;
    }

    /**
     * Returns true if the positionToCheck is occupied by an enemy piece.
     */
    public boolean checkEnemyOccupation(ChessPosition positionToCheck) {
        return board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor();
    }

    /**
     * Takes a ChessPosition object and verifies if the position is within the 8 x 8 board.
     * Returns true if position is on the board.
     */
    public boolean checkPositionOnBoard(ChessPosition position) {
        return (position.getRow() <= 8) && (position.getRow() > 0) && (position.getColumn() <= 8) && (position.getColumn() > 0);
    }
}
