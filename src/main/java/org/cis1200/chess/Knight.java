package org.cis1200.chess;

import java.util.ArrayList;
import java.util.Collections;

public class Knight extends NormalPiece {
    boolean hasMoved;
    boolean isWhite;
    State state;

    public Knight(int y, int x, boolean white, Board b, State s) {
        super(white ? "files/whiteknight.png" : "files/blackknight.png", x, y, white, b, s);
        rank = y;
        file = x;
        hasMoved = false;
        isWhite = white;
        board = b;
        state = s;
        linesOfSight = new ArrayList<>();
    }

    public String getSymbol() {
        return (isWhite ? "W" : "B") + "N";
    }

    public void update() {
        linesOfSight.clear();
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank + 1, file + 2 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank + 1, file - 2 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank - 1, file + 2 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank - 1, file - 2 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank + 2, file + 1 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank + 2, file - 1 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank - 2, file + 1 })),
                        board, this
                )
        );
        linesOfSight.add(
                new LineOfSight(
                        new ArrayList<>(Collections.singleton(new int[] { rank - 2, file - 1 })),
                        board, this
                )
        );
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public int getPoints() {
        return 3;
    }
}
