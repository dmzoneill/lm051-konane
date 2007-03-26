import java.util.Stack;
import ie.ul.konane.Konane;
import ie.ul.konane.KonaneMove;
import ie.ul.konane.Player;


public class C0813001 extends Player
{


	public C0813001()
	{

		// TODO Auto-generated constructor stub
	}


	public C0813001( String pName )
	{

		super( pName );
		// TODO Auto-generated constructor stub
	}


	@Override
	public void initialize( char pColour )
	{

		// TODO Auto-generated method stub

	}


	public void traverseTreeInOrder( KonaneState node , boolean MAX )
	{

		// incoming node is root
		Stack< KonaneState > nodes = new Stack();
		while ( !nodes.isEmpty() || null != node )
		{
			if( null != node )
			{				
				nodes.push( node );
				ArrayList< KonaneState > nextNodes = node.getNextStates();
				node = node.left;
			}
			else
			{
				node = nodes.pop();
				System.out.println( "Node data: " + node.value );
				node = node.right;
			}
		}
	}


	@Override
	public KonaneMove makeMove( Konane game )
	{
		KonaneState head = new KonaneState( game );
		// TODO Auto-generated method stub
		return null;
	}

}
