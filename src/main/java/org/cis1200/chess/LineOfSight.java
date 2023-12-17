package org.cis1200.chess;

import java.util.ArrayList;

public class LineOfSight {
    Piece owner;
    ArrayList<int[]> lineOfSight;
    boolean containsKing = false;
    ArrayList<Piece> piecesInSight;
    ArrayList<int[]> protectedSquares;
    ArrayList<int[]> squaresUntilKing = new ArrayList<>();
    ArrayList<Piece> piecesUntilKing = new ArrayList<>();
    String oppositeKing;
    Board board;

    public LineOfSight(ArrayList<int[]> lineOfSight, Board b, Piece owner) {
        board = b;
        oppositeKing = owner.isWhite ? "BK" : "WK";
        this.lineOfSight = lineOfSight;
        piecesInSight = new ArrayList<>();
        for (int[] location : lineOfSight) {
            if (b.getPiece(location[0], location[1]).getSymbol().equals(oppositeKing)) {
                containsKing = true;
                owner.state.setPin(this, !owner.isWhite);
            } else if (b.getPiece(location[0], location[1]).isWhite == owner.isWhite &&
                    !(b.getPiece(location[0], location[1]) instanceof EmptyPiece)) {
                if (!containsKing) {
                    squaresUntilKing.add(location);
                    piecesUntilKing.add(b.getPiece(location[0], location[1]));
                }
                b.getPiece(location[0], location[1]).protect(owner.isWhite);
                break;
            } else {
                piecesInSight.add(b.getPiece(location[0], location[1]));
                if (!containsKing) {
                    squaresUntilKing.add(location);
                    piecesUntilKing.add(b.getPiece(location[0], location[1]));
                }
            }
        }
        if (containsKing) {
            for (Piece piece : piecesInSight) {
                if (piece.isWhite != owner.isWhite) {
                    piece.setProtectingKingFrom(this);
                }
            }
            if (numPiecesInSight() == 0) {
                owner.state.setCheck();
            }
        }
        this.owner = owner;
        protectedSquares = getProtectedPieces();
        // protectedSquares.addAll(getThreatenedPieces());
    }

    public ArrayList<int[]> getLineOfSight() {
        return lineOfSight;
    }

    public ArrayList<int[]> getSeenSquares() {
        ArrayList<int[]> seenSquares = new ArrayList<>();
        seenSquares.addAll(getProtectedPieces());
        // seenSquares.addAll(getThreatenedPieces());
        return seenSquares;
    }

    public ArrayList<int[]> getSquaresUntilKing() {
        return squaresUntilKing;
    }

    public ArrayList<Piece> getPiecesUntilKing() {
        return piecesUntilKing;
    }

    public ArrayList<int[]> getProtectedPieces() {
        ArrayList<int[]> protectedPieces = new ArrayList<>();
        for (Piece piece : piecesInSight) {
            piece.protect(owner.isWhite);
            protectedPieces.add(new int[] { piece.rank, piece.file });
            if (!(piece instanceof EmptyPiece)) {
                break;
            }
        }
        return protectedPieces;
    }

    public ArrayList<int[]> getValidMoves() {
        ArrayList<int[]> validMoves = new ArrayList<>();
        for (Piece piece : getPiecesUntilKing()) {
            // Piece piece = board.getPiece(move[0], move[1]);
            if (piece instanceof EmptyPiece || piece.isWhite != owner.isWhite) {
                validMoves.add(new int[] { piece.rank, piece.file });
            }
            if (!piece.getSymbol().equals(".") && piece.isWhite != owner.isWhite) {
                break;
            }
        }
        return validMoves;
    }

    public boolean containsKing() {
        return containsKing;
    }

    public ArrayList<Piece> getPiecesInSight() {
        return piecesInSight;
    }

    public int numPiecesInSight() {
        int numPieces = 0;
        for (int[] move : getSquaresUntilKing()) {
            if (!(board.getPiece(move[0], move[1]) instanceof EmptyPiece)) {
                numPieces++;
            }
            if (board.getPiece(move[0], move[1]).getSymbol().equals(oppositeKing)) {
                break;
            }
        }
        return numPieces;
    }
}
