package hu.szamlazz.receipt.requester.xml.get;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import hu.szamlazz.receipt.requester.model.UserData;

@XmlRootElement(name = "xmlnyugtaget")
@XmlType(propOrder = { "beallitasok", "fejlec" })
public class XmlNyugtaGet {
	
	@XmlElement(name = "beallitasok")
	UserDataGet beallitasok;
	
	@XmlElement(name = "fejlec")
	Fejlec fejlec;

	public XmlNyugtaGet(UserData beallitasok, String nyugtaszam) {
		this.beallitasok = new UserDataGet(beallitasok);
		this.fejlec = new Fejlec(nyugtaszam);
	}

	public XmlNyugtaGet() {
	}

}
