import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class MyDrawing  extends JPanel {
	    
	private static final long serialVersionUID = -6540018193907144230L;

	/**
	 * 
	 */
	
	int value1, value2;
	
	
	public void set1(int value1){
		this.value1 = value1;		
	}

	public void set2(int value2){
		this.value2 = value2;		
	}
	
	public MyDrawing() {
//		this.setBounds(0,130, 600,300);
	}

		public void paint(Graphics g) {
	         super.paint(g);    // paints background
	         
	         this.setBackground(Color.white);
	         g.setColor(Color.black);
	       
	         g.drawLine(1+value1, 1, 300+value2, 300);
		
		}
		
	}

	
	


