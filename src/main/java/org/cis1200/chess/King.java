package org.cis1200.chess;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    boolean isWhite;
    ArrayList<LineOfSight> checkedBy = new ArrayList<>();

    State state;
    Board board;

    public King(int y, int x, boolean white, Board b, State s) {
        super(white ? "files/whiteking.png" : "files/blackking.png", x, y, white, s);
        rank = y;
        file = x;
        hasMoved = false;
        isWhite = white;
        board = b;
        state = s;
        linesOfSight = new ArrayList<>();
    }

    public void recieveCheck(LineOfSight line) {
        checkedBy.add(line);
    }

    public String getSymbol() {
        String color = isWhite ? "W" : "B";
        return color + "K";
    }

    public List<int[]> getAttacked() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (LineOfSight line : linesOfSight) {
            moves.addAll(line.getProtectedPieces());

        }
        return moves;
    }

    public List<int[]> getValidMoves() {
        ArrayList<int[]> validMoves = new ArrayList<>();
        for (LineOfSight line : linesOfSight) {
            for (int[] move : line.getValidMoves()) {
                if ((board.getPiece(move[0], move[1]).isWhite != isWhite
                        || board.getPiece(move[0], move[1]) instanceof EmptyPiece)
                        && !board.getPiece(move[0], move[1]).isProtected(!isWhite)) {
                    validMoves.add(move);
                }

            }
        }
        if (state.canCastleKingSide(isWhite) && state.checks == 0) {
            validMoves.add(new int[] { rank, file + 2 });
        }
        if (state.canCastleQueenSide(isWhite) && state.checks == 0) {
            validMoves.add(new int[] { rank, file - 2 });
        }
        return validMoves;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void update() {
        linesOfSight = new ArrayList<>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (rank + i < 0 || rank + i > 7 || file + j < 0 || file + j > 7) {
                    continue;
                }
                ArrayList<int[]> checkLineOfSight = new ArrayList<>();
                checkLineOfSight.add(new int[] { rank + i, file + j });
                linesOfSight.add(new LineOfSight(checkLineOfSight, board, this));
            }
        }
    }

    public void setProtectingKingFrom(LineOfSight lineOfSight) {
        return;
    }

    @Override
    public void castle() {

    }

    public boolean move(int newY, int newX) {
        if (arrayContains(getValidMoves(), new int[] { newY, newX })) {
            int oldFile = file;
            file = newX;
            rank = newY;
            hasMoved = true;
            if (newX - oldFile == 2) {
                board.castleKingSide();
            } else if (newX - oldFile == -2) {
                board.castleQueenSide();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrapped() {
        for (int[] move : getAttacked()) {
            Piece piece = board.getPiece(move[0], move[1]);
            if (piece == null || (piece.isWhite != isWhite && !piece.isProtected(!isWhite))) {
                return false;
            }
        }
        return true;
    }

    public void castle(boolean left) {
        if (left) {
            file = 2;
        } else {
            file = 6;
        }
    }

    public int getPoints() {
        return 0;
    }

}
