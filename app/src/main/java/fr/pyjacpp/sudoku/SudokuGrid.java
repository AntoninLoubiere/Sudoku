package fr.pyjacpp.sudoku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/*
  *
  * By mfgravesjr, from his github
  * (https://github.com/mfgravesjr/finished-projects/blob/master/SudokuGridGenerator)
  *
  * Modify by me (generateGrid, printGrid, and isPerfect by him)
  *
  * per box formula
  *
  *  this formula goes through each box instead of the natural order
  * ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3)
  *
  * get box origin formula
  * this formula gives the index of the origin of the box that contains index i (0-80)
  * ((i % 9) / 3) * 3 + (i / 27) * 27
  *
  * get row origin formula
  * this formula gives the index of the origin of the row that contains index i (0-80)
  * (i / 9) * 9
  *
  * get column origin formula
  * this formula gives the index of the origin of the column that contains index i (0-80)
  * i % 9
  *
  * get box origin formula
  * this formula gives the index of origin of box # i (0-8)
  * (i * 3) % 9 + ((i * 3) / 9) * 27
  *
  * get row origin formula
  * this formula gives the index of origin of row # i (0-8)
  * i*9
  *
  * get box origin formula
  * this formula gives the index of origin of column # i (0-8)
  * i
  *
  * box step formula
  * this formula runs through a box shape (i must be less than 9)
  * boxOrigin + (i / 3) * 9 + (i % 3)
  *
  * row step formula
  * rowOrigin + i
  *
  * col step formula
  * colOrigin + i*9
  */

class SudokuGrid
{
    private SudokuNumbersEnum[][] sudokuGrid = new SudokuNumbersEnum[9][9];
    private SudokuNumbersEnum[][] userModify = new SudokuNumbersEnum[9][9];
    private int numberTileToRemove;
    private int numberTileremaining = 0;

    private Stack<NumberChange> undoList = new Stack<>();
    private Stack<NumberChange> redoList = new Stack<>();
    private boolean gameFinish = false;

    SudokuGrid(int numberTileToRemove) {
        this.numberTileToRemove = numberTileToRemove;

        initGrid();
        do {

            if (generateGrid())
                break;

        } while (true);
        removeSomeTiles();
    }

