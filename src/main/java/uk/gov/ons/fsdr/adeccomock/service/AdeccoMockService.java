package uk.gov.ons.fsdr.adeccomock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.adeccomock.dto.Device;
import uk.gov.ons.fsdr.adeccomock.dto.Employee;
import uk.gov.ons.fsdr.adeccomock.factory.AdeccoEmployeeFactory;
import uk.gov.ons.fsdr.adeccomock.managers.ResponseManager;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseList;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import static uk.gov.ons.fsdr.adeccomock.EmployeeInitializer.devices;
import static uk.gov.ons.fsdr.adeccomock.EmployeeInitializer.employees;

@Service
@Slf4j
public class AdeccoMockService {

  @Autowired
  private ResponseManager responseManager;

  public static int totalCount;

  private static int remainingRecords;
  private static int deviceCount;
  private static int employeeCount;

  public AdeccoResponseList getInitialAdeccoResponses(String sql) {
    AdeccoResponseList adeccoResponseList = new AdeccoResponseList();

    List<AdeccoResponse> allResponses = getResponses(sql);
    allResponses.sort(Comparator.comparing((AdeccoResponse aR) -> aR.getAdeccoResponseWorker().getEmployeeId()));
    adeccoResponseList.setRecords(allResponses);
    adeccoResponseList.setTotalSize(String.valueOf(allResponses.size()));
    adeccoResponseList.setNextRecordsUrl("nextRecord");
    adeccoResponseList.setDone(getDone(remainingRecords));

    return adeccoResponseList;
  }

  public AdeccoResponseList getRemainingAdeccoResponses() throws IOException {
    AdeccoResponseList adeccoResponseList = new AdeccoResponseList();
    createAdeccoResponses();
    if (responseManager.getAllResponses().size()==0){
      List<AdeccoResponse> idBadges = responseManager.getAllIdBadgeResponses();
      adeccoResponseList.setRecords(idBadges);
      adeccoResponseList.setTotalSize(String.valueOf(idBadges.size()));
      adeccoResponseList.setNextRecordsUrl("nextRecord");
      adeccoResponseList.setDone(true);
      log.info("Getting next set of id badges");
    } else {
      adeccoResponseList.setDone(getDone(remainingRecords));
      List<AdeccoResponse> allResponses = responseManager.getAllResponses();
      adeccoResponseList.setRecords(allResponses);
      adeccoResponseList.setTotalSize(String.valueOf(allResponses.size()));
      adeccoResponseList.setNextRecordsUrl("nextRecord");
      log.info("Getting next set of employees");
    }
    return adeccoResponseList;
  }

  private boolean getDone(int remainingRecords) {
    return remainingRecords == 0;
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

  public void createManyRecords(int count) throws IOException {
    remainingRecords = count;
    totalCount = count;
    createAdeccoResponses();
  }

  public void clearRecords() {
    responseManager.reset();
    responseManager.resetIdBadges();
    remainingRecords = 0;
    deviceCount = 0;
    totalCount = 0;
    employeeCount = 0;
    log.info("cleared Adecco Responses.");
  }

  public void enableLogger() {
    responseManager.enableCaseManager();
  }

  public void disableLogger() {
    responseManager.disableCaseManager();
  }


  private List<AdeccoResponse> getResponses(String sql) {
    if(sql.contains("Worker__r")){
      log.info("Getting initial Id Badges.");
      return responseManager.getAllIdBadgeResponses();
    } else {
      log.info("Getting initial Employee set.");
      return responseManager.getAllResponses();
    }
  }

  private void createAdeccoResponses() {
    responseManager.reset();

    int numberOfEmployees;
    if(remainingRecords > 250) {
      numberOfEmployees = 250;
    } else {
      numberOfEmployees = remainingRecords;
    }
    for (int i = 0; i < numberOfEmployees; i++) {
      Device device = devices.get(deviceCount);
      Employee employee = employees.get(employeeCount);
      employee.setUniqueEmployeeId(String.valueOf(UUID.randomUUID()));
      employee.setRoleId(device.getRoleId());
      employee.setJobRole(setJobRole(device));
      AdeccoResponse adeccoResponse = AdeccoEmployeeFactory.buildAdeccoResponse(employee);
      responseManager.addResponse(adeccoResponse);
      AdeccoResponse idBadgeResponse = AdeccoEmployeeFactory.buildIdBadgeResponse(employee);
      responseManager.addIdBadgeResponse(idBadgeResponse);
      employeeCount++;
      deviceCount++;
      remainingRecords--;
      if (employeeCount == employees.size()) {
        employeeCount = 0;
      }
    }
    log.info("Employees setup: {}", numberOfEmployees);
  }

  private String setJobRole(Device device) {
    if (device.getRoleId().length() == 10) {
      return "Field officer";
    } else if (device.getRoleId().length() == 7) {
      return "Coordinator";
    } else if (device.getRoleId().length() == 4) {
      return "Area Manager";
    }
    return null;
  }

}
