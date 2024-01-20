package chess;

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

            if (arm5){
                positionToCheck = new ChessPosition((myPosition.getRow() + rowInc), myPosition.getColumn());
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm5 = false;
                    } else {
                        arm5 = false;
                    }
                } else {
                    arm5 = false;
                }
            }

            if (arm6){
                positionToCheck = new ChessPosition((myPosition.getRow() - rowInc), myPosition.getColumn());
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm6 = false;
                    } else {
                        arm6 = false;
                    }
                } else {
                    arm6 = false;
                }
            }

            if (arm7){
                positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() + colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm7 = false;
                    } else {
                        arm7 = false;
                    }
                } else {
                    arm7 = false;
                }
            }

            if (arm8){
                positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() - colInc));
                if (checkPositionOnBoard(positionToCheck)) {
                    if (checkUnoccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    } else if (checkEnemyOccupation(positionToCheck)) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                        arm8 = false;
                    } else {
                        arm8 = false;
                    }
                } else {
                    arm8 = false;
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
