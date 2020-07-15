package com.mobileisaccframework;

public class Vector2D {
    public int x;
    public int y;

    Vector2D() { x = 0; y = 0; }
    public Vector2D(int _x, int _y) { x = _x; y = _y;}

    public Vector2D getDirection (Vector2D _dst) {
        int tempX = _dst.x - this.x;
        int tempY = _dst.y - this.y;

        return new Vector2D(tempX, tempY);
    }

    public int getDistance(Vector2D _dst) {
        double temp = (int) (Math.pow((_dst.x - this.x), 2) * Math.pow((_dst.y - this.y), 2));
        int distance = (int) Math.sqrt(temp);

        return distance;
    }
}
