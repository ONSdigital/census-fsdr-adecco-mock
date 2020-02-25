package uk.gov.ons.fsdr.adeccomock;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.gov.ons.fsdr.adeccomock.dto.Device;
import uk.gov.ons.fsdr.adeccomock.dto.Employee;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class EmployeeInitializer implements CommandLineRunner {

  @Value("classpath:files/csv/employee_data.csv")
  Resource employeeFile;

  @Value("classpath:files/csv/device_data.csv")
  Resource deviiceFile;

  public static List<Device> devices;
  public static List<Employee> employees;

  @Override
  public void run(String...args) throws Exception {
    devices = getDevicesFromCsv();
    employees = getEmployeesFromCsv();
  }

  private List<Device> getDevicesFromCsv() throws IOException {
    InputStream file = deviiceFile.getInputStream();
    return new CsvToBeanBuilder<Device>(new InputStreamReader(file))
        .withType(Device.class)
        .build()
        .parse();
  }

  private List<Employee> getEmployeesFromCsv() throws IOException {
    InputStream file = employeeFile.getInputStream();
    return new CsvToBeanBuilder<Employee>(new InputStreamReader(file))
        .withType(Employee.class)
        .build()
        .parse();
  }

}
