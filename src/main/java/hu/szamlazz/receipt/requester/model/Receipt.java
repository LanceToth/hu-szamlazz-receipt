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
//@XmlRootElement(name = "fejlec")
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	/**
	 * Nem kell adatbázisban tárolni, csak a kérés beküldésekor adjuk meg (bár kb
	 * mindig igen lesz az értéke)
	 */
	@Transient
	private Boolean pdfLetoltes = true;

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
	
	@Transient
	Double brutto;

	@OneToMany(mappedBy = "receipt")
	private Set<Item> items = new HashSet<Item>();

	@OneToMany(mappedBy = "receipt")
	private Set<Payment> payments = new HashSet<Payment>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getPdfLetoltes() {
		return pdfLetoltes;
	}

	public void setPdfLetoltes(Boolean pdfLetoltes) {
		this.pdfLetoltes = pdfLetoltes;
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

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public LocalDate getKelt() {
		return kelt;
	}

	public void setKelt(LocalDate kelt) {
		this.kelt = kelt;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * kiszámolja a tételek összegét
	 * 
	 * @return tételösszeg
	 */
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
		return "Receipt [id=" + id + ", fizmod=" + fizmod + ", penznem=" + penznem + ", kelt=" + kelt + "]";
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
