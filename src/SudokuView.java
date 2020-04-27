import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Objects;

/**
 * creates graphic interface for sudoku game
 */
public class SudokuView extends JFrame {

    private static Color UNEDITABLE_TEXT = Color.BLACK;
    private static Color EDITABLE_TEXT = Color.BLUE;
    private JButton solveButton = new JButton("Solve");
    // main panel
    private JPanel mainPanel = new JPanel(new BorderLayout());
    // list of cell panels
    private ArrayList<ArrayList<CellPanel>> matrix = new ArrayList<>(9);
    // matrix panel that will contain all cell panels
    private JPanel matrixPanel = new JPanel(new GridLayout(9,9,1,1));

    // bottom panel for footer menu
    private JPanel bottomPanel = new JPanel();
    private JLabel errorLabel = new JLabel("");
    private JButton setButton = new JButton("Set");
    private JButton clearButton = new JButton("Clear");
    //difficulty menu
    private JRadioButton easyRadioBtn = new JRadioButton("Easy", true);
    private JRadioButton mediumRadioBtn = new JRadioButton("Medium", false);
    private JRadioButton hardRadioBtn = new JRadioButton("Hard", false);
    private JRadioButton veryHardRadioBtn = new JRadioButton("Very Hard", false);
    private JRadioButton insaneRadioBtn = new JRadioButton("Insane", false);
    private JRadioButton inhumanRadioBtn = new JRadioButton("Inhuman", false);
    private ButtonGroup difficultyBtnGroup = new ButtonGroup();
    private JPanel difficultyPanel = new JPanel();
    /**
     * initialize the sudoku view
     */
    public SudokuView(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMatrix();
        setMatrixPanel();

        easyRadioBtn.setActionCommand("63");
        mediumRadioBtn.setActionCommand("54");
        hardRadioBtn.setActionCommand("45");
        veryHardRadioBtn.setActionCommand("36");
        insaneRadioBtn.setActionCommand("27");
        inhumanRadioBtn.setActionCommand("17");

        difficultyBtnGroup.add(easyRadioBtn);
        difficultyBtnGroup.add(mediumRadioBtn);
        difficultyBtnGroup.add(hardRadioBtn);
        difficultyBtnGroup.add(veryHardRadioBtn);
        difficultyBtnGroup.add(insaneRadioBtn);
        difficultyBtnGroup.add(inhumanRadioBtn);

        difficultyPanel.add(easyRadioBtn);
        difficultyPanel.add(mediumRadioBtn);
        difficultyPanel.add(hardRadioBtn);
        difficultyPanel.add(veryHardRadioBtn);
        difficultyPanel.add(insaneRadioBtn);
        difficultyPanel.add(inhumanRadioBtn);

        bottomPanel.add(solveButton);
        bottomPanel.add(setButton);
        bottomPanel.add(clearButton);
        errorLabel.setForeground(Color.RED);
        bottomPanel.add(errorLabel);


        mainPanel.add(difficultyPanel, BorderLayout.NORTH);
        mainPanel.add(matrixPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);

        add(mainPanel);
        setSize(500,600);
        setLocationRelativeTo(null);//center the window
        setVisible(true);
    }

