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
import javax.persistence.Transient;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * Nyugta tételeit tároló osztály. A végösszegeket számítással kapjuk, nem
 * tároljuk
 * 
 * @author Lance
 */

@Entity
@Table(name = "item")
@EntityListeners(AuditingEntityListener.class)
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "megnevezes", nullable = false)
	private String megnevezes;

	@Column(name = "mennyiseg", nullable = false)
	private Double mennyiseg = 1d;

	@Column(name = "egyseg", nullable = false)
	private String mennyisegiEgyseg = "db";

	@Column(name = "egysegar", nullable = false)
	private Double nettoEgysegar;

	@Column(name = "afakulcs", nullable = false)
	private Double afakulcs = 27d;

	@Transient
	private Double netto;

	@Transient
	private Double afa;

	@Transient
	private Double brutto;

	@ManyToOne
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMegnevezes() {
		return megnevezes;
	}

	public void setMegnevezes(String megnevezes) {
		this.megnevezes = megnevezes;
	}

	public Double getMennyiseg() {
		return mennyiseg;
	}

	public void setMennyiseg(Double mennyiseg) {
		this.mennyiseg = mennyiseg;
		calculate();
	}

	public String getMennyisegiEgyseg() {
		return mennyisegiEgyseg;
	}

	public void setMennyisegiEgyseg(String mennyisegiEgyseg) {
		this.mennyisegiEgyseg = mennyisegiEgyseg;
	}

	public Double getNettoEgysegar() {
		return nettoEgysegar;
	}

	public void setNettoEgysegar(Double nettoEgysegar) {
		this.nettoEgysegar = nettoEgysegar;
		calculate();
	}

	public Double getAfakulcs() {
		return afakulcs;
	}

	public void setAfakulcs(Double afakulcs) {
		this.afakulcs = afakulcs;
		calculate();
	}

	public Double getNetto() {
		if(netto == null) {
			calculate();
		}
		return netto;
	}

	public void setNetto(Double netto) {
		this.netto = netto;
	}

	public Double getAfa() {
		if(afa == null) {
			calculate();
		}
		return afa;
	}

	public void setAfa(Double afa) {
		this.afa = afa;
	}

	public Double getBrutto() {
		if(brutto == null) {
			calculate();
		}
		return brutto;
	}

	public void setBrutto(Double brutto) {
		this.brutto = brutto;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	/**
	 * Kiszámolja a tétel értékét a mennyiség, az egységár és az áfakulcs alapján
	 */
	private void calculate() {
		if (mennyiseg != null && nettoEgysegar != null && afakulcs != null) {
			netto = mennyiseg * nettoEgysegar;
			afa = netto * afakulcs / 100;
			brutto = netto + afa;
		}
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", megnevezes=" + megnevezes + ", mennyiseg=" + mennyiseg + ", nettoEgysegar="
				+ nettoEgysegar + "]";
	}

}
