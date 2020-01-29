package uk.gov.ons.fsdr.adeccomock.configuration;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class LatencyBean {
  private Integer defaultLatency = 0;

  private Map<String, Integer> latencyMap = new HashMap<String, Integer>();

  public Integer getLatency(String service) {
    Integer latency = latencyMap.get(service);
    if (latency==null) latency = defaultLatency;
    return latency;
  }
  
  public void setLatency(String service, Integer ms) {
    latencyMap.put(service, ms);
  }
}

