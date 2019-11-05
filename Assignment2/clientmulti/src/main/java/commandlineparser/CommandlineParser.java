package commandlineparser;
/**
 * The type Commandline parser.
 */
public class CommandlineParser {

  /**
   * Instantiates a new Commandline parser.
   */
  public CommandlineParser() {
  }

  /**
   * Parse param params.
   *
   * @param args the args
   * @return the params
   * @throws ModeErrorException the mode error exception
   */
  public Params parseParam(String[] args) throws ModeErrorException {
    int index = 0;
    Params params = Params.getInstance();
    while (true) {
      if (index >= args.length) {
        break;
      }
      String cur = args[index];
      switch (cur) {
        case "--meanLifts":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setMeanLifts(Integer.valueOf(args[index]));
          break;
        case "--maxThread":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setMaxThreads(Integer.valueOf(args[index]));
          break;
        case "--numberSkiers":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setNumberSkiers(Integer.valueOf(args[index]));
          break;
        case "--skiLifts":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setSkiLifts(Integer.valueOf(args[index]));
          break;
        case "--address":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setAddress(args[index]);
          break;
        case "--output":
          if (index + 1 >= args.length) {
            throw ModeErrorException.requireValueException(args[index]);
          }
          index++;
          params.setFileName(args[index]);
          break;
      }
      index++;
    }
    return params;
  }
}
