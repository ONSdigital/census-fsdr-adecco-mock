package uk.gov.ons.fsdr.adeccomock.controller.xma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/xma")
public class MockXMA {
  private final Map<String, List<String>> gsuiteMessages = new ConcurrentHashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(path = "/", consumes = "application/json")
  public ResponseEntity<String> postEmployee(@RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode emailNode = rootNode.path("xxx");
      String email = emailNode.asText();

      addMessage(email, body);
      return new ResponseEntity<String>(body, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(path = "/", consumes = "application/json")
  public ResponseEntity<String> updateEmployee(@RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode emailNode = rootNode.path("primaryEmail");
      String email = emailNode.asText();

      addMessage(email, body);
      return new ResponseEntity<String>(body, HttpStatus.OK);
    } catch (IOException e) {
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

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<String>> getUsersMessages(@PathParam("email") String email) {
    List<String> messages = gsuiteMessages.get(email);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    gsuiteMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String email, String body) {
    List<String> messages = gsuiteMessages.get(email);
    if (messages == null)
      messages = new ArrayList<String>();
    messages.add(new String(body));
  }
}
