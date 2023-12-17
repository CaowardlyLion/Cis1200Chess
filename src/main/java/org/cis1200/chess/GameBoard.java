package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class GameBoard extends JPanel {
    JLabel status;
    private Board board;
    private State state;
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 800;

    int hoveredPieceX;
    int hoveredPieceY;
    Piece clickedPiece;
    Stack<Board> boardHistory = new Stack<>();
    Stack<State> stateHistory = new Stack<>();

    ArrayList<int[]> highlightedSquares = new ArrayList<>();

    public GameBoard(JLabel status) {
        this("files/boards/startingboard.txt", status);
    }

    public GameBoard(String filePath, JLabel status) {
        this.status = status;
        setFocusable(true);
        board = new Board();
        this.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        this.setVisible(true);
        this.requestFocus();
        this.requestFocusInWindow();
        board.startGame(filePath);
        state = board.getState();
        update();

        addMouseListener(new MouseAdapter() {
            boolean clicked = false;

            @Override
            public void mousePressed(MouseEvent e) {
                status.setVisible(true);
                Point p = e.getPoint();
                clickedPiece = board.getPiece(p.y / 100, p.x / 100);
                if (clickedPiece != null) {
                    clicked = true;
                    // print piece's position and protected status
                    System.out.println(
                            clickedPiece.getSymbol() + ": " + p.y / 100 + ", " + p.x / 100 + "\n"
                                    + "Protected by white: " + clickedPiece.isProtected(true) + "\n"
                                    + "Protected by black: " + clickedPiece.isProtected(false)
                    );
                    for (int[] line : clickedPiece.getValidMoves()) {
                        System.out.println("    " + line[0] + ", " + line[1]);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // System.out.println(clickedPiece.getSymbol());
                if (clicked) {
                    Point p = e.getPoint();
                    if (clickedPiece != null) {
                        // System.out.println("Clicked piece is not null");
                        System.out.println(
                                clickedPiece.getSymbol() + ": " + p.y / 100 + ", " + p.x / 100
                        );
                        if (state.move(clickedPiece, p.y / 100, p.x / 100)) {
                            // System.out.println("Moved.");
                            // System.out.println(board.getBoard()[p.y/100][p.x/100].getSymbol());
                            update();
                            repaint();
                        }
                        clickedPiece.setHovered(false);
                    }
                }
                clicked = false;
                highlightedSquares.clear();
                repaint();
                update();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!(clickedPiece instanceof EmptyPiece)
                        && clickedPiece.isWhite == state.getTurn()) {
                    highlightedSquares.addAll(clickedPiece.getValidMoves());
                }
                // System.out.println("pain");
                clickedPiece.setHovered(true);
                Point p = e.getPoint();
                hoveredPieceX = p.x;
                hoveredPieceY = p.y;

                repaint();
                // System.out.println("dragged");
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    System.out.println("Undo");
                    if (boardHistory.size() > 0) {
                        board = boardHistory.pop();
                        System.out.println("Popped board");
                        state = stateHistory.pop();
                    }
                    update();
                    repaint();
                }
            }
        });
    }

    public void update() {
        state.update();
        status.setText(state.getStatus());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    g.setColor(Color.decode("#fccc74"));
                } else {
                    g.setColor(Color.decode("#573a2e"));
                }
                g.fillRect(i * 100, j * 100, 100, 100);
            }
        }
        for (int[] square : highlightedSquares) {
            if ((square[0] + square[1]) % 2 == 0) {
                g.setColor(Color.decode("#85f29d"));
            } else {
                g.setColor(Color.decode("#61b874"));
            }

            g.fillRect(square[1] * 100, square[0] * 100, 100, 100);
        }

        board.drawBoard(g, hoveredPieceY, hoveredPieceX);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }

    public void reset() {
        board = new Board();
        board.startGame("files/boards/startingboard.txt");
        state = board.getState();
        update();
        repaint();
    }

    public void reset(String filePath) {
        board = new Board();
        try {
            board.startGame(filePath);
            state = board.getState();
            update();
            repaint();
        } catch (IllegalArgumentException e) {
            System.out.println("Bad game board.");
        } catch (RuntimeException e) {
            System.out.println("Invalid file path.");
        }

    }

    public void outputToFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                board.writeToFile(writer);
                writer.write("turn: " + (state.getTurn() ? "W" : "B"));
                writer.newLine();
                writer.write(
                        "lastMove: " + state.lastMovedPiece.rank + " " + state.lastMovedPiece.file
                );
                writer.newLine();
                writer.write("whiteKingMoved: " + state.getWhiteKing().hasMoved);
                writer.newLine();
                writer.write("blackKingMoved: " + state.getBlackKing().hasMoved);

                writer.close();
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
}
