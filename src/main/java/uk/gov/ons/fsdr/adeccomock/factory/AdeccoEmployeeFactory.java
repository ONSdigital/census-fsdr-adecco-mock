package uk.gov.ons.fsdr.adeccomock.factory;

import uk.gov.ons.fsdr.adeccomock.dto.Employee;
import uk.gov.ons.fsdr.common.dto.AdeccoResponse;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseContact;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseJob;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseJobRoleCode;
import uk.gov.ons.fsdr.common.dto.AdeccoResponseWorker;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class AdeccoEmployeeFactory {

  private static long cr_id = 0;
  
  public static AdeccoResponse buildAdeccoResponse(Employee employee) {
    AdeccoResponseJob job = AdeccoResponseJob.builder()
        .jobRole(employee.getJobRole())
        .build();
    AdeccoResponseContact contact = AdeccoResponseContact.builder()
        .employeeId(employee.getUniqueEmployeeId())
        .firstName(employee.getFirstName())
        .lastName(employee.getSurname())
        .addressLine1(employee.getAddress1())
        .addressLine2(employee.getAddress2())
        .town(employee.getTown())
        .county(employee.getCounty())
        .postcode(employee.getPostcode())
        .personalEmail(employee.getFirstName()+employee.getSurname()+"yamail.com")
        .telephoneNo1(employee.getTelephoneNumberContact1())
        .emergencyContact(employee.getEmergencyContactFullName())
        .emergencyContactNumber1(employee.getEmergencyContactMobileNo())
        .mobility(employee.getMobility())
        .dob(String.valueOf(employee.getDob()))
        .build();
    AdeccoResponseWorker worker = AdeccoResponseWorker.builder().employeeId(employee.getUniqueEmployeeId()).build();

    AdeccoResponseJobRoleCode adeccoResponseJobRoleCode = new AdeccoResponseJobRoleCode();
    adeccoResponseJobRoleCode.setRoleId(employee.getRoleId());

    return AdeccoResponse.builder()
        .contractStartDate(LocalDate.now().minus(5, ChronoUnit.DAYS).toString())
        .contractEndDate(LocalDate.now().plus(5, ChronoUnit.DAYS).toString())
        .operationalEndDate(LocalDate.now().plus(5, ChronoUnit.DAYS).toString())
        .responseContact(contact)
        .adeccoResponseWorker(worker)
        .responseJob(job)
        .status("ASSIGNED")
        .crStatus("ACTIVE")
        .adeccoResponseJobRoleCode(adeccoResponseJobRoleCode)
        .closingReportId(String.valueOf(cr_id++))
        .build();
  }

  public static AdeccoResponse buildIdBadgeResponse(Employee employee) {
    AdeccoResponseWorker worker = AdeccoResponseWorker.builder().employeeId(employee.getUniqueEmployeeId()).build();
    return AdeccoResponse.builder()
        .idBadgeNumber(UUID.randomUUID().toString())
        .adeccoResponseWorker(worker)
        .build();
  }
}
