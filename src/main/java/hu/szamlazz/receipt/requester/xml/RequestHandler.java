package hu.szamlazz.receipt.requester.xml;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import hu.szamlazz.receipt.requester.controller.Utils;

public class RequestHandler {
	
	private static String URL = "https://www.szamlazz.hu/szamla/";
	
	private static String SESSIONLABEL = "JSESSIONID=";
	
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

	public XmlNyugtaValasz request(String requestBody) {
		String method = "request";
		Utils.log(method, "started");
		
		String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
		String CRLF = "\r\n"; // Line separator required by multipart/form-data.
		
		 try { 
			 
			 URL url = new URL(URL);
			 HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			 
			 connection.setRequestMethod("POST");
			 connection.setDoOutput(true);
			 connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			 connection.setRequestProperty("charset", "utf-8");
			 
			 if(JSESSIONID != null) {
				 connection.setRequestProperty("Cookie", SESSIONLABEL + JSESSIONID);
			 }
			 
			 try (OutputStream output = connection.getOutputStream();
					 PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, "utf-8"), true);) {
				// Send text file.
				writer.append("--" + boundary).append(CRLF);
				writer.append("Content-Disposition: form-data; name=\"action-szamla_agent_nyugta_create\"; filename=\"xml.xml\"").append(CRLF);
				writer.append("Content-Type: text/xml; charset=utf-8").append(CRLF); // Text file itself must be saved in this charset!
				writer.append(CRLF).flush();
				writer.append(requestBody).flush();
				writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
				
				// End of multipart/form-data.
				writer.append("--" + boundary + "--").append(CRLF).flush();

				writer.close();
			}
			
			if(JSESSIONID == null) {
				try{
					Map<String,List<String>> headerFields = connection.getHeaderFields();
					
					Utils.log(method, "header: " + headerFields);
					
					List<String> headerField = headerFields.get("Set-Cookie");
					
					Utils.log(method, "headerField: " + headerField);
					
		        	for(String cookie: headerField){
			        	Utils.log(method, "cookie: " + cookie);
			        	if(cookie.contains(SESSIONLABEL)) {
			        		int sessionindex = cookie.lastIndexOf(SESSIONLABEL) + SESSIONLABEL.length();
			        		String sessionId = cookie.substring(sessionindex, cookie.indexOf(";", sessionindex)); //JSESSIONID=
			        		Utils.log(method, "sessionId: " + sessionId);
				        
			        		JSESSIONID = sessionId;
			        	}
		        	}
		        }catch(Exception ex) {
		        	Utils.log(method, "error during sessionId retreaval: " + ex);
		        }
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
			
			Utils.store(content.toString());
			
			XmlNyugtaValasz body = unmashal(content.toString(), XmlNyugtaValasz.class);
			
			connection.disconnect();
		        
	        Utils.log(method, "resolved " + body);
	        
	        return body;
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
