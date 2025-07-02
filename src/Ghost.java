import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class Ghost extends JLabel {
	public double x, y, speedX, speedY;
	public final int sizeX = 30, sizeY = 40;
	private Image img;
	public int tX, tY;
	public int maxHealth, health;
	boolean isHit;
	int hitValue;
	
	public Ghost(int diff)
	{
		double x, y;
		do
		{
			x = Math.random()*(640);
			y = Math.random()*(600);
		}
		while ((x-320)*(x-320) + (y-300)*(y-300) <= 250*250);
		
		construct(x, y, diff);
		
	}
	
	public Ghost(double x, double y, int diff)
	{
		construct(x, y, diff);
	}
	
	public void construct(double x, double y, int diff)
	{
		this.x = x;
		this.y = y;
		
		speedX = Math.min(Math.random()*2+1, 5);
		speedY = Math.min(Math.random()*2+1, 5);
		
		try
			{img = ImageIO.read(new File("src/ghost.png"));}
		catch (IOException e) {}
		
		maxHealth = 50 + 15*diff;
		health = maxHealth;
		isHit = false;
		hitValue = 0;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(img, (int) x-sizeX, (int) y-sizeY, 2*sizeX, 2*sizeY, null);
//		g.drawRect((int) x, (int) y, sizeX, sizeY);
		
		// Health Bar
		g.setColor(new Color(242, 188, 172));
		g.fillRect((int) x-maxHealth/6, (int) y-sizeY-17, maxHealth/3, 10);
		
		g.setColor(new Color(240, 19, 45));
		g.fillRect((int) x-maxHealth/6, (int) y-sizeY-17, health/3, 10);
		
		g.setColor(Color.BLACK);
		g.drawRect((int) x-maxHealth/6, (int) y-sizeY-17, maxHealth/3, 10);
		
	}
	
	public boolean intersects(int x1, int y1)
	{
		return (x1 >= x-sizeX && x1 <= x+sizeX) && (y1 >= y-sizeY && y1 <= y+sizeY);
	}
	
	public boolean update()
	{
		if ((x-320)*(x-320) + (y-300)*(y-300) <= 60*60)
			return false;
		
		double moveX, moveY;
		if (x > 280)
			moveX = -speedX;
		else if (x < 360)
			moveX = speedX;
		else
			moveX = 0;
		
		if (y > 340)
			moveY = -speedY;
		else if (y < 260)
			moveY = speedY;
		else
			moveY = 0;
		
		x += moveX;
		y += moveY;
		
		return true;
	}
	
	public boolean hit(int damage)
	{
		health -= damage;
		if (health > 0)
			return true;
		return false;
	}
}



