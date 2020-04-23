package uk.gov.ons.fsdr.adeccomock.message;

import static uk.gov.ons.fsdr.adeccomock.configuration.GatewayEventsConfig.FSDR_COMPLETE;
import static uk.gov.ons.fsdr.adeccomock.configuration.QueueConfig.EVENTS_QUEUE;
import static uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService.totalCount;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.events.data.GatewayErrorEventDTO;
import uk.gov.ons.census.fwmt.events.data.GatewayEventDTO;

@Service
@RabbitListener(queues = EVENTS_QUEUE)
@Slf4j
public class EventListener {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  private static int xma = 0;
  private static int snow = 0;

  private Object lock = new Object();

  private String lastSnowId = null;

  @RabbitHandler
  public void recieveMessage(GatewayErrorEventDTO eventDTO) {
  }

  @RabbitHandler
  public void recieveMessage(GatewayEventDTO eventDTO) {
    boolean isUpdated = false;
    if (totalCount == 0)
      return;
    synchronized (lock) {
      switch (eventDTO.getEventType()) {
      case "SENDING_XMA_ACTION_RESPONSE":
        xma++;
        isUpdated = true;
        break;
      case "SENDING_SERVICE_NOW_ACTION_RESPONSE":
        if (!eventDTO.getCaseId().equals(lastSnowId)) {
          snow++;
        }else {
          log.error("Duplicate event: {}", eventDTO.toString());
        }
        isUpdated = true;
        break;
      }
      if (isUpdated) {
        log.info("xmacounter: {} snowcounter: {} total: {} ", xma, snow, totalCount);

      }
      if (xma == totalCount && snow == totalCount) {
        gatewayEventManager.triggerEvent("<N/A>", FSDR_COMPLETE);
        xma = 0;
        snow = 0;
      }
    }
  }

}
