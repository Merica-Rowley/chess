package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
        if (!legalMoves.contains(move)) {
            throw new InvalidMoveException("Illegal move: your king is in danger");
        }

        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.removePiece(move.getStartPosition());
        }
        // else clause only executes in the case of a pawn promotion
        else {
            board.addPiece(move.getEndPosition(),
                           new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                                          move.getPromotionPiece()));
            board.removePiece(move.getStartPosition());
        }
    }

    public boolean testMove(ChessMove move) {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
            board.removePiece(move.getStartPosition());
        }
        // else clause only executes in the case of a pawn promotion
        else {
            board.addPiece(move.getEndPosition(),
                    new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(),
                            move.getPromotionPiece()));
            board.removePiece(move.getStartPosition());
        }

        if (isInCheck(this.getTeamTurn())) {
            undoMove(move);
            return false;
        }
        else {
            undoMove(move);
            return true;
        }
    }

    public void undoMove(ChessMove move) {
        ChessMove backwardsMove = new ChessMove(move.getEndPosition(), move.getStartPosition(), null);
        if (move.getPromotionPiece() != null) { // In other words, the pawn was promoted, so we need to demote it
            board.addPiece(backwardsMove.getEndPosition(), new ChessPiece(board.getPiece(backwardsMove.getStartPosition()).getTeamColor(), ChessPiece.PieceType.PAWN));
            board.removePiece(backwardsMove.getStartPosition());
        }
        else {
            board.addPiece(backwardsMove.getEndPosition(), board.getPiece(backwardsMove.getStartPosition()));
            board.removePiece(backwardsMove.getStartPosition());
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i, j);
                if (!(board.getPiece(currentPosition) == null)){
                    ChessPiece currentPiece = board.getPiece(currentPosition);
                    if (currentPiece.getTeamColor() != teamColor){
                        Collection<ChessMove> possibleMoves = currentPiece.pieceMoves(board, currentPosition);
                        for (ChessMove move : possibleMoves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
//        if (isInCheck(teamColor)) {
//            Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
//            return legalMoves.isEmpty();
//        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
//        if (!isInCheck(teamColor)) {
//            Collection<ChessMove> legalMoves = validMoves(move.getStartPosition());
//            return legalMoves.isEmpty();
//        }
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    public ChessPosition getKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition positionToCheck = new ChessPosition(i, j);
                if (!(board.getPiece(positionToCheck) == null)){
                    if ((board.getPiece(positionToCheck).getPieceType() == ChessPiece.PieceType.KING)
                            && (board.getPiece(positionToCheck).getTeamColor() == teamColor)) {
                        return positionToCheck;
                    }
                }
            }
        }
        throw new RuntimeException("No king found");
    }

}
