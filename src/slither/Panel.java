package slither;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class Panel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final int B_HEIGHT = 300, B_WIDTH = 300, DOT_SIZE = 10, ALL_DOTS = 900, RAND_POS = 29, DELAY = 150;
	private final int[] x = new int[ALL_DOTS];
	private final int y[] = new int[ALL_DOTS];
	private int dots, appleX, appleY;
	private boolean leftDir = false, rightDir = false, upDir = false, downDir = false, inGame = true;
	private Timer timer;
	private Image ball, apple, head;

	public Panel() {
		addKeyListener(new TAdapter());
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
		loadImg();
		init();
	}

	public void loadImg() {
		ImageIcon iid = new ImageIcon(getClass().getResource("/img/dot.png"));
		ball = iid.getImage();

		ImageIcon iia = new ImageIcon(getClass().getResource("/img/apple.png"));
		apple = iia.getImage();

		ImageIcon iih = new ImageIcon(getClass().getResource("/img/head.png"));
		head = iih.getImage();
	}

	public void init() {
		dots = 3;
		for (int i = 0; i < dots; i++) {
			x[i] = 50 - i * 10;
			y[i] = 50;
		}
		locateApple();
		timer = new Timer(DELAY, this);
		timer.start();
		sing("school.wav");
	}

	public static void sing(String songLocation) {
		try {
			File musicPath = new File(songLocation);

			if (musicPath.exists()) {
				AudioInputStream audio = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audio);
				JOptionPane.showMessageDialog(null, "Use the arrow keys to move.", "Exit to play Slither",
						JOptionPane.PLAIN_MESSAGE);
				clip.start();

				clip.loop(Clip.LOOP_CONTINUOUSLY);
				JOptionPane.showMessageDialog(null, "Eat the dots, but don't eat yourself!", "TIP",
						JOptionPane.WARNING_MESSAGE);
			} else {
				System.out.println("File cannot be found.");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		draw(g);
	}

	public void draw(Graphics g) {
		if (inGame) {

			g.drawImage(apple, appleX, appleY, this);
			for (int i = 0; i < dots; i++) {
				if (i == 0) {
					g.drawImage(head, x[i], y[i], this);
				} else {
					g.drawImage(ball, x[i], y[i], this);
				}
			}
			Toolkit.getDefaultToolkit().sync();

		} else {
			gameOver(g);
		}
	}

	public void gameOver(Graphics g) {
		String msg = "Game Over.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = getFontMetrics(small);

		g.setColor(Color.white);
		g.setFont(small);
		g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
	}

	public void checkApple() {
		if ((x[0] == appleX) && (y[0] == appleY)) {
			dots++;
			locateApple();
		}
	}

	public void move() {
		for (int i = dots; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		if (leftDir) {
			x[0] -= DOT_SIZE;
		}
		if (rightDir) {
			x[0] += DOT_SIZE;
		}
		if (upDir) {
			y[0] -= DOT_SIZE;
		}
		if (downDir) {
			y[0] += DOT_SIZE;
		}
	}

	public void checkCollisions() {
		for (int i = dots; i > 0; i--) {
			if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
				inGame = false;
			}
		}
		if (y[0] >= B_HEIGHT) {
			inGame = false;
		}
		if (y[0] < 0) {
			inGame = false;
		}
		if (x[0] >= B_WIDTH) {
			inGame = false;
		}
		if (x[0] < 0) {
			inGame = false;
		}
		if (!inGame) {
			timer.stop();
		}
	}

	public void locateApple() {
		int r = (int) (Math.random() * RAND_POS);
		appleX = r * DOT_SIZE;

		r = (int) (Math.random() * RAND_POS);
		appleY = r * DOT_SIZE;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (inGame) {
			checkApple();
			checkCollisions();
			move();

		}

		repaint();
	}

	private class TAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			super.keyPressed(e);

			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT && !rightDir) {
				leftDir = true;
				upDir = false;
				downDir = false;

			}
			if (key == KeyEvent.VK_RIGHT && !leftDir) {
				rightDir = true;
				upDir = false;
				downDir = false;

			}
			if (key == KeyEvent.VK_UP && !downDir) {
				upDir = true;
				leftDir = false;
				rightDir = false;

			}
			if (key == KeyEvent.VK_DOWN && !upDir) {
				downDir = true;
				leftDir = false;
				rightDir = false;

			}
		}
	}
}
