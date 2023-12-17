package org.cis1200.chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Welcome extends JPanel {
    private JLabel welcomeLabel;

    public Welcome(JLabel label) {
        welcomeLabel = label;
        this.setSize(800, 850);
        this.setVisible(true);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        repaint();
        welcomeLabel.setText(
                "Welcome to Chess! Hopefully you know the rules." +
                        " Drag and drop pieces, and valid squares will be highlighted."
        );

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(Welcome.this);
                    frame.getContentPane().removeAll();
                    JPanel panel = new JPanel();
                    frame.add(panel, BorderLayout.SOUTH);

                    JLabel status = new JLabel("Test Label here");
                    panel.add(status);

                    GameBoard gameBoard = new GameBoard(status);
                    frame.getContentPane().add(gameBoard, BorderLayout.NORTH);

                    final JPanel control_panel = new JPanel();
                    frame.add(control_panel, BorderLayout.CENTER);

                    final JButton reset = new JButton("Reset");
                    reset.addActionListener(lambda -> gameBoard.reset());
                    control_panel.add(reset);

                    final JButton loadFromFile = new JButton("Load From File");
                    loadFromFile.addActionListener(e1 -> {
                        String name = JOptionPane.showInputDialog(
                                control_panel,
                                "What's the name of the board?", null
                        );
                        if (name != null) {
                            gameBoard.reset("files/boards/" + name + ".txt");
                        }
                    });
                    control_panel.add(loadFromFile);

                    final JButton saveToFile = new JButton("Save To File");
                    saveToFile.addActionListener(e1 -> {
                        String name = JOptionPane.showInputDialog(
                                control_panel,
                                "What's the name of the board?", null
                        );
                        if (name != null) {
                            gameBoard.outputToFile("files/boards/" + name + ".txt");
                        }
                    });
                    control_panel.add(saveToFile);

                    frame.getContentPane().repaint();
                }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // g.drawString("Welcome to Chess! Hopefully you know the rules.", 300, 300);
        g.drawString("Press Enter to start", 350, 400);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 850);
    }

}
