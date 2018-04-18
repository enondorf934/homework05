//******************************************************************************
// Copyright (C) 2016 University of Oklahoma Board of Trustees.
//******************************************************************************
// Last modified: Mon Feb 29 23:36:04 2016 by Chris Weaver
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

import com.sun.javafx.collections.ElementObservableListDecorator;
import com.sun.webkit.Utilities;

//******************************************************************************

/**
 * The <CODE>KeyHandler</CODE> class.<P>
 *
 * @author  Chris Weaver
 * @version %I%, %G%
 */
public final class KeyHandler extends KeyAdapter
{
	//**********************************************************************
	// Private Members
	//**********************************************************************

	// State (internal) variables
	private final View	view;

	//**********************************************************************
	// Constructors and Finalizer
	//**********************************************************************

	public KeyHandler(View view)
	{
		this.view = view;

		Component	component = view.getComponent();

		component.addKeyListener(this);
	}

	//**********************************************************************
	// Override Methods (KeyListener)
	//**********************************************************************

	public void		keyPressed(KeyEvent e)
	{
		Point2D.Double	p = view.getOrigin();
		double			a = (edu.ou.cs.cg.homework.Homework5.Utilities.isShiftDown(e) ? 0.01 : 0.1);
		LinkedList<Node> nodes = view.getNodes();
		int currentNodesIndex = view.getIndexOfNodesList();
		int currentSelected = view.getIndexSelected();

		System.out.println(currentSelected);

		boolean shiftDown = edu.ou.cs.cg.homework.Homework5.Utilities.isShiftDown(e);

		switch (e.getKeyCode())
		{
			case KeyEvent.VK_PERIOD:
				if(shiftDown)
				{
					boolean stopFlag = true;
					do{
						if(currentNodesIndex + 1 < nodes.size())
						{
							currentNodesIndex++;
		
						}
						else {
							currentNodesIndex = 0;
						}
						if(!nodes.get(currentNodesIndex).getIsRendered())
						{
							stopFlag = false;
							view.setIndexOfNodesList(currentNodesIndex);
						}
					}while(stopFlag);
				}
				else {
					boolean stopFlag = true;
					do{
						if(currentSelected + 1 < nodes.size())
						{
							currentSelected++;
						}
						else {
							currentSelected = -1;
						}
						if(currentSelected != -1 && nodes.get(currentSelected).getIsRendered())
						{
							stopFlag = false;
							view.setIndexSelected(currentSelected);
						}
						else if(currentSelected == -1)
						{
							stopFlag = false; 
							view.setIndexSelected(currentSelected);
						}
					}while(stopFlag);
				}
				break;
			case KeyEvent.VK_COMMA:
				if(shiftDown)
				{
					boolean stopFlag = true;
					do{
						if(currentNodesIndex - 1 >= 0)
						{
							currentNodesIndex--;
		
						}
						else {
							currentNodesIndex = nodes.size() -1;
						}
						if(!nodes.get(currentNodesIndex).getIsRendered())
						{
							stopFlag = false;
							view.setIndexOfNodesList(currentNodesIndex);
						}
					}while(stopFlag);
				}
				else {

					boolean stopFlag = true;
					do{
						if(currentSelected - 1 < nodes.size())
						{
							if(currentSelected == -1)
							{
								currentSelected = nodes.size() -1;
							}
							else{
								currentSelected--;
							}	
		
						}
						else {
							currentSelected = -1;
						}
						if(currentSelected != nodes.size() && nodes.get(currentSelected).getIsRendered())
						{
							stopFlag = false;
							view.setIndexSelected(currentSelected);
						}
						else if(currentSelected == -1){
							stopFlag = false;
							view.setIndexSelected(currentSelected);
						}
					}while(stopFlag);
				}
			break;

			case KeyEvent.VK_ENTER:
				nodes.get(currentNodesIndex).setIsRendered(true);
				view.setIndexSelected(currentNodesIndex);
				if(currentNodesIndex + 1 < nodes.size())
				{
					currentNodesIndex++;

				}
				else {
					currentNodesIndex = 0;
				}
				if(!nodes.get(currentNodesIndex).getIsRendered())
				{
					view.setIndexOfNodesList(currentNodesIndex);
				}
				break;





			case KeyEvent.VK_NUMPAD5:
				p.x = 0.0;	p.y = 0.0;	break;

			case KeyEvent.VK_NUMPAD4:
			case KeyEvent.VK_LEFT:
				p.x -= a;	p.y += 0.0;	break;

			case KeyEvent.VK_NUMPAD6:
			case KeyEvent.VK_RIGHT:
				p.x += a;	p.y += 0.0;	break;

			case KeyEvent.VK_NUMPAD2:
			case KeyEvent.VK_DOWN:
				p.x += 0.0;	p.y -= a;	break;

			case KeyEvent.VK_NUMPAD8:
			case KeyEvent.VK_UP:
				p.x += 0.0;	p.y += a;	break;

			case KeyEvent.VK_NUMPAD1:
				p.x -= a;	p.y -= a;	break;

			case KeyEvent.VK_NUMPAD7:
				p.x -= a;	p.y += a;	break;

			case KeyEvent.VK_NUMPAD3:
				p.x += a;	p.y -= a;	break;

			case KeyEvent.VK_NUMPAD9:
				p.x += a;	p.y += a;	break;

			case KeyEvent.VK_DELETE:
				nodes.get(currentSelected).setIsRendered(false);
				boolean stopFlag = true;
				do{
					if(currentSelected + 1 < nodes.size())
					{
						currentSelected++;
					}
					else {
						currentSelected = 0;
					}

					if(nodes.get(currentSelected).getIsRendered())
					{
						stopFlag = false;
						view.setIndexOfNodesList(currentSelected);
					}

				}while(stopFlag);

				return;
		}

		view.setOrigin(p);
	}
}

//******************************************************************************
