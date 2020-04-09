package uk.gov.ons.fsdr.adeccomock.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.fsdr.adeccomock.AdeccoMock;

@Configuration
public class GatewayEventsConfig {

  public static final String FSDR_COMPLETE = "FSDR_COMPLETE";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.setSource(AdeccoMock.APPLICATION_NAME);
    gatewayEventManager.addErrorEventTypes(new String[] {});
    gatewayEventManager.addEventTypes(new String[] {FSDR_COMPLETE});
    return gatewayEventManager;
  }
}
