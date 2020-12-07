package uk.gov.ons.fsdr.adeccomock.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.FfaEventManager;
import uk.gov.ons.fsdr.adeccomock.AdeccoMock;

@Configuration
public class GatewayEventsConfig {

  public static final String FSDR_COMPLETE = "FSDR_COMPLETE";

  @Bean
  public FfaEventManager ffaEventManager() {
    FfaEventManager ffaEventManager = new FfaEventManager();
    ffaEventManager.setSource(AdeccoMock.APPLICATION_NAME);
    return ffaEventManager;
  }
}
