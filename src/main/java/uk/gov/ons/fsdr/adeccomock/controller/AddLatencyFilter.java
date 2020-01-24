package uk.gov.ons.fsdr.adeccomock.controller;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fsdr.adeccomock.configuration.LatencyBean;

@Slf4j
public class AddLatencyFilter implements javax.servlet.Filter {

  private LatencyBean latencyBean;

  public AddLatencyFilter(LatencyBean latencyBean) {
    this.latencyBean = latencyBean;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    try {
      log.debug("Latency: {}ms", latencyBean.getLatency());
      Thread.sleep(latencyBean.getLatency());
    } catch (InterruptedException e) {
      log.error("Could not add latency", e);
    }finally {
      chain.doFilter(request, response);
    }
  }
}

