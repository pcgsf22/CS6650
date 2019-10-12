package commandlineparser;

public class Params {
  Integer maxThreads;

  Integer numberSkiers;

  Integer skiLifts;

  Integer meanLifts;

  String address;

  public Params() {}

  static public Params getInstance(){
    Params params = new Params();
    params.maxThreads = 512;
    params.numberSkiers = 20000;
    params.skiLifts = 40;
    params.meanLifts = 20;
    params.address = "http://35.162.224.73:8080/hw1/";
    return params;
  }

  public Integer getMaxThreads() {
    return maxThreads;
  }

  public void setMaxThreads(Integer maxThreads) {
    this.maxThreads = maxThreads;
  }

  public Integer getNumberSkiers() {
    return numberSkiers;
  }

  public void setNumberSkiers(Integer numberSkiers) {
    this.numberSkiers = numberSkiers;
  }

  public Integer getSkiLifts() {
    return skiLifts;
  }

  public void setSkiLifts(Integer skiLifts) {
    this.skiLifts = skiLifts;
  }

  public Integer getMeanLifts() {
    return meanLifts;
  }

  public void setMeanLifts(Integer meanLifts) {
    this.meanLifts = meanLifts;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
