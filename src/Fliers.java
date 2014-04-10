import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Fliers extends JFrame implements WindowListener
{
	private static final int DEFAULT_FPS = 20; 
	
	private String[] dirList = {"LEFT", "UP", "RIGHT", "DOWN" };
	
	private FlierPanel fp;
	private JTextField jtfScore;
	private JTextField jtfDir;
	private JTextField jtfTime;
	
	public Fliers( long period )
	{
		super( "Fliers" );
		makeGUI( period );
		
		addWindowListener( this );
		pack();
		setResizable( false );
		setVisible( true );
	}
	
	private void makeGUI( long period )
	{
		Container c = getContentPane();
		
		fp = new FlierPanel( this, period );
		c.add( fp, "Center" );
		
		JPanel ctrls = new JPanel();
		ctrls.setLayout( new BoxLayout( ctrls, BoxLayout.X_AXIS ));
		
		jtfScore = new JTextField( "Score: 0" );
		jtfScore.setEditable( false );
		ctrls.add( jtfScore );
		
		jtfDir = new JTextField( "Direction: None" );
		jtfDir.setEditable( false );
		ctrls.add( jtfDir );
		
		jtfTime = new JTextField( "Time spend: 0 secs" );
		jtfTime.setEditable( false );
		ctrls.add( jtfTime );
		
		c.add( ctrls, "South" );
	}
	
	public void setScore( int score )
	{ jtfScore.setText( "Score: " + score ); }
	
	public void setDirection( int dir )
	{ jtfDir.setText( "Direction: " + dirList[dir] ); }
	
	public void setTimeSpent( long t )
	{ jtfTime.setText( "Time Spent: " + t + " secs" ); }
		
	
	public void windowActivated( WindowEvent e )
	{ fp.resumeGame(); }
	
	public void windowDeactivated( WindowEvent e )
	{ fp.pauseGame(); }
	
	public void windowDeiconified( WindowEvent e )
	{ fp.resumeGame(); }
	
	public void windowIconified( WindowEvent e )
	{ fp.pauseGame(); }
	
	public void windowClosing( WindowEvent e )
	{ fp.stopGame(); }
	
	public void windowClosed( WindowEvent e ) {}
	public void windowOpened( WindowEvent e ) {}
	
	
	public static void main( String args[] )
	{
		int fps = DEFAULT_FPS;
		if( args.length != 0 ) {
			fps = Integer.parseInt( args[0] );
		}
		
		long period = (long) 1000.0/fps;
		System.out.println("fps: " + fps + "; period: " + period + " ms" );
		
		new Fliers( period*1000000L );
	}
}
