import java.util.ArrayList;
import ie.ul.konane.Konane;
import ie.ul.konane.KonaneMove;



public class KonaneState
{
	private Konane currentState;
	private ArrayList < KonaneState > nextStates;
	private ArrayList < KonaneMove > nextMoves;
	private int currentDepth;
	private boolean max;
	private KonaneMove moveThatCreateState;
	private KonaneState parentState;
	private int heuristicValue;
	private static int maxDepth = 4;
	public static char player;
	public static char opponent;
	
	
	public KonaneState( Konane state )
	{
		this.max = true;
		this.nextStates = new ArrayList< KonaneState >();
		this.currentState = state;
		this.currentDepth = 1;
		this.generateStates();				
	}
	
	
	
	public KonaneState( Konane state , int depth , boolean max , KonaneMove move , KonaneState parent )
	{
		this.parentState = parent;
		this.moveThatCreateState = move;
		this.max = max;
		this.currentDepth = depth;
		this.nextStates = new ArrayList< KonaneState >();
		this.currentState = state;
		this.generateStates();		
	}
	
	
	
	private void generateStates()
	{
		
		if( this.currentDepth == KonaneState.maxDepth )
		{
			return;
		}
		else
		{
			if( this.max == true )
			{
				this.nextMoves = this.currentState.generateMoves( KonaneState.opponent );
				this.generateMinNextStates();				
			}
			else
			{
				this.nextMoves = this.currentState.generateMoves( KonaneState.player );	
				this.generateMaxNextStates();				
			}
		}		
	}
	
		
	
	/*
	 * Generates the next board states from each possible move at this
	 * depth
	 */

	private void generateMaxNextStates()
	{

		Konane newBoard;
		KonaneState tempState;
		KonaneMove possibleMove;

		if( this.currentDepth == KonaneState.maxDepth )
		{
			return;
		}
		else
		{
			for ( int counter = 0 ; counter < this.nextMoves.size() ; counter++ )
			{
				newBoard = new Konane( this.currentState );
				possibleMove = new KonaneMove( this.nextMoves.get( counter ) );
				
				try
				{
					newBoard.makeMove( KonaneState.player, possibleMove );
				}
				catch ( Exception e )
				{
					//e.printStackTrace();
				}

				tempState = new KonaneState( newBoard , this.currentDepth + 1 , !this.max , possibleMove , this );
				this.nextStates.add( tempState );
				
			}
		}
		
		this.calculateHeuristicValue();		
	}
	
	
	
	/*
	 * Generates the next board states from each possible move at this
	 * depth
	 */

	private void generateMinNextStates()
	{		
		Konane newBoard;
		KonaneState tempState;
		KonaneMove possibleMove;

		if( this.currentDepth == KonaneState.maxDepth )
		{
			return;
		}
		else
		{
			for ( int counter = 0 ; counter < this.nextMoves.size() ; counter++ )
			{
				newBoard = new Konane( this.currentState );
				possibleMove = new KonaneMove( this.nextMoves.get( counter ) );
				
				try
				{
					newBoard.makeMove( KonaneState.opponent , possibleMove );
				}
				catch ( Exception e )
				{
					//e.printStackTrace();
				}

				tempState = new KonaneState( newBoard , this.currentDepth + 1 , !this.max , possibleMove , this );
				this.nextStates.add( tempState );
				
			}
		}
		
	}
	
	private void calculateHeuristicValue()
	{	
		this.heuristicValue = ( int ) 5;
	}
	
	
	public int getHeurisiticValue()
	{
		return this.heuristicValue;
	}
	
	
	public boolean isMax()
	{
		return this.max;
	}
	
	
	public KonaneMove getLastMove()
	{
		return this.moveThatCreateState;
	}
	
	public void setHeurisiticValue( int value )
	{
		this.heuristicValue = value;
	}
	
	public int getDepth()
	{
		return this.currentDepth;
	}
	
	public KonaneState getParent()
	{
		return this.parentState;
	}
		
	public ArrayList < KonaneState > getNextStates()
	{
		return this.nextStates;
	}
		
}
