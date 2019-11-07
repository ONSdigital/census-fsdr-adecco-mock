package uk.gov.ons.fsdr.adeccomock.controller.xma;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/xma")
public class MockXMA {
  private final Map<String, List<String>> xmaMessages = new ConcurrentHashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(consumes = "application/json")
  public ResponseEntity<String> postEmployee(@RequestParam(name="class_name") String className, @RequestParam(name="v") String version,  @RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode formValuesNode = rootNode.path("formValues");
      Iterator<JsonNode> elements = formValuesNode.elements();
      String id = null;

      while(elements.hasNext()){
        JsonNode node = elements.next();
        String name = node.path("name").asText();
        if ("Name".equals(name)) {
          id = node.path("value").asText();
        }
      }

      addMessage(id, body);
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
    Collection<List<String>> messages = xmaMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<List<String>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("email") String email) {
    List<String> messages = xmaMessages.get(email);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    xmaMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String email, String body) {
    List<String> messages = xmaMessages.get(email);
    if (messages == null);
    messages = new ArrayList<String>();
    messages.add(new String(body));
    xmaMessages.put(email, messages);
  }
}
