/**
 * controls the view and model Sudoku game
 */
public class SudokuController {

    SudokuModel sudokuModel;
    SudokuView sudokuView;

    /**
     * construct controller
     *
     * @param sm sudoku model
     * @param sv sudoku view
     */
    public SudokuController(SudokuModel sm, SudokuView sv) {
        sudokuModel = sm;
        sudokuView = sv;

        setListeners();

        sudokuModel.generateBoard(SudokuModel.EASY);
        setViewAsModel();
    }

    // sets the view to be like the model
    private void setViewAsModel() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (sudokuModel.getCellValue(row, col) != 0)
                    sudokuView.setCellUneditable(row, col, sudokuModel.getCellValue(row, col));
            }
        }
    }

    // creates action listeners and add them to graphical components (as buttons etc)
    private void setListeners() {
        // difficulty buttons
        sudokuView.difficultyButtonAddActionListener(e -> {
            sudokuModel.generateBoard(Integer.parseInt(e.getActionCommand()));
            sudokuView.clearBoard();
            setViewAsModel();
        });
        // solve button
        sudokuView.solveButtonAddActionListener(e -> {
            sudokuModel.solve();
            setViewAsModel();
        });
        // clear button
        sudokuView.clearButtonAddActionListener(e -> {
            sudokuModel.clearBoard();
            sudokuView.clearBoard();
        });
        // set button
        sudokuView.setButtonAddActionListener(e -> {
            sudokuView.setCustomBoard(); // color the numbers
        });
        // cells change listener
        sudokuView.cellsAddChangeListener(e -> {
            int row = ((SudokuView.CellPanel) e.getSource()).getRow();
            int col = ((SudokuView.CellPanel) e.getSource()).getCol();
            int num;
            if (((SudokuView.CellPanel) e.getSource()).getText().equals(""))
                num = 0;
            else
                num = ((SudokuView.CellPanel) e.getSource()).getInt();
            try {
                if (sudokuModel.getCellValue(row, col) != num) {
                    sudokuModel.setCellValue(row, col, num);
                    sudokuView.setErrorLabelText("");
                    if (sudokuModel.isSolvedBoard())
                        sudokuView.popupMsg("Well-Done!!!\nTry harder level...");
                }
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
                sudokuView.setErrorLabelText(e1.getMessage());
                ((SudokuView.CellPanel) e.getSource()).setText("");
            }
        });
    }
}
