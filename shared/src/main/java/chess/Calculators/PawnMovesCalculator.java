package chess.Calculators;

import chess.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final ChessGame.TeamColor color;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        this.board = board;
        this.myPosition = myPosition;
        this.color = color;
    }

    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> moveList = new ArrayList<ChessMove>();
        boolean startingPosition = false;
        boolean promotion = false;

        switch (this.color) {
            case BLACK: // Moving downward relative to board
                return pieceMovesBlack(moveList, startingPosition, promotion);
            case WHITE: // Moving upward relative to board
                return pieceMovesWhite(moveList, startingPosition, promotion);
        }
        return moveList;
    }

    public Collection<ChessMove> pieceMovesWhite(ArrayList<ChessMove> moveList, boolean startingPosition, boolean promotion) {
        ChessPosition positionToCheck;
        ChessPosition secondPositionToCheck;

        if (myPosition.getRow() == 2) {
            startingPosition = true;
        }
        else if (myPosition.getRow() == 7) {
            promotion = true;
        }
        // Check Moves
        // Normal
        positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
        if (startingPosition) {
            secondPositionToCheck = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
        }
        else {
            secondPositionToCheck = null;
        }
        moveList = addMovesNormal(positionToCheck, moveList, secondPositionToCheck, promotion);
        // Diagonal right
        positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
        moveList = addMovesEnemy(positionToCheck, moveList, promotion);
        // Diagonal left
        positionToCheck = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        moveList = addMovesEnemy(positionToCheck, moveList, promotion);
        return moveList;
    }

    public Collection<ChessMove> pieceMovesBlack(ArrayList<ChessMove> moveList, boolean startingPosition, boolean promotion) {
        ChessPosition positionToCheck;
        ChessPosition secondPositionToCheck;

        if (myPosition.getRow() == 7) {
            startingPosition = true;
        }
        else if (myPosition.getRow() == 2) {
            promotion = true;
        }
        // Check moves
        // Normal
        positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
        if (startingPosition) {
            secondPositionToCheck = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
        }
        else {
            secondPositionToCheck = null;
        }
        moveList = addMovesNormal(positionToCheck, moveList, secondPositionToCheck, promotion);
        // Diagonal right
        positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        moveList = addMovesEnemy(positionToCheck, moveList, promotion);
        // Diagonal left
        positionToCheck = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
        moveList = addMovesEnemy(positionToCheck, moveList, promotion);

        return moveList;
    }

    public ArrayList<ChessMove> addMovesNormal(ChessPosition positionToCheck, ArrayList<ChessMove> moveList, ChessPosition secondPositionToCheck, boolean promotion) {
        if (checkPositionOnBoard(positionToCheck)) {
            if (checkUnoccupation(positionToCheck)) {
                if (promotion) {
                    moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.KNIGHT));
                    moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.ROOK));
                    moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.QUEEN));
                    moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.BISHOP));
                } else {
                    moveList.add(new ChessMove(myPosition, positionToCheck, null));
                }
                if (secondPositionToCheck != null) {
                    if (checkUnoccupation(secondPositionToCheck)) {
                        moveList.add(new ChessMove(myPosition, secondPositionToCheck, null));
                    }
                }
            }
        }
        return moveList;
    }

    public ArrayList<ChessMove> addMovesEnemy(ChessPosition positionToCheck, ArrayList<ChessMove> moveList, boolean promotion) {
        if (checkPositionOnBoard(positionToCheck)) {
            if (!checkUnoccupation(positionToCheck)) {
                if (checkEnemyOccupation(positionToCheck)) {
                    if (promotion) {
                        moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.KNIGHT));
                        moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.ROOK));
                        moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.QUEEN));
                        moveList.add(new ChessMove(myPosition, positionToCheck, ChessPiece.PieceType.BISHOP));
                    } else {
                        moveList.add(new ChessMove(myPosition, positionToCheck, null));
                    }
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
