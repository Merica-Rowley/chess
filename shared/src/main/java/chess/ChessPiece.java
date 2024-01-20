package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceTeamColor;
    private ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        pieceTeamColor = pieceColor;
        pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceTeamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> movesArray = new ArrayList<>();
        Collection<ChessMove> possibleMoves = null;

        switch (pieceType) {
            case KING:
                break;
            case QUEEN:
                break;
            case BISHOP:
                BishopMovesCalculator bishopCalculator = new BishopMovesCalculator(board, myPosition);
                possibleMoves = bishopCalculator.pieceMoves();
                break;
            case KNIGHT:
                KnightMovesCalculator knightCalculator = new KnightMovesCalculator(board, myPosition);
                possibleMoves = knightCalculator.pieceMoves();
                break;
            case ROOK:
                RookMovesCalculator rookCalculator = new RookMovesCalculator(board, myPosition);
                possibleMoves = rookCalculator.pieceMoves();
                break;
            case PAWN:
                break;
        }

        // return movesArray;
        return possibleMoves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceTeamColor == that.pieceTeamColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceTeamColor, pieceType);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceTeamColor=" + pieceTeamColor +
                ", pieceType=" + pieceType +
                '}';
    }
}
