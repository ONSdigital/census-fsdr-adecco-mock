package uk.gov.ons.fsdr.adeccomock.controller.xma;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/xma")
@Slf4j
public class MockXMA {
  private final Map<String, List<String>> xmaMessages = Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String, String> employeeIds = new ConcurrentHashMap<>();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @RequestMapping(consumes = "application/json")
  public ResponseEntity<XmaResponse> postEmployee(@RequestParam(name="class_name") String className, @RequestParam(name="v") String version,  @RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode formValuesNode = rootNode.path("formValues");
      Iterator<JsonNode> elements = formValuesNode.elements();
      String id = null;
      String roleId = null;
      XmaResponse xmaResponse = new XmaResponse();
      if(body.contains("_DeletionUser")) {
        while(elements.hasNext()){
          JsonNode node = elements.next();
          String name = node.path("name").asText();
          if ("_DeletionUser".equals(name)) {
            id = node.path("value").asText();
          }
        }
      } else if(!body.contains("\"key\":")) {
          while (elements.hasNext()) {
            JsonNode node = elements.next();
            String name = node.path("name").asText();
            if ("_RoleID".equals(name)) {
              roleId = node.path("value").asText();
            }
          }
          id = UUID.randomUUID().toString();
          employeeIds.put(roleId, id);
      } else {
        id = rootNode.path("key").asText();
      }
      xmaResponse.setKey(id);
      addMessage(id, body);
      return new ResponseEntity<XmaResponse>(xmaResponse, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/messages")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<String>();
    Collection<List<String>> messages = xmaMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("email") String email) {
    log.info("/xma/messages/"+email);
    List<String> messages = xmaMessages.get(email);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @GetMapping("/id")
  public ResponseEntity<String> getId(@RequestParam(name="roleId") String roleId) {
    if(employeeIds.containsKey(roleId)) {
      return new ResponseEntity<String>(employeeIds.get(roleId),HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    xmaMessages.clear();
    employeeIds.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String email, String body) {
    log.info("xma post id:" + email);
    List<String> messages = xmaMessages.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(body);
    xmaMessages.put(email, messages);
  }
}
