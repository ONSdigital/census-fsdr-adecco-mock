package uk.gov.ons.fsdr.adeccomock.controller.lws;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/lwsUtil")
public class MockLwsMessages {
  private final Map<String, List<String>> gsuiteMessages = Collections.synchronizedMap(new LinkedHashMap());
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, String> emailAddresses = new ConcurrentHashMap<>();

//  @PostMapping(path = "/InsertUpdatePerson4", consumes = "application/xml")
//  public ResponseEntity<HttpResponse> postMember(@RequestBody String body) {
//    try {
//      System.out.println("BOOP" +body);
//      addMessage("sysId", body);
//      return new ResponseEntity<>(HttpStatus.CREATED);
//    } catch (Exception e) {
//      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//  }

  @GetMapping("/messages/")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<>();
    Collection<List<String>> messages = gsuiteMessages.values();
    for (List<String> list : messages) {
      all.addAll(list);
    }
    return new ResponseEntity<List<String>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{employeeId}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("employeeId") String employeeId) {
    List<String> messages = gsuiteMessages.get(emailAddresses.get(employeeId));
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    gsuiteMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }


  private void addMessage(String email, String extractedMessage) {
    List<String> messages = gsuiteMessages.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(extractedMessage);
    gsuiteMessages.put(email, messages);
  }

}

