package commandlineparser;

public class ModeErrorException extends Exception {
    public ModeErrorException(String s) {
        super(s);
    }

    public static ModeErrorException requireValueException(String s) {
        return new ModeErrorException("Given " + s + "without given value.");
    }
}
