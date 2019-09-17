package uk.gov.ons.fsdr.adeccomock.managers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ResponseManager {
  private Map<String, AdeccoResponse> responseDirectory = new ConcurrentHashMap<>();

  @Value("${customisation.logging.logFlagType.logAllMessages}")
  private boolean logAllMessages;

  public void enableCaseManager() {
    logAllMessages = true;
  }

  public void disableCaseManager() {
    logAllMessages = false;
  }

  public List<AdeccoResponse> getAllResponses() {
    return new ArrayList<>(responseDirectory.values());
  }

  public void addResponse(AdeccoResponse adeccoResponse) {
    responseDirectory.put(String.valueOf(adeccoResponse.getAdeccoResponseWorker().getEmployeeId()), adeccoResponse);
  }

  public AdeccoResponse getResponse(String employeeId) {
    return responseDirectory.get(employeeId);
  }

  public void reset() {
    responseDirectory.clear();
  }
}
