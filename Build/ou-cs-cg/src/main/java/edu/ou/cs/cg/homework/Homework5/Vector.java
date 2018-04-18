package edu.ou.cs.cg.homework.Homework5;

public final class Vector
{

    public double x;
    public double y;

    public Vector(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return this.x;
    }

    public double getY()
    {
        return this.y;
    }

    public void setX(double x)
    {
        this.x = x;
    }

    public void setY(double y)
    {
        this.y = y;
    }

    public Vector multiply(double scalar)
    {
        return new Vector(x * scalar, y * scalar);
    }

    public double magnitude()
    {
        return Math.sqrt( Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }

    public double dotProduct(Vector a, Vector b)
    {

        return a.x * b.x + a.y * b.y;
    }

    public double angleBetweenVectors(Vector a, Vector b)
    {
        return Math.acos((dotProduct(a, b)/(a.magnitude() * b.magnitude()))/180*2*Math.PI);
    }

    public Vector subtract(Vector a)
    {
        return new Vector(this.x - a.x, this.y - a.y);
    }
}