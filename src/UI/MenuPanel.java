package UI;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import Control.POS;

public class MenuPanel implements ActionListener {

	public static JButton[] mMenuButton;
	private int mWidth = 130;
	private int mHeight = 25;
	private int mBorder = 10;

	POS mPos;

	public MenuPanel(POS pos) {
		mPos = pos;
	}

	public JPanel menuPane() {
		JPanel panel = POSFrame.getDefaultPanel("¸Þ´º");

		mMenuButton = new JButton[20];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 10; j++) {

				mMenuButton[(i * 10) + j] = new JButton();
				mMenuButton[(i * 10) + j].setMargin(new Insets(0, 0, 0, 0));
				mMenuButton[(i * 10) + j].setActionCommand(Integer.toString((i * 10) + j));
				mMenuButton[(i * 10) + j].setBounds(i * mWidth + i * mBorder + 20, j * mHeight + j * mBorder + 30,
						mWidth, mHeight);
				panel.add(mMenuButton[(i * 10) + j]);
				mMenuButton[((i * 10) + j)].addActionListener(this);
			}
		}
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		int myButtonNum = Integer.parseInt(e.getActionCommand());
		String menuName = mMenuButton[myButtonNum].getText();
		if (menuName.equals(""))
			return;
		int tableNum = OrderPanel.getSelectedTableBoxItem();
		String customerName = OrderPanel.getCustomerName();
		mPos.saleControl.addTempOrder(tableNum, menuName, customerName);
	}

	public static void setButtonName(int menuNum, String menuName) {
		mMenuButton[menuNum].setText(menuName);
	}
}
