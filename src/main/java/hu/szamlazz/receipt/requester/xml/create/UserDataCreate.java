package hu.szamlazz.receipt.requester.xml.create;

import javax.xml.bind.annotation.XmlRootElement;

import hu.szamlazz.receipt.requester.model.UserData;

@XmlRootElement(namespace = "http://www.szamlazz.hu/xmlnyugtacreate")
public class UserDataCreate extends UserData {
	
	public UserDataCreate(UserData userData) {
		this.setId(userData.getId());
		this.setPdfLetoltes(userData.getPdfLetoltes());
		this.setSzamlaagentkulcs(userData.getSzamlaagentkulcs());
	}
	
	public UserDataCreate(){}

}
