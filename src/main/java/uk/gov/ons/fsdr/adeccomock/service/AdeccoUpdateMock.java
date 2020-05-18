package uk.gov.ons.fsdr.adeccomock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AdeccoUpdateMock {

  private final Map<String, List<String>> adeccoUpdateMessages = Collections.synchronizedMap(new LinkedHashMap<>());

 public void addUpdateMessage(String id, String message) {
   addMessage(id, message);
 }

 public List<String> getMessages() {
   List<String> all = new ArrayList<>();
   Collection<List<String>> messages = adeccoUpdateMessages.values();
   for (List<String> list : messages) {
     all.addAll(list);
   }
   return all;
 }

  public List<String> getMessagesById(String id) {
    return adeccoUpdateMessages.get(id);
  }

  private void addMessage(String id, String extractedMessage) {
    List<String> messages = adeccoUpdateMessages.get(id);
    if (messages == null) messages = new ArrayList<>();
    messages.add(extractedMessage);
    adeccoUpdateMessages.put(id, messages);
  }

  public void clear() {
   adeccoUpdateMessages.clear();
  }

}
