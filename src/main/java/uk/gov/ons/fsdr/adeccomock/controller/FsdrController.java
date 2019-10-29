package uk.gov.ons.fsdr.adeccomock.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.fsdr.adeccomock.service.AdeccoMockService;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseList;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fsdr")
public class FsdrController {

  @Autowired
  private AdeccoMockService adeccoService;

   @GetMapping(path = "/getEmployee/")
  private ResponseEntity<?> getAdeccoEmployee(@RequestParam String employeeId){
     final List<AdeccoResponse> employees = adeccoService.getEmployeeById(employeeId);
     final AdeccoResponseList adeccoResponseList = new AdeccoResponseList(String.valueOf(employees.size()), employees, true, null);
     return ResponseEntity.ok(adeccoResponseList);
  }
}
