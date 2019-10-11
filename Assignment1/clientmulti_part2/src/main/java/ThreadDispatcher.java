import commandlineparser.Params;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Thread.MAX_PRIORITY;

/**
 * The type Thread dispatcher, which serves as the boss in boss-worker model.
 */
public class ThreadDispatcher implements Runnable {

  private final Params params;
  private final ActiveCount successCount = new ActiveCount();
  private final ActiveCount failCount = new ActiveCount();

  /**
   * Instantiates a new Thread dispatcher.
   *
   * @param params the params
   */
  public ThreadDispatcher(Params params) {
    this.params = params;
  }

  /**
   * The start of each phase.
   *
   * @param pool the thread pool
   * @param phaseThread the number of thread in current phase
   * @param skierRange the skier range of current phase
   * @param startTime the start time of current phase
   * @param endTime the end time of current phase
   * @param numPost the num post per thread of current phase
   * @param response the response result of current phase
   * @throws InterruptedException the interrupted exception
   */
  private void phaseStart(ExecutorService pool, int phaseThread, int skierRange, int startTime,
      int endTime, int numPost, List<Future<List<RequestResult>>> response)
      throws InterruptedException {
    int currentStart = 1;
    // Use CountDownLatch to block the next phase until 10% of current phase has finished.
    final CountDownLatch phaseLatch = new CountDownLatch((int)Math.ceil(phaseThread / 10f));
    for (int i = 0; i < phaseThread; i++) {
      Callable<List<RequestResult>> newThread = new SkiApiHandlerCallable(phaseLatch, successCount,
          failCount, params.getAddress(), currentStart, currentStart + skierRange - 1, startTime,
          endTime, params.getSkiLifts(), numPost);
      currentStart += skierRange;
      response.add(pool.submit(newThread));
    }
    phaseLatch.await();
  }

  @Override
  public void run() {
    // Get the task start time.
    long taskStartTime = System.currentTimeMillis();
    // Calculate the number of thread of current phase so that we can determine the pool size.
    int phaseOneThreads = params.getMaxThreads() / 4;
    int phaseTwoThreads = params.getMaxThreads();
    int phaseThreeThreads = params.getMaxThreads() / 4;
    // Init the thread pool
    ExecutorService pool = Executors
        .newFixedThreadPool(phaseOneThreads + phaseTwoThreads + phaseThreeThreads);
    // Get phase attributes for phase one.
    int skierRange = params.getNumberSkiers() / phaseOneThreads;
    int startTime = 1;
    int endTime = 90;
    int numPost = params.getMeanLifts() / 10 * params.getNumberSkiers() / phaseOneThreads;

    // Init the response result list
    List<Future<List<RequestResult>>> response = new ArrayList<>();

    // Start phase one
    try {
      phaseStart(pool, phaseOneThreads, skierRange, startTime, endTime, numPost, response);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Get phase attributes for phase two.
    startTime = 91;
    endTime = 360;
    skierRange = params.getNumberSkiers() / phaseTwoThreads;
    numPost = params.getMeanLifts() * 4 / 5 * params.getNumberSkiers() / phaseTwoThreads;

    // Start phase two.
    try {
      phaseStart(pool, phaseTwoThreads, skierRange, startTime, endTime, numPost, response);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Get phase attributes for phase three.
    startTime = 361;
    endTime = 420;
    numPost = params.getMeanLifts() / 10;
    skierRange = params.getNumberSkiers() / phaseThreeThreads;

    // Start phase three.
    try {
      phaseStart(pool, phaseThreeThreads, skierRange, startTime, endTime, numPost, response);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Wait for the finishing of all phases.
    pool.shutdown();
    try {
      pool.awaitTermination(MAX_PRIORITY, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Calculate the wall time.
    long duration = System.currentTimeMillis() - taskStartTime;

    System.out.println("Finish " + successCount.getCount());
    System.out.println("Fails " + failCount.getCount());
    System.out.println("WallTime " + duration + "ms");

    try {
      DataAnalyser dataAnalyser = new DataAnalyser(response);
      System.out.println("mean response time (millisecs) " + dataAnalyser.meanResponseTime());
      System.out.println("median response time (millisecs) " + dataAnalyser.medianResponseTime());
      System.out.println("throughput " + (double)dataAnalyser.getNumberRequest()/(double)duration);
      System.out.println("p99 (99th percentile) response time " + dataAnalyser.getP99Response());
      System.out.println("max response time " + dataAnalyser.getMaxRespones());

    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Write the running result to csv file.
    CSVWriter csvWriter = new CSVWriter();
    try {
      csvWriter.writeFile("", params.getFileName(), response);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

}
