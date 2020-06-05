package slither;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class SlitherGame extends JFrame {
	private static final long serialVersionUID = 1L;

	public SlitherGame() {
		add(new Panel());
		setResizable(false);
		pack();

		setTitle("Slither");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame ex = new SlitherGame();
				ex.setVisible(true);

			}
		});
	}

}
