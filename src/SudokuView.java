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
import java.util.Objects;

/**
 * creates graphic interface for sudoku game
 */
public class SudokuView extends JFrame {

    /**
     * Jtextfieled to be  a cell in the matrix with background color
     */
    public class CellPanel extends JNumberTextField{
        private int row;
        private int col;

        private CellPanel(int row, int col, Color color){
            super(1,2);
            super.setAllowNegative(false);
            this.row = row;
            this.col = col;
            setFont(new Font(Font.MONOSPACED,Font.ITALIC,50));
            setBackground(color);
            setHorizontalAlignment(JTextField.CENTER);

        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }
    // main panel
    private JPanel mainPanel = new JPanel(new BorderLayout());
    // list of cell panels
    private ArrayList<ArrayList<CellPanel>> matrix = new ArrayList<>(9);
    // matrix panel that will contain all cell panels
    private JPanel matrixPanel = new JPanel(new GridLayout(9,9,1,1));

    // bottom panel for footer menu
    private JPanel bottomPanel = new JPanel();
    private JButton setButton = new JButton("Set");
    private JButton clearButton = new JButton("Clear");


    /**
     * initialize the sudoku view
     */
    public SudokuView(){

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMatrix();
        setMatrixPanel();

        bottomPanel.add(setButton);
        bottomPanel.add(clearButton);

        mainPanel.add(matrixPanel,BorderLayout.CENTER);
        mainPanel.add(bottomPanel,BorderLayout.SOUTH);

        add(mainPanel);
        setSize(500,600);
        setLocationRelativeTo(null);//center the window
        setVisible(true);
    }

    private void setMatrixPanel(){
        for (ArrayList<CellPanel> cells: matrix){
            for (CellPanel cell:cells){
                matrixPanel.add(cell);
            }
        }
    }
    private void setMatrix(){
        Color color ;
        for (int row = 0;row<9;row++){
            matrix.add(new ArrayList<>());
            for (int col = 0;col<9;col++){
                if(((row<3||row>5) && (col<3||col>5)) || (row > 2 && row<6 && col > 2 && col < 6 ))
                    color = Color.GRAY;
                else
                    color = Color.WHITE;
                matrix.get(row).add(new CellPanel(row,col,color));
            }
        }
    }

    public void clearButtonAddActionListener(ActionListener listener){
        clearButton.addActionListener(listener);
    }
    public void setButtonAddActionListener(ActionListener listener){
        setButton.addActionListener(listener);
    }
    public void clearMatrix(){
        for (int row = 0;row<9;row++){
            for (int col = 0;col<9;col++){
                matrix.get(row).get(col).setText("");
                matrix.get(row).get(col).setForeground(Color.BLACK);
                matrix.get(row).get(col).setEditable(true);

            }
        }
    }

    public void setCustomSudoku(){
        for (int row = 0;row<9;row++){
            for (int col = 0;col<9;col++){
                if( matrix.get(row).get(col).getText().compareTo("") != 0) {
                    matrix.get(row).get(col).setForeground(Color.RED);
                    matrix.get(row).get(col).setEditable(false);
                }
            }
        }
    }
    public void cellsAddActionListener(ActionListener l){
        for (ArrayList<CellPanel> cells: matrix){
            for (CellPanel cell:cells){
                cell.addActionListener(l);
            }
        }
    }
    public void cellsAddChangeListener(ChangeListener changeListener){
        for (ArrayList<CellPanel> cells: matrix){
            for (CellPanel cell:cells){
                addChangeListener(cell,changeListener);
            }
        }
    }
    public void errorMsg(String msg){
        JOptionPane.showMessageDialog(null,msg,"Error",JOptionPane.ERROR_MESSAGE);
    }


    public static void addChangeListener(JTextComponent text, ChangeListener changeListener) {
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
            Document d1 = (Document)e.getOldValue();
            Document d2 = (Document)e.getNewValue();
            if (d1 != null) d1.removeDocumentListener(dl);
            if (d2 != null) d2.addDocumentListener(dl);
            dl.changedUpdate(null);
        });
        Document d = text.getDocument();
        if (d != null) d.addDocumentListener(dl);
    }
}
