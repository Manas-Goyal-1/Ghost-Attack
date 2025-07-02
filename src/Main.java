import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Main
{
	public static void main(String[] args) 
	{		
		final int WIDTH = 640;
		final int HEIGHT = 600;
				
		JFrame frame = new JFrame("Ghost Game");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(new Panel(WIDTH, HEIGHT));
		
		frame.pack();
		frame.setVisible(true);
	}
}


