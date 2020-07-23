package com.mobileisaccframework;

public class Vector2D {
    public int x;
    public int y;

    public Vector2D() { x = 0; y = 0; }
    public Vector2D(int _x, int _y) { x = _x; y = _y;}
    public Vector2D(Vector2D _pos)   {x = _pos.x; y = _pos.y;}

    public Vector2D getDirection (Vector2D _dst) {
        double tempX = _dst.x - this.x;
        double tempY = _dst.y - this.y;

        // Normalize
        double vectorSize = Math.sqrt(tempX*tempX + tempY*tempY);
        tempX = tempX/vectorSize;
        tempY = tempY/vectorSize;

        int unitX;
        int unitY;

        if(tempX>0)
            unitX = 1;
        else if(tempX<0)
            unitX = -1;
        else
            unitX = 0;

        if(tempY>0)
            unitY = 1;
        else if(tempY<0)
            unitY = -1;
        else
            unitY = 0;

        return new Vector2D(unitX, unitY);
    }

    public int getDistance(Vector2D _dst) {
        double temp = (int) (Math.pow((_dst.x - this.x), 2) + Math.pow((_dst.y - this.y), 2));
        int distance = (int) Math.sqrt(temp);

        return distance;
    }
}
