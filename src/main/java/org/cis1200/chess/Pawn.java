package org.cis1200.chess;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {
    boolean isWhite;
    Board board;
    LineOfSight protectingKingFrom;

    public Pawn(int y, int x, boolean white, Board b, State s) {
        super(white ? "files/whitepawn.png" : "files/blackpawn.png", x, y, white, s);
        file = x;
        rank = y;
        hasMoved = false;
        isWhite = white;
        state = s;
        board = b;
    }

    public String getSymbol() {
        String color = isWhite ? "W" : "B";
        return color + "P";
    }

    public List<int[]> getValidMoves() {
        ArrayList<int[]> possibleMoves = new ArrayList<>();
        if (!isWhite && rank + 1 < 8 && board.getPiece(rank + 1, file) instanceof EmptyPiece) {
            possibleMoves.add(new int[] { rank + 1, file });
            if (!hasMoved && board.getPiece(rank + 2, file) instanceof EmptyPiece) {
                possibleMoves.add(new int[] { rank + 2, file });
            }
        } else if (isWhite && rank - 1 >= 0
                && board.getPiece(rank - 1, file) instanceof EmptyPiece) {
            possibleMoves.add(new int[] { rank - 1, file });
            if (!hasMoved && board.getPiece(rank - 2, file) instanceof EmptyPiece) {
                possibleMoves.add(new int[] { rank - 2, file });
            }
        }
        for (LineOfSight line : linesOfSight) {
            if (line.numPiecesInSight() > 0 && line.getValidMoves().size() > 0) {
                possibleMoves.add(line.getValidMoves().get(0));
            }
        }
        if (state.lastMovedPiece instanceof Pawn) {
            if (state.lastMovedPiece.rank == rank && state.lastMovedPiece.firstMoved) {
                if (isWhite) {
                    if (state.lastMovedPiece.file == file + 1) {
                        possibleMoves.add(new int[] { rank - 1, file + 1 });
                    } else if (state.lastMovedPiece.file == file - 1) {
                        possibleMoves.add(new int[] { rank - 1, file - 1 });
                    }
                } else {
                    if (state.lastMovedPiece.file == file + 1) {
                        possibleMoves.add(new int[] { rank + 1, file + 1 });
                    } else if (state.lastMovedPiece.file == file - 1) {
                        possibleMoves.add(new int[] { rank + 1, file - 1 });
                    }
                }

            }
        }
        List<int[]> valid = new ArrayList<>();
        if (state.checks == 0) {
            for (int[] line : possibleMoves) {
                if (line[0] >= 0 && line[0] < 8 && line[1] >= 0 && line[1] < 8) {
                    if (protectingKingFrom != null && protectingKingFrom.numPiecesInSight() == 1) {
                        if (arrayContains(state.getCheck().get(0).getSquaresUntilKing(), line) ||
                                board.getPiece(line[0], line[1])
                                        .equals(state.getCheck().get(0).owner)) {
                            valid.add(line);
                        }
                    } else {
                        valid.add(line);
                    }
                }
            }
        } else if (state.checks == 1) {
            for (int[] move : possibleMoves) {
                LineOfSight check = state.getCheck().get(0);
                if (move[0] >= 0 && move[0] < 8 && move[1] >= 0 && move[1] < 8) {
                    if (arrayContains(check.getSeenSquares(), move) ||
                            board.getPiece(move[0], move[1]).equals(check.owner)) {
                        valid.add(move);
                    }
                }
            }
        }
        return valid;
    }

    public void update() {
        linesOfSight.clear();

        if (!isWhite) {
            linesOfSight.add(
                    new LineOfSight(
                            new ArrayList<>(List.of(new int[] { rank + 1, file - 1 })), board, this
                    )
            );
            linesOfSight.add(
                    new LineOfSight(
                            new ArrayList<>(List.of(new int[] { rank + 1, file + 1 })), board, this
                    )
            );
        } else {
            linesOfSight.add(
                    new LineOfSight(
                            new ArrayList<>(List.of(new int[] { rank - 1, file + 1 })), board, this
                    )
            );
            linesOfSight.add(
                    new LineOfSight(
                            new ArrayList<>(List.of(new int[] { rank - 1, file - 1 })), board, this
                    )
            );
        }
    }

    public LineOfSight getProtectingKingFrom() {
        return protectingKingFrom;
    }

    public void setProtectingKingFrom(LineOfSight p) {
        protectingKingFrom = p;
    }

    @Override
    public void castle() {

    }

    @Override
    public boolean move(int newY, int newX) {
        int oldRank = rank;
        System.out.println("newX: " + newX + " newY: " + newY);
        if (arrayContains(getValidMoves(), new int[] { newY, newX })) {
            file = newX;
            rank = newY;
            hasMoved = true;
            firstMoved = rank == oldRank + 2 || rank == oldRank - 2;
            if (state.lastMovedPiece instanceof Pawn && state.lastMovedPiece.isWhite != isWhite) {
                if (isWhite && state.lastMovedPiece.rank == rank + 1 && rank == 2
                        && state.lastMovedPiece.firstMoved) {
                    board.enpassant(rank + 1, file);
                } else if (state.lastMovedPiece.rank == rank - 1 && rank == 5
                        && state.lastMovedPiece.firstMoved) {
                    board.enpassant(rank - 1, file);
                }
            }
            if ((isWhite && rank == 0) || (!isWhite && rank == 7)) {
                Object[] options = {
                    new ImageIcon(isWhite ? "files/whitequeen.png" : "files/blackqueen.png"),
                    new ImageIcon(isWhite ? "files/whiterook.png" : "files/blackrook.png"),
                    new ImageIcon(isWhite ? "files/whitebishop.png" : "files/blackbishop.png"),
                    new ImageIcon(isWhite ? "files/whiteknight.png" : "files/blackknight.png") };
                int option = JOptionPane.showOptionDialog(
                        null, "Promote:",
                        "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, options, 0
                );

                switch (option) {
                    case 1 -> state.promote(this, "R");
                    case 2 -> state.promote(this, "B");
                    case 3 -> state.promote(this, "N");
                    default -> state.promote(this, "Q");
                }
                hasPromoted = true;
            }
            return true;
        }
        return false;
    }

    public int[] getLocation() {
        return new int[] { file, rank };
    }

    public int getPoints() {
        return 4;
    }

    Object o = "hello";

}
