import commandlineparser.CommandlineParser;
import commandlineparser.ModeErrorException;
import commandlineparser.Params;

/**
 * The type Client multi, which is the main function
 */
public class  ClientMulti {

    /**
     * Show helper message.
     *
     * @param args the args
     */
    public static void showHelperMessage(String [] args) {
        System.out.println("Usage: "+args[0]+" [-options]");
        System.out.println("where options include:");
        System.out.println("    --maxThreads <maxThread>       the value of maxThread");
        System.out.println("    --numberSkiers <numberSkiers>  the value of numberSkiers");
        System.out.println("    --skiLifts <skiLifts>          the value of skiLifts");
        System.out.println("    --meanLifts <meanLifts>        the value of meanLifts");
        System.out.println("    --address <server_address>     the address of server");
        System.out.println("    --output <filename>            the filename of the csv file");
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {
        CommandlineParser commandlineParser = new CommandlineParser();
        Params params = Params.getInstance();
        try {
            params = commandlineParser.parseParam(args);
        } catch (ModeErrorException e) {
            showHelperMessage(args);
            return;
        }
        ThreadDispatcher threadDispatcher = new ThreadDispatcher(params);
        threadDispatcher.run();
    }
}