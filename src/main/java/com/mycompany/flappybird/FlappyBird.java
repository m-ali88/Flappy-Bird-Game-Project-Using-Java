/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flappybird;

/**
 *
 * @author SYED
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FlappyBird extends JFrame implements KeyListener {
    private JLabel background;
    private JButton bird;
    private Timer obstacleTimer;
    private JLabel[] topObstacles;
    private JLabel[] bottomObstacles;
    private JLabel gameOverLabel;
    private JLabel startLabel;
    private JLabel scoreLabel;
    private JLabel restartLabel;
    private boolean gameStarted = false;

    private int obstacleWidth = 50;
    private int gapBetweenObstacles = 300;
    private int obstacleSpeed = 5;
    private int obstacleCount = 4;
    private int score = 0;
    private boolean[] scoredObstacles = new boolean[obstacleCount];

    private int birdX = 100;
    private int birdY = 300;
    private int birdWidth = 50;
    private int birdHeight = 50;
    private int birdGravity = 1;
    private int birdVelocity = 0;

    FlappyBird() {
        setSize(1366, 768);
        setLayout(null);
        setTitle("Flappy Bird");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addKeyListener(this);

        ImageIcon img = new ImageIcon("Background.jpg");
        background = new JLabel("", img, JLabel.CENTER);
        background.setBounds(0, 0, 1366, 768);

        ImageIcon birdIcon = new ImageIcon("Bird.png");
        bird = new JButton(birdIcon);
        bird.setBounds(birdX, birdY, birdWidth, birdHeight);
        bird.setBorderPainted(false);
        bird.setContentAreaFilled(false);
        bird.setFocusPainted(false);

        topObstacles = new JLabel[obstacleCount];
        bottomObstacles = new JLabel[obstacleCount];

        for (int i = 0; i < obstacleCount; i++) {
            topObstacles[i] = new JLabel();
            topObstacles[i].setBackground(Color.GREEN);
            topObstacles[i].setOpaque(true);
            topObstacles[i].setBounds(1366 + i * 300, 0, obstacleWidth, generateRandomObstacleHeight());

            bottomObstacles[i] = new JLabel();
            bottomObstacles[i].setBackground(Color.GREEN);
            bottomObstacles[i].setOpaque(true);
            int bottomHeight = generateRandomObstacleHeight();
            bottomObstacles[i].setBounds(1366 + i * 300, 768 - bottomHeight, obstacleWidth, bottomHeight);

            add(topObstacles[i]);
            add(bottomObstacles[i]);
        }
        startLabel = new JLabel("Press Enter to Start");
        startLabel.setFont(new Font("Arial", Font.BOLD, 30));
        startLabel.setForeground(Color.BLACK);
        startLabel.setBounds(500, 300, 300, 50);
        add(startLabel);
        
        gameOverLabel = new JLabel("Game Over!");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 30));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(500, 300, 300, 50);
        gameOverLabel.setVisible(false);
        
        restartLabel = new JLabel("Press Space to restart the game");
        restartLabel.setFont(new Font("Arial", Font.BOLD, 20));
        restartLabel.setForeground(Color.BLACK);
        restartLabel.setBounds(450, 350, 500, 50);
        restartLabel.setVisible(false);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(20, 20, 150, 30);

        add(bird);
        add(gameOverLabel);
        add(restartLabel);
        add(scoreLabel);
        add(background);

        obstacleTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if (gameStarted) {
                    moveObstacles();
                    moveBird();
                    repaint();
                }
            }
        });
        obstacleTimer.start();
    }

    private void moveObstacles() {
        for (int i = 0; i < obstacleCount; i++) {
            int topX = topObstacles[i].getX() - obstacleSpeed;
            int bottomX = bottomObstacles[i].getX() - obstacleSpeed;

            if (topX + obstacleWidth < 0) {
                topX = 1366;
                bottomX = 1366;

                topObstacles[i].setBounds(1366, 0, obstacleWidth, generateRandomObstacleHeight());
                int bottomHeight = generateRandomObstacleHeight();
                bottomObstacles[i].setBounds(1366, 768 - bottomHeight, obstacleWidth, bottomHeight);

                scoredObstacles[i] = false; // Reset scored status when obstacle resets
            }

            topObstacles[i].setBounds(topX, 0, obstacleWidth, topObstacles[i].getHeight());
            bottomObstacles[i].setBounds(bottomX, bottomObstacles[i].getY(), obstacleWidth, bottomObstacles[i].getHeight());

            Rectangle birdBounds = bird.getBounds();
            Rectangle topBounds = topObstacles[i].getBounds();
            Rectangle bottomBounds = bottomObstacles[i].getBounds();

            if (!scoredObstacles[i] && topX + obstacleWidth < birdX) {
                score++;
                scoredObstacles[i] = true; // Mark the obstacle as scored
                scoreLabel.setText("Score: " + score);
            }

            if (birdBounds.intersects(topBounds) || birdBounds.intersects(bottomBounds)) {
                stopGame();
            }
        }
    }

    private void moveBird() {
        birdVelocity += birdGravity;
        birdY += birdVelocity;
        bird.setBounds(birdX, birdY, birdWidth, birdHeight);

        if (birdY <= 0) {
            birdY = 0;
        } else if (birdY >= 768 - birdHeight) {
            birdY = 768 - birdHeight;
        }
    }

    private void stopGame() {
        obstacleTimer.stop();
        birdVelocity = 0;
        gameOverLabel.setVisible(true);
        restartLabel.setVisible(true);
    }

    private void restartGame() {
        score = 0;
        scoreLabel.setText("Score: " + score);
        birdY = 300;
        birdVelocity = 0;
        gameOverLabel.setVisible(false);

        for (int i = 0; i < obstacleCount; i++) {
            topObstacles[i].setBounds(1366 + i * 300, 0, obstacleWidth, generateRandomObstacleHeight());
            int bottomHeight = generateRandomObstacleHeight();
            bottomObstacles[i].setBounds(1366 + i * 300, 768 - bottomHeight, obstacleWidth, bottomHeight);
            scoredObstacles[i] = false;
        }

        remove(restartLabel);
        obstacleTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (!gameStarted && key == KeyEvent.VK_ENTER) {
            gameStarted = true;
            startLabel.setVisible(false);
            obstacleTimer.start();
        }
        if (key == KeyEvent.VK_SPACE) {
            if (gameOverLabel.isVisible()) {
                restartGame();
            } else {
                birdVelocity = -15;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    private int generateRandomObstacleHeight() {
        return (int) (Math.random() * 300) + 100; 
    }

    public static void main(String[] args) {
        new FlappyBird();
    }
}

