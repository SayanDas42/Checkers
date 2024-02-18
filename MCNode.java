package edu.iastate.cs472.proj2;

/**
 * 
 * @author Sayan Das
 *
 */

/**
 * Node type for the Monte Carlo search tree.
 */

import java.util.*;
public class MCNode<E>
{
  // TODO 
  double u=0;
  int n=0;
  MCNode<E> parent=null;
  CheckersData board=null;
  int player=CheckersData.BLACK;
  public ArrayList<MCNode<E>> children=new ArrayList<MCNode<E>>();
  public void addChild(MCNode<E> child)
  {
    children.add(child);
  }
}

