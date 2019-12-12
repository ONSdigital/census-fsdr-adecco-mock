package uk.gov.ons.fsdr.adeccomock.controller.gsuite;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import javax.websocket.server.PathParam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/gsuite")
public class MockGSuite {
  private final Map<String, List<String>> gsuiteMessages = Collections.synchronizedMap(new LinkedHashMap());
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(path = "/groups/{group}/members", consumes = "application/json")
  public ResponseEntity<String> postMember(@PathVariable(value = "group") String group, @RequestBody byte[] body) {
    try {
      String extractedMessage = extractMessage(body);
      JsonNode rootNode = objectMapper.readTree(extractedMessage);
      JsonNode emailNode = rootNode.path("primaryEmail");
      String email = emailNode.asText();

      addMessage(email, extractedMessage);
      return new ResponseEntity<String>(extractedMessage, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(path = "/users", consumes = "application/json")
  public ResponseEntity<String> postUser(@RequestBody byte[] body) {
    try {
      String extractedMessage = extractMessage(body);
      JsonNode rootNode = objectMapper.readTree(extractedMessage);
      JsonNode emailNode = rootNode.path("primaryEmail");
      String email = emailNode.asText();

      addMessage(email, extractedMessage);
      return new ResponseEntity<String>(extractedMessage, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @PutMapping(path = "/users/{email}", consumes = "application/json")
  public ResponseEntity<String> putUser(@PathVariable("email") String email, @RequestBody byte[] body) {
    try {
      String extractedMessage = extractMessage(body);
      addMessage(email, extractedMessage);
      return new ResponseEntity<String>(extractedMessage, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private String extractMessage(byte[] body) throws IOException {
    String extractedMessage = "";
    GZIPInputStream zipIn = new GZIPInputStream(new ByteArrayInputStream(body));
    extractedMessage = extractFile(zipIn);
    zipIn.close();
    return extractedMessage;
  }

  @GetMapping("/messages/")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<>();
    Collection<List<String>> messages = gsuiteMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<List<String>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("email") String email) {
    List<String> messages = gsuiteMessages.get(email);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    gsuiteMessages.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private String extractFile(GZIPInputStream zipIn) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(baos);
    byte[] bytesIn = new byte[8192];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
    baos.close();
    return baos.toString();
  }

  private void addMessage(String email, String extractedMessage) {
    List<String> messages = gsuiteMessages.get(email);
    if (messages == null) messages = new ArrayList<String>();
    messages.add(extractedMessage);
    gsuiteMessages.put(email, messages);
  }
}
