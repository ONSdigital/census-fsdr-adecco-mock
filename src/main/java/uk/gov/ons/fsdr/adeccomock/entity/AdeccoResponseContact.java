package uk.gov.ons.fsdr.adeccomock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AdeccoResponseContact {

  @JsonProperty("Id")
  String employeeId;

  @JsonProperty("FirstName")
  String firstName;

  @JsonProperty("LastName")
  String lastName;

  @JsonProperty("TR1__Other_Name__c")
  String preferredName;

  @JsonProperty("Address_Line_1__c")
  String addressLine1;

  @JsonProperty("Address_Line_2__c")
  String addressLine2;

  @JsonProperty("Town__c")
  String town;

  @JsonProperty("County__c")
  String county;

  @JsonProperty("Postcode__c")
  String postcode;

  @JsonProperty("Email")
  String personalEmail;

  @JsonProperty("MobilePhone")
  String telephoneNo1;

  @JsonProperty("HomePhone")
  String telephoneNo2;

  @JsonProperty("Emergency_Contact__c")
  String emergencyContact;

  @JsonProperty("Emergency_Contact_Number__c")
  String emergencyContactNumber1;

  @JsonProperty("TR1__Candidate_Status__c")
  String status;

  @JsonProperty("Birthdate")
  String dob;

  @JsonProperty("UK_EU_Driving_Licence__c")
  String drivingInfo;

  @JsonProperty("Diversity_Age__c")
  String age;

  @JsonProperty("Ethnicity__c")
  String ethnicity;

  @JsonProperty("Ethnicity_Notes__c")
  String ethnicityNotes;

  @JsonProperty("Disability_or_Health_Condition__c")
  String disability;

  @JsonProperty("Disability_or_Health_Condition_notes__c")
  String disabilityNotes;

  @JsonProperty("Nationality__c")
  String nationality;

  @JsonProperty("Diversity_Gender__c")
  String gender;

  @JsonProperty("Sexual_Orientation__c")
  String sexualOrientation;

  @JsonProperty("Sexual_Orientation_Notes__c")
  String sexualOrientationNotes;

  @JsonProperty("Religion_or_belief__c")
  String religion;

  @JsonProperty("Religion_or_belief_notes__c")
  String religionNotes;

  Attributes attributes = new Attributes("Contact", "/services/data/v40.0/sobjects/Contact/");


}

