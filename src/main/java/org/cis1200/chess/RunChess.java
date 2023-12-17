package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;

public class RunChess implements Runnable {
    @Override
    public void run() {
        JFrame frame = new JFrame("Chess");
        JLabel message = new JLabel();
        final JPanel welcome = new Welcome(message);
        JPanel spacer = new JPanel();
        welcome.add(spacer, BorderLayout.NORTH);
        welcome.add(message, BorderLayout.CENTER);
        frame.add(welcome, BorderLayout.CENTER);
        frame.setLocation(300, 300);
        frame.add(welcome, BorderLayout.CENTER);

        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunChess());
    }

}
