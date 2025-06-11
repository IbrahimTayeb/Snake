import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {

    private static final int TILE_SIZE = 25;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int DELAY = 100;

    private final ArrayList<Point> snake;
    private Point food;
    private char direction = 'R'; // U = Up, D = Down, L = Left, R = Right
    private boolean running = false;
    private Timer timer;
    private int score = 0;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        snake = new ArrayList<>();
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        direction = 'R';
        score = 0;
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / TILE_SIZE);
        int y = rand.nextInt(HEIGHT / TILE_SIZE);
        food = new Point(x, y);
    }

    public void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case 'U': head.y--; break;
            case 'D': head.y++; break;
            case 'L': head.x--; break;
            case 'R': head.x++; break;
        }

        // Check collision
        if (head.x < 0 || head.x >= WIDTH / TILE_SIZE || head.y < 0 || head.y >= HEIGHT / TILE_SIZE || snake.contains(head)) {
            running = false;
            timer.stop();
            return;
        }

        // Eat food
        if (head.equals(food)) {
            snake.add(0, head);
            score++;
            spawnFood();
        } else {
            snake.add(0, head);
            snake.remove(snake.size() - 1);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw grid (optional)
        g.setColor(Color.DARK_GRAY);
        for (int i = 0; i < WIDTH / TILE_SIZE; i++) {
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, HEIGHT);
            g.drawLine(0, i * TILE_SIZE, WIDTH, i * TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw snake
        for (int i = 0; i < snake.size(); i++) {
            g.setColor(i == 0 ? Color.GREEN : Color.YELLOW);
            Point p = snake.get(i);
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("Score: " + score, 10, 20);

        // Game Over
        if (!running) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 48));
            g.drawString("Game Over", WIDTH / 2 - 130, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        char prev = direction;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (prev != 'D') direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (prev != 'U') direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (prev != 'R') direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (prev != 'L') direction = 'R';
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }
}
