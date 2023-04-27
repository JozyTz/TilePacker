import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class PackingGame extends JPanel {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;
    private static final int SQUARE_SIZE = 50;
    private Rectangle boundingBox;
    private int boundingBoxArea;

    private List<Square> squares;
    private Square selectedSquare;
    private boolean rotating;
    private JButton addButton;
    private JButton removeButton;

    public PackingGame() {
        squares = new ArrayList<>();
        selectedSquare = null;
        rotating = false;

        // Create the "Add Square" button
        addButton = new JButton("Add Square");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a new square to the list
                Square newSquare = new Square(0, 0, Color.BLUE);
                squares.add(newSquare);
                repaint();
            }
        });
        add(addButton);

        // Create the "Remove Square" button
        removeButton = new JButton("Remove Square");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remove the selected square from the list
                if (selectedSquare != null) {
                    squares.remove(selectedSquare);
                    selectedSquare = null;
                    repaint();
                }
            }
        });
        add(removeButton);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point p = e.getPoint();
                for (Square square : squares) {
                    if (square.contains(p)) {
                        if (e.getX() > square.x + square.width - square.ROTATION_HANDLE_SIZE
                                && e.getY() > square.y + square.height - Square.ROTATION_HANDLE_SIZE) {
                            // user clicked on the rotation handle, initiate rotation mode
                            selectedSquare = square;
                            rotating = true;
                            return;
                        } else {
                            // user clicked on the square, initiate drag mode
                            selectedSquare = square;
                            rotating = false;
                            return;
                        }
                    }
                }
                selectedSquare = null;
            }
        
            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedSquare != null && rotating) {
                    rotating = false;
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                rotating = false;
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedSquare != null) {
                    if (rotating) {
                        Point center = selectedSquare.getCenter();
                        double dy = e.getY() - center.getY();
                        double dx = e.getX() - center.getX();
                        double angle = Math.atan2(dy, dx);
                        selectedSquare.setRotation(angle);
                        repaint();
                    } else {
                        int dx = e.getX() - selectedSquare.x;
                        int dy = e.getY() - selectedSquare.y;
                        selectedSquare.move(dx, dy);
                        repaint();
                    }
                }
            }
        });
        

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.WHITE);
    }

    public void addSquare(Square square) {
        squares.add(new Square(square.x, square.y, square.getColor()));
        repaint();
    }

    public void removeSquare(Square square) {
        squares.remove(square);
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
    
        for (Square square : squares) {
            paintSquare(g2d, square);
    
            if (square == selectedSquare) {
                paintSelectedSquare(g2d, square);
            }
    
            paintRotationHandle(g2d, square);
            paintSquareLabel(g2d, square);
        }
    
        Rectangle perimeter = getPerimeterRectangle();
        paintPerimeter(g2d, perimeter);
        paintPerimeterLabel(g2d, perimeter);
    }
    
    private void paintSquare(Graphics2D g2d, Square square) {
        g2d.setColor(square.getColor());
    
        AffineTransform transform = new AffineTransform();
        transform.translate(square.x + square.width / 2, square.y + square.height / 2);
        transform.rotate(square.getRotation());
        transform.translate(-square.width / 2, -square.height / 2);
    
        g2d.setTransform(transform);
        g2d.fillRect(0, 0, square.width, square.height);
        g2d.setTransform(new AffineTransform());
    }
    
    private void paintSelectedSquare(Graphics2D g2d, Square square) {
        g2d.setColor(Color.RED);
        g2d.drawRect(square.x, square.y, square.width, square.height);
    }
    
    private void paintRotationHandle(Graphics2D g2d, Square square) {
        g2d.setColor(Color.BLACK);
        g2d.fillRect(square.x + square.width - Square.ROTATION_HANDLE_SIZE,
                     square.y + square.height - Square.ROTATION_HANDLE_SIZE,
                     Square.ROTATION_HANDLE_SIZE, Square.ROTATION_HANDLE_SIZE);
    }
    
    private void paintSquareLabel(Graphics2D g2d, Square square) {
        g2d.setColor(Color.BLACK);
    
        if (!rotating) {
            g2d.drawString(String.format("(%d, %d)", square.x, square.y),
                            square.x, square.y - 5);
        } else {
            g2d.drawString(String.format("%.0f degrees", Math.toDegrees(selectedSquare.getRotation())),
                            square.x, square.y - 5);
        }
    }
    
    private Rectangle getPerimeterRectangle() {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
    
        for (Square square : squares) {
            if (square.isMovable()) {
                SquareCorners squareCorners = square.getSquareCorners();
                minX = squareCorners.getMinX(minX);
                minY = squareCorners.getMinY(minY);
                maxX = squareCorners.getMaxX(maxX);
                maxY = squareCorners.getMaxY(maxY);
            }
        }
    
        int perimeterX = minX - Square.PERIMETER_WIDTH / 2;
        int perimeterY = minY - Square.PERIMETER_WIDTH / 2;
        int perimeterWidth = maxX - minX + Square.PERIMETER_WIDTH;
        int perimeterHeight = maxY - minY + Square.PERIMETER_WIDTH;
    
        boundingBoxArea = perimeterWidth * perimeterHeight;
    
        return new Rectangle(perimeterX, perimeterY, perimeterWidth, perimeterHeight);
    }
    
    private void paintPerimeter(Graphics2D g2d, Rectangle perimeter) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(perimeter.x, perimeter.y, perimeter.width, perimeter.height);
    }
    
    private void paintPerimeterLabel(Graphics2D g2d, Rectangle perimeter) {
        g2d.setColor(Color.BLACK);
        g2d.drawString(String.format("Bounding box area: %d", boundingBoxArea),
                        10, getHeight() - 20);
    }
    
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Packing Game");
            PackingGame game = new PackingGame();
            game.addSquare(new Square(100, 100, Color.BLUE));
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
    
