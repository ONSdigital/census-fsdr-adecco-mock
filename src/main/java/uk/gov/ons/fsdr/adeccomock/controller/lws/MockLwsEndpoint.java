package uk.gov.ons.fsdr.adeccomock.controller.lws;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import local.loneworker.InsertUpdatePerson4;
import local.loneworker.InsertUpdatePerson4Response;
import local.loneworker.ObjectFactory;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

@Endpoint
public class MockLwsEndpoint {

  private static final String NAMESPACE_URI = "http://loneworker.local";

  private ObjectFactory objectFactory = new ObjectFactory();

  @PayloadRoot(namespace = NAMESPACE_URI, localPart = "InsertUpdatePerson4")
  @ResponsePayload
  public JAXBElement<InsertUpdatePerson4Response> InsertUpdatePerson4(@RequestPayload JAXBElement<InsertUpdatePerson4> request) {

    System.out.println("Yo");
    InsertUpdatePerson4Response response = new InsertUpdatePerson4Response();


    return new JAXBElement(
        new QName(InsertUpdatePerson4Response.class.getSimpleName()), InsertUpdatePerson4Response.class, response);
  }
}
