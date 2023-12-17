package org.cis1200.chess;

// import org.cis1200.mushroom.GameObj;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    boolean hasPromoted;
    boolean hasMoved;
    boolean isWhite;
    int file;
    int rank;

    boolean whiteProtected;
    boolean blackProtected;
    State state;
    BufferedImage img;

    boolean isHovered;

    ArrayList<LineOfSight> linesOfSight = new ArrayList<>();

    boolean firstMoved;

    public abstract String getSymbol();

    public Piece(Piece p) {
        hasMoved = p.hasMoved;
        isWhite = p.isWhite;
        file = p.file;
        rank = p.rank;
        whiteProtected = p.whiteProtected;
        blackProtected = p.blackProtected;
        state = p.state;
        img = p.img;
        isHovered = p.isHovered;
        linesOfSight = p.linesOfSight;
        firstMoved = p.firstMoved;
    }

    public Piece(String imgFile, int x, int y, boolean white, State s) {
        try {
            if (imgFile != null) {
                img = ImageIO.read(new File(imgFile));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        this.file = x;
        this.rank = y;
        this.hasMoved = false;
        this.isWhite = white;
        this.state = s;
    }

    public List<int[]> getAttacked() {
        ArrayList<int[]> moves = new ArrayList<>();
        for (LineOfSight line : linesOfSight) {
            moves.addAll(line.getProtectedPieces());
        }
        return moves;
    }

    public abstract List<int[]> getValidMoves();

    public abstract boolean move(int newX, int newY);

    public abstract void update();

    public abstract void setProtectingKingFrom(LineOfSight lineOfSight);

    public void draw(Graphics g, int y, int x) {
        if (img != null) {
            g.drawImage(img, x + 10, y + 10, 80, 80, null);
        }
    }

    public void setHovered(boolean b) {
        isHovered = b;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public boolean arrayContains(List<int[]> arr, int[] target) {
        for (int[] i : arr) {
            if (i[0] == target[0] && i[1] == target[1]) {
                return true;
            }
        }
        return false;
    }

    public boolean isProtected(boolean isWhite) {
        if (isWhite) {
            return whiteProtected;
        } else {
            return blackProtected;
        }
    }

    public void protect(boolean isWhite) {
        if (isWhite) {
            whiteProtected = true;
        } else {
            blackProtected = true;
        }
    }

    public abstract void castle();

    public abstract int getPoints();
}
