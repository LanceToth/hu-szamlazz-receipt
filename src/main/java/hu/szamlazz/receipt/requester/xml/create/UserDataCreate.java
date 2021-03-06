package hu.szamlazz.receipt.requester.xml.create;

import javax.xml.bind.annotation.XmlRootElement;

import hu.szamlazz.receipt.requester.model.UserData;

//@XmlRootElement(namespace = "http://www.szamlazz.hu/xmlnyugtacreate")
@XmlRootElement(name = "beallitasok")
public class UserDataCreate {
	
	UserData userData;
	
	public UserDataCreate(UserData userData) {
		this.userData = userData;
	}
	
	public UserDataCreate(){}

	public String getSzamlaagentkulcs() {
		return userData.getSzamlaagentkulcs();
	}

	public void setSzamlaagentkulcs(String agentKey) {
		userData.setSzamlaagentkulcs(agentKey);
	}

	public Boolean getPdfLetoltes() {
		return userData.getPdfLetoltes();
	}

	public void setPdfLetoltes(Boolean pdfLetoltes) {
		userData.setPdfLetoltes(pdfLetoltes);
	}

}
