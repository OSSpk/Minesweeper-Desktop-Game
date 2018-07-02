package minesweeper;



public class Cell 
{
    private boolean mine;

    /*
     * The content of a field can be a...
     *  "" - indicating an unknown field
     *  "F" - a flagged field
     *  "M" - a mine
     *  a number ranging from 0 to 8 - indicating the number of surrounding mines
     */

    //Only the content of the Cell is visible to the player.
    private String content;

    //Number of adjacent surrounding mines
    private int surroundingMines;

    
    //----------------------------------------------------------//

    public Cell()
    {
        mine = false;
        content = "";
        surroundingMines = 0;
    }


    
    //-------------GETTERS AND SETTERS----------------------------//
    public boolean getMine()
    {
        return mine;
    }

    public void setMine(boolean mine)
    {
        this.mine = mine;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getSurroundingMines()
    {
        return surroundingMines;
    }

    public void setSurroundingMines(int surroundingMines)
    {
        this.surroundingMines = surroundingMines;
    }

    //-------------------------------------------------------------//
}
