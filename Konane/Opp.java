/**
 * Opponent player for testing.
 *
 * <p>Same MiniMax DFS search as C0813001, with heuristic 'g'.
 *
 * @author David O Neill ( 0813001 )
 * @version 4.0
 */
public class Opp extends C0813001 {
  /**
   * Constructor.
   *
   * @param pName ( The name Associated with the user )
   */
  public Opp(final String pName) {
    super(pName);
  }

  /**
   * {@inheritDoc}
   *
   * @return "Opp"
   */
  @Override
  protected String playerName() {
    return "Opp";
  }

  /**
   * {@inheritDoc}
   *
   * @return 'g', which scores a state by its number of moves
   */
  @Override
  protected char heuristic() {
    return 'g';
  }
}
