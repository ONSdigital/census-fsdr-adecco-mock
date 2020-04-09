package uk.gov.ons.fsdr.adeccomock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"uk.gov.ons.census.fwmt.events", "uk.gov.ons.fsdr"})
public class AdeccoMock {

  public static final String APPLICATION_NAME = "Fsdr Mock";

  public static void main(String[] args) {
    SpringApplication.run(AdeccoMock.class, args);
  }
}

