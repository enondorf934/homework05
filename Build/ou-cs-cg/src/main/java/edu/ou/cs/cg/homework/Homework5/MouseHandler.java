//******************************************************************************
// Copyright (C) 2016 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Feb 29 23:46:15 2016 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20160225 [weaver]:	Original file.
//
//******************************************************************************
// Notes:
//
//******************************************************************************

package edu.ou.cs.cg.homework.Homework5;

//import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;

//******************************************************************************

/**
 * The <CODE>MouseHandler</CODE> class.<P>
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class MouseHandler extends MouseAdapter
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final View	view;
	private Point2D.Double originSpot;
	private  Point2D.Double clickSpot;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public MouseHandler(View view)
	{
		this.view = view;
		this.originSpot = view.getOrigin();
		this.clickSpot = view.getOrigin();

		Component	component = view.getComponent();

		component.addMouseListener(this);
		component.addMouseMotionListener(this);
		component.addMouseWheelListener(this);
	}

	//**********************************************************************
	// Override Methods (MouseListener)
	//**********************************************************************

	public void		mouseClicked(MouseEvent e)
	{
		Point2D.Double	v = calcCoordinatesInView(e.getX(), e.getY());

		LinkedList<Node> nodes = view.getNodes();

		int newSelectedIndex = -1;
		boolean notFound = true;

		for(int i = 0; i<nodes.size(); i++)
		{
			if(nodes.get(i).getIsRendered())
			{
				if(notFound)
				{
					if(view.checkIfClickInsideNode(v, nodes.get(i)))
					{
						newSelectedIndex = i;
						notFound = !notFound;
					}
				}

			}
		}

		view.setIndexSelected(newSelectedIndex);
	
	}

	public void		mouseEntered(MouseEvent e)
	{
		Point2D.Double	v = calcCoordinatesInView(e.getX(), e.getY());

		view.setCursor(v);
	}

	public void		mouseExited(MouseEvent e)
	{
		view.setCursor(null);
	}

	public void		mousePressed(MouseEvent e)
	{

		//formatted for view
		originSpot = view.getOrigin();
		//pixel cooredinates
		clickSpot = new Point2D.Double(e.getX(), e.getY());
	}

	public void		mouseReleased(MouseEvent e)
	{
	}

	//**********************************************************************
	// Override Methods (MouseMotionListener)
	//**********************************************************************

	public void		mouseDragged(MouseEvent e)
	{
		Point2D.Double firstClickFormatted = calcCoordinatesInView((int) clickSpot.x, (int) clickSpot.y);
		Point2D.Double dragToFormatted = calcCoordinatesInView(e.getX(), e.getY());
		double xChange = dragToFormatted.x - firstClickFormatted.x;
		double yChange = dragToFormatted.y - firstClickFormatted.y;
		
		Point2D.Double newOrigin = new Point2D.Double( (originSpot.x + xChange),  (originSpot.y + yChange));
		view.setOrigin(newOrigin);
		view.setCursor(calcCoordinatesInView(e.getX(), e.getY()));

		// Point2D.Double	v = calcCoordinatesInView(e.getX(), e.getY());

		// view.add(v);
		// view.setCursor(v);
	}

	public void		mouseMoved(MouseEvent e)
	{
		Point2D.Double	v = calcCoordinatesInView(e.getX(), e.getY());

		view.setCursor(v);
	}

	//**********************************************************************
	// Override Methods (MouseWheelListener)
	//**********************************************************************

	public void		mouseWheelMoved(MouseWheelEvent e)
	{
	}

	//**********************************************************************
	// Private Methods
	//**********************************************************************

	private Point2D.Double	calcCoordinatesInView(int sx, int sy)
	{
		int				w = view.getWidth();
		int				h = view.getHeight();
		Point2D.Double	p = view.getOrigin();
		double			vx = p.x + (sx * 2.0) / w - 1.0;
		double			vy = p.y - (sy * 2.0) / h + 1.0;

		return new Point2D.Double(vx, vy);
	}
}

//******************************************************************************
