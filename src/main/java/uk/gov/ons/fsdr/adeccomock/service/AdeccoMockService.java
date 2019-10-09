package uk.gov.ons.fsdr.adeccomock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.adeccomock.managers.ResponseManager;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseList;

import java.util.List;

@Service
@Slf4j
public class AdeccoMockService {

  @Autowired
  private ResponseManager responseManager;

  public AdeccoResponseList getAdeccoResponses() {
    AdeccoResponseList adeccoResponseList = new AdeccoResponseList();
    List<AdeccoResponse> allResponses = responseManager.getAllResponses();
    adeccoResponseList.setRecords(allResponses);
    adeccoResponseList.setTotalSize(String.valueOf(allResponses.size()));
    adeccoResponseList.setDone(true);
    return adeccoResponseList;
  }

  public List<AdeccoResponse> getEmployeeById(String employeeId) {
    List<AdeccoResponse> adeccoResponse;
    adeccoResponse = responseManager.getResponse(employeeId);
    return adeccoResponse;
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
