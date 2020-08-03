package uk.gov.ons.fsdr.adeccomock.managers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResponseManager {
  private Map<String, List<AdeccoResponse>> responseDirectory = new ConcurrentHashMap<>();
  private Map<String, List<AdeccoResponse>> idBadgeDirectory = new ConcurrentHashMap<>();

  @Value("${customisation.logging.logFlagType.logAllMessages}")
  private boolean logAllMessages;

  public void enableCaseManager() {
    logAllMessages = true;
  }

  public void disableCaseManager() {
    logAllMessages = false;
  }

  public List<AdeccoResponse> getAllResponses() {
    List<AdeccoResponse>  responses = new ArrayList<>();
    Collection<List<AdeccoResponse>> values = responseDirectory.values();
    for (List<AdeccoResponse> list : values) {
      responses.addAll(list);
    }
    return responses;
  }

  public void addResponse(AdeccoResponse adeccoResponse) {
    String employeeId = adeccoResponse.getResponseContact().getEmployeeId();
    List<AdeccoResponse>  responses = new ArrayList<AdeccoResponse>();
    if (responseDirectory.containsKey(employeeId)) {
      responses.addAll(responseDirectory.get(employeeId));
    }
    responses.add(adeccoResponse);
    responseDirectory.put(String.valueOf(employeeId), responses);
  }

  public List<AdeccoResponse> getResponse(String employeeId) {
    return responseDirectory.get(employeeId);
  }

  public void reset() {
    responseDirectory.clear();
  }

  public void resetIdBadges() {
    idBadgeDirectory.clear();
  }

  public List<AdeccoResponse> getAllIdBadgeResponses() {
    List<AdeccoResponse>  responses = new ArrayList<AdeccoResponse>();
    Collection<List<AdeccoResponse>> values = idBadgeDirectory.values();
    for (List<AdeccoResponse> list : values) {
      responses.addAll(list);
    }
    return responses;
  }

  public void addIdBadgeResponse(AdeccoResponse adeccoResponse) {
    String employeeId = adeccoResponse.getAdeccoResponseWorker().getEmployeeId();
    List<AdeccoResponse>  responses = new ArrayList<AdeccoResponse>();
    if (idBadgeDirectory.containsKey(employeeId)) {
      responses.addAll(idBadgeDirectory.get(employeeId));
    }
    responses.add(adeccoResponse);
    idBadgeDirectory.put(String.valueOf(employeeId), responses);
  }

  public List<AdeccoResponse> getIdBadgeResponse(String employeeId) {
    return idBadgeDirectory.get(employeeId);
  }

}
