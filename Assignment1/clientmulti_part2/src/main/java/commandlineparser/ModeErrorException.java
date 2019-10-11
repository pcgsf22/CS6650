package commandlineparser;

/**
 * The type Mode error exception.
 */
public class ModeErrorException extends Exception {

  /**
   * Instantiates a new Mode error exception.
   *
   * @param s the s
   */
  public ModeErrorException(String s) {
    super(s);
  }

  /**
   * Require value exception mode error exception.
   *
   * @param s the s
   * @return the mode error exception
   */
  public static ModeErrorException requireValueException(String s) {
    return new ModeErrorException("Given " + s + "without given value.");
  }
}
