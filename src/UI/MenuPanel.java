package UI;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Control.POS;

public class MenuPanel implements ActionListener {

	public static JButton[] menuButton;
	private int width = 130;
	private int height = 25;
	private int border = 10;

	POS mPos;

	public MenuPanel(POS pos) {
		mPos = pos;
	}

	public JPanel menuPane() {
		JPanel panel = POSFrame.getDefaultPanel();

		menuButton = new JButton[20];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {

				menuButton[(i * 10) + j] = new JButton();
				menuButton[(i * 10) + j].setMargin(new Insets(0, 0, 0, 0));
				menuButton[(i * 10) + j].setActionCommand(Integer.toString((i * 10) + j));
				menuButton[(i * 10) + j].setBounds(i * width + i * border + 20, j * height + j * border + 30, width,
						height);
				panel.add(menuButton[(i * 10) + j]);
				menuButton[((i * 10) + j)].addActionListener(this);
			}
		}
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		int myButtonNum = Integer.parseInt(e.getActionCommand());
		String menuName = menuButton[myButtonNum].getText();
		if (menuName.equals(""))
			return;
		int tableNum = OrderPanel.getSelectedTableBoxItem();
		String customerName = OrderPanel.getCustomerName();
		mPos.saleControl.addMenu(tableNum, menuName, customerName);
	}

	public static void setButtonName(int menuNum, String menuName) {
		menuButton[menuNum].setText(menuName);
	}
}
