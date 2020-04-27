import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A logic model for Sudoku board generator and solver
 */
public class SudokuModel {

    public static final int EASY = 63;
    public static final int MEDIUM = 54;
    public static final int HARD = 45;
    public static final int VERY_HARD = 36;
    public static final int INSANE = 27;
    public static final int INHUMAN = 17;


    private ArrayList<ArrayList<Integer>> matrix;

    /**
     * construct an empty Sudoku board
     */
    public SudokuModel(){
        matrix = new ArrayList<>();
        for (int row = 0; row < 9 ; row++){
            matrix.add(row,new ArrayList<>(Collections.nCopies(9,0)));
        }
    }

    /**
     * method to add a number to cell in sudoku matrix - (0 represent empty cell)
     * @param row row index in sudoku matrix
     * @param col column index in soduko matrix
     * @param num number to add in the specified cell [row][col]
     * @throws IllegalArgumentException if number is smaller then 0  or grater than 9 or if  number already exist in row, column or square
     */
    public void setCellValue(int row, int col, int num) throws IllegalArgumentException {
        if (num<0 || num>9)
            throw new IllegalArgumentException("not valid number");
        if(num==0){
            matrix.get(row).set(col,num);
            return;
        }
        if(! isRowLegal(row,num) )
            throw new IllegalArgumentException(num + " already exists in row");
        else if ( ! isColLegal(col,num))
            throw new IllegalArgumentException(num + " already exists in column");
        else if (! isSquareLegal(row,col,num))
            throw new IllegalArgumentException(num + " already exists in square");
        matrix.get(row).set(col,num);
    }

    /**
     * method to get the number of cell in sudoku matrix - (0 represent empty cell)
     *
     * @param row row index in sudoku matrix
     * @param col column index in soduko matrix
     * @return number of the cell
     */
    public int getCellValue(int row, int col) {
        return matrix.get(row).get(col);
    }

    /**
     * clears a Sudoku board (sets all cells as empty cells)
     */
    public void clearBoard() {
        for (int row = 0; row <9; row++)
            for (int col = 0; col<9; col++)
                matrix.get(row).set(col,0);
    }

    /**
     * @return true when the board is completely solved
     */
    public boolean isSolvedBoard() {
        for (ArrayList<Integer> row : matrix)
            if (row.contains(0))
                return false;
        return true;
    }

    // fills the 3 diagonal square with random numbers
    private void fillRectRandom() {
        int row = 0, col = 0, num = 0;
        List<Integer> numbers =
                IntStream.range(1, 10)
                        .boxed()
                        .collect(Collectors.toList());
        for (int i = 0; i < 3; i++, num = 0) {
            Collections.shuffle(numbers);
            for (int z = 0; z < 3; z++, row++) {
                if (col != row)
                    col -= 3;
                for (int j = 0; j < 3; j++, col++, num++)
                    setCellValue(row, col, numbers.get(num));
            }
        }

    }

    /**
     * Generate a new Sudoku board of a particular `difficulty`
     *
     * @param difficulty number of filled cells in the board
     */
    public void generateBoard(int difficulty) {
        if (difficulty >= 81)
            difficulty = EASY;
        else if (difficulty < 17)
            difficulty = INHUMAN;
        clearBoard();
        fillRectRandom();
        solve();
        clearCells(81 - difficulty);
    }

    // clears num cells in the board
    private void clearCells(int num) {
        for (; num >= 9; num -= 9) {
            clearCellInEachSquare();
        }
        List<Integer> squares = IntStream.range(0, 9).boxed().collect(Collectors.toList());
        Collections.shuffle(squares);
        for (; num > 0; num--)
            clearCellInSquare(squares.remove(0));
    }

    // clear a random cell in the specified square
    private void clearCellInSquare(int square) {
        int firstIndexInSquare;
        if (square % 3 == 0)
            firstIndexInSquare = square * 9;
        else if (square % 3 == 1)
            firstIndexInSquare = square * 9 - 6;
        else
            firstIndexInSquare = square * 9 - 12;
        List<Integer> squareIndexes = new ArrayList<>();
        for (int i = 0; i < 3; i++, firstIndexInSquare += 6)
            for (int j = 0; j < 3; j++)
                squareIndexes.add(firstIndexInSquare++);
        Collections.shuffle(squareIndexes);
        while (getCellValue(squareIndexes.get(0) / 9, squareIndexes.get(0) % 9) == 0) {
            squareIndexes.remove(0);
        }
        clearCell(squareIndexes.get(0));
    }

    // clears 1 cell in each square
    private void clearCellInEachSquare() {
        for (int square = 0; square < 9; square++)
            clearCellInSquare(square);
    }

    // clear a cell
    private void clearCell(int index) {
        int row = index / 9, col = index % 9;
        setCellValue(row, col, 0);
    }

    /**
     * solve a Sudoku board
     *
     * @return true if the boura is solvable
     */
    public boolean solve() {
        return findSolution(0);
    }

    // checks for solution of Sudoku board using backtracking alg starting from index cell
    private boolean findSolution(int index) {
        int row = index / 9, column = index % 9;

        if (index == 9 * 9) return true;
        if (getCellValue(row, column) != 0) return findSolution(index + 1);

        for (Integer value : getOptionsForCell(row, column)) {
            setCellValue(row, column, value);
            if (findSolution(index + 1)) return true;
        }
        setCellValue(row, column, 0);
        return false;
    }

    // returns all possible values of a cell according to the current board
    private List<Integer> getOptionsForCell(int row, int col) {
        List<Integer> options = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        removeOptionsInRow(options, row);
        removeOptionsInColumn(options, col);
        removeOptionsInBox(options, row, col);
        return options;
    }

    // delete values in option list that exist in the square of cell [row][col]
    private void removeOptionsInBox(List<Integer> options, int row, int col) {
        row = row - (row % 3);
        col = col - (col % 3);
        for (int i = 0; i < 3; i++, row++, col -= 3) {
            for (int j = 0; j < 3; j++, col++) {
                options.remove(matrix.get(row).get(col));
            }
        }
    }

    // delete values in option list that exist in column col
    private void removeOptionsInColumn(List<Integer> options, int col) {
        for (ArrayList<Integer> row : matrix)
            options.remove(row.get(col));
    }

    // delete values in option list that exist in row
    private void removeOptionsInRow(List<Integer> options, int row) {
        options.removeAll(matrix.get(row));
    }

    // checks if num is legal in row/col/square
    private boolean isRowLegal(int row, int num){
        return ! matrix.get(row).contains(num);
    }

    private  boolean isColLegal(int col, int num){
        for(ArrayList<Integer> list:matrix)
            if(list.get(col)==num)
                return false;
        return true;
    }

    private boolean isSquareLegal(int row, int col, int num){
        row = row - (row % 3);
        col = col - (col % 3);
        for (int i = 0; i<3; i++,row++,col-=3){
            for (int j = 0; j<3; j++,col++){
                if (matrix.get(row)
                        .get(col)==num)
                    return false;
            }
        }
        return true;
    }
}
