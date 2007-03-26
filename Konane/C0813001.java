import java.util.ArrayList;
import ie.ul.konane.Konane;
import ie.ul.konane.KonaneMove;
import ie.ul.konane.Player;


/**
 * MiniMax DFS Implementation to play Konane
 * 
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */

public class C0813001 extends Player
{
	private Konane currentState;


	/**
	 * Constructor
	 * 
	 * @param pName ( The name Associated with the user )
	 */
	
	public C0813001( String pName )
	{
		super( pName );
	}


	/* (non-Javadoc)
	 * @see ie.ul.konane.Player#initialize(char)
	 */
	
	@Override
	public void initialize( char pColour )
	{
		this.name = "0813001";
		this.colour = pColour;
	}


	/**
	 * Instantiates the tree and gets the best move
	 * 
	 * @return KonaneMove ( the next best move )
	 */
	
	private KonaneMove decideMyMove()
	{
		KonaneMove move = null;

		if( this.currentState.generateMoves( this.colour ).size() == 0 )
		{
			KonaneMove gameOver = new KonaneMove( 0 , 0 );
			gameOver.lostGame();
			return gameOver;
		}

		try
		{
			Tree tree = new Tree( this.currentState , this.colour );
			move = tree.getNextMove();
		}
		catch ( Exception e )
		{
			move = this.currentState.generateMoves( this.colour ).get( 0 );
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}

		return move;
	}


	/* (non-Javadoc)
	 * @see ie.ul.konane.Player#makeMove(ie.ul.konane.Konane)
	 */
	
	@Override
	public KonaneMove makeMove( Konane game )
	{
		this.currentState = game;
		KonaneMove m = this.decideMyMove();
		return m;
	}

}







/**
 * MiniMax DFS Implementation to play Konane
 * 
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */

class Tree
{	
	public static char PLAYER;
	public static char OPPONENT;
	public static int MAXDEPTH;
	public static int MAXVALUE;
	public static int MINVALUE;
	public static char heurisitic;
	private Node head;

	
	/**
	 * Java's version of a static constructor
	 */
	
	static
	{
		Tree.MAXDEPTH = 4;
		Tree.MAXVALUE = Integer.MAX_VALUE - ( Tree.MAXDEPTH * 5000 );
		Tree.MINVALUE = Integer.MIN_VALUE + ( Tree.MAXDEPTH * 5000 );
		Tree.heurisitic = 'b';
	}


	/**
	 * Constructor for the Tree 
	 * 
	 * @param board ( the current Konane )
	 * @param me ( the char representing my color )
	 */
	
	public Tree( Konane board , char me )
	{
		Tree.PLAYER = me;
		Tree.OPPONENT = ( me == 'w' ) ? 'b' : 'w';
		this.head = new Node( board );
	}


	/**
	 * After the tree has completed evaluating itself
	 * This is use to get the move that was pushed to the top
	 * 
	 * @return KonaneMove ( the best move )
	 */
	
	public KonaneMove getNextMove()
	{
		return this.head.getBestChild().getLastMove();
	}
}







/**
 * MiniMax DFS Implementation to play Konane
 * 
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */


class Node
{
	private boolean max;

	private Konane currentBoard;
	private int currentDepth = 0;

	private ArrayList< Node > nextNodes = null;
	private ArrayList< KonaneMove > nextMoves = null;

	private KonaneMove moveThatCreatedState = null;
	private Node parentNode = null;
	private Node bestChildNode = null;

	private int heuristicValue = 0;
	

	/**
	 * Default constructor for the Head	 * 
	 * This is the current state ( not considered a move )
	 * Begins the recursive generation of the Tree
	 * 
	 * @param board ( Konane the current state )
	 */
	
	public Node( Konane board )
	{
		this.max = false;
		this.nextNodes = new ArrayList< Node >();
		this.currentBoard = board;
		this.currentDepth = 0;
		this.generateNextNodes( Tree.PLAYER , this.max );
	}


	/**
	 * Overloaded constructor
	 * Recursively generates next moves until MAXDEPTH is reached
	 * The base case is, if this node's heuristic value is better than its parents
	 * Then push this child Node to its parent's Node
	 * 
	 * @param board ( Konane, that was generate for the next possible move )
	 * @param depth ( The depth of this Node )
	 * @param isMax ( Indicates whether its a min or max node )
	 * @param move ( The KonaneMove that generated this state )
	 * @param parent ( The parent of this state )
	 * @param whichPlayer ( The player at this depth )
	 */
	
