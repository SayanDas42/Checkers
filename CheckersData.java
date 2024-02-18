package edu.iastate.cs472.proj2;

/**
 * 
 * @author Sayan Das
 *
 */

import java.util.ArrayList;
import java.util.Arrays;

/**
 * An object of this class holds data about a game of checkers.
 * It knows what kind of piece is on each square of the checkerboard.
 * Note that RED moves "up" the board (i.e. row number decreases)
 * while BLACK moves "down" the board (i.e. row number increases).
 * Methods are provided to return lists of available legal moves.
 */
public class CheckersData {

  /*  The following constants represent the possible contents of a square
      on the board.  The constants RED and BLACK also represent players
      in the game. */

    static final int
            EMPTY = 0,
            RED = 1,
            RED_KING = 2,
            BLACK = 3,
            BLACK_KING = 4;


    int[][] board;  // board[r][c] is the contents of row r, column c.

    /**
     * Constructor.  Create the board and set it up for a new game.
     */
    CheckersData() {
        board = new int[8][8];
        setUpGame();
    }

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < board.length; i++) {
            int[] row = board[i];
            sb.append(8 - i).append(" ");
            for (int n : row) {
                if (n == 0) {
                    sb.append(" ");
                } else if (n == 1) {
                    sb.append(ANSI_RED + "R" + ANSI_RESET);
                } else if (n == 2) {
                    sb.append(ANSI_RED + "K" + ANSI_RESET);
                } else if (n == 3) {
                    sb.append(ANSI_YELLOW + "B" + ANSI_RESET);
                } else if (n == 4) {
                    sb.append(ANSI_YELLOW + "K" + ANSI_RESET);
                }
                sb.append(" ");
            }
            sb.append(System.lineSeparator());
        }
        sb.append("  a b c d e f g h");

        return sb.toString();
    }

    /**
     * Set up the board with checkers in position for the beginning
     * of a game.  Note that checkers can only be found in squares
     * that satisfy  row % 2 == col % 2.  At the start of the game,
     * all such squares in the first three rows contain black squares
     * and all such squares in the last three rows contain red squares.
     */
    void setUpGame() {
        // TODO
    	//
    	// Set up the board with pieces BLACK, RED, and EMPTY
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(i%2==j%2)
                {
                    if(i==0||i==1||i==2)
                        this.board[i][j]=BLACK;
                    else if(i==5||i==6||i==7)
                        this.board[i][j]=RED;
                    else
                        this.board[i][j]=EMPTY;
                }
                else
                    this.board[i][j]=EMPTY;
            }
        }
    }


    /**
     * Return the contents of the square in the specified row and column.
     */
    int pieceAt(int row, int col) {
        return board[row][col];
    }


    /**
     * Make the specified move.  It is assumed that move
     * is non-null and that the move it represents is legal.
     *
     * Make a single move or a sequence of jumps
     * recorded in rows and cols.
     *
     */
    void makeMove(CheckersMove move) {
        int l = move.rows.size();
        for(int i = 0; i < l-1; i++)
            makeMove(move.rows.get(i), move.cols.get(i), move.rows.get(i+1), move.cols.get(i+1));
    }


    /**
     * Make the move from (fromRow,fromCol) to (toRow,toCol).  It is
     * assumed that this move is legal.  If the move is a jump, the
     * jumped piece is removed from the board.  If a piece moves to
     * the last row on the opponent's side of the board, the
     * piece becomes a king.
     *
     * @param fromRow row index of the from square
     * @param fromCol column index of the from square
     * @param toRow   row index of the to square
     * @param toCol   column index of the to square
     */
    void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO
    	// 
    	// Update the board for the given move. You need to take care of the following situations:
        // 1. move the piece from (fromRow,fromCol) to (toRow,toCol)
        // 2. if this move is a jump, remove the captured piece
        // 3. if the piece moves into the kings row on the opponent's side of the board, crowned it as a king
        if(fromRow-toRow==2||fromRow-toRow==-2)
        {
            if(this.board[fromRow][fromCol]==RED)
                this.board[toRow][toCol]=RED;
            else if(this.board[fromRow][fromCol]==BLACK)
                this.board[toRow][toCol]=BLACK;
            else if(this.board[fromRow][fromCol]==RED_KING)
                this.board[toRow][toCol]=RED_KING;
            else if(this.board[fromRow][fromCol]==BLACK_KING)
                this.board[toRow][toCol]=BLACK_KING;
            this.board[fromRow][fromCol]=EMPTY;
            if(toRow<fromRow&&toCol<fromCol)
                this.board[fromRow-1][fromCol-1]=EMPTY;
            else if(toRow<fromRow&&toCol>fromCol)
                this.board[fromRow-1][fromCol+1]=EMPTY;
            else if(toRow>fromRow&&toCol<fromCol)
                this.board[fromRow+1][fromCol-1]=EMPTY;
            else if(toRow>fromRow&&toCol>fromCol)
                this.board[fromRow+1][fromCol+1]=EMPTY;
        }
        else
        {
            if(this.board[fromRow][fromCol]==RED)
                this.board[toRow][toCol]=RED;
            else if(this.board[fromRow][fromCol]==BLACK)
                this.board[toRow][toCol]=BLACK;
            else if(this.board[fromRow][fromCol]==RED_KING)
                this.board[toRow][toCol]=RED_KING;
            else if(this.board[fromRow][fromCol]==BLACK_KING)
                this.board[toRow][toCol]=BLACK_KING;
            this.board[fromRow][fromCol]=EMPTY;
        }
        if(this.board[toRow][toCol]==RED&&toRow==0)
            this.board[toRow][toCol]=RED_KING;
        else if(this.board[toRow][toCol]==BLACK&&toRow==7)
            this.board[toRow][toCol]=BLACK_KING;
    }   

    ArrayList<CheckersMove> jumps;
    CheckersMove move=new CheckersMove();

    /**
     * Return an array containing all the legal CheckersMoves
     * for the specified player on the current board.  If the player
     * has no legal moves, null is returned.  The value of player
     * should be one of the constants RED or BLACK; if not, null
     * is returned.  If the returned value is non-null, it consists
     * entirely of jump moves or entirely of regular moves, since
     * if the player can jump, only jumps are legal moves.
     *
     * @param player color of the player, RED or BLACK
     */
    CheckersMove[] getLegalMoves(int player) {
        // TODO
        if(player!=RED&&player!=BLACK)
            return null;
        ArrayList<CheckersMove> legalmoves=new ArrayList<CheckersMove>();
        CheckersMove totaljumps[]=new CheckersMove[0];
        CheckersMove temp[]=new CheckersMove[0];
        int jumpflag=0,k=0;
        if(player==RED)
        {
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(this.board[i][j]==RED)
                    {
                        jumps=new ArrayList<CheckersMove>();
                        move=new CheckersMove();
                        CheckersMove legaljumps[]=this.getLegalJumpsFrom(RED, i, j);
                        if(legaljumps!=null)
                        {
                            jumpflag=1;
                            temp=new CheckersMove[totaljumps.length+legaljumps.length];
                            System.arraycopy(totaljumps,0,temp,0,totaljumps.length);
                            System.arraycopy(legaljumps,0,temp,totaljumps.length,legaljumps.length);
                            totaljumps=new CheckersMove[temp.length];
                            System.arraycopy(temp,0,totaljumps,0,temp.length);
                        }
                        else if(jumpflag==0)
                        {
                            if(i-1>=0&&j-1>=0&&this.board[i-1][j-1]==EMPTY)
                            {
                                legalmoves.add(new CheckersMove(i,j,i-1,j-1));
                            }
                            if(i-1>=0&&j+1<=7&&this.board[i-1][j+1]==EMPTY)
                            {
                                legalmoves.add(new CheckersMove(i,j,i-1,j+1));
                            }
                        }
                    }
                    else if(this.board[i][j]==RED_KING)
                    {
                        jumps=new ArrayList<CheckersMove>();
                        move=new CheckersMove();
                        CheckersMove legaljumps[]=this.getLegalJumpsFrom(RED_KING, i, j);
                        if(legaljumps!=null)
                        {
                            jumpflag=1;
                            temp=new CheckersMove[totaljumps.length+legaljumps.length];
                            System.arraycopy(totaljumps,0,temp,0,totaljumps.length);
                            System.arraycopy(legaljumps,0,temp,totaljumps.length,legaljumps.length);
                            totaljumps=new CheckersMove[temp.length];
                            System.arraycopy(temp,0,totaljumps,0,temp.length);
                        }
                        else if(jumpflag==0)
                        {
                            if(i-1>=0&&j-1>=0)
                                {
                                    if(this.board[i-1][j-1]==EMPTY)
                                    {
                                        legalmoves.add(new CheckersMove(i,j,i-1,j-1));
                                    }
                                }
                            if(i-1>=0&&j+1<=7)
                            {
                                if(this.board[i-1][j+1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i-1,j+1));
                                    
                                }
                            }
                            if(i+1<=7&&j-1>=0)
                                {
                                    if(this.board[i+1][j-1]==EMPTY)
                                    {
                                        legalmoves.add(new CheckersMove(i,j,i+1,j-1));
                                    }
                                }
                            if(i+1<=7&&j+1<=7)
                            {
                                if(this.board[i+1][j+1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i+1,j+1));
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if(this.board[i][j]==BLACK)
                    {
                        jumps=new ArrayList<CheckersMove>();
                        move=new CheckersMove();
                        CheckersMove legaljumps[]=this.getLegalJumpsFrom(BLACK, i, j);
                        if(legaljumps!=null)
                        {
                            jumpflag=1;
                            temp=new CheckersMove[totaljumps.length+legaljumps.length];
                            System.arraycopy(totaljumps,0,temp,0,totaljumps.length);
                            System.arraycopy(legaljumps,0,temp,totaljumps.length,legaljumps.length);
                            totaljumps=new CheckersMove[temp.length];
                            System.arraycopy(temp,0,totaljumps,0,temp.length);
                        }
                       else if(jumpflag==0)
                       {
                            if(i+1<=7&&j-1>=0)
                            {
                                if(this.board[i+1][j-1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i+1,j-1));  
                                }
                            }
                            if(i+1<=7&&j+1<=7)
                            {
                                if(this.board[i+1][j+1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i+1,j+1));
                                }
                            }
                       }
                    }
                    else if(this.board[i][j]==BLACK_KING)
                    {
                        jumps=new ArrayList<CheckersMove>();
                        move=new CheckersMove();
                        CheckersMove legaljumps[]=this.getLegalJumpsFrom(BLACK_KING, i, j);
                        if(legaljumps!=null)
                        {
                            jumpflag=1;
                            temp=new CheckersMove[totaljumps.length+legaljumps.length];
                            System.arraycopy(totaljumps,0,temp,0,totaljumps.length);
                            System.arraycopy(legaljumps,0,temp,totaljumps.length,legaljumps.length);
                            totaljumps=new CheckersMove[temp.length];
                            System.arraycopy(temp,0,totaljumps,0,temp.length);
                        }
                        else if(jumpflag==0)
                        {
                            if(i-1>=0&&j-1>=0)
                                {
                                    if(this.board[i-1][j-1]==EMPTY)
                                    {
                                        legalmoves.add(new CheckersMove(i,j,i-1,j-1));
                                    }
                                }
                            if(i-1>=0&&j+1<=7)
                            {
                                if(this.board[i-1][j+1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i-1,j+1));
                                }
                            }
                            if(i+1<=7&&j-1>=0)
                                {
                                    if(this.board[i+1][j-1]==EMPTY)
                                    {
                                        legalmoves.add(new CheckersMove(i,j,i+1,j-1));
                                    }
                                }
                            if(i+1<=7&&j+1<=7)
                            {
                                if(this.board[i+1][j+1]==EMPTY)
                                {
                                    legalmoves.add(new CheckersMove(i,j,i+1,j+1));
                                }
                            }
                        }
                    }
                }
            }
        }
        if(legalmoves.size()==0&&jumpflag==0)
            return null;
        if(jumpflag!=0)
            return totaljumps;
        CheckersMove legmoves[]=new CheckersMove[legalmoves.size()];
        legalmoves.toArray(legmoves);
        return legmoves;
    }


    /**
     * Return a list of the legal jumps that the specified player can
     * make starting from the specified row and column.  If no such
     * jumps are possible, null is returned.  The logic is similar
     * to the logic of the getLegalMoves() method.
     *
     * Note that each CheckerMove may contain multiple jumps. 
     * Each move returned in the array represents a sequence of jumps 
     * until no further jump is allowed.
     *
     * @param player The player of the current jump, either RED or BLACK.
     * @param row    row index of the start square.
     * @param col    col index of the start square.
     */
    CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
        // TODO 
        move.addMove(row, col);
        if(player==RED)
        {
            int x1=0,x2=0;
            if(row-2>=0&&col-2>=0&&!(move.rows.contains(row-2)&&move.cols.contains(col-2)))
            {
                if(this.board[row-2][col-2]==EMPTY&&(this.board[row-1][col-1]==BLACK||this.board[row-1][col-1]==BLACK_KING))
                {
                    x1=1;
                    this.getLegalJumpsFrom(player,row-2,col-2);
                }
            }
            if(row-2>=0&&col+2<=7&&!(move.rows.contains(row-2)&&move.cols.contains(col+2)))
            {
                if(this.board[row-2][col+2]==EMPTY&&(this.board[row-1][col+1]==BLACK||this.board[row-1][col+1]==BLACK_KING))
                {
                    x2=1;
                    this.getLegalJumpsFrom(player,row-2,col+2);
                }
            }
            if(x1==0&&x2==0&&move.rows.size()>1)
            {
                jumps.add(move.clone());
                move.rows.remove(move.rows.size()-1);
                move.cols.remove(move.cols.size()-1);
            }
            if(jumps.size()>0)
            {
                CheckersMove legaljumps[]= new CheckersMove[jumps.size()];
                jumps.toArray(legaljumps);
                return legaljumps;
            }
            else
                return null;
        }
        else if(player==RED_KING)
        {
            int x1=0,x2=0,x3=0,x4=0;
            if(row-2>=0&&col-2>=0&&!(move.rows.contains(row-2)&&move.cols.contains(col-2)))
            {
                if(this.board[row-2][col-2]==EMPTY&&(this.board[row-1][col-1]==BLACK||this.board[row-1][col-1]==BLACK_KING))
                {
                    x1=1;
                    this.getLegalJumpsFrom(player,row-2,col-2);
                }
            }
            if(row-2>=0&&col+2<=7&&!(move.rows.contains(row-2)&&move.cols.contains(col+2)))
            {
                if(this.board[row-2][col+2]==EMPTY&&(this.board[row-1][col+1]==BLACK||this.board[row-1][col+1]==BLACK_KING))
                {
                    x2=1;
                    this.getLegalJumpsFrom(player,row-2,col+2);
                }
            }
            if(row+2<=7&&col-2>=0&&!(move.rows.contains(row+2)&&move.cols.contains(col-2)))
            {
                if(this.board[row+2][col-2]==EMPTY&&(this.board[row+1][col-1]==BLACK||this.board[row+1][col-1]==BLACK_KING))
                {
                    x3=1;
                    this.getLegalJumpsFrom(player,row+2,col-2);
                }
            }
            if(row+2<=7&&col+2<=7&&!(move.rows.contains(row+2)&&move.cols.contains(col+2)))
            {
                if(this.board[row+2][col+2]==EMPTY&&(this.board[row+1][col+1]==BLACK||this.board[row+1][col+1]==BLACK_KING))
                {
                    x4=1;
                    this.getLegalJumpsFrom(player,row+2,col+2);
                }
            }
            if(x1==0&&x2==0&&x3==0&&x4==0&&move.rows.size()>1)
                jumps.add(move.clone());
            move.rows.remove(move.rows.size()-1);
            move.cols.remove(move.cols.size()-1);
            if(jumps.size()>0)
            {
                CheckersMove legaljumps[]= new CheckersMove[jumps.size()];
                jumps.toArray(legaljumps);
                return legaljumps;
            }
            else
                return null;
        }
        else if(player==BLACK)
        {
            int x1=0,x2=0;
            if(row+2<=7&&col-2>=0&&!(move.rows.contains(row+2)&&move.cols.contains(col-2)))
            {
                if(this.board[row+2][col-2]==EMPTY&&(this.board[row+1][col-1]==RED||this.board[row+1][col-1]==RED_KING))
                {
                    x1=1;
                    this.getLegalJumpsFrom(player,row+2,col-2);
                }
            }
            if(row+2<=7&&col+2<=7&&!(move.rows.contains(row+2)&&move.cols.contains(col+2)))
            {
                if(this.board[row+2][col+2]==EMPTY&&(this.board[row+1][col+1]==RED||this.board[row+1][col+1]==RED_KING))
                {
                    x2=1;
                    this.getLegalJumpsFrom(player,row+2,col+2);
                }
            }
            if(x1==0&&x2==0&&move.rows.size()>1)
            {
                jumps.add(move.clone());
                move.rows.remove(move.rows.size()-1);
                move.cols.remove(move.cols.size()-1);
            }
            if(jumps.size()>0)
            {
                CheckersMove legaljumps[]= new CheckersMove[jumps.size()];
                jumps.toArray(legaljumps);
                return legaljumps;
            }
            else
                return null;
        }
        else if(player==BLACK_KING)
        {
            int x1=0,x2=0,x3=0,x4=0;
            if(row-2>=0&&col-2>=0&&!(move.rows.contains(row-2)&&move.cols.contains(col-2)))
            {
                if(this.board[row-2][col-2]==EMPTY&&(this.board[row-1][col-1]==RED||this.board[row-1][col-1]==RED_KING))
                {
                    x1=1;
                    this.getLegalJumpsFrom(player,row-2,col-2);
                }
            }
            if(row-2>=0&&col+2<=7&&!(move.rows.contains(row-2)&&move.cols.contains(col+2)))
            {
                if(this.board[row-2][col+2]==EMPTY&&(this.board[row-1][col+1]==RED||this.board[row-1][col+1]==RED_KING))
                {
                    x2=1;
                    this.getLegalJumpsFrom(player,row-2,col+2);
                }
            }
            if(row+2<=7&&col-2>=0&&!(move.rows.contains(row+2)&&move.cols.contains(col-2)))
            {
                if(this.board[row+2][col-2]==EMPTY&&(this.board[row+1][col-1]==RED||this.board[row+1][col-1]==RED_KING))
                {
                    x3=1;
                    this.getLegalJumpsFrom(player,row+2,col-2);
                }
            }
            if(row+2<=7&&col+2<=7&&!(move.rows.contains(row+2)&&move.cols.contains(col+2)))
            {
                if(this.board[row+2][col+2]==EMPTY&&(this.board[row+1][col+1]==RED||this.board[row+1][col+1]==RED_KING))
                {
                    x4=1;
                    this.getLegalJumpsFrom(player,row+2,col+2);
                }
            }
            if(x1==0&&x2==0&&x3==0&&x4==0&&move.rows.size()>1)
            {
                jumps.add(move.clone());
                move.rows.remove(move.rows.size()-1);
                move.cols.remove(move.cols.size()-1);
            }
            if(jumps.size()>0)
            {
                CheckersMove legaljumps[]= new CheckersMove[jumps.size()];
                jumps.toArray(legaljumps);
                return legaljumps;
            }
            else
                return null;
        }
        return null;
    }

    CheckersData copyBoard()
    {
        CheckersData new_board = new CheckersData();
        for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                new_board.board[i][j]=this.pieceAt(i, j);
            }
        }
        return new_board;
    }

    boolean isEndgame()
    {
        int r=0,b=0;
         for(int i=0;i<8;i++)
        {
            for(int j=0;j<8;j++)
            {
                if(this.board[i][j]==CheckersData.RED)
                    r++;
                else if(this.board[i][j]==CheckersData.RED_KING)
                    r++;
                else if(this.board[i][j]==CheckersData.BLACK)
                    b++;
                else if(this.board[i][j]==CheckersData.BLACK_KING)
                    b++;
            }
        }
        if(r==1&&b==1)
            return true;
        else
            return false;
    }

}
