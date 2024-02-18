package edu.iastate.cs472.proj2;

/**
 * 
 * @author Sayan Das
 *
 */


/**
 * This class implements the Alpha-Beta pruning algorithm to find the best 
 * move at current state.
*/
public class AlphaBetaSearch extends AdversarialSearch {

    static int SEARCH_DEPTH=10, nm=0;
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
    private static final long MEGABYTE = 1024 * 1024;

    public static double bytesToMegabytes(double bytes) {
        return bytes / MEGABYTE;
    }
    public CheckersMove makeMove(CheckersMove[] legalMoves)
    {
        // The checker board state can be obtained from this.board,
        // which is a int 2D array. The numbers in the `board` are
        // defined as
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
        long startTime = System.currentTimeMillis();
        CheckersMove lmove=legalMoves[0];
        int f=0;
        for(int i=0;i<legalMoves.length;i++)
        {
            nm=0;
            CheckersData boardcopy=board.copyBoard();
            int value=MAX_VALUE(boardcopy, legalMoves[i], Integer.MIN_VALUE, Integer.MAX_VALUE,0);
            if(value==1)
            {
                f=1;
                lmove=legalMoves[i];
            }
            else if(value==0&&f==0)
                lmove=legalMoves[i];
        }
        return lmove;
    }
    
    // TODO
    // Implement your helper methods here.
    
    static int MAX_VALUE(CheckersData board, CheckersMove move, int alpha, int beta, int nummoves)
    {
        board.makeMove(move);
        int termstate=terminalTest(board);
        if(termstate!=-2)
            return termstate;
        nummoves++;
        if(nummoves>=SEARCH_DEPTH)
            return EVAL(board);
        int v=Integer.MIN_VALUE;
        CheckersMove[] legalMoves=board.getLegalMoves(CheckersData.RED);
        for(int i=0;i<legalMoves.length;i++)
        {
            CheckersData boardcopy=board.copyBoard();
            v=MAX(v,MIN_VALUE(boardcopy,legalMoves[i],alpha,beta,nummoves));
            if(v>=beta)
                return v;
            alpha=MAX(alpha,v);
        }
        return v;
    }

    static int MIN_VALUE(CheckersData board, CheckersMove move, int alpha, int beta, int nummoves)
    {
        board.makeMove(move);
        int termstate=terminalTest(board);
        if(termstate!=-2)
            return termstate;
        nummoves++;
        if(nummoves>=SEARCH_DEPTH)
            return EVAL(board);
        int v=Integer.MAX_VALUE;
        CheckersMove[] legalMoves=board.getLegalMoves(CheckersData.BLACK);
        for(int i=0;i<legalMoves.length;i++)
        {
            CheckersData boardcopy=board.copyBoard();
            v=MIN(v,MAX_VALUE(boardcopy,legalMoves[i],alpha,beta,nummoves));
            if(v<=alpha)
                return v;
            beta=MIN(beta,v);
        }
        return v;
    }

    static int EVAL(CheckersData board)
    {
        int r=0,b=0;
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(board.board[i][j]==CheckersData.RED)
                    r++;
                else if(board.board[i][j]==CheckersData.RED_KING)
                    r=r+2;
                else if(board.board[i][j]==CheckersData.BLACK)
                    b++;
                else if(board.board[i][j]==CheckersData.BLACK_KING)
                    b=b+2;
            }
            if(b>r)
                return 1;
            else if(r>b)
                return -1;
            else
                return 0;
        }
        return 0;
    }

    static int terminalTest(CheckersData board)
    {
        int player1=CheckersData.RED;
        int player2=CheckersData.BLACK;
        if(board.getLegalMoves(player1)==null&&board.getLegalMoves(player2)!=null)
            return 1;
        else if(board.getLegalMoves(player1)!=null&&board.getLegalMoves(player2)==null)
            return -1;
        else if(board.isEndgame()==true&&nm>=20)
            return 0;
        else if(board.isEndgame()==true&&nm<20)
        {
            nm++;
            return -2;
        }
        else
            return -2;
    }

    static int MAX(int a, int b)
    {
        if(a>=b)
            return a;
        else
            return b;
    }

    static int MIN(int a, int b)
    {
        if(a<=b)
            return a;
        else
            return b;
    }
}
