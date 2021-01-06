package hu.szamlazz.receipt.requester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 
 * Agent kulcs tárolására szolgáló osztály
 * 
 * @author Lance
 */
@Entity
@Table(name = "userdata")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement(name = "beallitasok")
public class UserData {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(name = "agent_key", nullable = false)
	private String szamlaagentkulcs;

	/**
	 * Nem kell adatbázisban tárolni, csak a kérés beküldésekor adjuk meg (bár kb
	 * mindig igen lesz az értéke)
	 */
	@Transient
	private Boolean pdfLetoltes = true;
	
	@XmlTransient
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSzamlaagentkulcs() {
		return szamlaagentkulcs;
	}

	public void setSzamlaagentkulcs(String agentKey) {
		this.szamlaagentkulcs = agentKey;
	}

	public Boolean getPdfLetoltes() {
		return pdfLetoltes;
	}

	public void setPdfLetoltes(Boolean pdfLetoltes) {
		this.pdfLetoltes = pdfLetoltes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UserData [id=").append(id)
		.append(", szamlaagentkulcs=").append(szamlaagentkulcs)
		.append(", pdfLetoltes=").append(pdfLetoltes)
		.append("]");
		return builder.toString();
	}
}
