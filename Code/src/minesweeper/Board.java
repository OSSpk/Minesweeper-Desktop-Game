package minesweeper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.util.Pair;



public class Board 
{
    private int numberOfMines;	
    private Cell cells[][];

    private int rows;
    private int cols;

        
    //---------------------------------------------//
    
    public Board(int numberOfMines, int r, int c)
    {
        this.rows = r;
        this.cols = c;
        this.numberOfMines = numberOfMines;

        cells = new Cell[rows][cols];

        //Step 1: First create a board with empty Cells
        createEmptyCells();         

        //Step 2: Then set mines randomly at cells
        setMines();

        //Step 3: Then set the number of surrounding mines("neighbours") at each cell
        setSurroundingMinesNumber();
    }


    //------------------------------------------------------------------//
    //STEP 1//
    public void createEmptyCells()
    {
        for (int x = 0; x < cols; x++)
        {
            for (int y = 0; y < rows; y++)
            {
                cells[x][y] = new Cell();
            }
        }
    }

    //------------------------------------------------------------------//
    //STEP 2//
    public void setMines()
    {
        int x,y;
        boolean hasMine;
        int currentMines = 0;                

        while (currentMines != numberOfMines)
        {
            // Generate a random x coordinate (between 0 and cols)
            x = (int)Math.floor(Math.random() * cols);

            // Generate a random y coordinate (between 0 and rows)
            y = (int)Math.floor(Math.random() * rows);

            hasMine = cells[x][y].getMine();

            if(!hasMine)
            {		
                cells[x][y].setMine(true);
                currentMines++;	
            }			
        }
    }
    //------------------------------------------------------------------//

    //------------------------------------------------------------------//
    //STEP 3//
    public void setSurroundingMinesNumber()
    {	
        for(int x = 0 ; x < cols ; x++) 
        {
            for(int y = 0 ; y < rows ; y++) 
            {
                cells[x][y].setSurroundingMines(calculateNeighbours(x,y));                        
            }
        }
    }
    //------------------------------------------------------------------//	




    //---------------------HELPER FUNCTIONS---------------------------//        

    //Calculates the number of surrounding mines ("neighbours")
    public int calculateNeighbours(int xCo, int yCo)
    {
        int neighbours = 0;

        // Check the neighbours (the columns xCo - 1, xCo, xCo + 1)
        for(int x=makeValidCoordinateX(xCo - 1); x<=makeValidCoordinateX(xCo + 1); x++) 
        {
            // Check the neighbours (the rows yCo - 1, yCo, yCo + 1).
            for(int y=makeValidCoordinateY(yCo - 1); y<=makeValidCoordinateY(yCo + 1); y++) 
            {
                // Skip (xCo, yCo), since that's no neighbour.
                if(x != xCo || y != yCo)
                    if(cells[x][y].getMine())   // If the neighbour contains a mine, neighbours++.
                        neighbours++;
            }
        }

        return neighbours;
    }

    //------------------------------------------------------------------//	

    //Simply makes a coordinate a valid one (i.e within the boundaries of the Board)
    public int makeValidCoordinateX(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > cols-1)
            i = cols-1;

