package uk.gov.ons.fsdr.adeccomock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AdeccoResponse {

  @JsonIgnore
  LocalDateTime LastModifiedDate;

  @JsonProperty("TR1__Start_Date__c")
  String contractStartDate;

  @JsonProperty("TR1__End_Date__c")
  String contractEndDate;

  @JsonProperty("TR1__Person_Placed__r")
  AdeccoResponseContact responseContact;

  @JsonProperty("TR1__Job__r")
  AdeccoResponseJob responseJob;

  Attributes attributes = new Attributes("TR1__Closing_Report__c", "/services/data/v40.0/sobjects/TR1__Closing_Report__c/");
}
