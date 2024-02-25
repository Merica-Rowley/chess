package chess.Calculators;

import chess.ChessMove;

import java.util.Collection;

public interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves();
}