        return i;
    }	
    
    //Simply makes a coordinate a valid one (i.e within the boundaries of the Board)
    public int makeValidCoordinateY(int i)
    {
        if (i < 0)
            i = 0;
        else if (i > rows-1)
            i = rows-1;

        return i;
    }	
    
    //------------------------------------------------------------------//	        

    //-------------DATA BASE------------------------//
    
    // to check whether there is a save game or not
    public boolean checkSave()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        boolean saveExists = false;

        try {
            String dbURL = Game.dbPath; 
            
            connection = DriverManager.getConnection(dbURL); 
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM GAME_STATE");
            
            while(resultSet.next()) 
            {
                saveExists = true;
            }
            
            // cleanup resources, once after processing
            resultSet.close();
            statement.close();
                       
            // and then finally close connection
            connection.close();            
            
            return saveExists;
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
            return false;
        }        
    }
    
    //--------------LOAD SAVED GAME-----------------//
    
    
    public Pair loadSaveGame()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            String dbURL = Game.dbPath; 
            
            connection = DriverManager.getConnection(dbURL); 
            
            //--------------Load Cells State-------------------//
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM CELL");

            for(int x = 0 ; x < cols ; x++) 
            {
                for(int y = 0 ; y < rows ; y++) 
                {                                        
                    resultSet.next();
                    
                    cells[x][y].setContent(resultSet.getString("CONTENT"));
                    cells[x][y].setMine(resultSet.getBoolean("MINE"));
                    cells[x][y].setSurroundingMines(resultSet.getInt("SURROUNDING_MINES"));                    
                }
            }
            
            statement.close();
            resultSet.close();
            //----------------------------------------------------//

            //---------------Load Game Variables-----------------//
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM GAME_STATE");

            resultSet.next();
                        
            Pair p = new Pair(resultSet.getInt("TIMER"),resultSet.getInt("MINES"));
            
            //After loading, delete the saved game
            deleteSavedGame();
            
            // cleanup resources, once after processing
            resultSet.close();
            statement.close();
                       
            // and then finally close connection
            connection.close();

            return p;
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
            return null;
        }                
    }
    
    
    //------------------------------------------------------------------------//
    public void deleteSavedGame()
    {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            String dbURL = Game.dbPath; 
            
            connection = DriverManager.getConnection(dbURL); 

            
            //----------EMPTY GAME_STATE TABLE------//
            String template = "DELETE FROM GAME_STATE"; 
            statement = connection.prepareStatement(template);
            statement.executeUpdate();
            
            //----------EMPTY CELL TABLE------//
            template = "DELETE FROM CELL"; 
            statement = connection.prepareStatement(template);
            statement.executeUpdate();
            
            statement.close();
            
            // and then finally close connection
            connection.close();            
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
        }                
    }
    
           
    //--------------SAVE GAME IN DATABASE-----------//
    public void saveGame(int timer, int mines)
    {
        Connection connection = null;
        PreparedStatement statement = null;
        
        try {
            String dbURL = Game.dbPath; 
            
            connection = DriverManager.getConnection(dbURL); 

            
            //--------------INSERT DATA INTO CELL TABLE-----------//            
            String template = "INSERT INTO CELL (CONTENT, MINE, SURROUNDING_MINES) values (?,?,?)";
            statement = connection.prepareStatement(template);

            for(int x = 0 ; x < cols ; x++) 
            {
                for(int y = 0 ; y < rows ; y++) 
                {
                    statement.setString(1, cells[x][y].getContent());
                    statement.setBoolean(2, cells[x][y].getMine());
                    statement.setInt(3, (int)cells[x][y].getSurroundingMines());                    

                    statement.executeUpdate();
                }
            }
            //--------------------------------------------------//

            
            //--------------------SAVE GAME STATE----------------------//
            template = "INSERT INTO GAME_STATE (TIMER,MINES) values (?,?)";
            statement = connection.prepareStatement(template);
            
            statement.setInt(1, timer);
            statement.setInt(2, mines);

            statement.executeUpdate();
            
            //---------------------------------------------------------//
            
            statement.close();
            
            // and then finally close connection
            connection.close();            
        }
        catch(SQLException sqlex)
        {
            sqlex.printStackTrace();
        }
        
    }
    
    
    
    //--------------------------------------------//
    //---------GETTERS AND SETTERS-------------//
    public void setNumberOfMines(int numberOfMines)
    {
        this.numberOfMines = numberOfMines;
    }

    public int getNumberOfMines()
    {
        return numberOfMines;
    }

    public Cell[][] getCells()
    {
        return cells;
    }
    
    public int getRows()
    {
        return rows;
    }
    
    public int getCols()
    {
        return cols;
    }
    //-----------------------------------------//

    public void resetBoard()
    {
        for(int x = 0 ; x < cols ; x++) 
        {
            for(int y = 0 ; y < rows ; y++) 
            {
                cells[x][y].setContent("");                        
            }
        }
    }
    
}
