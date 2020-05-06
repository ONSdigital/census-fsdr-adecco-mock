package uk.gov.ons.fsdr.adeccomock.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fsdr.adeccomock.service.DeviceService;

@RestController
@RequestMapping("/device")
public class DeviceController {

  @Autowired
  private DeviceService deviceService;

  @PostMapping("/createPubSub")
  public ResponseEntity<?> createDevice(@RequestParam(name="onsId") String onsId, @RequestParam(name="phoneNumber") String phoneNumber)
      throws JsonProcessingException {
    deviceService.publishMessage(onsId, phoneNumber);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
