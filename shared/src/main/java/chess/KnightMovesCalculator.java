package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPosition positionToCheck;

        // 1. Position (2,1)
        positionToCheck = new ChessPosition((myPosition.getRow() + 2), myPosition.getColumn() + 1);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 2. Position (1,2)
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), myPosition.getColumn() + 2);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 3. Position (-1,2)
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), myPosition.getColumn() + 2);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 4. Position (-2,1)
        positionToCheck = new ChessPosition((myPosition.getRow() - 2), myPosition.getColumn() + 1);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 5. Position (-2,-1)
        positionToCheck = new ChessPosition((myPosition.getRow() - 2), myPosition.getColumn() - 1);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 6. Position (-1,-2)
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), myPosition.getColumn() - 2);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 7. Position (1,-2)
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), myPosition.getColumn() - 2);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }
        // 8. Position (2,-1)
        positionToCheck = new ChessPosition((myPosition.getRow() + 2), myPosition.getColumn() - 1);
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) != null) {
                if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
            }
            else {
                moveList.add(new ChessMove(myPosition, positionToCheck, null));
            }
        }

        return moveList;
    }

    /**
     * Takes a ChessPosition object and verifies if the position is within the 8 x 8 board.
     * Returns true if position is on the board.
     */
    public boolean checkPositionOnBoard(ChessPosition position) {
        return (position.getRow() <= 8) && (position.getRow() > 0) && (position.getColumn() <= 8) && (position.getColumn() > 0);
    }
}
