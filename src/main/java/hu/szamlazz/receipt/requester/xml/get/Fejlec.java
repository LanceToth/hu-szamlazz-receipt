package hu.szamlazz.receipt.requester.xml.get;

import javax.xml.bind.annotation.XmlElement;

public class Fejlec {

	@XmlElement(name = "nyugtaszam")
	String nyugtaszam;
	
	public Fejlec(String nyugtaszam) {
		super();
		this.nyugtaszam = nyugtaszam;
	}

	public Fejlec() {
	}
	
}
