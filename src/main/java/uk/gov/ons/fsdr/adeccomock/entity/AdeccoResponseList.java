package uk.gov.ons.fsdr.adeccomock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdeccoResponseList {

  int totalSize;

  boolean done;

  List<AdeccoResponse> records;

}
