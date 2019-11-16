package commandlineparser;

/**
 * The type Params, stores all needed parameters.
 */
public class Params {

  /**
   * The Max threads.
   */
  Integer maxThreads;

  /**
   * The Number skiers.
   */
  Integer numberSkiers;

  /**
   * The Ski lifts.
   */
  Integer skiLifts;

  /**
   * The Mean lifts.
   */
  Integer meanLifts;

  /**
   * The Address.
   */
  String address;

  /**
   * The File name.
   */
  String fileName;

  /**
   * Instantiates a new Params.
   */
  public Params() {
  }

  /**
   * Get instance params.
   *
   * @return the params
   */
  static public Params getInstance() {
    Params params = new Params();
    params.maxThreads = 256;
    params.numberSkiers = 20000;
    params.skiLifts = 40;
    params.meanLifts = 20;
    params.address = "http://34.211.252.3:8080/a2/";
    params.fileName = "1024.csv";
    return params;
  }

  /**
   * Gets file name.
   *
   * @return the file name
   */
  public String getFileName() {
    return fileName;
  }

  /**
   * Sets file name.
   *
   * @param fileName the file name
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * Gets max threads.
   *
   * @return the max threads
   */
  public Integer getMaxThreads() {
    return maxThreads;
  }

  /**
   * Sets max threads.
   *
   * @param maxThreads the max threads
   */
  public void setMaxThreads(Integer maxThreads) {
    this.maxThreads = maxThreads;
  }

  /**
   * Gets number skiers.
   *
   * @return the number skiers
   */
  public Integer getNumberSkiers() {
    return numberSkiers;
  }

  /**
   * Sets number skiers.
   *
   * @param numberSkiers the number skiers
   */
  public void setNumberSkiers(Integer numberSkiers) {
    this.numberSkiers = numberSkiers;
  }

  /**
   * Gets ski lifts.
   *
   * @return the ski lifts
   */
  public Integer getSkiLifts() {
    return skiLifts;
  }

  /**
   * Sets ski lifts.
   *
   * @param skiLifts the ski lifts
   */
  public void setSkiLifts(Integer skiLifts) {
    this.skiLifts = skiLifts;
  }

  /**
   * Gets mean lifts.
   *
   * @return the mean lifts
   */
  public Integer getMeanLifts() {
    return meanLifts;
  }

  /**
   * Sets mean lifts.
   *
   * @param meanLifts the mean lifts
   */
  public void setMeanLifts(Integer meanLifts) {
    this.meanLifts = meanLifts;
  }

  /**
   * Gets address.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets address.
   *
   * @param address the address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  @Override
  public String toString() {
    return "Params{" +
        "maxThreads=" + maxThreads +
        ", numberSkiers=" + numberSkiers +
        ", skiLifts=" + skiLifts +
        ", meanLifts=" + meanLifts +
        ", address='" + address + '\'' +
        ", fileName='" + fileName + '\'' +
        '}';
  }
}
