package uk.gov.ons.fsdr.adeccomock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;

import java.util.List;

@Slf4j
@RestController
public class AdeccoController {

  @Autowired
  private AdeccoMockService adeccoService;

  @GetMapping("/{sqlStatement}")
  // Below line causes a build error. Not sure if we need the @RequestParam
//  public ResponseEntity<?> getAllEmployeesFromAdecco(@RequestParam String sql) {
  public ResponseEntity<?> getAllEmployeesFromAdecco(String sql) {

    List<AdeccoResponse> responses =  adeccoService.getAdeccoResponses();
    return new ResponseEntity<>(responses, HttpStatus.OK);
  }

  @PostMapping(path = "/postResponse", consumes = "application/json")
  public ResponseEntity<?> putAddecoResponse(@RequestBody List<AdeccoResponse> adeccoResponses) {
    adeccoService.putRecords(adeccoResponses);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
