import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The Csv writer, which write csv file to given filepath
 */
public class CSVWriter {

  /**
   * Write file.
   *
   * @param filename the filename
   * @param filepath the filepath
   * @param content the content
   * @throws IOException the io exception
   * @throws ExecutionException the execution exception
   * @throws InterruptedException the interrupted exception
   */
  public void writeFile(String filename, String filepath, List<Future<List<RequestResult>>> content)
      throws IOException, ExecutionException, InterruptedException {
    FileWriter fileWriter = new FileWriter(String.valueOf(Paths.get(filepath, filename)));
    fileWriter.write("\"start time\",\"request type\",\"latency\",\"response code\"\n");
    for (Future<List<RequestResult>> list : content) {
      for (RequestResult requestResult : list.get()) {
        fileWriter.write(requestResult.toString());
      }
    }
    fileWriter.close();
  }
}
