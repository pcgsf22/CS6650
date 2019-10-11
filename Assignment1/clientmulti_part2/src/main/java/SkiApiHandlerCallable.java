
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import org.apache.log4j.Logger;

/**
 * The SKierApiHandler, which is the worker in the boss-worker model.
 */
public class SkiApiHandlerCallable implements Callable<List<RequestResult>> {

  private final Logger logger = Logger.getLogger(SkiApiHandlerCallable.class);

  private final CountDownLatch finishCount;
  private final ActiveCount successCount;
  private final ActiveCount failCount;

  private SkiersApi apiInstance;
  private ApiClient client;
  private int startIDRange;
  private int endIDRange;
  private int startTime;
  private int endTime;
  private int liftRange;
  private int numPost;
  private final Random random = new Random();

  /**
   * Instantiates a new Ski api handler callable.
   *
   * @param finishCount the finish count
   * @param successCount the success count
   * @param failCount the fail count
   * @param urlBase the url base
   * @param startIDRange the start id range
   * @param endIDRange the end id range
   * @param startTime the start time
   * @param endTime the end time
   * @param liftRange the lift range
   * @param numPost the num post
   */
  public SkiApiHandlerCallable(CountDownLatch finishCount, ActiveCount successCount,
      ActiveCount failCount, String urlBase, int startIDRange, int endIDRange, int startTime,
      int endTime, int liftRange, int numPost) {
    this.finishCount = finishCount;
    this.successCount = successCount;
    this.failCount = failCount;
    this.startIDRange = startIDRange;
    this.endIDRange = endIDRange;
    this.startTime = startTime;
    this.endTime = endTime;
    this.liftRange = liftRange;
    this.numPost = numPost;
    this.apiInstance = new SkiersApi();
    this.client = apiInstance.getApiClient();
    client.setBasePath(urlBase);
  }


  @Override
  public List<RequestResult> call() {
    // The result of current phase.
    List<RequestResult> result = new ArrayList<>();
    // The local counter of current thread, this is to reduce number of times of synchronization
    int localSuccessCount = 0;
    int localFailCount = 0;

    for (int i = 0; i < numPost; i++) {
      int id = random.ints(startIDRange, endIDRange + 1).findAny().getAsInt();
      int time = random.ints(startTime, endTime + 1).findAny().getAsInt();
      int lift = random.ints(1, liftRange + 1).findAny().getAsInt();
      LiftRide liftRide = new LiftRide();
      liftRide.setLiftID(lift);
      liftRide.setTime(time);
      long requestTime = System.currentTimeMillis();
      try {
        ApiResponse<Void> response = apiInstance
            .writeNewLiftRideWithHttpInfo(liftRide, 0, "2019", "29", id);
        result.add(new RequestResult(requestTime, System.currentTimeMillis() - requestTime,
            response.getStatusCode()));
        localSuccessCount++;
      } catch (ApiException e) {
        localFailCount++;
        logger.trace(e);
        e.printStackTrace();
        result.add(
            new RequestResult(requestTime, System.currentTimeMillis() - requestTime, e.getCode()));
      }
    }
    // Synchronize local result to global result.
    successCount.incrementBy(localSuccessCount);
    failCount.incrementBy(localFailCount);
    finishCount.countDown();
    return result;
  }

}
