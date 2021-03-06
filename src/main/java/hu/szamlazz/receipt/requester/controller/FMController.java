package hu.szamlazz.receipt.requester.controller;

import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import hu.szamlazz.receipt.requester.model.Item;
import hu.szamlazz.receipt.requester.model.ItemRepository;
import hu.szamlazz.receipt.requester.model.Payment;
import hu.szamlazz.receipt.requester.model.PaymentRepository;
import hu.szamlazz.receipt.requester.model.Receipt;
import hu.szamlazz.receipt.requester.model.Receipt.Fizmod;
import hu.szamlazz.receipt.requester.model.Receipt.PdfSablon;
import hu.szamlazz.receipt.requester.model.Receipt.Status;
import hu.szamlazz.receipt.requester.model.ReceiptRepository;
import hu.szamlazz.receipt.requester.model.UserData;
import hu.szamlazz.receipt.requester.model.UserDataRepository;
import hu.szamlazz.receipt.requester.xml.RequestHandler;
import hu.szamlazz.receipt.requester.xml.XmlNyugtaValasz;
import hu.szamlazz.receipt.requester.xml.create.XmlNyugtaCreate;
import hu.szamlazz.receipt.requester.xml.get.XmlNyugtaGet;

@Controller
public class FMController {
	
    //RECEIPT
	
    @Autowired
    private ReceiptRepository receiptRepository;
    
    @RequestMapping("/list")
    public String getAllReceipts(Model model) {
    	String method = "getAllReceipts";
    	Utils.log(method, "started");
    	
    	Utils.log(method, "model is " + model.toString());
    	Utils.log(method, "model is " + model.asMap().toString());
    	
    	List<Receipt> list = receiptRepository.findAll(Sort.by("id"));
    	Utils.log(method, "loaded " + list.size());
    	
    	model.addAttribute("receipts", list);
        return "list";
    }
    
    @RequestMapping("/addreceipt")
    public String emptyReceipt(Model model) {
    	String method = "emptyReceipt";
    	Utils.log(method, "started");
    	
    	model.addAttribute("pdfSablonList", PdfSablon.values());
    	model.addAttribute("fizmodList", Fizmod.values());
    	model.addAttribute("receipt", new Receipt());
        return "receipt";
    }
    
    @PostMapping("/savereceipt")
	public String saveReceipt(Model model, Receipt receipt) {
    	String method = "saveReceipt";
    	Utils.log(method, "started");
    	
		receipt = receiptRepository.save(receipt);
		Utils.log(method, "saved " + receipt);
		return "redirect:/receipt/" + receipt.getId();
	}
    
    @RequestMapping("/receipt/{id}")
    public String getReceipt(Model model, @PathVariable(value = "id") Long receiptId) throws IllegalArgumentException{
    	String method = "getReceipt";
    	Utils.log(method, "started");
    	
    	Utils.log(method, "model is " + model.toString());
    	Utils.log(method, "model is " + model.asMap().toString());
    	
    	if(receiptId == null) {
    		return "addreceipt";
    	}
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
        
    	Utils.log(method, "loaded " + receipt);
    	
        model.addAttribute("pdfSablonList", PdfSablon.values());
        model.addAttribute("fizmodList", Fizmod.values());
        model.addAttribute("receipt", receipt);
        return "receipt";
    }
    
