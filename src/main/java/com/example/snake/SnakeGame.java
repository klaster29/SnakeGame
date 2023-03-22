package com.example.snake;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class SnakeGame {

    private final int ROWS = 30;
    private final int COLS = 30;
    private final int CELL_SIZE = 20;
    private final int DELAY = 100;

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;
    private Timer timer;
    private final Random random = new Random();

    private JPanel board;

    public static void main(String[] args) {
        new SnakeGame().init();
    }

    private void init() {
        JFrame frame = new JFrame("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        board.setPreferredSize(new Dimension(COLS * CELL_SIZE, ROWS * CELL_SIZE));
        board.setBackground(Color.black);
        frame.getContentPane().add(board);

        initGame();

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);

        timer = new Timer(DELAY, e -> {
            if (inGame) {
                move();
                checkCollisions();
                checkFood();
                board.repaint();
            } else {
                timer.stop();
            }
        });
        timer.start();

        board.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) && !rightDirection) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if ((key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) && !leftDirection) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                } else if ((key == KeyEvent.VK_UP || key == KeyEvent.VK_W) && !downDirection) {
                    upDirection = true;
                    leftDirection = false;
                    rightDirection = false;
                } else if ((key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) && !upDirection) {
                    downDirection = true;
                    leftDirection = false;
                    rightDirection = false;
                }
            }
        });

        board.setFocusable(true);
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(ROWS / 2, COLS / 2));
        generateFood();
        leftDirection = false;
        rightDirection = true;
        upDirection = false;
        downDirection = false;
        inGame = true;
    }

    private void generateFood() {
        food = new Point(random.nextInt(ROWS), random.nextInt(COLS));
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            snake.add(new Point(-1, -1));
            generateFood();
        }
    }

    private void checkCollisions() {
        if (snake.get(0).x < 0 || snake.get(0).x >= ROWS || snake.get(0).y < 0 || snake.get(0).y >= COLS) {
            inGame = false;
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                inGame = false;
                break;
            }
        }
    }

    private void move() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Point(snake.get(i - 1)));
        }

        Point head = snake.get(0);
        if (leftDirection) {
            head.x--;
        } else if (rightDirection) {
            head.x++;
        } else if (upDirection) {
            head.y--;
        } else if (downDirection) {
            head.y++;
        }

        snake.set(0, head);
    }

    private void drawSnake(Graphics g) {
        g.setColor(Color.white);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    private void drawFood(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    private void drawGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        FontMetrics metrics = g.getFontMetrics();
        String gameOver = "Game Over!";
        g.drawString(gameOver, (board.getWidth() - metrics.stringWidth(gameOver)) / 2, board.getHeight() / 2);
    }

    private class Board extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (inGame) {
                drawSnake(g);
                drawFood(g);
            } else {
                drawGameOver(g);
            }
        }
    }
}