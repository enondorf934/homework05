package edu.ou.cs.cg.homework.Homework5;

import java.awt.Color;

import edu.ou.cs.cg.homework.Homework5.Vector;

public final class Node {

    private String name;
    private int sides;
    private Color color;
    private boolean isRendered;
    private double cx;
    private double cy;
    private Vector[] vectors;
    private double radius;

    public Node(String name, int sides, Color color)
    {
        this.name = name;
        this.sides = sides;
        this.color = color;
        this.isRendered = false;
        this.cx = Math.random();

        if(Math.random() < .5)
            this.cx = this.cx * -1;

        this.cy = Math.random();

        if(Math.random() < .5)
            this.cy = this.cy * -1;

        this.radius = .1;

        this.vectors = this.drawShape();
    }

    public Vector[] drawShape()
    {
        Vector[] newArray = new Vector[(this.sides)];

        for (int i=0; i<this.sides; i++)
		{
			double	a = (2.0 * Math.PI) * (i / (double)this.sides);

			newArray[i] = new Vector(this.cx + this.radius * Math.cos(a), this.cy + this.radius * Math.sin(a));
        }
        
        return newArray;
    }

    public void updateVectors()
    {
        Vector[] newArray = new Vector[(this.sides)];

        for (int i=0; i<this.sides; i++)
		{
			double	a = (2.0 * Math.PI) * (i / (double)this.sides);

			newArray[i] = new Vector(this.cx + this.radius * Math.cos(a), this.cy + this.radius * Math.sin(a));
        }
        
        this.vectors = newArray;
    }

    public double getRadius()
    {
        return this.radius;
    }

    public Vector[] getVectors()
    {
        return this.vectors;
    }

    public String getName()
    {
        return this.name;
    }

    public int getSides()
    {
        return this.sides;
    }

    public Color getColor()
    {
        return this.color;
    }

    public boolean getIsRendered()
    {
        return this.isRendered;
    }

    public void setIsRendered(boolean bool)
    {
        this.isRendered = bool;
    }

    public double getCx()
    {
        return this.cx;
    }

    public double getCy()
    {
        return this.cy;
    }

    public void setCx(double n)
    {
        this.cx = n;
    }

    public void setCy(double n)
    {
        this.cy = n;
    }

    public void setRadius(double n)
    {
        this.radius = n;
    }
}