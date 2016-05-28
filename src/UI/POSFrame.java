package UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class POSFrame {
	JFrame frame;
	JPanel posPanel;
	public POSFrame(){
		frame = new JFrame();
		posPanel = new JPanel(null);
		posPanel.setBounds(10,10,700,700);
		
		TablePanel tablePanel = new TablePanel();
		JPanel tablePane = tablePanel.tablePane();
		tablePane.setSize(300, 300);
		tablePane.setBounds(10,10,310,280);
		
		OrderPanel orderPanel = new OrderPanel();
		JPanel orderPane = orderPanel.orderPane();
		orderPane.setBounds(330, 10, 350, 280);
		
		MenuPanel menuPanel = new MenuPanel();
		JPanel menuPane = menuPanel.menuPane();
		menuPane.setBounds(10, 300, 310, 390);
		
		TabbedPane tabbed = new TabbedPane();
		JTabbedPane tabbedPane = tabbed.createAndShowGUI();
		tabbedPane.setBounds(330,300,350,390);
		
		posPanel.add(tablePane);
		posPanel.add(orderPane);
		posPanel.add(menuPane);
		posPanel.add(tabbedPane);
		
		frame.add(posPanel);
		frame.setTitle("POS");
		frame.setSize(710, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}
	public static  void main(String[] args){
		new POSFrame();
	}
}
