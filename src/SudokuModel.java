import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SudokuModel {
    private ArrayList<ArrayList<Integer>> matrix;


    /**
     * construct an empty Sudoku matrix
     */
    public SudokuModel(){
        matrix = new ArrayList<>();
        for (int row = 0; row < 9 ;row++){
            matrix.add(row,new ArrayList<>(Collections.nCopies(9,0)));
        }
    }

    /**
     * method to add a number to cell in sudoku matrix - (0 represent empty cell)
     * @param row row index in sudoku matrix
     * @param col column matrix in soduko matrix
     * @param num number to add in the specified cell [row][col]
     * @throws IllegalArgumentException if number is smaller then 0  or grater than 9 or if  number already exist in row, column or square
     */
    public void set(int row, int col, int num) throws IllegalArgumentException{
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

    public void clearMatrix(){
        for (int row= 0; row <9;row++)
            for (int col = 0 ; col<9; col++)
                matrix.get(row).set(col,0);
    }

    private boolean isRowLegal(int row,int num){
        return ! matrix.get(row).contains(num);
    }
    private  boolean isColLegal(int col,int num){
        for(ArrayList<Integer> list:matrix)
            if(list.get(col)==num)
                return false;
        return true;
    }
    private boolean isSquareLegal(int row, int col, int num){
        row = row - (row % 3);
        col = col - (col % 3);
        for (int i=0;i<3;i++,row++,col-=3){
            for (int j = 0 ;j<3;j++,col++){
                if (matrix.get(row)
                        .get(col)==num)
                    return false;
            }
        }
        return true;
    }

    public void print(){
        for (ArrayList<Integer> list : matrix)
            System.out.println(list);
    }

}
