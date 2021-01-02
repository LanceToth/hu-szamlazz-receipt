package hu.szamlazz.receipt.requester.controller;

import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class RequestHandler {
	
	private static RequestHandler instance = null;
	
	private String JSESSIONID = null;
	
	private RequestHandler() {
		
	}
	
	public static RequestHandler getInstance() {
		if(instance == null) {
			instance = new RequestHandler();
		}
		
		return instance;
	}

	public String getJSESSIONID() {
		return JSESSIONID;
	}

	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}
	
	public void request() {
		 try { 
			 
			 HttpClient client = HttpClient.newHttpClient();
		        HttpRequest request = HttpRequest.newBuilder()
		                .uri(URI.create("https://www.szamlazz.hu/szamla/"))
		                .header("JSESSIONID", JSESSIONID)
		                //.POST(HttpRequest.BodyPublishers.ofString(requestBody))
		                .build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());
			 
		        /*URL url = new URL("https://www.szamlazz.hu/szamla/");
		        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		        connection.setDoOutput(true);
		        connection.setInstanceFollowRedirects(false);
		        connection.setRequestMethod("POST");
		        connection.setRequestProperty("Content-Type", "application/xml");

		        OutputStream os = connection.getOutputStream();
		        // Write your XML to the OutputStream (JAXB is used in this example)
		        jaxbContext.createMarshaller().marshal(customer, os);
		        os.flush();
		        connection.getResponseCode();
		        connection.disconnect();*/
		    } catch(Exception e) {
		        throw new RuntimeException(e);
		    }
	}
	
	public String mashal(Object object) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(object.getClass());
	    Marshaller mar= context.createMarshaller();
	    mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	    
	    StringWriter sw = new StringWriter();
	    mar.marshal(object, sw);

	    return sw.toString();
	}

}
