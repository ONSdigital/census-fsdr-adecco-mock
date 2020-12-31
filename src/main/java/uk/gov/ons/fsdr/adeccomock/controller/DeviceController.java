package uk.gov.ons.fsdr.adeccomock.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fsdr.adeccomock.service.DeviceService;
import uk.gov.ons.fsdr.common.dto.devicelist.DeviceDto;

@RestController
@RequestMapping("/device")
public class DeviceController {

  @Autowired
  private DeviceService deviceService;

  @PostMapping("/createPubSub")
  public ResponseEntity<?> createDevice(@RequestParam(name = "onsId") String onsId,
      @RequestParam(name = "phoneNumber") String phoneNumber, @RequestParam(name = "imeiNumber") String imeiNumber)
      throws JsonProcessingException {
    deviceService.publishMessage(onsId, "+" +phoneNumber.trim(), imeiNumber);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/createLotsOfPubSub")
  public ResponseEntity<?> createDevice(@RequestBody List<DeviceDto> devices)
      throws JsonProcessingException {
    for (DeviceDto deviceDto : devices) {
      System.out.println(deviceDto.getOnsId() + deviceDto.getPhoneNumber().trim());
      deviceService.publishMessage(deviceDto.getOnsId(), deviceDto.getPhoneNumber().trim(), null);      
    }
    return new ResponseEntity<>(HttpStatus.OK);
  }


}