    /**
     * Init the grid
     */
    private void initGrid() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                sudokuGrid[x][y] = SudokuNumbersEnum.One;
                userModify[x][y] = SudokuNumbersEnum.NotModifiable; // -1 -> not modifiable number
            }
        }
    }

    /**
     *Generates a valid 9 by 9 Sudoku grid with 1 through 9 appearing only once in every box, row, and column
     */
    private boolean generateGrid()
    {
        ArrayList<Integer> arr = new ArrayList<>(9);
        int[] grid = new int[81];
        for(int i = 1; i <= 9; i++) arr.add(i);

        //loads all boxes with numbers 1 through 9
        for(int i = 0; i < 81; i++)
        {
            if(i%9 == 0) Collections.shuffle(arr);
            int perBox = ((i / 3) % 3) * 9 + ((i % 27) / 9) * 3 + (i / 27) * 27 + (i %3);
            grid[perBox] = arr.get(i%9);
        }

        //tracks rows and columns that have been sorted
        boolean[] sorted = new boolean[81];

        for(int i = 0; i < 9; i++)
        {
            boolean backtrack = false;
            //0 is row, 1 is column
            for(int a = 0; a<2; a++)
            {
                //every number 1-9 that is encountered is registered
                boolean[] registered = new boolean[10]; //index 0 will intentionally be left empty since there are only number 1-9.
                int rowOrigin = i * 9;
                int colOrigin = i;

                ROW_COL: for(int j = 0; j < 9; j++)
                {
                    //row/column stepping - making sure numbers are only registered once and marking which cells have been sorted
                    int step = (a%2==0? rowOrigin + j: colOrigin + j*9);
                    int num = grid[step];

                    if(!registered[num]) registered[num] = true;
                    else //if duplicate in row/column
                    {
                        //box and adjacent-cell swap (BAS method)
                        //checks for either unregistered and unsorted candidates in same box,
                        //or unregistered and sorted candidates in the adjacent cells
                        for(int y = j; y >= 0; y--)
                        {
                            int scan = (a%2==0? i * 9 + y: i + 9 * y);
                            if(grid[scan] == num)
                            {
                                //box stepping
                                for(int z = (a%2==0? (i%3 + 1) * 3: 0); z < 9; z++)
                                {
                                    if(a%2 == 1 && z%3 <= i%3)
                                        continue;
                                    int boxOrigin = ((scan % 9) / 3) * 3 + (scan / 27) * 27;
                                    int boxStep = boxOrigin + (z / 3) * 9 + (z % 3);
                                    int boxNum = grid[boxStep];
                                    if((!sorted[scan] && !sorted[boxStep] && !registered[boxNum])
                                            || (sorted[scan] && !registered[boxNum] && (a%2==0? boxStep%9==scan%9: boxStep/9==scan/9)))
                                    {
                                        grid[scan] = boxNum;
                                        grid[boxStep] = num;
                                        registered[boxNum] = true;
                                        continue ROW_COL;
                                    }
                                    else if(z == 8) //if z == 8, then break statement not reached: no candidates available
                                    {
                                        //Preferred adjacent swap (PAS)
                                        //Swaps x for y (preference on unregistered numbers), finds occurence of y
                                        //and swaps with z, etc. until an unregistered number has been found
                                        int searchingNo = num;

                                        //noting the location for the blindSwaps to prevent infinite loops.
                                        boolean[] blindSwapIndex = new boolean[81];

                                        //loop of size 18 to prevent infinite loops as well. Max of 18 swaps are possible.
                                        //at the end of this loop, if continue or break statements are not reached, then
                                        //fail-safe is executed called Advance and Backtrack Sort (ABS) which allows the
                                        //algorithm to continue sorting the next row and column before coming back.
                                        //Somehow, this fail-safe ensures success.
                                        for(int q = 0; q < 18; q++)
                                        {
                                            SWAP: for(int b = 0; b <= j; b++)
                                            {
                                                int pacing = (a%2==0? rowOrigin+b: colOrigin+b*9);
                                                if(grid[pacing] == searchingNo)
                                                {
                                                    int adjacentCell;
                                                    int adjacentNo;
                                                    int decrement = (a%2==0? 9: 1);

                                                    for(int c = 1; c < 3 - (i % 3); c++)
                                                    {
                                                        adjacentCell = pacing + (a%2==0? (c + 1)*9: c + 1);

                                                        //this creates the preference for swapping with unregistered numbers
                                                        if(   (a%2==0 && adjacentCell >= 81)
                                                                || (a%2==1 && adjacentCell % 9 == 0)) adjacentCell -= decrement;
                                                        else
                                                        {
                                                            adjacentNo = grid[adjacentCell];
                                                            if(i%3!=0
                                                                    || c!=1
                                                                    || blindSwapIndex[adjacentCell]
                                                                    || registered[adjacentNo])
                                                                adjacentCell -= decrement;
                                                        }
                                                        adjacentNo = grid[adjacentCell];

                                                        //as long as it hasn't been swapped before, swap it
                                                        if(!blindSwapIndex[adjacentCell])
                                                        {
                                                            blindSwapIndex[adjacentCell] = true;
                                                            grid[pacing] = adjacentNo;
                                                            grid[adjacentCell] = searchingNo;
                                                            searchingNo = adjacentNo;

                                                            if(!registered[adjacentNo])
                                                            {
                                                                registered[adjacentNo] = true;
                                                                continue ROW_COL;
                                                            }
                                                            break SWAP;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        //begin Advance and Backtrack Sort (ABS)
                                        backtrack = true;
                                        break ROW_COL;
                                    }
                                }
                            }
                        }
                    }
                }

                if(a%2==0)
                    for(int j = 0; j < 9; j++) sorted[i*9+j] = true; //setting row as sorted
                else if(!backtrack)
                    for(int j = 0; j < 9; j++) sorted[i+j*9] = true; //setting column as sorted
                else //reseting sorted cells through to the last iteration
                {
                    backtrack = false;
                    for(int j = 0; j < 9; j++) sorted[i*9+j] = false;
                    for(int j = 0; j < 9; j++) sorted[(i-1)*9+j] = false;
                    for(int j = 0; j < 9; j++) sorted[i-1+j*9] = false;
                    i-=2;
                }
            }
        }

        if(!isPerfect(grid)) return false;

        // convert
        for (int i = 0; i < 81; i++) {
            sudokuGrid[i / 9][i % 9] = SudokuNumbersEnum.get(grid[i]);
        }

        return true;
    }

    /*
     *Prints a visual representation of a 9x9 Sudoku grid
     *@param grid an array with length 81 to be printed
     */
    /*public static void printGrid(int[] grid)
    {
        if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");
        for(int i = 0; i < 81; i++)
        {
            Log.i("SudokuGrid", "["+grid[i]+"] "+(i%9 == 8?"\n":""));
            Log.i("SudokuGrid", (i%9 == 8?"LINE":""));
        }
    }*/

    /**
     *Tests an int array of length 81 to see if it is a valid Sudoku grid. i.e. 1 through 9 appearing once each in every row, column, and box
     *@param grid an array with length 81 to be tested
     *@return a boolean representing if the grid is valid
     */
    private static boolean isPerfect(int[] grid)
    {
        if(grid.length != 81) throw new IllegalArgumentException("The grid must be a single-dimension grid of length 81");

        //tests to see if the grid is perfect

        //for every box
        for(int i = 0; i < 9; i++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int boxOrigin = (i * 3) % 9 + ((i * 3) / 9) * 27;
            for(int j = 0; j < 9; j++)
            {
                int boxStep = boxOrigin + (j / 3) * 9 + (j % 3);
                int boxNum = grid[boxStep];
                registered[boxNum] = true;
            }
            for(boolean b: registered)
                if(!b) return false;
        }

        //for every row
        for(int i = 0; i < 9; i++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            int rowOrigin = i * 9;
            for(int j = 0; j < 9; j++)
            {
                int rowStep = rowOrigin + j;
                int rowNum = grid[rowStep];
                registered[rowNum] = true;
            }
            for(boolean b: registered)
                if(!b) return false;
        }

        //for every column
        for(int colOrigin = 0; colOrigin < 9; colOrigin++)
        {
            boolean[] registered = new boolean[10];
            registered[0] = true;
            for(int j = 0; j < 9; j++)
            {
                int colStep = colOrigin + j*9;
                int colNum = grid[colStep];
                registered[colNum] = true;
            }
            for(boolean b: registered)
                if(!b) return false;
        }

        return true;
    }

    private void removeSomeTiles() {
        Random random = new Random();
        int numberDisable = 0;
        while (numberDisable < numberTileToRemove / 2) {
            int x = random.nextInt(9);
            int y = random.nextInt(9);
            if (userModify[x][y] != SudokuNumbersEnum.Blank) {
                int reverseX = x * -1 + 8;
                int reverseY = y * -1 + 8;
                userModify[x][y] = SudokuNumbersEnum.Blank;
                userModify[reverseX][reverseY] = SudokuNumbersEnum.Blank;

                if (x == reverseX && reverseY == y) // same tile
                    numberTileremaining += 1;
                else
                    numberTileremaining += 2;

                numberDisable++;
            }
        }
    }

    SudokuNumbersEnum get(int x, int y) {
        return sudokuGrid[x][y];
    }

    SudokuNumbersEnum getUserModify(int x, int y) {
        return userModify[x][y];
    }

    void setUserModify(int x, int y, SudokuNumbersEnum value) {
        userModify[x][y] = value;
    }

    int getNumberTileRemaining() {
        return numberTileremaining;
    }

    void addNumberTileRemaining() {
        numberTileremaining++;
    }

    void removeNumberTileRemaining() {
        numberTileremaining--;
    }

    int getNumberTileToRemove() {
        return numberTileToRemove;
    }

    Stack<NumberChange> getUndoList() {
        return undoList;
    }

    Stack<NumberChange> getRedoList() {
        return redoList;
    }

    boolean isGameFinish() {
        return gameFinish;
    }

    void finishGame() {
        gameFinish = true;
    }

    void reinitGrid() {
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (userModify[x][y].isModifiable()) {
                    userModify[x][y] = SudokuNumbersEnum.Blank;
                }
            }
        }
    }
}