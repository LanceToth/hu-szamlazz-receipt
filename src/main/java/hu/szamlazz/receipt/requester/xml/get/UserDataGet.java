package hu.szamlazz.receipt.requester.xml.get;

import javax.xml.bind.annotation.XmlRootElement;

import hu.szamlazz.receipt.requester.model.UserData;

//@XmlRootElement(namespace = "http://www.szamlazz.hu/xmlnyugtaget")
@XmlRootElement(name = "beallitasok")
public class UserDataGet {
	
	UserData userData;
	
	public UserDataGet(UserData userData) {
		this.userData = userData;
	}
	
	public UserDataGet(){}

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
