package uk.gov.ons.fsdr.adeccomock.controller;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

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
      String pathInfo = ((HttpServletRequest)request).getRequestURI();
      String[] split = pathInfo.split("/");
      if (split.length>1) {
        log.info(split[1]);
        Integer latency = latencyBean.getLatency(split[1]);
        log.debug("Latency: {}ms", latency);
        Thread.sleep(latencyBean.getDefaultLatency());
      }
    } catch (InterruptedException e) {
      log.error("Could not add latency", e);
    }finally {
      chain.doFilter(request, response);
    }
  }
}