	public Node( Konane board , int depth , boolean isMax , KonaneMove move , Node parent , char whichPlayer )
	{
		this.parentNode = parent;
		this.moveThatCreatedState = move;
		this.max = isMax;
		this.currentDepth = depth;
		this.nextNodes = new ArrayList< Node >();
		this.currentBoard = board;
		this.generateNextNodes( whichPlayer , this.max );

		if( parent.getBestChild() == null )
		{
			parent.setHeurisiticValue( this.heuristicValue , this );
		}
		else
		{
			if( this.max )
			{
				if( this.heuristicValue > parent.getHeurisiticValue() )
				{
					parent.setHeurisiticValue( this.heuristicValue , this );
				}
			}
			else
			{
				if( this.heuristicValue < parent.getHeurisiticValue() )
				{
					parent.setHeurisiticValue( this.heuristicValue , this );
				}
			}
		}
		this.nextMoves.clear();
		this.nextNodes.clear();
	}


	/**
	 * Gets the list of possible moves
	 * And generates the next Node states for each move
	 * 
	 * @param whichPlayer ( The player at this depth )
	 * @param isMax ( is max or min )
	 */
	
	private void generateNextNodes( char whichPlayer , boolean isMax )
	{
		Konane newBoard;
		Node tempNode;
		KonaneMove possibleMove;

		this.nextMoves = this.currentBoard.generateMoves( whichPlayer );

		this.calculateHeuristicValue();

		if( this.currentDepth == Tree.MAXDEPTH )
		{
			return;
		}
		else
		{
			for ( int counter = 0 ; counter < this.nextMoves.size() ; counter++ )
			{
				newBoard = new Konane( this.currentBoard );
				possibleMove = new KonaneMove( this.nextMoves.get( counter ) );

				try
				{
					newBoard.makeMove( whichPlayer , possibleMove );
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}

				tempNode = new Node( newBoard , this.currentDepth + 1 , !isMax , possibleMove , this , ( whichPlayer == Tree.PLAYER ) ? Tree.OPPONENT : Tree.PLAYER );
				this.nextNodes.add( tempNode );
			}
		}
	}


	/**
	 * Calculates the heuristic value of this Node
	 */
	
	private void calculateHeuristicValue()
	{
		int mymoves = 1;
		int opmoves = 1;
		int heuristic = this.currentDepth;	
		int mypieces = this.currentBoard.countSymbol( Tree.PLAYER );
		int oppieces = this.currentBoard.countSymbol( Tree.OPPONENT );
		
		if( this.currentDepth > 0 )
		{		
			mymoves = ( this.max ) ? this.parentNode.getParentMoveCount() + 1 : this.nextMoves.size() + 1;
			opmoves = ( this.max ) ? this.nextMoves.size() + 1 : this.parentNode.getParentMoveCount() + 1;
		}

		if( this.currentDepth != Tree.MAXDEPTH && this.nextMoves.size() == 0 )
		{
			heuristic += ( this.max == true ) ? Tree.MAXVALUE : Tree.MINVALUE;
		}
		
		if( Tree.heurisitic == 'a' )
		{
			heuristic += ( int ) Math.round( mymoves - ( opmoves * 3 ) );
		}
		else if( Tree.heurisitic == 'b' )
		{
			heuristic += mymoves - opmoves;
		}
		else if( Tree.heurisitic == 'c' )
		{
			heuristic += ( int ) Math.round( mymoves / ( opmoves * 3 ) );
		}
		else if( Tree.heurisitic == 'd' )
		{
			heuristic += ( int ) Math.round( mymoves / opmoves );
		}
		else if( Tree.heurisitic == 'e' )
		{
			heuristic += ( int ) Math.round( mypieces / ( oppieces * 3 ) );
		}
		else if( Tree.heurisitic == 'f' )
		{
			heuristic += ( int ) Math.round( mypieces / oppieces );
		}
		else
		{
			heuristic += this.nextMoves.size();
		}

		

		this.heuristicValue = heuristic;
	}

	
	/**
	 * Gets a count of the moves for this Node
	 * @return ( number of possible moves )
	 */
	
	public int getParentMoveCount()
	{
		return this.nextMoves.size();
	}
	

	/**
	 * Gets the heuristic of this Node
	 * 
	 * @return ( Heuristic Value )
	 */
	
	public int getHeurisiticValue()
	{
		return this.heuristicValue;
	}


	/**
	 * Gets the move that created this state
	 * 
	 * @return ( The move )
	 */
	
	public KonaneMove getLastMove()
	{
		return this.moveThatCreatedState;
	}


	/**
	 * Sets the parent of a Node's, Heuristic value and besctChild 
	 * 
	 * @param betterHeuristicValue ( the heuristic of the child )
	 * @param betterChildNode ( the child )
	 */
	
	public void setHeurisiticValue( int betterHeuristicValue , Node betterChildNode )
	{
		this.heuristicValue = betterHeuristicValue;
		this.bestChildNode = betterChildNode;
	}


	/**
	 * Gets the parent of a Node
	 * 
	 * @return ( the parent Node )
	 */
	
	public Node getParent()
	{
		return this.parentNode;
	}


	/**
	 * Gets the best child from a Node
	 * Typically used with the Head
	 * 
	 * @return ( the best child )
	 */
	
	public Node getBestChild()
	{
		return this.bestChildNode;
	}

}
