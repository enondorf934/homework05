package edu.ou.cs.cg.homework.Homework5;

import java.awt.Color;

public final class Node {

    private String name;
    private int sides;
    private Color color;
    private boolean isRendered;
    private double cx;
    private double cy;

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

}