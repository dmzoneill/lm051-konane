import ie.ul.konane.Konane;
import ie.ul.konane.KonaneMove;
import ie.ul.konane.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * MiniMax DFS Implementation to play Konane.
 *
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */
public class C0813001 extends Player {
  /** Heuristic used by this player's search tree. */
  private static final char HEURISTIC = 'b';

  /** The board currently being played. */
  private Konane currentState;

  /**
   * Constructor.
   *
   * @param pName ( The name Associated with the user )
   */
  public C0813001(final String pName) {
    super(pName);
  }

  /**
   * Name this player reports once initialised.
   *
   * @return the player name
   */
  protected String playerName() {
    return "0813001";
  }

  /**
   * Heuristic letter ('a' to 'f', anything else counts moves) for the search.
   *
   * @return the heuristic letter
   */
  protected char heuristic() {
    return HEURISTIC;
  }

  /**
   * {@inheritDoc}
   *
   * @see ie.ul.konane.Player#initialize(char)
   */
  @Override
  public void initialize(final char pColour) {
    this.name = playerName();
    this.colour = pColour;
  }

  /**
   * Instantiates the tree and gets the best move.
   *
   * @return KonaneMove ( the next best move )
   */
  private KonaneMove decideMyMove() {
    KonaneMove move = null;

    if (this.currentState.generateMoves(this.colour).size() == 0) {
      KonaneMove gameOver = new KonaneMove(0, 0);
      gameOver.lostGame();
      return gameOver;
    }

    try {
      Tree tree = new Tree(this.currentState, this.colour, heuristic());
      move = tree.getNextMove();
    } catch (Exception e) {
      move = this.currentState.generateMoves(this.colour).get(0);
      System.out.println(e.getMessage());
      e.printStackTrace();
    }

    return move;
  }

  /**
   * {@inheritDoc}
   *
   * @see ie.ul.konane.Player#makeMove(ie.ul.konane.Konane)
   */
  @Override
  public KonaneMove makeMove(final Konane game) {
    this.currentState = game;
    KonaneMove m = this.decideMyMove();
    return m;
  }
}

/**
 * MiniMax DFS search tree used by the Konane players.
 *
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */
class Tree {
  /** Maximum search depth. */
  static final int MAX_DEPTH = 4;

  /** Per-depth margin kept free below the int limits. */
  private static final int DEPTH_MARGIN = 5000;

  /** Score for a state where the opponent cannot move. */
  static final int MAX_VALUE = Integer.MAX_VALUE - (MAX_DEPTH * DEPTH_MARGIN);

  /** Score for a state where this player cannot move. */
  static final int MIN_VALUE = Integer.MIN_VALUE + (MAX_DEPTH * DEPTH_MARGIN);

  /** The colour this tree plays for. */
  private final char player;

  /** The opposing colour. */
  private final char opponent;

  /** Heuristic letter used to score states. */
  private final char heuristic;

  /** Root of the tree (the current state). */
  private final Node head;

  /**
   * Constructor for the Tree.
   *
   * @param board ( the current Konane )
   * @param me ( the char representing my color )
   * @param heuristicType ( the heuristic letter to score states with )
   */
  Tree(final Konane board, final char me, final char heuristicType) {
    this.player = me;
    if (me == 'w') {
      this.opponent = 'b';
    } else {
      this.opponent = 'w';
    }
    this.heuristic = heuristicType;
    this.head = new Node(this, board);
  }

  /**
   * The colour this tree plays for.
   *
   * @return the player colour
   */
  char getPlayer() {
    return this.player;
  }

  /**
   * The opposing colour.
   *
   * @return the opponent colour
   */
  char getOpponent() {
    return this.opponent;
  }

  /**
   * The heuristic letter used to score states.
   *
   * @return the heuristic letter
   */
  char getHeuristic() {
    return this.heuristic;
  }

  /**
   * Gets the move pushed to the top once the tree is evaluated.
   *
   * @return KonaneMove ( the best move )
   */
  KonaneMove getNextMove() {
    return this.head.getBestChild().getLastMove();
  }
}

/**
 * A state in the MiniMax DFS search tree.
 *
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */
class Node {
  /** Weight applied to the opponent in the weighted heuristics. */
  private static final int OPPONENT_WEIGHT = 3;

  /** The tree this node belongs to. */
  private final Tree tree;

  /** Whether this is a max node. */
  private boolean max;

  /** The board for this state. */
  private Konane currentBoard;

  /** Depth of this node. */
  private int currentDepth = 0;

  /** Child nodes. */
  private List<Node> nextNodes = null;

  /** Moves available from this state. */
  private List<KonaneMove> nextMoves = null;

  /** The move that created this state. */
  private KonaneMove moveThatCreatedState = null;

  /** Parent node. */
  private Node parentNode = null;

  /** Best child found so far. */
  private Node bestChildNode = null;

  /** Heuristic value of this node. */
  private int heuristicValue = 0;

  /**
   * Head constructor: the current state, not a move.
   *
   * <p>Begins the recursive generation of the Tree.
   *
   * @param owner ( the tree being built )
   * @param board ( Konane the current state )
   */
  Node(final Tree owner, final Konane board) {
    this.tree = owner;
    this.max = false;
    this.nextNodes = new ArrayList<Node>();
    this.currentBoard = board;
    this.currentDepth = 0;
    this.generateNextNodes(owner.getPlayer(), this.max);
  }

