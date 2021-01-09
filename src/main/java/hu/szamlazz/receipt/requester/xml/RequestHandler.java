package hu.szamlazz.receipt.requester.xml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import hu.szamlazz.receipt.requester.controller.Utils;

public class RequestHandler {
	
	private static String URL = "https://www.szamlazz.hu/szamla/";
	
	private static RequestHandler instance = null;
	
	private HttpClient client = HttpClient.newHttpClient();
	
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
	
	public XmlNyugtaValasz request(String requestBody) {
		String method = "request";
		Utils.log(method, "started");
		
		 try { 
			 
			 URL url = new URL(URL);
			 HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			 
			 connection.setRequestMethod("POST");
			 connection.setDoOutput(true);
			 connection.setRequestProperty("Content-Type", "multipart/form-data");
			 connection.setRequestProperty("charset", "utf-8");
			 
			 if(JSESSIONID != null) {
				 connection.setRequestProperty("Cookie", "JSESSIONID=" + JSESSIONID);
			 	}
			 
			 String requestData = URLEncoder.encode("action-szamla_agent_nyugta_create", "UTF-8") + "=" + URLEncoder.encode(requestBody, "UTF-8");
			 byte[] requestDataBytes = requestData.getBytes("UTF-8");
			 connection.setRequestProperty("Content-Length", String.valueOf(requestDataBytes.length));
			 

			try (DataOutputStream writer = new DataOutputStream(connection.getOutputStream())) {
			    writer.write(requestDataBytes);

			    // Always flush and close
			    writer.flush();
			    writer.close();
			}
			
			try{
	        	String cookie = connection.getHeaderField("set-cookie");
		        String sessionId = cookie.substring(11, cookie.indexOf(";")); //JSESSIONID=
		        
		        Utils.log(method, "sessionId: " + sessionId);
		        
		        setJSESSIONID(sessionId);
	        }catch(Exception ex) {
	        	Utils.log(method, "error during sessionId retreaval: " + ex);
	        }
			
			StringBuilder content;
			try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			    String line;
			    content = new StringBuilder();
			    while ((line = input.readLine()) != null) {
			        // Append each line of the response and separate them
			        content.append(line);
			        content.append(System.lineSeparator());
			    }
			}
			
			Utils.log(method, "response content: " + content.toString());
			
			XmlNyugtaValasz body = unmashal(connection.getInputStream(), XmlNyugtaValasz.class);
			
			connection.disconnect();
			
			/*
			
			 	HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
		                .uri(URI.create(URL))
		                .POST(HttpRequest.BodyPublishers.ofString("action-szamla_agent_nyugta_create=" + requestBody));
			 	
			 	if(JSESSIONID != null) {
			 		requestBuilder.header("Cookie", "JSESSIONID=" + JSESSIONID);
			 	}
			 	
		        HttpRequest request = requestBuilder.build();

		        HttpResponse<String> response = client.send(request,
		                HttpResponse.BodyHandlers.ofString());
		        
		        Utils.log(method, "headers: " + response.headers());
		        Utils.log(method, "body: " + response.body());
		        
		        try{
		        	String cookie = response.headers().map().get("set-cookie").get(0);
			        String sessionId = cookie.substring(11, cookie.indexOf(";")); //JSESSIONID=
			        
			        Utils.log(method, "sessionId: " + sessionId);
			        
			        setJSESSIONID(sessionId);
		        }catch(Exception ex) {
		        	Utils.log(method, "error during sessionId retreaval: " + ex);
		        }
		        
		        XmlNyugtaValasz body = unmashal(response.body(), XmlNyugtaValasz.class);*/
		        
		        Utils.log(method, "resolved " + body);
		        
		        return body;
			 
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
		    	Utils.log(method, e.toString());
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
	
	public <T> T unmashal(Reader reader, Class<T> target) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(target);
	    Unmarshaller mar= context.createUnmarshaller();
	    
	    Object result = mar.unmarshal(reader);
	    	
	    return target.cast(result);
	}
	
	public <T> T unmashal(InputStream stream, Class<T> target) throws JAXBException {
	    InputStreamReader reader = new InputStreamReader(stream);
	    
	    return unmashal(reader, target);
	}
	
	public <T> T unmashal(String xml, Class<T> target) throws JAXBException {
		StringReader reader = new StringReader(xml);
		
		return unmashal(reader, target);
	}

}
