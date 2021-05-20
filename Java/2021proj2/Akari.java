/**
 * PROJECT TITLE: PUZZLE OF THE GAME AKARI(LIGHT UP)
 * PURPOSE OF PROJECT: DEVELOPE AND PRACTICE OF UNDERSTANDING IN JAVA PROGRAMMING
 * 
 * Akari represents a single puzzle of the game Akari.
 * Start with creating akari by selecting one of pre-set text file.
 * 
 * AUTHOR: SUNGHYUN(SEAN) PARK
 * DATE: 20-05-2021
 * V.1.00
 */
import java.util.ArrayList;

public class Akari
{
    private String filename; // the name of the puzzle file
    private int size;        // the board is size x size
    private Space[][] board; // the board is a square grid of spaces of various types
    
    /**
     * Constructor for objects of class Akari. 
     * Creates and initialises the fields with the contents of filename. 
     * The layout of a puzzle file is described on the LMS page; 
     * you may assume the layout is always correct. 
     */
    public Akari(String filename)
    {
        // TODO 3
        this.filename = filename;
        FileIO fio = new FileIO(filename);
        size = Integer.parseInt(fio.getLines().get(0)); // first row of text file clariify the size of the board
        board = new Space[size][size];
        FillEmpty();
        FillBoard(fio.getLines().get(1),Space.BLACK); // other rows in text file clarify the characteristic of each tiles.
        FillBoard(fio.getLines().get(2),Space.ZERO);
        FillBoard(fio.getLines().get(3),Space.ONE);
        FillBoard(fio.getLines().get(4),Space.TWO);
        FillBoard(fio.getLines().get(5),Space.THREE);
        FillBoard(fio.getLines().get(6),Space.FOUR);
        
    }
    
    /**
     * Uses the example file from the LMS page.
     */
    public Akari()
    {
        this("Puzzles/p7-e7.txt");
    }
    
    /**
     * This helper method helps to fill board with given Space enum
     */
    private void FillBoard(String infos, Space status)
    {
        int row;
        int col;
        if (!(infos.isEmpty())){
            String[] contains = infos.split(" ");
            for(String info: contains){
                row = parseIndex(info.charAt(0));
                for (int i = 1; i < info.length(); i++){
                    col = parseIndex(info.charAt(i));
                    board[row][col] = status;
                }
            }
        }
    }
    
