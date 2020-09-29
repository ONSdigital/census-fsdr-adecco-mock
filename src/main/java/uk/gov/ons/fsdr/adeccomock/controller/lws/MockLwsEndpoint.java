package uk.gov.ons.fsdr.adeccomock.controller.lws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import local.loneworker.InsertUpdatePerson4;
import local.loneworker.InsertUpdatePerson4Response;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Endpoint
public class MockLwsEndpoint {

  @Autowired
  MockLwsMessages mockLwsMessages;

  private static final String NAMESPACE_URI = "http://loneworker.local/";


  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InsertUpdatePerson4")
  @ResponsePayload
  public JAXBElement<InsertUpdatePerson4Response> InsertUpdatePerson4(@RequestPayload JAXBElement<InsertUpdatePerson4> request) {

    mockLwsMessages.acceptRequest(request.getValue());
    System.out.println(request.toString());

    InsertUpdatePerson4Response response = new InsertUpdatePerson4Response();
    response.setInsertUpdatePerson4Result("\"2\"");

    return new JAXBElement(new QName(NAMESPACE_URI, InsertUpdatePerson4Response.class.getSimpleName()), InsertUpdatePerson4Response.class, response);
  }
}