    // adds changeListener to monitor every change on the specified JTextComponent
    private static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
        Objects.requireNonNull(text);
        Objects.requireNonNull(changeListener);
        DocumentListener dl = new DocumentListener() {
            private int lastChange = 0, lastNotifiedChange = 0;

            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                lastChange++;
                SwingUtilities.invokeLater(() -> {
                    if (lastNotifiedChange != lastChange) {
                        lastNotifiedChange = lastChange;
                        changeListener.stateChanged(new ChangeEvent(text));
                    }
                });
            }
        };
        text.addPropertyChangeListener("document", (PropertyChangeEvent e) -> {
            Document d1 = (Document) e.getOldValue();
            Document d2 = (Document) e.getNewValue();
            if (d1 != null) d1.removeDocumentListener(dl);
            if (d2 != null) d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });
        Document d = text.getDocument();
        if (d != null) d.addDocumentListener(dl);
    }

    /**
     * adds listener to clear button
     *
     * @param listener to add to the clear button
     */
    public void clearButtonAddActionListener(ActionListener listener){
        clearButton.addActionListener(listener);
    }

    /**
     * adds listener to set button
     * @param listener to add to the set button
     */
    public void setButtonAddActionListener(ActionListener listener){
        setButton.addActionListener(listener);
    }

    /**
     * adds listener to solve button
     *
     * @param listener to add
     */
    public void solveButtonAddActionListener(ActionListener listener) {
        solveButton.addActionListener(listener);
    }

    /**
     * adds listener to all difficulty buttons
     *
     * @param listener to add
     */
    public void difficultyButtonAddActionListener(ActionListener listener) {
        for (Enumeration<AbstractButton> btn = difficultyBtnGroup.getElements(); btn.hasMoreElements(); ) {
            btn.nextElement().addActionListener(listener);
        }
    }

    /**
     * adds a changeListener to all the cells - to  monitor every change on a cell
     *
     * @param changeListener to add to every cell in board
     */
    public void cellsAddChangeListener(ChangeListener changeListener) {
        for (ArrayList<CellPanel> cells : matrix) {
            for (CellPanel cell : cells) {
                addChangeListener(cell, changeListener);
            }
        }
    }

    /**
     * clear the board - delete all numbers from the board
     */
    public void clearBoard(){
        for (int row = 0;row<9;row++){
            for (int col = 0;col<9;col++){
                matrix.get(row).get(col).setText("");
                matrix.get(row).get(col).setForeground(EDITABLE_TEXT);
                matrix.get(row).get(col).setEditable(true);

            }
        }
    }

    /**
     * this colors the numbers on the board and sets them as not editable
     */
    public void setCustomBoard() {
        for (int row = 0; row < 9; row++)
            for (int col = 0; col < 9; col++)
                if (matrix.get(row).get(col).getText().compareTo("") != 0)
                    setCellUneditable(row, col, Integer.parseInt(matrix.get(row).get(col).getText()));
    }

    /**
     * set number in specified cell and sets it as uneditable cell
     *
     * @param row row index
     * @param col col index
     * @param num number to add in cell
     */
    public void setCellUneditable(int row, int col, int num) {
        matrix.get(row).get(col).setText(String.valueOf(num));
        matrix.get(row).get(col).setForeground(UNEDITABLE_TEXT);
        matrix.get(row).get(col).setEditable(false);
    }

    /**
     * Brings up a dialog that displays a message
     *
     * @param msg string massage
     */
    public void popupMsg(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * sets text for the error label
     *
     * @param txt text for the label
     */
    public void setErrorLabelText(String txt) {
        errorLabel.setText(txt);
    }

    // adds cells to the matrix panel
    private void setMatrixPanel() {
        for (ArrayList<CellPanel> cells : matrix) {
            for (CellPanel cell : cells) {
                matrixPanel.add(cell);
            }
        }
    }

    //create cells and stores them in the matrix array list
    private void setMatrix() {
        Color color;
        for (int row = 0; row < 9; row++) {
            matrix.add(new ArrayList<>());
            for (int col = 0; col < 9; col++) {
                if (((row < 3 || row > 5) && (col < 3 || col > 5)) || (row > 2 && row < 6 && col > 2 && col < 6))
                    color = Color.LIGHT_GRAY;
                else
                    color = Color.WHITE;
                matrix.get(row).add(new CellPanel(row, col, color));
            }
        }
    }

    /**
     * JtextFieled to be  a cell in the matrix with background color
     */
    public static class CellPanel extends JNumberTextField {
        private int row;
        private int col;

        public CellPanel(int row, int col, Color color) {
            super(1, 2);
            super.setAllowNegative(false);
            this.row = row;
            this.col = col;
            setFont(new Font(Font.MONOSPACED, Font.ITALIC, 30));
            setBackground(color);
            setForeground(EDITABLE_TEXT);
            setHorizontalAlignment(JTextField.CENTER);
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
}
