import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuController {

     SudokuModel sModel = new SudokuModel();
     SudokuView sView = new SudokuView();

     public SudokuController(){
        sView.clearButtonAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sModel.clearMatrix();
                sView.clearMatrix();
            }
        });
        sView.setButtonAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sView.setCustomSudoku();
            }
        });
         sView.cellsAddChangeListener(new ChangeListener() {
             @Override
             public void stateChanged(ChangeEvent e) {
                 int row = ((SudokuView.CellPanel)e.getSource()).getRow();
                 int col = ((SudokuView.CellPanel)e.getSource()).getCol();
                 int num;
                 if( ((SudokuView.CellPanel)e.getSource()).getText().equals(""))
                     num = 0;
                 else
                     num = ((SudokuView.CellPanel)e.getSource()).getInt();
                 try {
                     sModel.set(row,col,num);

                 } catch (IllegalArgumentException e1){
                     e1.printStackTrace();
                     sView.errorMsg(e1.getMessage());
                     ((SudokuView.CellPanel)e.getSource()).setText("");
                 }
             }
         } );
       /* sView.cellsAddActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = ((SudokuView.CellPanel)e.getSource()).getRow();
                int col = ((SudokuView.CellPanel)e.getSource()).getCol();
                try {
                    int num = Integer.parseInt( ((SudokuView.CellPanel)e.getSource()).getText() );
                    sModel.add(row,col,num);

                }catch (NumberFormatException e1){
                    e1.printStackTrace();
                    sView.errorMsg("Please enter only legal numbers");
                    ((SudokuView.CellPanel)e.getSource()).setText("");
                }
                catch (IllegalArgumentException e1){
                    e1.printStackTrace();
                    sView.errorMsg(e1.getMessage());
                    ((SudokuView.CellPanel)e.getSource()).setText("");
                }
            }
        });*/


     }




}
