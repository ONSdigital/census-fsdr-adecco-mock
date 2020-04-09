package uk.gov.ons.fsdr.adeccomock.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.events.data.GatewayErrorEventDTO;
import uk.gov.ons.census.fwmt.events.data.GatewayEventDTO;

import static uk.gov.ons.fsdr.adeccomock.configuration.GatewayEventsConfig.FSDR_COMPLETE;
import static uk.gov.ons.fsdr.adeccomock.configuration.QueueConfig.EVENTS_QUEUE;
import static uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService.totalCount;

@Service
@RabbitListener(queues = EVENTS_QUEUE)
@Slf4j
public class EventListener {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  int xma = 0;
  int snow = 0;

  @RabbitHandler
  public void recieveMessage(GatewayErrorEventDTO eventDTO) {}

  @RabbitHandler
  public void recieveMessage(GatewayEventDTO eventDTO) {

    switch(eventDTO.getEventType()) {
    case "SENDING_XMA_ACTION_RESPONSE":
      xma++;
      break;
    case "SENDING_SERVICE_NOW_ACTION_RESPONSE":
      snow++;
      break;
    }
    if(xma == totalCount && snow == totalCount) {
      gatewayEventManager.triggerEvent("<N/A>", FSDR_COMPLETE);
      xma = 0;
      snow = 0;
    }
  }

}
