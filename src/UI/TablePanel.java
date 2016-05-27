package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class TablePanel implements ActionListener {
	    private JFrame frame = new JFrame();
	    private JPanel panel = new JPanel();

	    private JButton[] tableButton;
	    private int width = 50;
        private int height = 50;
        private int border = 10;
	    public TablePanel(){
	        panel.setLayout(null);
	        tableButton = new JButton[20];

			Border loweredbevel = BorderFactory.createLoweredBevelBorder();
			TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, "테이블 현황");
			title.setTitlePosition(TitledBorder.ABOVE_TOP);
			panel.setBorder(title);

	        for(int i = 0; i < 4; i++){
	        	for (int j =0; j < 5; j++){
	        		tableButton[(i*5)+j] = new JButton(""+((i*5)+j+1)+"");
	        		tableButton[(i*5)+j].setActionCommand(Integer.toString((i*5)+j+1));
	        		tableButton[(i*5)+j].setBounds(j*width + j*border + 10, i*height + i*border+ 30, width, height);
	        		tableButton[(i*5)+j].setBackground(Color.WHITE);
//	        		tableButton[(i*j)+j].setForeground(Color.black);
	        		panel.add(tableButton[(i*5)+j]);		
	        		tableButton[(i*5)+j].addActionListener(this);
	        	}
	        }

	        frame.add(panel);

	        frame.setTitle("Table Status");
	        frame.setSize(330, 350);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);
	    }
	    
	    public void actionPerformed(ActionEvent e){
	        System.out.println(e.getActionCommand());
	        int myButton =Integer.parseInt(e.getActionCommand())-1; 
	        changeTableColor(myButton);
	    }
	    
		public void changeTableColor(int tableNum){
			if(tableButton[tableNum].getBackground() == Color.red){
				tableButton[tableNum].setBackground(Color.WHITE);
			}else{
				tableButton[tableNum].setBackground(Color.red);
			}
		}
	    public static void main(String[] args){
	    	new TablePanel();
	    }

}
