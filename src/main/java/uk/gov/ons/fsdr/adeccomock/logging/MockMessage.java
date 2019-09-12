package uk.gov.ons.fsdr.adeccomock.logging;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MockMessage {

  public String endpoint;
  public String method;

  // request
  public String requestTimestamp;
  public String requestRawHeaders;
  public String requestRawContents;
  public Object requestMessageParsed;

  // response
  public String responseTimestamp;
  public String responseRawHeaders;
  public String responseRawContents;
  public Object responseMessageParsed;
  public boolean isFault;
  
  public Class<?> klass;
  public String entityId;

}
