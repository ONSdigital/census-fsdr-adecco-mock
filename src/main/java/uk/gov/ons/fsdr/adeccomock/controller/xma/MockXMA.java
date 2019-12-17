package uk.gov.ons.fsdr.adeccomock.controller.xma;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.ons.fsdr.common.dto.devicelist.AllocatedEndUserLookup;
import uk.gov.ons.fsdr.common.dto.devicelist.DataRow;
import uk.gov.ons.fsdr.common.dto.devicelist.DataRowValues;
import uk.gov.ons.fsdr.common.dto.devicelist.DeviceListQuery;
import uk.gov.ons.fsdr.common.dto.devicelist.Status;

@RestController
@RequestMapping("/xma")
public class MockXMA {
  private final Map<String, List<String>> xmaMessages = Collections.synchronizedMap(new LinkedHashMap());
  private final Map<String, String> employeeIds = new ConcurrentHashMap<>();
  private final List<DataRow> devices = new ArrayList<>();
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping(consumes = "application/json")
  public ResponseEntity<XmaResponse> postEmployee(@RequestParam(name="class_name") String className, @RequestParam(name="v") String version,  @RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode formValuesNode = rootNode.path("formValues");
      Iterator<JsonNode> elements = formValuesNode.elements();
      String id = null;
      String roleId = null;

      while(elements.hasNext()){
        JsonNode node = elements.next();
        String name = node.path("name").asText();
        if ("Name".equals(name)) {
          id = node.path("value").asText();
        }
        if ("_RoleID".equals(name)) {
          roleId = node.path("value").asText();
        }
      }
      String xmaId = UUID.randomUUID().toString();
      XmaResponse xmaResponse = new XmaResponse();
      xmaResponse.setKey(xmaId);

      employeeIds.put(roleId, xmaId);
      addMessage(id, body);
      return new ResponseEntity<XmaResponse>(xmaResponse, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PatchMapping(path = "/", consumes = "application/json")
  public ResponseEntity<String> updateEmployee(@RequestBody String body) {
    try {
      JsonNode rootNode = objectMapper.readTree(body);
      JsonNode emailNode = rootNode.path("primaryEmail");
      String email = emailNode.asText();

      addMessage(email, body);
      return new ResponseEntity<String>(body, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/messages")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<String>();
    Collection<List<String>> messages = xmaMessages.values();
    for (List<String> list : messages) {
        all.addAll(list);
    }
    return new ResponseEntity<>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{email}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("email") String email) {
    List<String> messages = xmaMessages.get(email);
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @PostMapping("/devices/create")
  public ResponseEntity<?> createDevice(@RequestParam(name="roleId") String roleId, @RequestParam(name="phoneNumber") String phoneNumber, @RequestParam(name="Status") String status) {
    if(employeeIds.containsKey(roleId)) {
      devices.add(createDevice(roleId, employeeIds.get(roleId), phoneNumber, status));
      return new ResponseEntity<>(HttpStatus.OK);
    } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping("/devices")
  public ResponseEntity<DeviceListQuery> getDevices(@RequestParam(name="class_name") String className, @RequestParam(name="v") String version,  @RequestParam(name="attributes") String attributes, @RequestParam(name="page_size") String pageSize) {
    DeviceListQuery deviceListQuery = new DeviceListQuery();
    deviceListQuery.setDataRows(devices);
    return new ResponseEntity<DeviceListQuery>(deviceListQuery, HttpStatus.OK);
  }

  @GetMapping(value ="/devices", params = "c0")
  public ResponseEntity<DeviceListQuery> getDevicesLastUpdated(@RequestParam(name="class_name") String className, @RequestParam(name="v") String version,
      @RequestParam(name="attributes") String attributes, @RequestParam(name="page_size") String pageSize, @RequestParam(name="cns") String cns, @RequestParam(name="c0") String c0) {
    DeviceListQuery deviceListQuery = new DeviceListQuery();
    deviceListQuery.setDataRows(devices);
    return new ResponseEntity<DeviceListQuery>(deviceListQuery, HttpStatus.OK);
  }

  @GetMapping("/id")
  public ResponseEntity<String> getId(@RequestParam(name="roleId") String roleId) {
    if(employeeIds.containsKey(roleId)) {
      return new ResponseEntity<String>(employeeIds.get(roleId),HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    xmaMessages.clear();
    employeeIds.clear();
    devices.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private void addMessage(String email, String body) {
    List<String> messages = xmaMessages.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(body);
    xmaMessages.put(email, messages);
  }

  private DataRow createDevice(String roleId, String id, String phoneNumber, String st) {
    DataRow device =  new DataRow();
    device.setClassName("Config._MobileDevice");
    DataRowValues dataRowValues = new DataRowValues();
    dataRowValues.setImie(UUID.randomUUID().toString());
    dataRowValues.setDeviceTelephoneNumber(phoneNumber);

    Status status = new Status(st);
    AllocatedEndUserLookup user = new AllocatedEndUserLookup();
    user.setGuid(id);
    user.setRoleID(roleId);

    dataRowValues.setStatus(status);
    dataRowValues.setAllocatedEndUserLookup(user);
    device.setDataRowValues(dataRowValues);

    return device;
  }
}
