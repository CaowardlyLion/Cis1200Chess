package org.cis1200.chess;

import java.util.List;

public class State {
    private final Board board;
    private boolean isWhiteTurn;
    private boolean isCheckmate;
    private boolean isStalemate;
    Piece lastMovedPiece;

    private King whiteKing;
    private King blackKing;
    int checks;

    public State(State s, Board b) {
        board = b;
        isWhiteTurn = s.isWhiteTurn;
        isCheckmate = s.isCheckmate;
        isStalemate = s.isStalemate;
        lastMovedPiece = s.lastMovedPiece;
        whiteKing = s.whiteKing;
        blackKing = s.blackKing;
        checks = s.checks;
    }

    public State(Board b, boolean whiteTurn, King whiteKing, King blackKing) {
        board = b;
        isWhiteTurn = whiteTurn;
        this.whiteKing = whiteKing;
        this.blackKing = blackKing;
    }

    public void setWhiteTurn(boolean whiteTurn) {
        isWhiteTurn = whiteTurn;
    }

    public void update() {
        int checkFlag = 0;
        resetState();
        board.update();
        int totalPoints = board.checkPoints();
        if (totalPoints <= 3) {
            isStalemate = true;
        }
        boolean isStuck = board.checkStuck();
        if (isStuck) {
            if (checks > 0) {
                isCheckmate = true;
                System.out.println(isWhiteTurn ? "Black wins!" : "White wins!");
            } else {
                isStalemate = true;
                System.out.println("Stalemate!");
            }
        }

    }

    public boolean inCheck(boolean isWhite) {
        return false;
    }

    public boolean willBeInCheck(NormalPiece p, int newX, int newY, boolean isWhite) {
        return false;
    }

    public boolean move(Piece p, int newY, int newX) {
        if (p.isWhite == isWhiteTurn) {
            if (board.movePiece(p, newY, newX)) {
                isWhiteTurn = !isWhiteTurn;
                update();
                lastMovedPiece = p;
                return true;
            }
        }
        return false;
    }

    public void setCheck() {

        checks++;
        System.out.println("CHECK");
    }

    public void setPin(LineOfSight s, boolean isWhite) {
        if (isWhite) {
            whiteKing.recieveCheck(s);
        } else {
            blackKing.recieveCheck(s);
        }
    }

    public void setKing(King k, boolean isWhite) {
        if (isWhite) {
            whiteKing = k;
        } else {
            blackKing = k;
        }
    }

    public List<LineOfSight> getCheck() {
        if (isWhiteTurn) {
            return whiteKing.checkedBy;
        } else {
            return blackKing.checkedBy;
        }
    }

    public void resetState() {
        board.resetBoard();
        checks = 0;
        whiteKing.checkedBy.clear();
        blackKing.checkedBy.clear();
    }

    public boolean getTurn() {
        return isWhiteTurn;
    }

    public boolean canCastleKingSide(boolean isWhite) {
        if (isWhite) {
            Piece p = board.getPiece(7, 7);
            if (p instanceof Rook) {
                if (!p.hasMoved && !whiteKing.hasMoved) {
                    return board.getPiece(7, 6) instanceof EmptyPiece
                            && !board.getPiece(7, 6).isProtected(false) &&
                            board.getPiece(7, 5) instanceof EmptyPiece
                            && !board.getPiece(7, 5).isProtected(false);
                } else {
                    return false;
                }
            }
        } else {
            Piece p = board.getPiece(0, 7);
            if (p instanceof Rook) {
                if (!p.hasMoved && !blackKing.hasMoved) {
                    return board.getPiece(0, 6) instanceof EmptyPiece
                            && !board.getPiece(0, 6).isProtected(true) &&
                            board.getPiece(0, 5) instanceof EmptyPiece
                            && !board.getPiece(0, 5).isProtected(true);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean canCastleQueenSide(boolean isWhite) {
        if (isWhite) {
            Piece p = board.getPiece(7, 0);
            if (p instanceof Rook) {
                if (!p.hasMoved && !whiteKing.hasMoved) {
                    return board.getPiece(7, 1) instanceof EmptyPiece
                            && !board.getPiece(7, 1).isProtected(false) &&
                            board.getPiece(7, 2) instanceof EmptyPiece
                            && !board.getPiece(7, 2).isProtected(false) &&
                            board.getPiece(7, 3) instanceof EmptyPiece
                            && !board.getPiece(7, 3).isProtected(false);
                } else {
                    return false;
                }
            }
        } else {
            Piece p = board.getPiece(0, 0);
            if (p instanceof Rook) {
                if (!p.hasMoved && !blackKing.hasMoved) {
                    return board.getPiece(0, 1) instanceof EmptyPiece
                            && !board.getPiece(0, 1).isProtected(true) &&
                            board.getPiece(0, 2) instanceof EmptyPiece
                            && !board.getPiece(0, 2).isProtected(true) &&
                            board.getPiece(0, 3) instanceof EmptyPiece
                            && !board.getPiece(0, 3).isProtected(true);
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isCheckmate() {
        return isCheckmate;
    }

    public boolean isStalemate() {
        return isStalemate;
    }

    public String getStatus() {
        String message = (isWhiteTurn ? "White's turn" : "Black's turn") + ". "
                + ((checks > 0) ? "Check!" : "");
        if (isCheckmate) {
            message = (isWhiteTurn ? "Black wins!" : "White wins!");
        } else if (isStalemate) {
            message = "Stalemate!";
        }
        return message;
    }

    public void promote(Pawn p, String choice) {
        int rank = p.rank;
        int file = p.file;
        switch (choice) {
            case "Q" -> board.setPiece(rank, file, new Queen(
                    rank, file, p.isWhite, board, this
            ));
            case "R" -> board.setPiece(rank, file, new Rook(
                    rank, file, p.isWhite, board, this
            ));
            case "B" -> board.setPiece(rank, file, new Bishop(
                    rank, file, p.isWhite, board, this
            ));
            case "N" -> board.setPiece(rank, file, new Knight(
                    rank, file, p.isWhite, board, this
            ));
            default -> System.out.println("Invalid choice");
        }
    }

    public Piece getWhiteKing() {
        return whiteKing;
    }

    public Piece getBlackKing() {
        return blackKing;
    }
}
