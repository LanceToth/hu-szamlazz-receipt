package hu.szamlazz.receipt.requester.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.SortNatural;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 
 * Nyugta tárolására szolgáló osztály, valamint ide kerültek a használathoz
 * szükséges enum-ok is
 * 
 * @author Lance
 */

@Entity
@Table(name = "receipt")
@EntityListeners(AuditingEntityListener.class)
@XmlRootElement(name = "fejlec")
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "elotag", nullable = false)
	private String elotag;

	@Column(name = "fizmod", nullable = false)
	private Fizmod fizmod = Fizmod.készpénz;

	@Column(name = "penznem", nullable = false)
	private String penznem = "HUF";

	@Column(name = "pdf_sablon", nullable = false)
	private PdfSablon pdfSablon = PdfSablon.A;

	@Column(name = "kelt", nullable = false)
	private LocalDate kelt = LocalDate.now();
	
	@Column(name = "status", nullable = false)
	private Status status = Status.F;
	
	@Column(name = "nyugtaszam")
	private String nyugtaszam;
	
	@Transient
	Double brutto;

	@OneToMany(mappedBy = "receipt")
	@SortNatural
	@OrderBy(clause = "id ASC")
	private Set<Item> items = new HashSet<Item>();

	@OneToMany(mappedBy = "receipt")
	@SortNatural
	@OrderBy(clause = "id ASC")
	private Set<Payment> payments = new HashSet<Payment>();

	@XmlTransient
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getElotag() {
		return elotag;
	}

	public void setElotag(String elotag) {
		this.elotag = elotag;
	}

	public Fizmod getFizmod() {
		return fizmod;
	}

	public void setFizmod(Fizmod fizmod) {
		this.fizmod = fizmod;
	}

	public String getPenznem() {
		return penznem;
	}

	public void setPenznem(String penznem) {
		this.penznem = penznem;
	}

	public PdfSablon getPdfSablon() {
		return pdfSablon;
	}

	public void setPdfSablon(PdfSablon pdfSablon) {
		this.pdfSablon = pdfSablon;
	}

	@XmlTransient
	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public boolean addItem(Item e) {
		if(items != null) {
			return items.add(e);
		}
		return false;
	}

	@XmlTransient
	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public boolean addPayment(Payment e) {
		if(payments != null) {
		return payments.add(e);
		}
		return false;
	}

	@XmlTransient
	public LocalDate getKelt() {
		return kelt;
	}

	public void setKelt(LocalDate kelt) {
		this.kelt = kelt;
	}

	@XmlTransient
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getNyugtaszam() {
		return nyugtaszam;
	}

	public void setNyugtaszam(String nyugtaszam) {
		this.nyugtaszam = nyugtaszam;
	}

	public void copy(Receipt source) {
		this.elotag = source.elotag;
		this.fizmod = source.fizmod;
		this.penznem = source.penznem;
		this.pdfSablon = source.pdfSablon;
	}

	/**
	 * kiszámolja a tételek összegét
	 * 
	 * @return tételösszeg
	 */
	@XmlTransient
	public Double getBrutto() {
		brutto = 0d;
		for (Item item : items) {
			brutto += item.getBrutto();
		}
		return brutto;
	}

	/**
	 * kiszámolja a befizetések összegét
	 * 
	 * @return fizetésösszeg
	 */
	public Double getFizetesOsszeg() {
		Double fizetesOsszeg = 0d;
		for (Payment payment : payments) {
			fizetesOsszeg += payment.getOsszeg();
		}
		return fizetesOsszeg;
	}

	/**
	 * ellenőrzi, megfelel-e a feltételeknek a nyugta
	 * 
	 * @return helyes-e az összeg
	 */
	public Boolean validate() {
		if (getBrutto() != getFizetesOsszeg()) {
			// nincs teljesen kifizetve, vagy túlfizetés
			return false;
		}

		if (getBrutto() > 900000d) {
			// nyugta max értéke
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Receipt [id=").append(id)
		.append(", fizmod=").append(fizmod)
		.append(", penznem=").append(penznem)
		.append(", kelt=").append(kelt)
		.append(", items=").append(items.size())
		.append(", payments=").append(payments.size())
		.append("]");
		return builder.toString();
	}

	public enum PdfSablon {
		A("A4"), J("keskeny"), L("logoval");

		PdfSablon(String description) {
			this.description = description;
		}

		private final String description;

		public String getDescription() {
			return description;
		}
	}

	public enum Fizmod {
		átutalás, készpénz, bankkártya, csekk, utánvét, ajándékutalvány, barion, barter, csoportos_beszedés, OTP_Simple,
		kompenzáció, kupon, PayPal, PayU, SZÉP_kártya, utalvány;

		public String getXmlString() {
			return this.name().replace("_", " ");
		}
	}
	
	public enum Status{
		F("folyamatban"), S("siker"), H("hiba");
		
		Status(String description){this.description = description;
		}

		private final String description;

		public String getDescription() {
			return description;
		}
	}
}
