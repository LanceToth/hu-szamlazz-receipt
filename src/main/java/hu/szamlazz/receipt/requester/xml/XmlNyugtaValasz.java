package hu.szamlazz.receipt.requester.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xmlnyugtavalasz"/*, namespace = "http://www.szamlazz.hu/xmlnyugtavalasz"*/)
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNyugtaValasz {

//	@XmlElement(name = "sikeres")
	private Boolean sikeres;
	
//	@XmlElement(name = "hibakod")
	private Integer hibakod;
	
//	@XmlElement(name = "hibauzenet")
	private String hibauzenet;
	
//	@XmlElement(name = "nyugtaPdf")
	private String nyugtaPdf;

	public XmlNyugtaValasz() {
		super();
	}

	public Boolean getSikeres() {
		return sikeres;
	}

	public void setSikeres(Boolean sikeres) {
		this.sikeres = sikeres;
	}

	public Integer getHibakod() {
		return hibakod;
	}

	public void setHibakod(Integer hibakod) {
		this.hibakod = hibakod;
	}

	public String getHibauzenet() {
		return hibauzenet;
	}

	public void setHibauzenet(String hibauzenet) {
		this.hibauzenet = hibauzenet;
	}

	public String getNyugtaPdf() {
		return nyugtaPdf;
	}

	public void setNyugtaPdf(String nyugtaPdf) {
		this.nyugtaPdf = nyugtaPdf;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("XmlNyugtaValasz [sikeres=").append(sikeres)
			.append(", hibakod=").append(hibakod)
			.append(", hibauzenet=").append(hibauzenet);
		if(nyugtaPdf != null) {
			builder.append(", nyugtaPdf=").append(nyugtaPdf.length());
		}
		builder.append("]");
		return builder.toString();
	}
}
