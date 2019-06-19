package uk.gov.ons.fsdr.adeccomock.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.ons.fsdr.adeccomock.entity.AdeccoResponse;
import uk.gov.ons.fsdr.adeccomock.entity.AdeccoResponseList;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdeccoMockService {

  private static AdeccoResponseList adeccoResponseList = new AdeccoResponseList(0, true, new ArrayList<AdeccoResponse>());

  public AdeccoResponseList getAdeccoResponse() {

    adeccoResponseList.setTotalSize(adeccoResponseList.getRecords().size());
    return adeccoResponseList;
  }

  public boolean addContacts(List<AdeccoResponse> newRecords) {
    List<AdeccoResponse> adeccoRecords = adeccoResponseList.getRecords();

    return adeccoRecords.addAll(newRecords);
  }

  public boolean clearRecords() {

    adeccoResponseList.getRecords().clear();

    return adeccoResponseList.getRecords().isEmpty();
  }
}
