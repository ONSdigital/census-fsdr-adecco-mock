package uk.gov.ons.fsdr.adeccomock.controller.servicenow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicenow")
public class MockServiceNow {
  private final Map<String, List<String>> snowMessages = Collections.synchronizedMap(new LinkedHashMap<>());

  @PostMapping(path = "/", consumes = "application/json")
  public ResponseEntity<ServiceNowResponse> postMember(@RequestBody String body) {
    try {
      String sysId = UUID.randomUUID().toString().replace("-","").substring(0,30);
      ServiceNowResponse serviceNowResponse = new ServiceNowResponse();
      serviceNowResponse.setSysId(sysId);
      addMessage(sysId, body);
      return new ResponseEntity<ServiceNowResponse>(serviceNowResponse, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(path = "/{sysid}", consumes = "application/json")
  public ResponseEntity<String> postUser(@PathVariable("sysid") String sysid, @RequestBody String body) {
    try {
      addMessage(sysid, body);
      return new ResponseEntity<String>(body, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/messages")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<>();
    Collection<List<String>> messages = snowMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{sysid}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("sysid") String sysid) {
    List<String> messages = snowMessages.get(sysid);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    snowMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String sysid, String body) {
    List<String> messages = snowMessages.get(sysid);
    if(messages == null) messages = new ArrayList<String>();
    messages.add(body);
    snowMessages.put(sysid, messages);
  }
}
