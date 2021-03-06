//******************************************************************************
// Copyright (C) 2016 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Tue Mar  1 18:52:22 2016 by Chris Weaver
//******************************************************************************
// Major Modification History:
//
// 20160209 [weaver]:	Original file.
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
import java.text.DecimalFormat;
import java.util.*;
import javax.swing.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.TextRenderer;

//******************************************************************************

/**
 * The <CODE>Interaction</CODE> class.<P>
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class View
	implements GLEventListener
{
	//**********************************************************************
	// Public Class Members
	//**********************************************************************

	public static final int				DEFAULT_FRAMES_PER_SECOND = 60;
	private static final DecimalFormat	FORMAT = new DecimalFormat("0.000");

	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final GLJPanel			canvas;
	private int						w;				// Canvas width
	private int						h;				// Canvas height

	private final KeyHandler		keyHandler;
	private final MouseHandler		mouseHandler;

	private final FPSAnimator		animator;
	private int						counter = 0;	// Frame display counter

	private TextRenderer			renderer;

	private Point2D.Double				origin;		// Current origin coordinates
	private Point2D.Double				cursor;		// Current cursor coordinates
	private ArrayList<Point2D.Double>	points;		// User's polyline points

	private LinkedList<Node> nodes = new LinkedList<Node>();

	private boolean isFirstRender = true;

	private int indexOfNodesList = 0;
	private int indexSelectedNode = -1;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public View(GLJPanel canvas)
	{
		this.canvas = canvas;

		// Initialize model
		origin = new Point2D.Double(0.0, 0.0);
		cursor = null;
		points = new ArrayList<Point2D.Double>();

		// Initialize rendering
		canvas.addGLEventListener(this);
		animator = new FPSAnimator(canvas, DEFAULT_FRAMES_PER_SECOND);
		//animator.start();

		// Initialize interaction
		keyHandler = new KeyHandler(this);
		mouseHandler = new MouseHandler(this);
	}

	//**********************************************************************
	// Getters and Setters
	//**********************************************************************

	public void refresh()
	{
		canvas.repaint();
	}

	public int	getWidth()
	{
		return w;
	}

	public LinkedList<Node> getNodes()
	{
		return nodes;
	}

	public int getIndexOfNodesList()
	{
		return indexOfNodesList;
	}

	public void setIndexOfNodesList(int n)
	{
		indexOfNodesList = n;
	}

	public int getIndexSelected()
	{
		return indexSelectedNode;
	}

	public void setIndexSelected(int n)
	{
		indexSelectedNode = n;
		canvas.repaint();
	}

	public int	getHeight()
	{
		return h;
	}

	public Point2D.Double	getOrigin()
	{
		return new Point2D.Double(origin.x, origin.y);
	}

	public void		setOrigin(Point2D.Double origin)
	{
		this.origin.x = origin.x;
		this.origin.y = origin.y;
		canvas.repaint();
	}

	public Point2D.Double	getCursor()
	{
		return cursor;
	}

	public void		setCursor(Point2D.Double cursor)
	{
		this.cursor = cursor;
		canvas.repaint();
	}

	public void		clear()
	{
		points.clear();
		canvas.repaint();
	}

	public void		add(Point2D.Double p)
	{
		points.add(p);
		canvas.repaint();
	}

	//**********************************************************************
	// Public Methods
	//**********************************************************************

	public Component	getComponent()
	{
		return (Component)canvas;
	}

	//**********************************************************************
	// Override Methods (GLEventListener)
	//**********************************************************************

	public void		init(GLAutoDrawable drawable)
	{
		w = drawable.getWidth();
		h = drawable.getHeight();

		renderer = new TextRenderer(new Font("Monospaced", Font.PLAIN, 12),
									true, true);
	}

	public void		dispose(GLAutoDrawable drawable)
	{
		renderer = null;
	}

	public void		display(GLAutoDrawable drawable)
	{
		updateProjection(drawable);

		update(drawable);
		render(drawable);
	}

	public void		reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
	{
		this.w = w;
		this.h = h;
	}

	//**********************************************************************
	// Private Methods (Viewport)
	//**********************************************************************

	private void	updateProjection(GLAutoDrawable drawable)
	{
		GL2		gl = drawable.getGL().getGL2();
		GLU		glu = new GLU();

		float	xmin = (float)(origin.x - 1.0);
		float	xmax = (float)(origin.x + 1.0);
		float	ymin = (float)(origin.y - 1.0);
		float	ymax = (float)(origin.y + 1.0);

		gl.glMatrixMode(GL2.GL_PROJECTION);			// Prepare for matrix xform
		gl.glLoadIdentity();						// Set to identity matrix
		glu.gluOrtho2D(xmin, xmax, ymin, ymax);		// 2D translate and scale
	}

	//**********************************************************************
	// Private Methods (Rendering)
	//**********************************************************************

	private void	update(GLAutoDrawable drawable)
	{
		counter++;								// Counters are useful, right?
	}

	private void	render(GLAutoDrawable drawable)
	{
		GL2		gl = drawable.getGL().getGL2();

		if(isFirstRender)
		{
			String[] nameList = Network.getAllNames();

			for(int i = 0; i<nameList.length; i++)
			{
				nodes.add(new Node(nameList[i], Network.getSides(nameList[i]), Network.getColor(nameList[i])));
			}
		}
		isFirstRender = false;

		gl.glClear(GL.GL_COLOR_BUFFER_BIT);		// Clear the buffer
		drawBounds(gl);							// Unit bounding box
		drawAxes(gl);							// X and Y axes
		drawCursor(gl);							// Crosshairs at mouse location
		drawNodeNames(drawable);		// Draw some text
		drawRenderedNodes(gl);
	//	drawPolyline(gl);						// Draw the user's sketch
	}

	//**********************************************************************
	// Private Methods (Scene)
	//**********************************************************************

	private void drawRenderedNodes(GL2 gl)
	{
		for(Node node : nodes) {
			if(node.getIsRendered())
			{
				if(nodes.indexOf(node) == indexSelectedNode)
				{
					drawNode(gl, node, true, node.getCx(), node.getCy());
				}
				else
				{
					drawNode(gl, node, false, node.getCx(), node.getCy());
				}
				
			}
				
		}
	}

	private void drawNode(GL2 gl, Node node, boolean isSelected, double cx, double cy)
	{
		Color color = node.getColor();
		
		gl.glColor3f(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f);

		if(isSelected)
		{
			//gl.glColor3f(14/255.0f, 28/255.0f, 51/255.0f);
			gl.glColor3f(1.0f, 1.0f, 1.0f);
		}

		gl.glBegin(GL.GL_LINE_LOOP);

		for (int i=0; i<node.getVectors().length; i++)
		{

			gl.glVertex2d(node.getVectors()[i].x, node.getVectors()[i].y);
		}
		gl.glEnd();

		gl.glColor3f(color.getRed()/255.0f, color.getGreen()/255.0f, color.getBlue()/255.0f);
		gl.glBegin(GL2.GL_POLYGON);

		for (int i=0; i<node.getVectors().length; i++)
		{
			gl.glVertex2d(node.getVectors()[i].x, node.getVectors()[i].y);
		}

		gl.glEnd();
	}

	private void	drawBounds(GL2 gl)
	{
		gl.glColor3f(0.1f, 0.1f, 0.1f);
		gl.glBegin(GL.GL_LINE_LOOP);

		gl.glVertex2d(1.0, 1.0);
		gl.glVertex2d(-1.0, 1.0);
		gl.glVertex2d(-1.0, -1.0);
		gl.glVertex2d(1.0, -1.0);

		gl.glEnd();
	}

	private void	drawAxes(GL2 gl)
	{
		gl.glBegin(GL.GL_LINES);

		gl.glColor3f(0.25f, 0.25f, 0.25f);
		gl.glVertex2d(-10.0, 0.0);
		gl.glVertex2d(10.0, 0.0);

		gl.glVertex2d(0.0, -10.0);
		gl.glVertex2d(0.0, 10.0);

		gl.glEnd();
	}

	private void	drawCursor(GL2 gl)
	{
		if (cursor == null)
			return;

		gl.glBegin(GL.GL_LINE_LOOP);
		gl.glColor3f(0.5f, 0.5f, 0.5f);

		for (int i=0; i<32; i++)
		{
			double	theta = (2.0 * Math.PI) * (i / 32.0);

			gl.glVertex2d(cursor.x + 0.05 * Math.cos(theta),
						  cursor.y + 0.05 * Math.sin(theta));
		}

		gl.glEnd();
	}

	private void	drawNodeNames(GLAutoDrawable drawable)
	{
		if (cursor == null)
			return;


		String	s = nodes.get(indexOfNodesList).getName();

		boolean allRendered = true;

		for(Node node : nodes) {
			if(!node.getIsRendered())
				allRendered = false;
		}

		if(allRendered)
			s = "No nodes left";

		renderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		renderer.setColor(1.0f, 1.0f, 0, 1.0f);
		renderer.draw(s, 2, 2);
		renderer.endRendering();
	}

	private void	drawPolyline(GL2 gl)
	{
		gl.glColor3f(1.0f, 0.0f, 0.0f);

		for (Point2D.Double p : points)
		{
			gl.glBegin(GL2.GL_POLYGON);

			gl.glVertex2d(p.x - 0.01, p.y - 0.01);
			gl.glVertex2d(p.x - 0.01, p.y + 0.01);
			gl.glVertex2d(p.x + 0.01, p.y + 0.01);
			gl.glVertex2d(p.x + 0.01, p.y - 0.01);

			gl.glEnd();
		}

		gl.glColor3f(1.0f, 1.0f, 0.0f);
		gl.glBegin(GL.GL_LINE_STRIP);

		for (Point2D.Double p : points)
			gl.glVertex2d(p.x, p.y);

		gl.glEnd();
	}

	private double crossproduct(Vector line, Vector nextLine,  double speedx, double speedy)
	{
		Vector tipMinusPoint = new Vector(nextLine.x - speedx, nextLine.y - speedy);
		Vector tailMinusPoint = new Vector(line.x - speedx, line.y - speedy);

		return (tipMinusPoint.x * tailMinusPoint.y - tipMinusPoint.y * tailMinusPoint.x);

	}

	//returns index of selected node, or -1 if click isnt in node
	public boolean checkIfClickInsideNode(Point2D.Double clickPoint, Node node) 
	{
		Vector[] vectors = node.getVectors();
		boolean isInside = false;

		for(int i =0 ; i<vectors.length; i++)
		{
			if(i+1 < vectors.length)
			{
				//check if the next point is outside the container, if it is reflect and set the normal vector
				if(crossproduct(vectors[i], vectors[i+1], clickPoint.x, clickPoint.y) > 0)
				{
					isInside = true;					
				}
			}
			else
			{
				if(crossproduct(vectors[i], vectors[0], clickPoint.x, clickPoint.y) > 0)
				{
					isInside = true;
				}
			}
		}

		return isInside;
	}
}

//******************************************************************************
