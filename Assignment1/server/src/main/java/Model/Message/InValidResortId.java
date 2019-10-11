package Model.Message;

/**
 * The type In valid resort id.
 */
public class InValidResortId extends AbstractMessage {
    private String message;

  /**
   * Instantiates a new In valid resort id.
   */
  public InValidResortId() {
        super("Invalid Model.Resort ID supplied");
    }
}
