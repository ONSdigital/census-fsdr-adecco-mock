package uk.gov.ons.fsdr.adeccomock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fsdr.adeccomock.managers.ResponseManager;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseList;

@Slf4j
@RestController
@RequestMapping("/fsdr")
public class FsdrController {

  @Autowired
  private AdeccoMockService adeccoService;
  
  @Autowired
  private ResponseManager responseManager;

  @GetMapping(path = "/getEmployee/")
 private ResponseEntity<?> getAdeccoEmployee(@RequestParam String employeeId){
    final List<AdeccoResponse> employees = adeccoService.getEmployeeById(employeeId);
    final AdeccoResponseList adeccoResponseList = new AdeccoResponseList(String.valueOf(employees.size()), employees, true, null);
    return ResponseEntity.ok(adeccoResponseList);
 }
  
  @GetMapping(path = "/getAllEmployees/")
 private ResponseEntity<?> getAllAdeccoEmployees(){
    final List<AdeccoResponse> employees = responseManager.getAllResponses();
    final AdeccoResponseList adeccoResponseList = new AdeccoResponseList(String.valueOf(employees.size()), employees, true, null);
    return ResponseEntity.ok(adeccoResponseList);
 }
  
   
}