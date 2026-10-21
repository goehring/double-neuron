


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;



public class DrawNeuron extends JFrame{

	Graphics offscreenGraphics;
	Image offscreenImage;
	
	
	
	int SIZE = 320;
	double n[];
	double[] backup = new double[2];
	
	Color col;
	
	
	public void setValues(double[] n)
	{
		this.n = n;	
	}
	
	public DrawNeuron() {
		setSize(SIZE,SIZE);
		setTitle("Neural Outputs");
	}
	
	public void init() {
		offscreenImage = createImage( SIZE, SIZE );
		offscreenGraphics = offscreenImage.getGraphics();
		
	}
	
	
	private void offPaint()
	{
		
		
		
		int eunit = (int)(1.0/3.2*SIZE);
		int unit = (int)(eunit*1.5);
		int twonit = 2*unit;
		
		col = new Color((float)0.7, (float)0.7, (float)0.7);
		offscreenGraphics.setColor(col);
		offscreenGraphics.drawOval((int)(backup[0]*100)-2+unit, twonit-(int)(backup[1]*100)-2-unit, 4, 4);
		offscreenGraphics.drawOval((int)(backup[0]*100)-1+unit, twonit-(int)(backup[1]*100)-1-unit, 4, 4);
		
		
		col = new Color((float)1.0, (float)0.3, (float)0.3);
		offscreenGraphics.setColor(col);
		offscreenGraphics.drawOval((int)(backup[0]*100)-1+unit, twonit-(int)(backup[1]*100)-1-unit, 2, 2);

		
		col = new Color(0, 0, 0);
		offscreenGraphics.setColor(col);
		offscreenGraphics.drawOval((int)(n[0]*100)-2+unit, twonit-(int)(n[1]*100)-2-unit, 4, 4);
		offscreenGraphics.drawOval((int)(n[0]*100)-1+unit, twonit-(int)(n[1]*100)-1-unit, 4, 4);
		
		
		
		backup[0] = n[0];
		backup[1] = n[1];
		
		offscreenGraphics.drawLine(unit-eunit, unit+eunit, unit+eunit, unit-eunit);
		offscreenGraphics.drawLine(unit-eunit, unit, unit+eunit, unit);
		offscreenGraphics.drawLine(unit, unit+eunit, unit, unit-eunit);
	
	}
	
	public void paint(Graphics g) {
		//if (i++ == 0)
		offPaint();
		
		 if ( offscreenImage != null )
			    g.drawImage( offscreenImage, 0, 0, this );	
		
	}
	
	
	
	
}
