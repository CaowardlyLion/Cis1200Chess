package org.cis1200.chess;

import java.util.ArrayList;
import java.util.List;

public class EmptyPiece extends Piece {

    public EmptyPiece(int y, int x) {
        super(null, x, y, false, null);

    }

    public String getSymbol() {
        return ".";
    }

    public List<int[]> getAttacked() {
        return new ArrayList<>();
    }

    public List<int[]> getValidMoves() {
        return new ArrayList<>();
    }

    public void update() {
        return;
    }

    @Override
    public void setProtectingKingFrom(LineOfSight lineOfSight) {
        return;
    }

    @Override
    public void castle() {

    }

    public boolean move(int newX, int newY) {
        return false;
    }

    public int getPoints() {
        return 0;
    }
}
