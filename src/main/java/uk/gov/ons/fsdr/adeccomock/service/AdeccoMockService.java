package uk.gov.ons.fsdr.adeccomock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.adeccomock.managers.ResponseManager;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;

import java.util.List;

@Service
@Slf4j
public class AdeccoMockService {

  @Autowired
  private ResponseManager responseManager;

  public List<AdeccoResponse> getAdeccoResponses() {
    return responseManager.getAllResponses();
  }

  public void putRecords(List<AdeccoResponse> newRecords) {
    for (AdeccoResponse record : newRecords) {
      responseManager.addResponse(record);
    }
  }

  public void clearRecords() {
    responseManager.reset();
    log.info("cleared Adecco Responses.");
  }

  public void enableLogger() {
    responseManager.enableCaseManager();
  }

  public void disableLogger() {
    responseManager.disableCaseManager();
  }
}
