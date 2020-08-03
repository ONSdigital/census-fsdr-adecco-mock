package uk.gov.ons.fsdr.adeccomock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gcp.pubsub.core.PubSubTemplate;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.common.dto.devicelist.Account;
import uk.gov.ons.fsdr.common.dto.devicelist.DeviceDto;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    deviceDto.setImeiNumber(imeiNumber);
    Account account = new Account();
    account.setName(onsId);
    account.setType("test");
    deviceDto.setAccounts(List.of(account));
    deviceDto.setPhoneNumber(phoneNumber);

    return deviceDto;
  }

}
