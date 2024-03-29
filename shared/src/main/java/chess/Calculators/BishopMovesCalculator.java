package chess.Calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPosition positionToCheck;

        boolean arm1 = true; // Diagonal in quadrant 1
        boolean arm2 = true; // Diagonal in quadrant 2
        boolean arm3 = true; // Diagonal in quadrant 3
        boolean arm4 = true; // Diagonal in quadrant 4

        int rowInc = 0;
        int colInc = 0;

        while (arm1 || arm2 || arm3 || arm4) {
            rowInc++;
            colInc++;

            if (arm1){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm1 = false;
                    } else {
                        arm1 = false;
                    }
                } else {
                    arm1 = false;
                }
            }

            if (arm2){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm2 = false;
                    } else {
                        arm2 = false;
                    }
                } else {
                    arm2 = false;
                }
            }

            if (arm3){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm3 = false;
                    } else {
                        arm3 = false;
                    }
                } else {
                    arm3 = false;
                }
            }

            if (arm4){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm4 = false;
                    } else {
                        arm4 = false;
                    }
                } else {
                    arm4 = false;
                }
            }
        }

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
