package uk.gov.ons.fsdr.adeccomock.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  @CsvBindByName(column = "unique_employee_id")
  private String uniqueEmployeeId;

  @CsvBindByName(column = "first_name")
  private String firstName;

  @CsvBindByName(column = "surname")
  private String surname;

  @CsvBindByName(column = "preferred_name")
  private String preferredName;

  @CsvBindByName(column = "address_1")
  private String address1;

  @CsvBindByName(column = "address_2")
  private String address2;

  @CsvBindByName(column = "town")
  private String town;

  @CsvBindByName(column = "county")
  private String county;

  @CsvBindByName(column = "postcode")
  private String postcode;

  @CsvBindByName(column = "country")
  private String country;

  @CsvBindByName(column = "personal_email_address")
  private String personalEmailAddress;

  @CsvBindByName(column = "telephone_number_contact_1")
  private String telephoneNumberContact1;

  @CsvBindByName(column = "telephone_number_contact_2")
  private String telephoneNumberContact2;

  @CsvBindByName(column = "emergency_contact_full_name")
  private String emergencyContactFullName;

  @CsvBindByName(column = "emergency_contact_mobile_no")
  private String emergencyContactMobileNo;

  @CsvBindByName(column = "mobility")
  private String mobility;

  @CsvBindByName(column = "mobile_staff")
  private Boolean mobileStaff;

  @CsvBindByName(column = "id_badge_no")
  private String idBadgeNo;

  @CsvBindByName(column = "weekly_hours")
  private Double weeklyHours;

  @CsvBindByName(column = "dob")
  private String dob;

  @CsvBindByName(column = "role_id")
  private String roleId;

  @CsvBindByName(column = "job_role")
  private String jobRole;

}
