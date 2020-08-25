package uk.gov.ons.fsdr.adeccomock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.common.dto.devicelist.DeviceDto;

@Service
public class DeviceService {
  @Autowired
  private PubSubTemplate pubSubTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  public void publishMessage(String onsId, String phoneNumber, String imeiNumber) throws JsonProcessingException {
    DeviceDto deviceDto = buildDevice(onsId, phoneNumber, imeiNumber);
    String message = objectMapper.writeValueAsString(deviceDto);
    pubSubTemplate.publish("device-info", message);

  }

  private DeviceDto buildDevice(String onsId, String phoneNumber, String imeiNumber) {
    DeviceDto deviceDto = new DeviceDto();
    deviceDto.setOnsId(onsId);
    deviceDto.setPhoneNumber(phoneNumber);

    return deviceDto;
  }

}
