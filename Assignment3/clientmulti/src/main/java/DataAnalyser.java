import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * The type Data analyser.
 */
public class DataAnalyser {

  /**
   * The Data.
   */
  private List<RequestResult> data;

  /**
   * Instantiates a new Data analyser.
   *
   * @param input the input
   * @throws ExecutionException the execution exception
   * @throws InterruptedException the interrupted exception
   */
  public DataAnalyser(List<Future<List<RequestResult>>> input)
      throws ExecutionException, InterruptedException {
    data = new ArrayList<>();
    for (Future<List<RequestResult>> list : input) {
      for (RequestResult requestResult : list.get()) {
        data.add(requestResult);
      }
    }
    Collections.sort(data);
  }

  /**
   * Mean response time double.
   *
   * @return the double
   */
  double meanResponseTime() {
    long sum = 0;
    for(RequestResult requestResult:data) {
      sum+=requestResult.getLatency();
    }
    return (double)sum/(double)data.size();
  }

  /**
   * Median response time double.
   *
   * @return the double
   */
  double medianResponseTime(){
    if(data.size()%2==1) {
      return data.get(data.size()/2).getLatency();
    }
    return (data.get(data.size()/2).getLatency() + data.get(data.size()/2-1).getLatency())*0.5;
  }

  /**
   * Get number request long.
   *
   * @return the long
   */
  long getNumberRequest(){
    return data.size();
  }

  /**
   * Gets p 99 response.
   *
   * @return the p 99 response
   */
  long getP99Response() {
    return data.get((int)Math.ceil(data.size()*0.99)).getLatency();
  }

  /**
   * Gets max respones.
   *
   * @return the max respones
   */
  long getMaxRespones() {
    return data.get(data.size()-1).getLatency();
  }

}
