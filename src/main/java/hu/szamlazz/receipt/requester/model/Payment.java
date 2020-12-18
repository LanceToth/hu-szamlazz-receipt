package hu.szamlazz.receipt.requester.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import hu.szamlazz.receipt.requester.model.Receipt.Fizmod;

/**
 * Nyugta kifizetéseit tároló osztály
 * 
 * @author Lance
 *
 */
@Entity
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "fizetoeszkoz", nullable = false)
	private Fizmod fizetoeszkoz = Fizmod.készpénz;

	@Column(name = "osszeg", nullable = false)
	private Double osszeg;

	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Fizmod getFizetoeszkoz() {
		return fizetoeszkoz;
	}

	public void setFizetoeszkoz(Fizmod fizetoeszkoz) {
		this.fizetoeszkoz = fizetoeszkoz;
	}

	public Double getOsszeg() {
		return osszeg;
	}

	public void setOsszeg(Double osszeg) {
		this.osszeg = osszeg;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	@Override
	public String toString() {
		return "Payment [id=" + id + ", fizetoeszkoz=" + fizetoeszkoz + ", osszeg=" + osszeg + "]";
	}

}
