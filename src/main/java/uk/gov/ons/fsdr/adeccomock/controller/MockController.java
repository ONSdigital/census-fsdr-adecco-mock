package uk.gov.ons.fsdr.adeccomock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;

@RestController
@RequestMapping("/mock")
public class MockController {

  @Autowired
  private AdeccoMockService adeccoService;

  @GetMapping("/reset")
  public ResponseEntity<?> resetMock() {
    adeccoService.clearRecords();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/enable")
  public ResponseEntity<?> enableLogger() {
    adeccoService.enableLogger();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/disable")
  public ResponseEntity<?> disableLogger() {
    adeccoService.disableLogger();
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
