package uk.gov.ons.fsdr.adeccomock.configuration;

import lombok.Getter;

@Getter
public class LatencyBean {
  private Integer latency;

  public LatencyBean(Integer latency) {
    this.latency = latency;
  }
}

