package chess.Calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    /**
     * Note: similar to the Knight implementation (just checking 8 spaces)
     */
    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPosition positionToCheck;

        // Right
        positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() + 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Upper Right
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() + 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Upper
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), myPosition.getColumn());
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Upper Left
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() - 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Left
        positionToCheck = new ChessPosition(myPosition.getRow(), (myPosition.getColumn() - 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Lower Left
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() - 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Lower
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), myPosition.getColumn());
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // Lower Right
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() + 1));
        if (checkToAddMove(positionToCheck)) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        return moveList;
    }

    /**
     * Returns true if the positionToCheck is on the board and is either empty or occupied by an enemy piece.
     */
    public boolean checkToAddMove(ChessPosition positionToCheck) {
        if (checkPositionOnBoard(positionToCheck)) {
            if (board.getPiece(positionToCheck) == null) {
                return true;
            }
            if (board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Takes a ChessPosition object and verifies if the position is within the 8 x 8 board.
     * Returns true if position is on the board.
     */
    public boolean checkPositionOnBoard(ChessPosition position) {
        return (position.getRow() <= 8) && (position.getRow() > 0) && (position.getColumn() <= 8) && (position.getColumn() > 0);
    }
}
