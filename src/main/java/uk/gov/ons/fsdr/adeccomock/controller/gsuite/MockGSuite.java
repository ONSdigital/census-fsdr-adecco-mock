package uk.gov.ons.fsdr.adeccomock.controller.gsuite;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.GZIPInputStream;

import com.google.api.services.admin.directory.model.ChromeOsDevice;
import com.google.api.services.admin.directory.model.ChromeOsDevices;
import com.google.api.services.admin.directory.model.Group;
import com.google.api.services.admin.directory.model.Groups;
import com.google.api.services.admin.directory.model.User;
import com.google.api.services.admin.directory.model.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.ons.fsdr.common.dto.HqJobRole;
import uk.gov.ons.fsdr.common.dto.devicelist.DeviceDto;

@RestController
@RequestMapping("/gsuite")
public class MockGSuite {
  private final Map<String, List<String>> gsuiteMessages = Collections.synchronizedMap(new LinkedHashMap<>());
  private final Map<String, List<String>> groups = Collections.synchronizedMap(new LinkedHashMap<>());
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Map<String, String> emailAddresses = new ConcurrentHashMap<>();
  private final List<User> hqJobRoles = Collections.synchronizedList(new ArrayList<>());
  private final List<ChromeOsDevice> chromeOsDeviceList = Collections.synchronizedList(new ArrayList<>());
  private final Map<String, String> gsuiteIds = Collections.synchronizedMap(new LinkedHashMap<>());

  @PostMapping(path = "/groups/{group}/members", consumes = "application/json")
  public ResponseEntity<String> postMember(@PathVariable(value = "group") String group, @RequestBody byte[] body) {
    try {
      String extractedMessage = extractMessage(body);
      JsonNode rootNode = objectMapper.readTree(extractedMessage);
      String email = rootNode.path("email").asText();

      addGroups(email, group);
      return new ResponseEntity<String>(extractedMessage, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(path = "/addRoleId", consumes = "application/json")
  public ResponseEntity<HttpStatus> addHqRoleIdToGsuite(@RequestBody HqJobRole jobRole) {
    User user = new User();
    user.setId(gsuiteIds.get(jobRole.getUniqueEmployeeId()));
    user.setExternalIds("External"+jobRole.getUniqueRoleId());
    hqJobRoles.add(user);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping(path = "/users", consumes = "application/json")
  public ResponseEntity<User> postUser(@RequestBody byte[] body) {
    try {
      String id = UUID.randomUUID().toString();

      User user = new User();
      user.setId(id);
      String extractedMessage = extractMessage(body);
      JsonNode rootNode = objectMapper.readTree(extractedMessage);
      String empId = null;
      System.out.println(extractedMessage);
      if(!extractedMessage.contains("externalIds")) {
        empId = rootNode.path("recoveryEmail").asText().split("@")[0];
      } else empId = rootNode.path("externalIds").get(0).path("value").asText();
      String email = rootNode.path("primaryEmail").asText();

      gsuiteIds.put(empId, id);
      emailAddresses.put(empId, email);
      addMessage(email, extractedMessage);
      return new ResponseEntity<>(user, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  @PutMapping(path = "/users/{email}", consumes = "application/json")
  public ResponseEntity<String> putUser(@PathVariable("email") String email, @RequestBody byte[] body) {
    try {
      String extractedMessage = extractMessage(body);
      addMessage(email, extractedMessage);
      return new ResponseEntity<String>(extractedMessage, HttpStatus.OK);
    } catch (IOException e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping(path = "/groups/{group}/members/{member}")
  public ResponseEntity<?> deleteMember(@PathVariable(value = "group") String group, @PathVariable(value = "member") String member) {
    List<String> grps =  groups.get(member);
    grps.remove(group);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping(path = "/groups")
  public ResponseEntity<Groups> getMembersGroups(@RequestParam String userKey) {
    Groups grps = new Groups();
    List<Group> groupsList = new ArrayList<>();

    for(String grpName : groups.get(userKey)) {
      Group grp = new Group();
      grp.setEmail(grpName);
      groupsList.add(grp);
    }

    grps.setGroups(groupsList);
    return new ResponseEntity<>(grps, HttpStatus.OK);
  }

  @GetMapping(path = "/users")
  public ResponseEntity<Users> getRoleIds(@RequestParam String domain, @RequestParam String maxResults, @RequestParam String query) {
    Users users = new Users();
    users.setUsers(hqJobRoles);
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  private String extractMessage(byte[] body) throws IOException {
    String extractedMessage = "";
    GZIPInputStream zipIn = new GZIPInputStream(new ByteArrayInputStream(body));
    extractedMessage = extractFile(zipIn);
    zipIn.close();
    return extractedMessage;
  }

  @GetMapping("/groups/{employeeId}")
  public ResponseEntity<List<String>> getUsersGroups(@PathVariable("employeeId") String employeeId) {
    List<String> messages = groups.get(emailAddresses.get(employeeId));
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @GetMapping("/messages/")
  public ResponseEntity<List<String>> getAllMessages() {
    List<String> all = new ArrayList<>();
    Collection<List<String>> messages = gsuiteMessages.values();
    for (List<String> list : messages) {
      all.addAll(list);
    }
    return new ResponseEntity<List<String>>(all, HttpStatus.OK);
  }

  @GetMapping("/messages/{employeeId}")
  public ResponseEntity<List<String>> getUsersMessages(@PathVariable("employeeId") String employeeId) {
    List<String> messages = gsuiteMessages.get(emailAddresses.get(employeeId));
    return new ResponseEntity<List<String>>(messages, HttpStatus.OK);
  }

  @DeleteMapping("/messages/reset")
  public ResponseEntity<?> delete() {
    gsuiteMessages.clear();
    groups.clear();
    hqJobRoles.clear();
    chromeOsDeviceList.clear();
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("customer/{customerId}/devices/chromeos")
  public ResponseEntity<ChromeOsDevices> getChromebooks(@PathVariable("customerId") String customerId) {
    ChromeOsDevices chromeOsDevices = new ChromeOsDevices();
    chromeOsDevices.setChromeosdevices(chromeOsDeviceList);
    return new ResponseEntity<>(chromeOsDevices, HttpStatus.OK);
  }

  @PostMapping("/device")
  public ResponseEntity<HttpStatus> postDevice(@RequestBody DeviceDto deviceDto) {
    ChromeOsDevice chromeOsDevice = new ChromeOsDevice();
    chromeOsDevice.setSerialNumber(deviceDto.getDeviceId());
    ChromeOsDevice.RecentUsers recentUsers = new ChromeOsDevice.RecentUsers();
    recentUsers.setEmail(deviceDto.getOnsId());
    chromeOsDevice.setRecentUsers(List.of(recentUsers));

    chromeOsDeviceList.add(chromeOsDevice);

    return new ResponseEntity<>(HttpStatus.OK);

  }

  private String extractFile(GZIPInputStream zipIn) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    BufferedOutputStream bos = new BufferedOutputStream(baos);
    byte[] bytesIn = new byte[8192];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
    baos.close();
    return baos.toString();
  }

  private void addMessage(String email, String extractedMessage) {
    List<String> messages = gsuiteMessages.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(extractedMessage);
    gsuiteMessages.put(email, messages);
  }

  private void addGroups(String email, String group) {
    List<String> messages = groups.get(email);
    if (messages == null) messages = new ArrayList<>();
    messages.add(group);
    groups.put(email, messages);
  }

}
