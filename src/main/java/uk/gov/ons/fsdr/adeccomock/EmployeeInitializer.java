package uk.gov.ons.fsdr.adeccomock;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import uk.gov.ons.fsdr.adeccomock.dto.Device;
import uk.gov.ons.fsdr.adeccomock.dto.Employee;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    File file = deviiceFile.getFile();
    return (List<Device>) new CsvToBeanBuilder(new FileReader(file))
        .withType(Device.class)
        .build()
        .parse();
  }

  private List<Employee> getEmployeesFromCsv() throws IOException {
    File file = employeeFile.getFile();
    return (List<Employee>) new CsvToBeanBuilder(new FileReader(file))
        .withType(Employee.class)
        .build()
        .parse();
  }

}
