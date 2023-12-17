package org.cis1200.chess;

import java.awt.*;
import java.io.*;

public class Board implements Serializable {
    private Piece[][] board;
    private int numTurns;
    private Piece lastMovedPiece;

    private State state;

    public Board() {
        board = new Piece[8][8];
        numTurns = 0;
    }

    public Board(Board b, State s) {
        board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = b.getPiece(i, j);
            }
        }
        numTurns = b.numTurns;
        lastMovedPiece = b.lastMovedPiece;
        state = new State(s, this);
    }

    public Piece getLastMovedPiece() {
        return lastMovedPiece;
    }

    public void startGame(String fileName) {
        try {
            state = new State(this, true, null, null);
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            for (int i = 0; i < 8; i++) {
                String[] row = br.readLine().split(" ");
                for (int j = 0; j < 8; j++) {
                    boolean isWhite = row[j].charAt(0) == 'W';
                    if (!row[j].equals(".")) {
                        switch (row[j].charAt(1)) {
                            case 'P' -> {
                                board[i][j] = new Pawn(i, j, isWhite, this, state);
                                if (i != 1 && i != 6) {
                                    board[i][j].hasMoved = true;
                                }
                            }
                            case 'R' -> board[i][j] = new Rook(i, j, isWhite, this, state);
                            case 'N' -> board[i][j] = new Knight(i, j, isWhite, this, state);
                            case 'B' -> board[i][j] = new Bishop(i, j, isWhite, this, state);
                            case 'Q' -> board[i][j] = new Queen(i, j, isWhite, this, state);
                            case 'K' -> {
                                board[i][j] = new King(i, j, isWhite, this, state);
                                state.setKing((King) board[i][j], isWhite);
                            }
                            default -> board[i][j] = new EmptyPiece(i, j);
                        }
                    } else {
                        board[i][j] = new EmptyPiece(i, j);
                    }
                }
            }
            if (state.getWhiteKing() == null || state.getBlackKing() == null) {
                throw new IllegalArgumentException("Board must have a white and black king");
            }
            String line = br.readLine().split(" ")[1];
            if (line.equals("W")) {
                state.setWhiteTurn(true);
            } else if (line.equals("B")) {
                state.setWhiteTurn(false);
            } else {
                throw new IllegalArgumentException("Invalid turn");
            }
            String[] l = br.readLine().split(" ");
            lastMovedPiece = getPiece(Integer.parseInt(l[1]), Integer.parseInt(l[2]));
            state.lastMovedPiece = lastMovedPiece;
            line = br.readLine().split(" ")[1];
            state.getWhiteKing().hasMoved = line.equals("1");
            line = br.readLine().split(" ")[1];
            state.getBlackKing().hasMoved = line.equals("1");

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Board not found: " + fileName);
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid board file");
        }

    }

    public void startGame() {
        startGame("files/boards/startingboard.txt");
    }
    public Piece getPiece(int rank, int file) {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            return new EmptyPiece(-1, -1);
        } else {
            return board[rank][file];
        }
    }

    public int getNumTurns() {
        return numTurns;
    }

    public boolean movePiece(Piece piece, int newRank, int newFile) {
        int rank = piece.rank;
        int file = piece.file;
        if (piece.move(newRank, newFile) && !piece.hasPromoted) {
            lastMovedPiece = piece;
            board[newRank][newFile] = piece;
            // System.out.println(piece.getSymbol());
            board[rank][file] = new EmptyPiece(rank, file);
            System.out.println(board[newRank][newFile].getSymbol());
            numTurns++;
            return true;
        } else if (piece.hasPromoted) {
            lastMovedPiece = board[newRank][newFile];
            board[rank][file] = new EmptyPiece(rank, file);
            numTurns++;
            return true;
        } else {
            return false;
        }
    }

    public void castleKingSide() {
        if (state.getTurn()) {
            board[7][5] = board[7][7];
            board[7][7] = new EmptyPiece(7, 7);
            board[7][5].castle();
        } else {
            board[0][5] = board[0][7];
            board[0][7] = new EmptyPiece(0, 7);
            board[0][5].castle();
        }
    }

    public void castleQueenSide() {
        if (state.getTurn()) {
            board[7][3] = board[7][0];
            board[7][0] = new EmptyPiece(7, 0);
            board[7][3].castle();
        } else {
            board[0][3] = board[0][0];
            board[0][0] = new EmptyPiece(0, 0);
            board[0][3].castle();
        }
    }

    public void enpassant(int rank, int file) {
        board[rank][file] = new EmptyPiece(rank, file);
    }

    public void newGame() {

    }

    public void setPiece(int rank, int file, Piece piece) {
        board[rank][file] = piece;
    }

    public State getState() {
        return state;
    }

    public void resetBoard() {
        for (Piece[] row : board) {
            for (Piece p : row) {
                p.setProtectingKingFrom(null);
                p.whiteProtected = false;
                p.blackProtected = false;
            }
        }
    }

    public void update() {
        for (Piece[] row : board) {
            for (Piece p : row) {
                p.update();
            }
        }
    }

    public int checkPoints() {
        int totalPoints = 0;
        for (Piece[] row : board) {
            for (Piece p : row) {
                totalPoints += p.getPoints();
            }
        }
        return totalPoints;
    }

    public boolean checkStuck() {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (p.isWhite == state.getTurn() && p.getValidMoves().size() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void drawBoard(Graphics g, int hoveredPieceY, int hoveredPieceX) {
        for (Piece[] row : board) {
            for (Piece p : row) {
                if (!p.isHovered()) {
                    p.draw(g, p.rank * 100, p.file * 100);
                } else {
                    p.draw(g, hoveredPieceY - 50, hoveredPieceX - 50);
                }
            }
        }
    }

    public void writeToFile(BufferedWriter writer) throws IOException {
        for (Piece[] row : board) {
            for (Piece p : row) {
                writer.write(p.getSymbol() + " ");
            }
            writer.newLine();
        }
    }
}