    @PostMapping("/updatereceipt/{id}")
    public String updateReceipt(Model model, @PathVariable(value="id") Long receiptId, Receipt modifiedReceipt) throws Exception {
    	String method = "updateReceipt";
    	Utils.log(method, "started");
    	
    	Receipt originalReceipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));

    	Utils.log(method, "modifying " + originalReceipt + " via " + modifiedReceipt);
    	
        originalReceipt.copy(modifiedReceipt);

        Receipt updatedReceipt = receiptRepository.save(originalReceipt);
        Utils.log(method, "saved " + updatedReceipt);
        return "redirect:/receipt/" + originalReceipt.getId();
    }
    
    @RequestMapping("/export/{id}")
	public String export(Model model, @PathVariable(value = "id") Long receiptId) {
    	String method = "export";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	Utils.log(method, "loaded " + receipt);
    	
    	XmlNyugtaCreate create = new XmlNyugtaCreate(receipt, getUserData(method));
    	try {
        	String xml = RequestHandler.getInstance().marshal(create);
        	Utils.log(xml);
			model.addAttribute("xml", xml);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return "export";
    }
    
    @RequestMapping("/sendreceipt/{id}")
	public String sendReceipt(Model model, @PathVariable(value = "id") Long receiptId) {
    	String method = "sendReceipt";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	Utils.log(method, "loaded " + receipt);
    	
    	try {
        	XmlNyugtaCreate create = new XmlNyugtaCreate(receipt, getUserData(method));
        	String xml = RequestHandler.getInstance().marshal(create);
			Utils.log(xml);
			
			XmlNyugtaValasz valasz = RequestHandler.getInstance().request(xml, "action-szamla_agent_nyugta_create");
			
			model.addAttribute("sikeres", valasz.getSikeres());
			
			if(valasz.getSikeres()) {
				receipt.setStatus(Status.S);
				receipt.setNyugtaszam(valasz.getNyugtaszam());
				
				model.addAttribute("pdf", valasz.getNyugtaPdf());
			}else {
				receipt.setStatus(Status.H);
				model.addAttribute("hibakod", valasz.getHibakod());
				model.addAttribute("hibauzenet", valasz.getHibauzenet());
			}
			
			receipt = receiptRepository.save(receipt);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
    	return "forward:/receipt/" + receipt.getId();
	}
    
    @RequestMapping("/download/{id}")
	String downloadPdf(Model model, @PathVariable(value = "id") Long receiptId) {
    	String method = "downloadPdf";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	Utils.log(method, "loaded " + receipt);
    	
    	
		try {
			XmlNyugtaGet get = new XmlNyugtaGet(getUserData(method), receipt.getNyugtaszam());
			 String xml = RequestHandler.getInstance().marshal(get);
			 Utils.log(xml);
    	
			XmlNyugtaValasz valasz = RequestHandler.getInstance().request(xml, "action-szamla_agent_nyugta_get");
    	
    		model.addAttribute("pdf", valasz.getNyugtaPdf());
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	
    	return "forward:/list";
	}
    
    //ITEM
    
    @Autowired
    private ItemRepository itemRepository;
    
    @RequestMapping("/additem/{id}")
    public String addItem(Model model, @PathVariable(value = "id") Long receiptId) {
    	String method = "addItem";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	Utils.log(method, "loaded " + receipt);
    	Item item = new Item();
    	item.setReceipt(receipt);
    	
        model.addAttribute("item", item);
        
        return "item";
    }
    
    @PostMapping("/saveitem/{id}")
	public String saveItem(Model model, @PathVariable(value = "id") Long receiptId, Item item) {
    	String method = "saveItem";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
				.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	
    	Utils.log(method, "loaded " + receipt);
    	
    	item.setReceipt(receipt);
    	
		item = itemRepository.save(item);
		
		Utils.log(method, "saved " + item);
		
		return "redirect:/item/" + item.getId();
	}
    
    @RequestMapping("/item/{id}")
    public String getItem(Model model, @PathVariable(value = "id") Long itemId) {
    	String method = "getItem";
    	Utils.log(method, "started");
    	
    	Item item = itemRepository.findById(itemId)
    			.orElseThrow(() -> new IllegalArgumentException("Item " + itemId + " not found"));
        //return ResponseEntity.ok().body(phone);
        model.addAttribute("item", item);
        return "item";
    }
    
    @PostMapping("/updateitem/{id}")
    public String updateItem(Model model, @PathVariable(value="id") Long itemId, Item modifiedItem) throws Exception {
    	String method = "updateItem";
    	Utils.log(method, "started");
    	
    	Item originalItem = itemRepository.findById(itemId)
    			.orElseThrow(() -> new IllegalArgumentException("Item " + itemId + " not found"));

    	Utils.log(method, "modifying " + originalItem + " via " + modifiedItem);
    	
        originalItem.copy(modifiedItem);

        Item updatedItem = itemRepository.save(originalItem);
        Utils.log(method, "saved " + updatedItem);
        return "redirect:/item/" + originalItem.getId();
    }

    //PAYMENT
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @RequestMapping("/addpayment/{id}")
    public String addPayment(Model model, @PathVariable(value = "id") Long receiptId) {
    	String method = "addPayment";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	Utils.log(method, "loaded " + receipt);
    	Payment payment = new Payment();
    	payment.setReceipt(receipt);
    	
        model.addAttribute("fizmodList", Fizmod.values());
        model.addAttribute("payment", payment);
        return "payment";
    }
    
    @PostMapping("/savepayment/{id}")
	public String savePayment(Model model, @PathVariable(value = "id") Long receiptId, Payment payment) {
    	String method = "savePayment";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
				.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	
    	Utils.log(method, "loaded " + receipt);
    	
    	payment.setReceipt(receipt);
    	
		payment = paymentRepository.save(payment);
		Utils.log(method, "saved " + payment);
		
		return "redirect:/payment/" + payment.getId();
	}
    
    @RequestMapping("/payment/{id}")
    public String getPayment(Model model, @PathVariable(value = "id") Long paymentId) {
    	String method = "getPayment";
    	Utils.log(method, "started");
    	
    	Payment payment = paymentRepository.findById(paymentId)
    			.orElseThrow(() -> new IllegalArgumentException("Payment " + paymentId + " not found"));
        //return ResponseEntity.ok().body(phone);
        model.addAttribute("fizmodList", Fizmod.values());
        model.addAttribute("payment", payment);
        return "payment";
    }
    
    @PostMapping("/updatepayment/{id}")
    public String updatePayment(Model model, @PathVariable(value="id") Long paymentId, Payment modifiedPayment) throws Exception {
    	String method = "updatePayment";
    	Utils.log(method, "started");
    	
    	Payment originalPayment = paymentRepository.findById(paymentId)
    			.orElseThrow(() -> new IllegalArgumentException("Payment " + paymentId + " not found"));

    	Utils.log(method, "modifying " + originalPayment + " via " + modifiedPayment);
    	
        originalPayment.copy(modifiedPayment);

        Payment updatedPayment = paymentRepository.save(originalPayment);
        Utils.log(method, "saved " + updatedPayment);
        return "redirect:/payment/" + originalPayment.getId();
    }
    
    //USERDATA
    
    @Autowired
    private UserDataRepository userDataRepository;
    
	@RequestMapping("/userdata")
    public String getUserData(Model model) {
		String method = "getUserData";
    	Utils.log(method, "started");
		model.addAttribute("userdata", getUserData(method));
        return "userdata";
    }
	
	public UserData getUserData(String method) {
		List<UserData> userDataList = userDataRepository.findAll();
		
		UserData userData = null;
		
		if(userDataList.isEmpty()){
			userData = new UserData();
		}else {
			userData = userDataList.get(0);
		}
		
		Utils.log(method, "prepared " + userData);
		
		return userData;
	}
	
	@PostMapping("/setuserdata")
    public String setUserData(Model model, UserData modifiedUserData) {
		String method = "setUserData";
    	Utils.log(method, "started");
		
		UserData originalUserData = getUserData(method);
		
		Utils.log(method, "modifying " + originalUserData + " via " + modifiedUserData);
		
		originalUserData.setSzamlaagentkulcs(modifiedUserData.getSzamlaagentkulcs());
		
		userDataRepository.save(originalUserData);
		
		return "redirect:/userdata/";
	}
	
}
