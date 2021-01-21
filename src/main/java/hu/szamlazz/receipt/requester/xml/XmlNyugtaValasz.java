package hu.szamlazz.receipt.requester.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "xmlnyugtavalasz")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlNyugtaValasz {

	private Boolean sikeres;
	
	private Integer hibakod;
	
	private String hibauzenet;
	
	private String nyugtaPdf;
	
	private Nyugta nyugta;

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

	public Nyugta getNyugta() {
		return nyugta;
	}

	public void setNyugta(Nyugta nyugta) {
		this.nyugta = nyugta;
	}
	
	public String getNyugtaszam() {
		if(nyugta != null) {
			return nyugta.getNyugtaszam();
		}
		return null;
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
		if(nyugta != null && nyugta.getAlap() != null && nyugta.getAlap().getNyugtaszam() != null) {
			builder.append(", nyugtaszam=").append(nyugta.getAlap().getNyugtaszam());
		}
		builder.append("]");
		return builder.toString();
	}
	
	@XmlRootElement(name = "nyugta")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Nyugta{
		Alap alap;

		public Alap getAlap() {
			return alap;
		}

		public void setAlap(Alap alap) {
			this.alap = alap;
		}

		public String getNyugtaszam() {
			if(alap != null) {
				return alap.getNyugtaszam();
			}
			return null;
		}
		
	}
	
	@XmlRootElement(name = "alap")
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Alap{
		String nyugtaszam;

		public String getNyugtaszam() {
			return nyugtaszam;
		}

		public void setNyugtaszam(String nyugtaszam) {
			this.nyugtaszam = nyugtaszam;
		}
	}
}
