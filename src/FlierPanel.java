import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.lang.*;


public class FlierPanel extends JPanel implements Runnable
{
	private static final int SPACE = 32;
	private static final int C_KEY = 67;
	private static final int H_KEY = 72;
	private static final int LEFT_ARROW = 37;
	private static final int RIGHT_ARROW = 39;
	private static final int DOWN_ARROW = 40;
	
	public static final int PWIDTH = 1504; // Multiple of 32
	public static final int PHEIGHT = 700; //400;
	public static final int BOX_WIDTH = 32;
	public static final int BOX_HEIGHT = 32;

	private static final int NUM_FPS = 10;
	private static final int SHIP_COUNT = 1;
	private static final int MAX_LAG = 15;
	
	private boolean running = false;
	private boolean isPaused = false;
	private boolean showSelected = false;
	private boolean drawBest = false;
	private boolean doRender = true;
	
	private long gameStartTime;
	
	private Thread animator;
	private double fpsStore[];
	private double upsStore[];
	
	private Fliers flTop;
	private Obstacles obs;
	private BlockDropperContainer bdc;
	private ShipContainer ships;
	private Ship selectedShip;
	
	//private Ship player;
	private long period;
	private long ticks;
	
	private int lag;
	private int timeSpentInGame;
	
	private Font font;
	private FontMetrics metrics;
	
	private Graphics dbg;
	private Image dbImage = null;
	
	public FlierPanel( Fliers fl, long period )
	{
		flTop = fl;
		ticks = 0;
		lag = 5;
		this.period = period;
		
		setBackground( Color.white );
		setPreferredSize( new Dimension( PWIDTH, PHEIGHT ));
		
		setFocusable( true );
		requestFocus();
		//readyForTermination();
		
		obs = new Obstacles( flTop, BOX_WIDTH, BOX_HEIGHT );
		int[][] spos = new int[][] {
				{-10,5},
				{-6,11},
				{0,3},
				{7,17},
				{12,12}
		};

		//player = new Ship( new int[][] {{-100,15},{-6,41},{0,10},{27,17}}, PWIDTH, PHEIGHT, obs );
		ships = new ShipContainer( PWIDTH, PHEIGHT, obs );
		//ships.buildShips( SHIP_COUNT );
		
		bdc = new BlockDropperContainer( PWIDTH, BOX_WIDTH, 60, obs ); //( int width, int blockSize, int pd, Obstacles os )
		
		addKeyListener( new KeyListener() {
			
			@Override
			public void keyTyped( KeyEvent e ) {}
			
			@Override
			public void keyReleased( KeyEvent e ) {}
			
			@Override
			public void keyPressed( KeyEvent e ) {
				int kc = e.getKeyCode();
				if ( kc == SPACE ) ships.printGenomeScores();
				if ( kc == C_KEY ) {
					System.out.println( ships.getBestScore( ShipContainer.SHOW_BEST_LIMIT ));
					drawBest = !drawBest; //ships.printShipScores();
				}
				if ( kc == H_KEY ) doRender = !doRender;
				if ( kc == LEFT_ARROW && lag < MAX_LAG ) lag++;
				if ( kc == RIGHT_ARROW && lag > 0 ) lag--;
				//if ( kc >= LEFT_ARROW && kc <= DOWN_ARROW ) {
					//System.out.println( "Pressed " + (kc-LEFT_ARROW) );
					//flTop.setDirection( kc-LEFT_ARROW );
					//player.move( kc-37 );
				//}
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
		if (!isPaused) selectedShip = ships.getSelectedAt(x, y);
		
		/*
		if (!isPaused) {
			if ( !ships.isSelectedAt(x,y) ) {
				//showSelected = true;
				//obs.deSelect();
			} else {
				ships.buildRandom();
				//obs.add(x, y);
				//showSelected = false;
			}
		}
		*/
	}
	
	public void run()
	{
		gameStartTime = System.nanoTime();
		running = true;
		
		while (running) {
			gameUpdate();
			if( doRender ) gameRender();
			paintScreen();
			ticks++;
			
			if( doRender ) {
				try {
					Thread.sleep( lag );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			long timeNow = System.nanoTime();
			timeSpentInGame = (int) ((timeNow - gameStartTime)/1000000000L);
			//timeSpentInGame = 5;
			
			
			
			if( selectedShip != null ) {
				flTop.setScore( selectedShip.getFitness() );
				flTop.setLifeSpan( selectedShip.getLifespan() );
				if( !selectedShip.isActive() ) selectedShip = null;
			} else {
				flTop.setScore( ships.getBestScore(0) );
				flTop.setLifeSpan( ships.getCurrentLifeSpan() );
			}
			
			flTop.setTimeSpent( timeSpentInGame );
			flTop.setThreshChance( bdc.getThresholdAverage() );
			
			flTop.setTicks( ticks >> 5 );
		}
		
		
	}
	
	private void gameUpdate()
	{
		obs.update();
		//player.update();
		ships.update();
		bdc.update();
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
		ships.draw(dbg,drawBest);
		//player.draw(dbg);
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
