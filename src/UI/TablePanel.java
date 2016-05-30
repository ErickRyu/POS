package UI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import Control.POS;

public class TablePanel implements ActionListener {
	private JPanel panel = new JPanel();

	private static JButton[] tableButton;
	private int width = 50;
	private int height = 50;
	private int border = 10;

	POS mPos;

	public TablePanel(POS pos) {
		mPos = pos;
	}

	public JPanel tablePane() {
		panel.setLayout(null);
		tableButton = new JButton[20];

		Border loweredbevel = BorderFactory.createLoweredBevelBorder();
		TitledBorder title = BorderFactory.createTitledBorder(loweredbevel, "테이블 현황");
		title.setTitlePosition(TitledBorder.ABOVE_TOP);
		panel.setBorder(title);

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 5; j++) {
				tableButton[(i * 5) + j] = new JButton("" + ((i * 5) + j + 1) + "");
				tableButton[(i * 5) + j].setActionCommand(Integer.toString((i * 5) + j + 1));
				tableButton[(i * 5) + j].setBounds(j * width + j * border + 10, i * height + i * border + 30, width,
						height);
				tableButton[(i * 5) + j].setBackground(Color.WHITE);
				panel.add(tableButton[(i * 5) + j]);
				tableButton[(i * 5) + j].addActionListener(this);
			}
		}
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		int tableNum = Integer.parseInt(e.getActionCommand());
		mPos.saleControl.rollBack();
		OrderPanel.setResultArea(null);
		mPos.saleControl.showOrder(tableNum);
		OrderPanel.setTableBoxItem(tableNum);
	}

	public static void setTableColorWhite(int tableNum) {
		tableButton[tableNum - 1].setBackground(Color.WHITE);
	}

	public static void setTableColorYellow(int tableNum) {
		tableButton[tableNum - 1].setBackground(Color.yellow);
	}
}
