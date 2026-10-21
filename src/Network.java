import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Network {

	static final int NUMOFNEURONS = 8;
	static final int ACTIVENEURONS = 2; // WITHOUT BIAS
	static final int HISTLENGTH = 128;
	static final double EPSILON = 0.00001;
	static final int INVALID = 100;

	static final double w[][] = new double[NUMOFNEURONS][NUMOFNEURONS];
	static final double n[] = new double[NUMOFNEURONS];
	static final double a[] = new double[NUMOFNEURONS];

	static final double n_hist[][] = new double[HISTLENGTH][NUMOFNEURONS];
	static int histcounter = 0;

	static boolean run = true;

	static DrawNeuron drawNeuron;
	static DrawValley drawValley;

	final JSlider[][] jSliderWeights = new JSlider[ACTIVENEURONS][ACTIVENEURONS + 1];
	final JLabel[][] jLabelWeights = new JLabel[ACTIVENEURONS][ACTIVENEURONS + 1];

	final JSlider[] jSliderNeurons = new JSlider[ACTIVENEURONS + 1];
	final JLabel[] jLabelNeurons = new JLabel[ACTIVENEURONS + 1];

	public void delay(int milliSeconds) // in ms
	{
		long strt = System.nanoTime();
		while (System.nanoTime() - strt < milliSeconds * 1000000) {
		} // 20 ms
	}

	private void updateCurrentNetwork() {
		updateArbitraryNetwork(w, n);
	}

	private void updateArbitraryNetwork(double[][] w, double[] n) {
		for (int i = 0; i < ACTIVENEURONS; i++) {
			a[i] = 0;
			for (int j = 0; j < (ACTIVENEURONS + 1); j++) {
				a[i] += w[i][j] * n[j];
			}
		}
		for (int i = 0; i < ACTIVENEURONS; i++) {
			n[i] = Math.tanh(a[i]);

			// if (i < ACTIVENEURONS + 1) {
			// try {
			// jSliderNeurons[i].setValue((int) (n[i] * 100));
			// } catch (ArrayIndexOutOfBoundsException e) {
			// System.out.println("AIOBE: index " + i);
			// }
			//
			// }
		}
	}

	public void initExampleWeights() {
		w[0][0] = 1.1;
		w[0][1] = 0.3;
		w[0][2] = 0.0;

		w[1][0] = -0.3;
		w[1][1] = 1.1;
		w[1][2] = 0.0;

		n[0] = 0.000001;
		n[1] = 0.000001;
		n[2] = 1;

		a[0] = 0.0;
		a[1] = 0.0;
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		Network net = new Network();

		net.initExampleWeights();
		net.createAndShowGui();

		drawNeuron = new DrawNeuron();
		drawValley = new DrawValley();

		drawNeuron.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		drawNeuron.setVisible(true);
		drawNeuron.init();

		drawValley.setVisible(true);
		drawValley.init();

		while (true) {
			if (run) {

				drawNeuron.setValues(n);
				drawNeuron.repaint();

				net.delay(20);
				net.updateCurrentNetwork();

				// System.out.println("n[0] = " + n[0] + "; n[1] = " + n[1]);

				net.determineFixpoints();
			}
		}
	}

	private void determineFixpoints() {

		for (int i = 0; i < ACTIVENEURONS; i++) {
			n_hist[histcounter][i] = n[i];
		}

		// cluster:

		if (histcounter == HISTLENGTH - 1) {
			int countFixP = HISTLENGTH;
			for (int i = 0; i < HISTLENGTH; i++) {
				if (n_hist[i][0] == INVALID) {
					continue;
				}
				for (int j = i + 1; j < HISTLENGTH; j++) {
					if ((n_hist[j][0] != INVALID)
							&& (sqrdist(n_hist[i], n_hist[j]) < EPSILON)) {
						n_hist[j][0] = INVALID;
						countFixP--;
					}
				}
			}
			System.out.println("NumOfDiffPoints at least: " + countFixP);
		}

		histcounter++;
		histcounter %= HISTLENGTH;
	}

	private double sqrdist(double[] n, double[] nh) {
		double help = 0;
		for (int i = 0; i < ACTIVENEURONS; i++) {
			help += (n[i] - nh[i]) * (n[i] - nh[i]);

		}

		return help;
	}

	void createAndShowGui() {
		JFrame jFrame = new JFrame("Skynet");

		jFrame.setSize(640, 480);

		jFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		BorderLayout bLayout = new BorderLayout();
		jFrame.setLayout(bLayout);

		GridLayout sliderGrid = new GridLayout();
		sliderGrid.setRows(1);

		JPanel sliderPanel = new JPanel();
		jFrame.add(sliderPanel, BorderLayout.NORTH);

		sliderPanel.setLayout(sliderGrid);

		JPanel jPanelS = new JPanel();
		jFrame.add(jPanelS, BorderLayout.SOUTH);

		for (int i = 0; i < ACTIVENEURONS + 1; i++) {
			for (int j = 0; j < (ACTIVENEURONS + 1); j++) {
				if (i == ACTIVENEURONS)
					break;

				GridLayout weightGrid = new GridLayout();
				weightGrid.setColumns(1);
				weightGrid.setRows(2);

				JPanel weightPanel = new JPanel();
				weightPanel.setLayout(weightGrid);

				sliderPanel.add(weightPanel);

				jSliderWeights[i][j] = new JSlider();
				jLabelWeights[i][j] = new JLabel();

				jSliderWeights[i][j].setOrientation(JSlider.VERTICAL);

				jSliderWeights[i][j].setMinimum(-400);
				jSliderWeights[i][j].setMaximum(+400);

				jSliderWeights[i][j].setMajorTickSpacing(100);
				jSliderWeights[i][j].setMinorTickSpacing(20);

				// System.out.println(w[0][0]);
				jSliderWeights[i][j].setValue((int) (w[i][j] * 100));
				jLabelWeights[i][j].setText("w" + i + j + "="
						+ String.valueOf(w[i][j]));

				jSliderWeights[i][j].setPaintTicks(true);
				jSliderWeights[i][j].setPaintLabels(true);

				final int k, l;

				k = i;
				l = j;
				jSliderWeights[i][j].addChangeListener(new ChangeListener() {

					public void stateChanged(ChangeEvent arg0) {
						w[k][l] = 0.01 * jSliderWeights[k][l].getValue();
						jLabelWeights[k][l].setText("w" + k + l + "="
								+ String.valueOf(w[k][l]));
						// System.out.println(w11);
						drawValley.calculateValley(w, n);
						drawValley.repaint();

					}
				});

				weightPanel.add(jSliderWeights[i][j]);
				weightPanel.add(jLabelWeights[i][j]);
			}

			jSliderNeurons[i] = new JSlider();
			jLabelNeurons[i] = new JLabel();
			jSliderNeurons[i].setOrientation(JSlider.VERTICAL);

			jSliderNeurons[i].setMinimum(-100);
			jSliderNeurons[i].setMaximum(+100);

			jSliderNeurons[i].setMajorTickSpacing(100);
			jSliderNeurons[i].setMinorTickSpacing(20);

			jSliderNeurons[i].setValue((int) (n[i] * 100));
			jLabelNeurons[i].setText("n" + i + "=" + String.valueOf(n[i]));

			jSliderNeurons[i].setPaintTicks(true);
			jSliderNeurons[i].setPaintLabels(true);

			final int o = i;

			jSliderNeurons[i].addChangeListener(new ChangeListener() {

				public void stateChanged(ChangeEvent arg0) {
					n[o] = 0.01 * jSliderNeurons[o].getValue();
					jLabelNeurons[o].setText("n" + o + "="
							+ String.valueOf(n[o]));

					// System.out.println(w11);

				}
			});

			GridLayout neuronGrid = new GridLayout();
			neuronGrid.setColumns(1);
			neuronGrid.setRows(2);

			JPanel neuronPanel = new JPanel();
			neuronPanel.setLayout(neuronGrid);

			sliderPanel.add(neuronPanel);

			neuronPanel.add(jSliderNeurons[i]);
			neuronPanel.add(jLabelNeurons[i]);

		}

		JButton startButton = new JButton();
		startButton.setText("Start");

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run = true;
			}
		});

		JButton stopButton = new JButton();
		stopButton.setText("Stop");

		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				run = false;
			}
		});

		JButton drawButton = new JButton();
		drawButton.setText("Draw");

		drawButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawValley.calculateValley(w, n);
				drawValley.repaint();
			}
		});

		jPanelS.add(startButton);
		jPanelS.add(stopButton);
		jPanelS.add(drawButton);
		jFrame.setVisible(true);

	}

}
