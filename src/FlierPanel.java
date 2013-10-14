import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.lang.*;


public class FlierPanel extends JPanel implements Runnable
{
	private static final int PWIDTH = 500;
	private static final int PHEIGHT = 400;
	
	private static final int NUM_FPS = 10;
	
	private boolean running = false;
	private boolean isPaused = false;
	private boolean showSelected = false;
	
	private long gameStartTime;
	
	private Thread animator;
	private double fpsStore[];
	private double upsStore[];
	
	private Fliers flTop;
	private Obstacles obs;
	private Ship player;
	private long period;
	
	
	private int timeSpentInGame;
	
	private Font font;
	private FontMetrics metrics;
	
	private Graphics dbg;
	private Image dbImage = null;
	
	public FlierPanel( Fliers fl, long period )
	{
		flTop = fl;
		this.period = period;
		
		setBackground( Color.white );
		setPreferredSize( new Dimension( PWIDTH, PHEIGHT ));
		
		setFocusable( true );
		requestFocus();
		//readyForTermination();
		
		obs = new Obstacles( flTop );
		player = new Ship( PWIDTH, PHEIGHT, obs );
		
		addKeyListener( new KeyListener() {
			
			@Override
			public void keyTyped( KeyEvent e ) {}
			
			@Override
			public void keyReleased( KeyEvent e ) {}
			
			@Override
			public void keyPressed( KeyEvent e ) {
				int kc = e.getKeyCode();

				if ( kc >= 37 && kc <= 40 ) {
					System.out.println( "Pressed " + (kc-37) );
					flTop.setDirection( kc-37 );
					//player.move( kc-37 );
				}
			}
		});
		
		addMouseListener( new MouseAdapter() {
			
			@Override
			public void mousePressed( MouseEvent e )
			{ testPress( e.getX(), e.getY()); }
		});
		
		font = new Font( "SansSerif", Font.BOLD, 24 );
		metrics = this.getFontMetrics( font );
		
		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for ( int i=0; i< NUM_FPS; i++ ) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}
	}
	
	private void readyForTermination()
	{
		//code here
	}
	
	public void addNotify()
	{
		super.addNotify();
		startGame();
	}
	
	private void startGame()
	{
		if ( animator == null || !running ) {
			animator = new Thread( this );
			animator.start();
		}
	}
	
	
	public void resumeGame()
	{ isPaused = false; }
	
	public void pauseGame()
	{ isPaused = true; }
	
	public void stopGame()
	{ running = false; }
	
	
	private void testPress( int x, int y )
	{
		if (!isPaused) {
			if ( obs.isSelectedAt(x,y)) {
				showSelected = true;
				obs.deSelect();
			} else {
				obs.add(x, y);
				showSelected = false;
			}
		}
	}
	
	public void run()
	{
		gameStartTime = System.nanoTime();
		running = true;
		
		while (running) {
			gameUpdate();
			gameRender();
			paintScreen();
			
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			long timeNow = System.nanoTime();
			timeSpentInGame = (int) ((timeNow - gameStartTime)/1000000000L);
			//timeSpentInGame = 5;
			flTop.setTimeSpent( timeSpentInGame );
		}
		
		
	}
	
	private void gameUpdate()
	{
		obs.update();
		player.update();
	}
	
	private void gameRender()
	{
		if (dbImage == null ) {
			dbImage = createImage( PWIDTH, PHEIGHT );
			if (dbImage == null ) {
				System.out.println( "dbImage is null" );
				return;
			} else { dbg = dbImage.getGraphics(); }
		}
		
		//clear the background
		dbg.setColor( Color.white );
		dbg.fillRect( 0, 0, PWIDTH, PHEIGHT );
		dbg.setColor( Color.blue );
		dbg.setFont( font );
		
		if (showSelected) {
			dbg.drawString("You have selected a square", 20, 25);
			dbg.setColor(Color.black);
		}
		
		obs.draw(dbg);
		player.draw(dbg);
	}
	
	//private void gameOverMessage( Graphics g )
	
	private void paintScreen()
	{
		Graphics g;
		try {
			g = this.getGraphics();
			if (( g != null ) && (dbImage != null )) {
				g.drawImage( dbImage, 0, 0, null );
			}
			
			g.dispose();
		}
		catch ( Exception e )
		{ System.out.println( "Graphics context error: " + e ); }
	}
	
	//private void storeStats()
	//private void printStats()
}
