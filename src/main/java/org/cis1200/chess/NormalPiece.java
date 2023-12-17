package org.cis1200.chess;

import java.util.ArrayList;
import java.util.List;

public abstract class NormalPiece extends Piece {
    boolean hasMoved;
    boolean isWhite;
    int file;
    int rank;
    boolean isProtected;
    boolean isThreatened;
    State state;
    Board board;


    ArrayList<LineOfSight> linesOfSight = new ArrayList<>();

    LineOfSight protectingKingFrom;

    public NormalPiece(String imgFile, int x, int y, boolean white, Board b, State s) {
        super(imgFile, x, y, white, s);
        file = x;
        rank = y;
        hasMoved = false;
        isWhite = white;
        board = b;
        state = s;
    }

    public abstract String getSymbol();
    public boolean hasMoved() {
        return hasMoved;
    }

    public boolean move(int newY, int newX) {
        if (arrayContains(getValidMoves(), new int[] { newY, newX })) {
            rank = newY;
            file = newX;
            super.rank = newY;
            super.file = newX;
            // board.getBoard()[oldRank][oldFile] = new EmptyPiece(oldRank, oldFile);
            hasMoved = true;
            super.hasMoved = true;
            return true;
        } else {
            return false;
        }
    }

    public int[] getLocation() {
        return new int[] { rank, file };
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isThreatened() {
        return isThreatened;
    }

    public LineOfSight getProtectingKingFrom() {
        return protectingKingFrom;
    }

    public void setProtectingKingFrom(LineOfSight p) {
        protectingKingFrom = p;
    }

    public abstract void update();

    public void setThreatened(boolean threatened) {
        isThreatened = threatened;
    }

    public void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public List<int[]> getAttacked() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (LineOfSight line : linesOfSight) {
            moves.addAll(line.getProtectedPieces());
        }
        return moves;
    }

    public List<int[]> getValidMoves() {
        List<int[]> valid = new ArrayList<>();
        if (state.checks == 0) {
            for (LineOfSight line : linesOfSight) {
                if (protectingKingFrom != null && protectingKingFrom.numPiecesInSight() == 1) {
                    for (int[] move : line.getValidMoves()) {
                        if (arrayContains(state.getCheck().get(0).getSquaresUntilKing(), move)
                                ||
                                board.getPiece(move[0], move[1])
                                        .equals(state.getCheck().get(0).owner)) {
                            valid.add(move);
                        }
                    }
                } else {
                    valid.addAll(line.getValidMoves());
                }
            }
        } else if (state.checks == 1) {
            for (LineOfSight line : linesOfSight) {
                LineOfSight check = state.getCheck().get(0);
                for (int[] move : line.getValidMoves()) {
                    if (arrayContains(check.getSquaresUntilKing(), move)
                            || board.getPiece(move[0], move[1]).equals(check.owner)) {
                        if (protectingKingFrom != null
                                && protectingKingFrom.numPiecesInSight() == 1) {
                            if (arrayContains(
                                    state.getCheck().get(0).getSquaresUntilKing(), move
                            ) &&
                                    arrayContains(
                                            state.getCheck().get(1).getSquaresUntilKing(), move
                                    )) {
                                valid.add(move);
                            }
                        } else {
                            valid.add(move);
                        }
                    }
                }
            }
        }
        return valid;
    }

    public List<int[]> getProtected() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (LineOfSight line : linesOfSight) {
            moves.addAll(line.getProtectedPieces());
        }
        return moves;
    }

    @Override
    public void castle() {
        if (file == 7) {
            file = 5;
            super.file = 5;
        } else if (file == 0) {
            file = 3;
            super.file = 3;
        }
    }
}
