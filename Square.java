import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Square extends Rectangle {

    public static final int ROTATION_HANDLE_SIZE = 10;
    public static final int PERIMETER_WIDTH = 4;
    private static final int SQUARE_SIZE = 50;
    private Color color;
    private double rotation;
    private boolean movable;
    private SquareCorners squareCorners;

    public Square(int x, int y, Color color) {
        super(x, y, SQUARE_SIZE, SQUARE_SIZE);
        this.color = color;
        this.rotation = 0.0;
        this.movable = true;
        this.squareCorners = new SquareCorners(this);
    }

    public boolean isMovable() {
        return movable;
    }

    public void move(int dx, int dy) {
        x += dx;
        y += dy;
        squareCorners.update(this);
    }

    public Point getCenter() {
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        return new Point(centerX, centerY);
    }

    public void setRotation(double angle) {
        rotation = angle;
        squareCorners.update(this);
    }

    public double getRotation() {
        return rotation;
    }

    public Color getColor() {
        return color;
    }

    public SquareCorners getSquareCorners()
    {
        return squareCorners;
    }
}
