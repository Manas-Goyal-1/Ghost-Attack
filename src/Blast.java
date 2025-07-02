import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Blast {
	public int r, x, y;
	boolean growing;
	
	public Blast(int x, int y)
	{
		this.x = x;
		this.y = y;
		r = 500;
		growing = true;
	}
	
	public boolean update(ArrayList<Ghost> ghosts)
	{
		if (growing)
		{
			r += 10;
			if (r >= 900)
				growing = false;
		}
		else
		{
			r -= 3;
		}
		
		for (int i = ghosts.size()-1; i >= 0; i--)
		{
			Ghost ghost = ghosts.get(i);
			if ((ghost.x-x)*(ghost.x-x) + (ghost.y-y)*(ghost.y-y) <= r*r/25)
			{
				if (!ghost.hit(1))
					ghosts.remove(ghost);
			}
		}
		
		return r > 0;
		
	}
	
	public void draw (Graphics g)
	{
		g.setColor(new Color(191, 77, 240, 120));	// purple ground
		g.fillOval(x-r/5, y-r/5, 2*r/5, 2*r/5);
	}
	
}


