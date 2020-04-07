package uk.gov.ons.fsdr.adeccomock.controller.lws;

import com.fasterxml.jackson.databind.ObjectMapper;
import local.loneworker.InsertUpdatePerson4;
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
  private final Map<String, List<InsertUpdatePerson4>> lwsMessages = Collections.synchronizedMap(new LinkedHashMap());
  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping("/messages/")
  public ResponseEntity<List<InsertUpdatePerson4>> getAllMessages() {
    List<InsertUpdatePerson4> all = new ArrayList<>();
    Collection<List<InsertUpdatePerson4>> messages = lwsMessages.values();
    for (List<InsertUpdatePerson4> list : messages) {
      all.addAll(list);
    }
    System.out.println(all.size());
    return new ResponseEntity<List<InsertUpdatePerson4>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{employeeId}")
  public ResponseEntity<List<InsertUpdatePerson4>> getUsersMessages(@PathVariable("employeeId") String employeeId) {
    List<InsertUpdatePerson4> messages = lwsMessages.get(employeeId);
    return new ResponseEntity<List<InsertUpdatePerson4>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    lwsMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  public void acceptRequest(InsertUpdatePerson4 insertUpdatePerson4) {
    addMessage(insertUpdatePerson4.getExternalSystemPersonCode(),insertUpdatePerson4);
  }

  private void addMessage(String email, InsertUpdatePerson4 extractedMessage) {
    List<InsertUpdatePerson4> messages = lwsMessages.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(extractedMessage);
    lwsMessages.put(email, messages);
  }

}

