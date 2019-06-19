package uk.gov.ons.fsdr.adeccomock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fsdr.adeccomock.entity.AdeccoResponse;
import uk.gov.ons.fsdr.adeccomock.entity.AdeccoResponseList;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/adecco-mock")
public class AdeccoController {

  @Autowired AdeccoMockService adeccoMockService;

  @GetMapping("/records")
  public ResponseEntity<AdeccoResponseList> getContacts() {
    List<String> json = new ArrayList<>();

    return ResponseEntity.ok(adeccoMockService.getAdeccoResponse());
  }

  @PostMapping("/accessToken")
  public ResponseEntity<String> getAccessToken() {
    String token = "token";

    return ResponseEntity.ok(token);
  }

  @PostMapping(path = "/addContacts", consumes = "application/json")
  public ResponseEntity<HttpStatus> postContacts(@RequestBody @Valid List<AdeccoResponse> contacts) {
    if(adeccoMockService.addContacts(contacts)) {
      return ResponseEntity.ok(HttpStatus.OK);
    } else return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping("/clear")
  public ResponseEntity<HttpStatus> clearMock() {
    if(adeccoMockService.clearRecords()) {
      return ResponseEntity.ok(HttpStatus.OK);
    }
    else return ResponseEntity.ok(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
