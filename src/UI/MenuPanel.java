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

public class MenuPanel implements ActionListener {
	private JFrame frame = new JFrame();
	private JPanel panel = new JPanel();

	private JButton[] menuButton;
	private int width = 180;
	private int height = 30;
	private int border = 10;

	public MenuPanel(){
		        panel.setLayout(null);
		        menuButton = new JButton[20];
				Border loweredbevel = BorderFactory.createLoweredBevelBorder();
				TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, "�޴�");
				title.setTitlePosition(TitledBorder.ABOVE_TOP);
				panel.setBorder(title);

		        for(int i = 0; i < 2; i++){
		        	for(int j = 0; j < 10; j++){
		        		menuButton[(i*10)+j] = new JButton(""+((i*10)+j+1)+"�Ƚɽ�����ũ");
		        		menuButton[(i*10)+j].setActionCommand(Integer.toString((i*10)+j));
		        		menuButton[(i*10)+j].setBounds(i*width + i*border + 20, j*height + j*border+ 30, width, height);
		        		panel.add(menuButton[(i*10)+j]);		
		        		menuButton[((i*10)+j)].addActionListener(this);
		        	}
		        }
		        frame.add(panel);

		        frame.setTitle("Table Status");
		        frame.setSize(430, 490);
		        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		        frame.setVisible(true);
		    }

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		int myButton = Integer.parseInt(e.getActionCommand());
		changeTableColor(myButton);
	}
	
	public void changeTableColor(int tableNum){
		if(menuButton[tableNum].getBackground() == Color.red){
			menuButton[tableNum].setBackground(null);
		}else{
			menuButton[tableNum].setBackground(Color.red);
		}
	}

	public static void main(String[] args) {
		new MenuPanel();
	}


}