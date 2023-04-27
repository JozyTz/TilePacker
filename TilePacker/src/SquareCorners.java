import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class SquareCorners {
    private int X1, X2, X3, X4, Y1, Y2, Y3, Y4;

    public SquareCorners(Square square) {
        update(square);
    }

    public void update(Square square) {
        // Calculate the coordinates of the corners based on the current position and rotation of the square
        AffineTransform transform = new AffineTransform();
        transform.translate(square.getX() + square.getWidth() / 2, square.getY() + square.getHeight() / 2);
        transform.rotate(square.getRotation());
        transform.translate(-square.getWidth() / 2, -square.getHeight() / 2);

        Point2D corner1 = transform.transform(new Point2D.Double(0, 0), null);
        X1 = (int) corner1.getX();
        Y1 = (int) corner1.getY();

        Point2D corner2 = transform.transform(new Point2D.Double(square.getWidth(), 0), null);
        X2 = (int) corner2.getX();
        Y2 = (int) corner2.getY();

        Point2D corner3 = transform.transform(new Point2D.Double(0, square.getHeight()), null);
        X3 = (int) corner3.getX();
        Y3 = (int) corner3.getY();

        Point2D corner4 = transform.transform(new Point2D.Double(square.getWidth(), square.getHeight()), null);
        X4 = (int) corner4.getX();
        Y4 = (int) corner4.getY();
    }

    public int getX1() {
        return X1;
    }

    public int getY1() {
        return Y1;
    }

    public int getX2() {
        return X2;
    }

    public int getY2() {
        return Y2;
    }

    public int getX3() {
        return X3;
    }

    public int getY3() {
        return Y3;
    }

    public int getX4() {
        return X4;
    }

    public int getY4() {
        return Y4;
    }

    public int getMinX(int minX)
    {
        return Math.min(minX, Math.min(Math.min(X1, X2), Math.min(X3, X4)   )   );
    }

    public int getMinY(int minY)
    {
        return Math.min(minY, Math.min(Math.min(Y1, Y2), Math.min(Y3, Y4)   )   );
    }

    public int getMaxX(int maxX)
    {
        return Math.max(maxX, Math.max(Math.max(X1, X2), Math.max(X3, X4)   )   );
    }

    public int getMaxY(int maxY)
    {
        return Math.max(maxY, Math.max(Math.max(Y1, Y2), Math.max(Y3, Y4)   )   );
    }
}

