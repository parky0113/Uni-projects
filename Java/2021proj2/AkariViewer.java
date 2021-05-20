/**
 * PROJECT TITLE: PUZZLE OF THE GAME AKARI(LIGHT UP)
 * PURPOSE OF PROJECT: DEVELOPE AND PRACTICE OF UNDERSTANDING IN JAVA PROGRAMMING
 * 
 * AkariViewer represents an interface for a player of Akari.
 * Start akariviewer object by selecting one of akari puzzle objects.
 * 
 * AUTHOR: SUNGHYUN(SEAN) PARK
 * DATE: 20-05-2021
 * V.1.00
 */

import java.awt.*;
import java.awt.event.*; 

public class AkariViewer implements MouseListener
{    
    private Akari puzzle;    // the internal representation of the puzzle
    private SimpleCanvas sc; // the display window
    int size;
    int tilesize;
    int offset   = 1;
    
    /**
     * Constructor for objects of class AkariViewer.
     * Sets all fields and displays the initial puzzle.
     */
    public AkariViewer(Akari puzzle)
    {
        // TODO 10
        this.puzzle = puzzle;
        size = puzzle.getSize();
        tilesize = 500/size; // if tilesize would be fixed than 25x25 board will be larger than monitor display
        sc = new SimpleCanvas("Akari", size * tilesize, (size+2) * tilesize, Color.white);
        sc.addMouseListener(this);
        sc.setFont(new Font("Times", 10, 225/size));
        displayPuzzle();
    }       
    
    /**
     * Selects from among the provided files in folder Puzzles. 
     * The number xyz selects pxy-ez.txt. 
     */
    public AkariViewer(int n)
    {
        this(new Akari("Puzzles/p" + n / 10 + "-e" + n % 10 + ".txt"));
    }
    
    /**
     * Uses the provided example file on the LMS page.
     */
    public AkariViewer()
    {
        this(77);
    }
    
    /**
     * Returns the internal state of the puzzle.
     */
    public Akari getPuzzle()
    {
        // TODO 9a
        return puzzle;
    }
    
    /**
     * Returns the canvas.
     */
    public SimpleCanvas getCanvas()
    {
        // TODO 9b
        return sc;
    }
    
    /**
     * Displays the initial puzzle; see the LMS page for a suggested layout. 
     */
    private void displayPuzzle()
    {
        // TODO 11
        Color c = Color.white;
        // drawing the grid line
        for (int i = 0; i <= size; i++)
            sc.drawLine(i * tilesize, 0, i * tilesize, size * tilesize, Color.black);
        for (int j = 0; j <= size; j++)
            sc.drawLine(0, j * tilesize, size * tilesize, j * tilesize, Color.black);
        
        // drawing the tiles
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
            {
                if (Space.isMutable(puzzle.getBoard(i,j))){
                    if (puzzle.getBoard(i,j) == Space.EMPTY) c = Color.white;
                    if (puzzle.getBoard(i,j) == Space.BULB) c = Color.yellow;
                    if (puzzle.canSeeBulb(i,j)) c = Color.yellow;
                }
                else  c = Color.black;
                drawTile (i, j, c);
            }
        
        // drawing buttons at bottom
        sc.drawString("SOLVED?", (int) (5), 
                            (int) (tilesize * (size + 0.5)), 
                            Color.black);
        sc.drawString("CLEAR", (int) (500 - tilesize*2), 
                            (int) (tilesize * (size + 0.5)), 
                            Color.black);
            
    }
    
    /**
     * this method helps displayPuzzle by creating tiles in the grids
     */
    private void drawTile(int x, int y, Color c)
    {
        sc.drawRectangle(y * tilesize + offset, x * tilesize + offset, 
                         (y + 1) * tilesize, (x + 1) * tilesize, 
                         c);
        if (!(Space.isMutable(puzzle.getBoard(x,y)))){
            addNumber(x, y, puzzle.getBoard(x,y));
        }
        if (puzzle.getBoard(x,y) == Space.BULB){
            if (puzzle.canSeeBulb(x,y)){
                sc.drawString("\uD83D\uDCA1", (int) (tilesize * (y + 0.275)), 
                            (int) (tilesize * (x + 0.65)), 
                            Color.red);
            }
            else{
                sc.drawString("\uD83D\uDCA1", (int) (tilesize * (y + 0.275)), 
                            (int) (tilesize * (x + 0.65)), 
                            Color.blue);
            }
        }
    }
    
    /**
     * this helper method put numbers on the BLACK tile
     */
    private void addNumber(int x, int y, Space s)
    {
           // it adds string top of the tile if it is one of numbered BLACK tiles 
           String k = "";
           if (s == Space.ZERO) k = "0";
           if (s == Space.ONE) k = "1";
           if (s == Space.TWO) k = "2";
           if (s == Space.THREE) k = "3";
           if (s == Space.FOUR) k = "4";
           sc.drawString(k, (int) (tilesize * (y + 0.35)), 
                            (int) (tilesize * (x + 0.65)), 
                            Color.white);
    } 
    
    /**
     * Performs a left click on the square at r,c if the indices are legal, o/w does nothing. 
     * Updates both puzzle and the display. 
     */
    public void leftClick(int r, int c)
    {
        // TODO 12
        if (puzzle.isLegal(r,c)){
            puzzle.leftClick(r,c);
            displayPuzzle();
        }
    }
    
    // TODO 13
    public void mousePressed (MouseEvent e) {}
    public void mouseClicked (MouseEvent e) {}
    
    /**
     * whenever one of tiles is clicked it perform leftClick
     * when SOLVED? clicked, it checks for isSolution and output string
     * when CLEAR clicked, it clear the mutable tiles in the board
     */
    public void mouseReleased(MouseEvent e) {
        // normal leftClick to turn on and off the BULB
        leftClick((e.getY()/tilesize),(e.getX()/tilesize));
        
        // it's when SOLVED? button get clicked by user
        // it overwrite previous warning message with white rectangle and print out new string from isSolution
        if (e.getY()/tilesize == size && (e.getX()/tilesize < 2)){
            sc.drawRectangle(0, (size+1)*tilesize , 
                         size * tilesize, (size+2) * tilesize, 
                         Color.white);
            String state = puzzle.isSolution();
            sc.drawString(state, (int) (5), 
                            (int) (tilesize * (size + 1.5)), 
                            Color.black);
        }
        
        // it activates when CLEAR button get clikced
        // it simply clears the board
        if (e.getY()/tilesize == size && (e.getX()/tilesize >= size-2)){
            puzzle.clear();
            displayPuzzle();
        }
    }
    public void mouseEntered (MouseEvent e) {}
    public void mouseExited  (MouseEvent e) {}
}
