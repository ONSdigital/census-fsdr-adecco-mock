package uk.gov.ons.fsdr.adeccomock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoUpdateMock;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseList;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/adecco")
public class AdeccoController {

  @Autowired
  private AdeccoMockService adeccoService;

  @Autowired
  private AdeccoUpdateMock adeccoUpdateMock;

  @GetMapping("/{sqlStatement}")
  public ResponseEntity<?> getFirstRecordSet(@PathVariable String sqlStatement) {
    AdeccoResponseList responses =  adeccoService.getInitialAdeccoResponses(sqlStatement);
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @GetMapping("/nextRecord")
  public ResponseEntity<?> getNextRecordSet() throws IOException {
    AdeccoResponseList responses =  adeccoService.getRemainingAdeccoResponses();
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @PatchMapping("/services/data/v40.0/sobjects/Contact/{uniqueEmployeeId}")
  public ResponseEntity<?> updateEmployee(@PathVariable String uniqueEmployeeId,  @RequestBody String body) throws IOException {
    adeccoUpdateMock.addUpdateMessage(uniqueEmployeeId, body);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/getMockUpdateMessages")
  public ResponseEntity<?> getUpdates() {
    List<String> messages = adeccoUpdateMock.getMessages();
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  @GetMapping("/getMockUpdateMessages/{id}")
  public ResponseEntity<?> getUpdatesById(@PathVariable String id) {
    List<String> messages = adeccoUpdateMock.getMessagesById(id);
    return new ResponseEntity<>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    adeccoUpdateMock.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

 }
