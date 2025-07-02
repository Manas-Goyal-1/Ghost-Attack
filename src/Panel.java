import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel
{
	private int w, h;
	private Timer timer;
	private ArrayList<Ghost> ghosts;
	int tick, diff;
	int score;
	MouseListener ml;
	Image person;
	int playerMaxHealth, playerHealth;
	boolean dead;
	JButton playAgain;
	int nextWave;
	int blast;
	ArrayList<Blast> blasts;
	
	public Panel(int w, int h)
	{
		this.w = w;
		this.h = h;
		this.setPreferredSize(new Dimension(w,h));
		ghosts = new ArrayList<>();
		
		tick = 0;
		diff = 1;
		score = 0;
		nextWave = 50;
		ml = new ML();
		this.addMouseListener(ml);
//		System.out.println(this.getMouseListeners()[0]);
//		spawnGhost();
		super.setBackground(new Color(89, 14, 237));
		
		try
			{person = ImageIO.read(new File("src/person.png"));}
		catch (IOException e) {}
		
		dead = false;
		
		blast = 0;
		
		playerMaxHealth = 1000;
		playerHealth = playerMaxHealth;
		
		blasts = new ArrayList<>();

		playAgain = new JButton("Play Again");
		playAgain.setFont(new Font("Arial", Font.BOLD, 18));
		playAgain.setPreferredSize(new Dimension(150, 50));
		playAgain.setBackground(Color.GREEN);
		playAgain.addActionListener(new AL());
		
		playAgain.setBounds(240, 500, 160, 60);
		playAgain.setVisible(false);
		super.add(playAgain);
		
		super.setLayout(null);
		this.addKeyListener(new KL());
		
		super.setFocusable(true);
		super.requestFocus();
		
		
		timer = new Timer(10, new AL());
		timer.start();
		
	}
	
	public void paintComponent(Graphics g)
	{
		//this line sets up the graphics - always needed
		super.paintComponent(g);
		
		if (!dead)
		{
			g.setColor(new Color(63, 214, 43));
			g.drawOval(260, 240, 120, 120);	// radius = 60
			
			// Plaeyr
			g.drawImage(person, 260, 240, 120, 120, null);

			g.setColor(new Color(209, 247, 195));
			g.fillRect((int) 320-playerMaxHealth/20, (int) 50, playerMaxHealth/10, 20);
			
			g.setColor(new Color(82, 237, 43));
			g.fillRect((int) 320-playerMaxHealth/20, (int) 50, playerHealth/10, 20);
			
			for (Blast b: blasts)
				b.draw(g);
			
		}
		else
		{
			Image blood = null;
			try
				{blood = ImageIO.read(new File("src/red-splatter.png"));}
			catch (IOException e) {}
			
			g.drawImage(blood, 220, 220, 200, 160, null);
			playAgain.setVisible(true);
			
		}
		
		for (Ghost ghost: ghosts)
		{
			ghost.draw(g);
		}

		g.setColor(Color.WHITE);
		g.setFont(new Font("Dialog", Font.BOLD, 20));
		g.drawString("Score: "+score, 30, 40);
		
		g.setFont(new Font("Default", Font.PLAIN, 15));
		g.drawString("Level: "+diff, 30, 60);
		
//		g.drawOval(70, 50, 500, 500);
	}
	
	public void spawnGhost()
	{
		Ghost ghost = new Ghost(diff);
//		ghost.addMouseListener(ml);
		ghosts.add(ghost);
//		System.out.println(ghost.getMouseListeners()[0]);
	}
	
	public void dealDamage()
	{
		playerHealth -= 2;
		if (playerHealth <= 0)
		{
			timer.stop();
			dead = true;
			repaint();
		}
	}
	
	private class AL implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == timer)
			{
				tick++;
				if (tick >= 100-diff*2)
				{
					tick = 0;
					spawnGhost();
				}
				
				// A big wave
				if (score >= nextWave)
				{
					ghosts.clear();
					for (double i = 0; i < 2*Math.PI; i += Math.PI/3)
					{
						ghosts.add(new Ghost(320+300*Math.cos(i), 300+300*Math.sin(i), 3));
					}
					nextWave = score + 20;
				}
				
				for (Ghost ghost: ghosts)
					if (!ghost.update())
						dealDamage();
				
				for (int i = blasts.size() -1; i >= 0; i--)
				{
					Blast blast = blasts.get(i);
					if (!blast.update(ghosts))
						blasts.remove(blast);
				}
			}
			
			else if (e.getSource() == playAgain)
			{
				dead = false;
				playerHealth = playerMaxHealth;
				ghosts.clear();
				diff = 0;
				tick = 0;
				score = 0;
				timer.start();
				playAgain.setVisible(false);
				nextWave = 50;
				blasts.clear();
			}
			
			diff = Math.min(10, score / 5);
			repaint();
		}
	}

	private class ML extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e) {
//			System.out.println(e.getX() + " " + e.getY());
			boolean hit = false;
			
			int x = e.getX();
			int y = e.getY();
			for (int i = 0; i < ghosts.size(); i++)
			{
				Ghost ghost = ghosts.get(i);
				if (ghost.intersects(x, y))
				{
					hit = true;
//					ghosts.remove(ghost);
					if (!ghost.hit(100))
					{
						ghosts.remove(ghost);
						score += 2;
					}
//						System.out.println(score);
				}
			}
			
			if (blast >= 1)
			{
				blast--;
				blasts.add(new Blast(x, y));
			}
			
			if (!hit)
				score--;
		}
	}
	
	private class KL extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_W)
			{
				if (score >= 20)
				{
					blast++;
					score -= 20;
				}
			}
		}
	}	
}




