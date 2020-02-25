package uk.gov.ons.fsdr.adeccomock.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.gov.ons.fsdr.adeccomock.configuration.LatencyBean;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;

@RestController
@RequestMapping("/mock")
public class MockController {

  @Autowired
  private AdeccoMockService adeccoService;
  
  @Autowired
  private LatencyBean latencyBean;

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
  
  @PostMapping(path = "/postResponse", consumes = "application/json")
  public ResponseEntity<?> putAddecoResponse(@RequestBody List<AdeccoResponse> adeccoResponses) {
    adeccoService.putRecords(adeccoResponses);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(value = "/postManyResponse/{amount}")
  public void postManyAddecoResponse(@PathVariable Integer amount) throws IOException {
    adeccoService.createManyRecords(amount);
  }

  @PutMapping(value = "latency/default/{ms}")
  public void setDefaultLatency(@PathVariable Integer ms) {
    latencyBean.setDefaultLatency(ms);
  }

  @PutMapping(value = "latency/snow/{ms}")
  public void setSnowLatency(@PathVariable Integer ms) {
    latencyBean.setLatency("servicenow", ms);
  }

  @PutMapping(value = "latency/adecco/{ms}")
  public void setAdeccoLatency(@PathVariable Integer ms) {
    latencyBean.setLatency("adecco", ms);
  }

  @PutMapping(value = "latency/xma/{ms}")
  public void setXmaLatency(@PathVariable Integer ms) {
    latencyBean.setLatency("xma", ms);
  }
  
  @PutMapping(value = "latency/gsuite/{ms}")
  public void setGsuiteLatency(@PathVariable Integer ms) {
    latencyBean.setLatency("gsuite", ms);
  }
  
}
