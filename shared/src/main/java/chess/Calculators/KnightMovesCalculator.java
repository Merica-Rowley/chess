package chess.Calculators;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        ChessPosition positionToCheck;

        // 1. Position (row + 2, col + 1)
        positionToCheck = new ChessPosition((myPosition.getRow() + 2), (myPosition.getColumn() + 1));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 2. Position (row + 1, col + 2)
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() + 2));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 3. Position (row - 1, col + 2)
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() + 2));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 4. Position (row - 2, col + 1)
        positionToCheck = new ChessPosition((myPosition.getRow() - 2), (myPosition.getColumn() + 1));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 5. Position (row - 2, row - 1)
        positionToCheck = new ChessPosition((myPosition.getRow() - 2), (myPosition.getColumn() - 1));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 6. Position (row - 1, col - 2)
        positionToCheck = new ChessPosition((myPosition.getRow() - 1), (myPosition.getColumn() - 2));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 7. Position (row + 1, col - 2)
        positionToCheck = new ChessPosition((myPosition.getRow() + 1), (myPosition.getColumn() - 2));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
        }

        // 8. Position (row + 2, col - 1)
        positionToCheck = new ChessPosition((myPosition.getRow() + 2), (myPosition.getColumn() - 1));
        if (checkPositionOnBoard(positionToCheck) && ((board.getPiece(positionToCheck) == null) || board.getPiece(positionToCheck).getTeamColor() != board.getPiece(myPosition).getTeamColor())) {
            moveList.add(new ChessMove(myPosition, positionToCheck, null));
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