    /**
     * This helper method fill the board with EMPTY tiles
     */
    private void FillEmpty()
    {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++){
                board[i][j] = Space.EMPTY;
            }
    }
    
    /**
     * Returns the name of the puzzle file.
     */
    public String getFilename()
    {
        // TODO 1a
        return filename;
    }
    
    /**
     * Returns the size of the puzzle.
     */
    public int getSize()
    {
        // TODO 1b
        return size;
    }
    
    /**
     * Returns true iff k is a legal index into the board. 
     */
    public boolean isLegal(int k)
    {
        // TODO 5
        return k < size && k > -1 ; 
    }
    
    /**
     * Returns true iff r and c are both legal indices into the board. 
     */
    public boolean isLegal(int r, int c)
    {
        // TODO 6
        return (r < size && r > -1) && (c < size && c > -1); 
    }
    
    /**
     * Returns the contents of the square at r,c if the indices are legal, 
     * o/w throws an illegal argument exception. 
     */
    public Space getBoard(int r, int c)
    {
        // TODO 7
        if (!(isLegal(r,c))){
            throw new IllegalArgumentException("coordinate is out of range");
        }
        return board[r][c];
    }
    
    /**
     * Returns the int equivalent of x. 
     * If x is a digit, returns 0 -> 0, 1 -> 1, etc; 
     * if x is an upper-case letter, returns A -> 10, B -> 11, etc; 
     * o/w throws an illegal argument exception. 
     */
    public static int parseIndex(char x)
    {
        // TODO 2
        if (Character.isDigit(x)) return x - 48;
        if (Character.isUpperCase(x)) return x - 55;
        else {
            throw new IllegalArgumentException("x is neither digit nor upper-case");
        }
    }
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * If r,c is empty, a bulb is placed; if it has a bulb, that bulb is removed.
     */
    public void leftClick(int r, int c)
    {
        // TODO 8
        // by using try and catch it only activates when click is made inside of the board.
        try{
            if (Space.isMutable(board[r][c])){
                if (getBoard(r,c) == Space.EMPTY) board[r][c] = Space.BULB;
                else board[r][c] = Space.EMPTY;
            }
        } 
        catch (java.lang.ArrayIndexOutOfBoundsException e){
        }
    }
    
    /**
     * Sets all mutable squares on the board to empty.
     */
    public void clear()
    {
        // TODO 4
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++){
                if (Space.isMutable(board[i][j])) board[i][j] = Space.EMPTY;
            }
    }
    
    /**
     * Returns the number of bulbs adjacent to the square at r,c. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public int numberOfBulbs(int r, int c)
    {
        // TODO 14
        // used try and catch for easier check when r,c at the edge
        int num = 0;
        if (isLegal(r,c)){
            try{if((board[r-1][c]) == Space.BULB) num += 1;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){}
            try{if((board[r][c-1]) == Space.BULB) num += 1;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){}
            try{if((board[r+1][c]) == Space.BULB) num += 1;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){}
            try{if((board[r][c+1]) == Space.BULB) num += 1;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){}
            return num;
        }
        else {
            throw new IllegalArgumentException("r,c is not proper coordinate.");
        }
    }
    
    /**
     * Returns true iff the square at r,c is lit by a bulb elsewhere. 
     * Throws an illegal argument exception if r,c are illegal coordinates.
     */
    public boolean canSeeBulb(int r, int c)
    {
        // TODO 15
        // each while loop goes for each direction, checking if BULB is appeared
        // it breaks when it counter BLACK tiles
        Space s = getBoard(r,c);
        int i = 1;
        while (true){
            try{if((board[r-i][c]) == Space.BULB) return true;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            try{if(!(Space.isMutable(board[r-i][c]))) break;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            i ++;
        }
        i = 1;
        while (true){
            try{if((board[r][c-i]) == Space.BULB) return true;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            try{if(!(Space.isMutable(board[r][c-i]))) break;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            i ++;
        }
        i = 1;
        while (true){
            try{if((board[r+i][c]) == Space.BULB) return true;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            try{if(!(Space.isMutable(board[r+i][c]))) break;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            i ++;
        }
        i = 1;
        while (true){
            try{if((board[r][c+i]) == Space.BULB) return true;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            try{if(!(Space.isMutable(board[r][c+i]))) break;}
            catch(java.lang.ArrayIndexOutOfBoundsException e){break;}
            i ++;
        }
        
        return false;
    }
    
    /**
     * Returns an assessment of the state of the puzzle, either 
     * "Clashing bulb at r,c", 
     * "Unlit square at r,c", 
     * "Broken number at r,c", or
     * three ticks, as on the LMS page. 
     * r,c must be the coordinates of a square that has that kind of error. 
     * If there are multiple errors on the board, you may return any one of them. 
     */
    public String isSolution()
    {
        // TODO 16
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (Space.isMutable(getBoard(i,j))){
                    if (isClashing(i,j)) return "Clashing bulb at " + i + ","+ j;
                    if (isUnlit(i,j)) return "Unlit square at " + i + "," + j;
                }
                else {
                    if (!(isBroken(i,j))) return "Broken number at " + i + "," + j;
                }
            }
        return "\u2713\u2713\u2713";
    }
    
    /**
     * this method tells if puzzle violates bulbs clashing
     */
    private boolean isClashing(int r, int c)
    {
        return ((getBoard(r,c) == Space.BULB) && (canSeeBulb(r,c)));
    }
    
    /**
     * this method tells if there is unlit square
     */
    private boolean isUnlit(int r, int c)
    {
        return (!((getBoard(r,c) == Space.BULB)) && (!(canSeeBulb(r,c))));
    }
    
    /**
     * this method tells you if any BLACK block violates adjacent bulbs
     */
    private boolean isBroken(int r, int c)
    {
        int num = numberOfBulbs(r,c);
        if (getBoard(r,c) == Space.BLACK) return true;
        if ((getBoard(r,c) == Space.ZERO) && (num == 0)) return true;
        if ((getBoard(r,c) == Space.ONE) && (num == 1)) return true;
        if ((getBoard(r,c) == Space.TWO) && (num == 2)) return true;
        if ((getBoard(r,c) == Space.THREE) && (num == 3)) return true;
        if ((getBoard(r,c) == Space.FOUR) && (num == 4)) return true;
        return false;
    }
}