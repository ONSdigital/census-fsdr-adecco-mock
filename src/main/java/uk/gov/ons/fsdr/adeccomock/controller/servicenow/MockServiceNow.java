package uk.gov.ons.fsdr.adeccomock.controller.servicenow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servicenow")
public class MockServiceNow {
  private final Map<String, List<String>> gsuiteMessages = new ConcurrentHashMap<>();

  @PostMapping(path = "/", consumes = "application/json")
  public ResponseEntity<ServiceNowResponse> postMember(@RequestBody String body) {
    try {
      String sysId = UUID.randomUUID().toString();
      ServiceNowResponse serviceNowResponse = new ServiceNowResponse();
      serviceNowResponse.setSysId(sysId);
      addMessage(sysId, body);
      return new ResponseEntity<ServiceNowResponse>(serviceNowResponse, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(path = "/{sysid}", consumes = "application/json")
  public ResponseEntity<String> postUser(@PathParam("sysid") String sysid, @RequestBody String body) {
    try {
      addMessage(sysid, body);
      return new ResponseEntity<String>(body, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/messages/")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<String>();
    Collection<List<String>> messages = gsuiteMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<List<String>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{sysid}")
  public ResponseEntity<List<String>> getUsersMessages(@PathParam("sysid") String sysid) {
    List<String> messages = gsuiteMessages.get(sysid);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    gsuiteMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String sysid, String body) {
    List<String> messages = gsuiteMessages.get(sysid);
    if (messages == null)
      messages = new ArrayList<String>();
    messages.add(new String(body));
  }
}