  /**
   * Child constructor, recursing until MAX_DEPTH is reached.
   *
   * <p>A child scoring better than the best so far becomes the best child.
   *
   * @param board ( Konane, that was generate for the next possible move )
   * @param depth ( The depth of this Node )
   * @param isMax ( Indicates whether its a min or max node )
   * @param move ( The KonaneMove that generated this state )
   * @param parent ( The parent of this state )
   * @param whichPlayer ( The player at this depth )
   */
  Node(
      final Konane board,
      final int depth,
      final boolean isMax,
      final KonaneMove move,
      final Node parent,
      final char whichPlayer) {
    this.tree = parent.tree;
    this.parentNode = parent;
    this.moveThatCreatedState = move;
    this.max = isMax;
    this.currentDepth = depth;
    this.nextNodes = new ArrayList<Node>();
    this.currentBoard = board;
    this.generateNextNodes(whichPlayer, this.max);

    int parentValue = parent.getHeurisiticValue();
    if (parent.getBestChild() == null) {
      parent.setHeurisiticValue(this.heuristicValue, this);
    } else if (this.max && this.heuristicValue > parentValue) {
      parent.setHeurisiticValue(this.heuristicValue, this);
    } else if (!this.max && this.heuristicValue < parentValue) {
      parent.setHeurisiticValue(this.heuristicValue, this);
    }
    this.nextMoves.clear();
    this.nextNodes.clear();
  }

  /**
   * Generates the next Node state for each possible move.
   *
   * @param whichPlayer ( The player at this depth )
   * @param isMax ( is max or min )
   */
  private void generateNextNodes(final char whichPlayer, final boolean isMax) {
    this.nextMoves = this.currentBoard.generateMoves(whichPlayer);

    this.calculateHeuristicValue();

    if (this.currentDepth == Tree.MAX_DEPTH) {
      return;
    }

    char next = this.tree.getPlayer();
    if (whichPlayer == this.tree.getPlayer()) {
      next = this.tree.getOpponent();
    }
    for (int counter = 0; counter < this.nextMoves.size(); counter++) {
      Konane board = new Konane(this.currentBoard);
      KonaneMove option = new KonaneMove(this.nextMoves.get(counter));

      try {
        board.makeMove(whichPlayer, option);
      } catch (Exception e) {
        e.printStackTrace();
      }

      int depth = this.currentDepth + 1;
      boolean childMax = !isMax;
      Node child = new Node(board, depth, childMax, option, this, next);
      this.nextNodes.add(child);
    }
  }

  /** Calculates the heuristic value of this Node. */
  private void calculateHeuristicValue() {
    int mymoves = 1;
    int opmoves = 1;
    int heuristic = this.currentDepth;
    int mypieces = this.currentBoard.countSymbol(this.tree.getPlayer());
    int oppieces = this.currentBoard.countSymbol(this.tree.getOpponent());
    int moveCount = this.nextMoves.size();

    if (this.currentDepth > 0) {
      int parentMoves = this.parentNode.getParentMoveCount();
      if (this.max) {
        mymoves = parentMoves + 1;
        opmoves = moveCount + 1;
      } else {
        mymoves = moveCount + 1;
        opmoves = parentMoves + 1;
      }
    }

    if (this.currentDepth != Tree.MAX_DEPTH && moveCount == 0) {
      if (this.max) {
        heuristic += Tree.MAX_VALUE;
      } else {
        heuristic += Tree.MIN_VALUE;
      }
    }

    switch (this.tree.getHeuristic()) {
      case 'a':
        heuristic += (int) Math.round(mymoves - (opmoves * OPPONENT_WEIGHT));
        break;
      case 'b':
        heuristic += mymoves - opmoves;
        break;
      case 'c':
        heuristic += (int) Math.round(mymoves / (opmoves * OPPONENT_WEIGHT));
        break;
      case 'd':
        heuristic += (int) Math.round(mymoves / opmoves);
        break;
      case 'e':
        heuristic += (int) Math.round(mypieces / (oppieces * OPPONENT_WEIGHT));
        break;
      case 'f':
        heuristic += (int) Math.round(mypieces / oppieces);
        break;
      default:
        heuristic += moveCount;
        break;
    }

    this.heuristicValue = heuristic;
  }

  /**
   * Gets a count of the moves for this Node.
   *
   * @return ( number of possible moves )
   */
  int getParentMoveCount() {
    return this.nextMoves.size();
  }

  /**
   * Gets the heuristic of this Node.
   *
   * @return ( Heuristic Value )
   */
  int getHeurisiticValue() {
    return this.heuristicValue;
  }

  /**
   * Gets the move that created this state.
   *
   * @return ( The move )
   */
  KonaneMove getLastMove() {
    return this.moveThatCreatedState;
  }

  /**
   * Sets the parent of a Node's, Heuristic value and besctChild.
   *
   * @param betterValue ( the heuristic of the child )
   * @param betterChild ( the child )
   */
  void setHeurisiticValue(final int betterValue, final Node betterChild) {
    this.heuristicValue = betterValue;
    this.bestChildNode = betterChild;
  }

  /**
   * Gets the parent of a Node.
   *
   * @return ( the parent Node )
   */
  Node getParent() {
    return this.parentNode;
  }

  /**
   * Gets the best child from a Node Typically used with the Head.
   *
   * @return ( the best child )
   */
  Node getBestChild() {
    return this.bestChildNode;
  }
}
