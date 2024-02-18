package edu.iastate.cs472.proj2;

import java.util.*;
import java.util.Random;

/**
 * 
 * @author Sayan Das
 *
 */

/**
 * This class implements the Monte Carlo tree search method to find the best
 * move at the current state.
 */
public class MonteCarloTreeSearch extends AdversarialSearch {

    static final double C=Math.sqrt(2);
    static int nm=0;
	/**
     * The input parameter legalMoves contains all the possible moves.
     * It contains four integers:  fromRow, fromCol, toRow, toCol
     * which represents a move from (fromRow, fromCol) to (toRow, toCol).
     * It also provides a utility method `isJump` to see whether this
     * move is a jump or a simple move.
     *
     * Each legalMove in the input now contains a single move
     * or a sequence of jumps: (rows[0], cols[0]) -> (rows[1], cols[1]) ->
     * (rows[2], cols[2]).
     *
     * @param legalMoves All the legal moves for the agent at current step.
     */
    private static final long MEGABYTE = 1024L * 1024L;

    public static double bytesToMegabytes(double bytes) {
        return bytes / MEGABYTE;
    }
    public CheckersMove makeMove(CheckersMove[] legalMoves) {
        // The checker board state can be obtained from this.board,
        // which is an 2D array of the following integers defined below:
    	// 
        // 0 - empty square,
        // 1 - red man
        // 2 - red king
        // 3 - black man
        // 4 - black king
        System.out.println(board);
        System.out.println();

        // TODO 
        
        // Return the move for the current state.
        // Here, we simply return the first legal move for demonstration.
        CheckersMove lmove=legalMoves[0];
        MCTree<Integer> tree=new MCTree<Integer>();
        MCNode<Integer> node=new MCNode<Integer>();
        tree.root=node;
        tree.root.player=CheckersData.BLACK;
        for(int i=0;i<legalMoves.length;i++)
        {
            MCNode<Integer> child=new MCNode<Integer>();
            CheckersData boardcopy=board.copyBoard();
            boardcopy.makeMove(legalMoves[i]);
            child.board=boardcopy;
            child.player=CheckersData.RED;
            child.parent=tree.root;
            tree.root.addChild(child);
        }
        int N=1000;
        while(N>0)
        {
            MCNode<Integer> leaf=selection(tree);
            expansion(leaf);
            double result=simulation(leaf);
            if(result<0)
                result=0;
            backPropagation(result,leaf);
            N--;
        }
        int maxn=Integer.MIN_VALUE;
        double maxu=Double.MIN_VALUE;
        for(int i=0;i<tree.root.children.size();i++)
        {
            if(maxn<tree.root.children.get(i).n)
            {
                maxu=tree.root.children.get(i).u;
                maxn=tree.root.children.get(i).n;
                lmove=legalMoves[i];
            }

            else if(maxn==tree.root.children.get(i).n)
            {
                if(maxu<tree.root.children.get(i).u)
                {
                    maxu=tree.root.children.get(i).u;
                    lmove=legalMoves[i];
                }
            }
        }
        return lmove;
    }
    
    // TODO
    // 
    // Implement your helper methods here. They include at least the methods for selection,  
    // expansion, simulation, and back-propagation. 
    // 
    // For representation of the search tree, you are suggested (but limited) to use a 
    // child-sibling tree already implemented in the two classes MCTree and MCNode (which  
    // you may feel free to modify).  If you decide not to use the child-sibling tree, simply 
    // remove these two classes. 
    // 

    static double ucb(MCNode<Integer> node)
    {
        if(node.n==0)
            return Double.MAX_VALUE;
        else
        {
            double v=(node.u/node.n) + C * Math.sqrt(Math.log(node.parent.n)/node.n);
            return v;
        }
    }

    private static MCNode<Integer> selection(MCTree<Integer> tree)
    {
        MCNode<Integer> current=tree.root;
        double max=Double.MIN_VALUE;
        MCNode<Integer> temp=current.children.get(0);
        for(int i=0;i<current.children.size();i++)
        {
            MCNode<Integer> childnode=current.children.get(i);
            double u=ucb(childnode);
            if(max<u)
            {
                max=u;
                temp=childnode;
            }
        }
        if(terminalTest(temp.board)==-2&&temp.n>=1)
        {
            MCTree<Integer> t=new MCTree<Integer>();
            t.root=temp;
            temp=selection(t);
        }
        return temp;
    }

    private static void expansion(MCNode<Integer> node)
    {
        if(terminalTest(node.board)!=-2)
            return;
        CheckersMove legalMoves[]=node.board.getLegalMoves(node.player);
        for(int i=0;i<legalMoves.length;i++)
        {
            MCNode<Integer> child=new MCNode<Integer>();
            CheckersData boardcopy=node.board.copyBoard();
            boardcopy.makeMove(legalMoves[i]);
            child.board=boardcopy;
            if(node.player==CheckersData.BLACK)
                child.player=CheckersData.RED;
            else
                child.player=CheckersData.BLACK;
            child.parent=node;
            node.addChild(child);
        }
    }

    private static double simulation(MCNode<Integer> node)
    {
        CheckersData boardcopy=node.board.copyBoard();
        int player=node.player;
        nm=0;
        while(terminalTest(boardcopy)==-2)
        {
            CheckersMove moves[]=boardcopy.getLegalMoves(player);
            int rnd = new Random().nextInt(moves.length);
            boardcopy.makeMove(moves[rnd]);
            if(player==CheckersData.BLACK)
                player=CheckersData.RED;
            else
                player=CheckersData.BLACK;
        }
        return terminalTest(boardcopy);
    }

    private static void backPropagation(double result, MCNode<Integer> node)
    {
        while(true)
        {
            node.u=node.u+result;
            node.n=node.n+1;
            node=node.parent;
            if(node.parent==null)
            {
                node.u=node.u+result;
                node.n=node.n+1;
                break;
            }
        }
    }

    static double terminalTest(CheckersData board)
    {
        int player1=CheckersData.RED;
        int player2=CheckersData.BLACK;
        if(board.getLegalMoves(player1)==null&&board.getLegalMoves(player2)!=null)
            return 1;
        else if(board.getLegalMoves(player1)!=null&&board.getLegalMoves(player2)==null)
            return -1;
        else if(board.isEndgame()==true&&nm>=20)
            return 0.5;
        else if(board.isEndgame()==true&&nm<20)
        {
            nm++;
            return -2;
        }
        else
            return -2;
    }
}
