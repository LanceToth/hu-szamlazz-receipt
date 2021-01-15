package hu.szamlazz.receipt.requester.xml.create;

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import hu.szamlazz.receipt.requester.model.Item;
import hu.szamlazz.receipt.requester.model.Payment;
import hu.szamlazz.receipt.requester.model.Receipt;

@XmlRootElement(name = "xmlnyugtacreate", namespace = "http://www.szamlazz.hu/xmlnyugtacreate")
@XmlType(propOrder = { "beallitasok", "fejlec", "tetelek", "kifizetesek" })
public class XmlNyugtaCreate {
	
	@XmlElement(name = "fejlec")
	Receipt fejlec;
	
	@XmlElement(name = "beallitasok")
	UserDataCreate beallitasok;
	
	@XmlElement(name = "tetel")
	@XmlElementWrapper(name = "tetelek")
	public Set<Item> getTetelek() {
		return fejlec.getItems();
	}
	
	@XmlElement(name = "kifizetes")
	@XmlElementWrapper(name = "kifizetesek")
	public Set<Payment> getKifizetesek() {
		return fejlec.getPayments();
	}

	public XmlNyugtaCreate(Receipt receipt, UserDataCreate userdata) {
		this.fejlec = receipt;
		this.beallitasok = userdata;
	}

	public XmlNyugtaCreate() {
	}
}
