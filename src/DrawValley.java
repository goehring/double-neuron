import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;

public class DrawValley extends JFrame {

	Graphics offscreenGraphics;
	Image offscreenImage;

	int PANELSIZE = 320;
	int FCTSIZE = (int) (1.0 / 1.6 * PANELSIZE);
	int RES = 2;
	double canvas[][] = new double[FCTSIZE][FCTSIZE];

	public DrawValley() {
		setSize(PANELSIZE, PANELSIZE);

		setTitle("Nullklinen");
	}

	public void init() {
		offscreenImage = createImage(PANELSIZE, PANELSIZE);
		offscreenGraphics = offscreenImage.getGraphics();
	}

	private void offPaint() {

		Color col = new Color(0, 0, 0);

		for (int i = 0; i < FCTSIZE; i += RES) {
			for (int j = 0; j < FCTSIZE; j += RES) {
				col = new Color((float) (1 - canvas[i][j]), (float)0.0,
						(float) canvas[i][j]);
				offscreenGraphics.setColor(col);
				offscreenGraphics.drawRect((PANELSIZE - FCTSIZE) / 2 + i,
						(PANELSIZE + FCTSIZE) / 2 - j, RES - 1, RES - 1);
				// offscreenGraphics.drawRect((PANELSIZE-FCTSIZE)/2+i+RES/4,(PANELSIZE+FCTSIZE)/2-j+RES/4,
				// RES/4, RES/4);

			}
		}

	}

	public void paint(Graphics g) {

		offPaint();

		if (offscreenImage != null)
			g.drawImage(offscreenImage, 0, 0, this);

	}

	public void calculateValley(double[][] w, double n[]) {
		double n0, n1;
		double nval0, nval1;
		double max = -1;
		double min =  1;

		for (int i = 0; i < FCTSIZE; i += RES) {
			for (int j = 0; j < FCTSIZE; j += RES) {
				n0 = (-0.5 * FCTSIZE + i) / (0.5 * FCTSIZE);
				n1 = (-0.5 * FCTSIZE + j) / (0.5 * FCTSIZE);

				nval0 = Math.tanh(w[0][0] * n0 + w[0][1] * n1 + w[0][2] * n[2]);
				nval1 = Math.tanh(w[1][0] * n0 + w[1][1] * n1 + w[1][2] * n[2]);

				canvas[i][j] = Math.log(0.00001+(Math.abs((nval0 - n0) * (nval1 - n1))));

//				canvas[i][j] = Math.sqrt(Math.sqrt(Math.abs((nval0 - n0)
//						* (nval1 - n1))));

				if (canvas[i][j] > max) {
					max = canvas[i][j];
				}
				
				if (canvas[i][j] < min) {
					min = canvas[i][j];
				}
				

//				if ((i % 10 == 0) && (j % 10 == 0)) {
//					System.out.println("Canvas x: " + i + " y: " + j + " val "
//							+ canvas[i][j]);
//				}

			}
		}
		
		for (int i = 0; i < FCTSIZE; i += RES) {
			for (int j = 0; j < FCTSIZE; j += RES) {
				canvas[i][j] -= min;
			}
		}
		
		max -= min;
		
		
		// normalize canvas
		if (max > 0) {

			for (int i = 0; i < FCTSIZE; i += RES) {
				for (int j = 0; j < FCTSIZE; j += RES) {
					canvas[i][j] /= max;
				}
			}
		}

	}

}
