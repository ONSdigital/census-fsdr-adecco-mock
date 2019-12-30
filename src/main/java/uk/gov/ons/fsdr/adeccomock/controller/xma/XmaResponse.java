package uk.gov.ons.fsdr.adeccomock.controller.xma;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmaResponse {
  @JsonProperty("ClassName")
  private String ClassName;
  @JsonProperty("Key")
  private String Key;
}
