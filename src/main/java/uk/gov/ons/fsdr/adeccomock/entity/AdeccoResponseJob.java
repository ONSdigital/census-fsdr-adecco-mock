package uk.gov.ons.fsdr.adeccomock.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdeccoResponseJob {

  @JsonProperty("Hiring_Manager_First_Name__c")
  String lineManagerFirstName;

  @JsonProperty("Hiring_Manager_Last_Name__c")
  String lineManagerSurName;

  @JsonProperty("Job_Title__c")
  String jobRole;

  Attributes attributes = new Attributes("TR1__Job__c", "/services/data/v40.0/sobjects/TR1__Job__c/");
}
