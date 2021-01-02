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
import hu.szamlazz.receipt.requester.model.ReceiptRepository;
import hu.szamlazz.receipt.requester.model.UserData;
import hu.szamlazz.receipt.requester.model.UserDataRepository;
import hu.szamlazz.receipt.requester.model.XmlNyugtaCreate;

@Controller
public class FMController {
	
    //RECEIPT
	
    @Autowired
    private ReceiptRepository receiptRepository;
    
    @RequestMapping("/list")
    public String getAllReceipts(Model model) {
    	String method = "getAllReceipts";
    	Utils.log(method, "started");
    	
    	model.addAttribute("receipts", receiptRepository.findAll(Sort.by("id")));
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
    
    @PostMapping("/sendreceipt/{id}")
	public String sendReceipt(Model model, @PathVariable(value = "id") Long receiptId) {
    	Receipt receipt = receiptRepository.findById(receiptId)
    			.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
		//response = sendxmltoserver
		
		/*Boolean sikeres = false; //= response.getBoolean("sikeres"); 
		
		if(sikeres) {
			receipt.setStatus(Status.S);
		}else {
			receipt.setStatus(Status.H);
		}
		
		receipt = receiptRepository.save(receipt);*/
		
		//show nyugtaPdf
		
		return "redirect:/receipt/" + receipt.getId();
	}
    
    @RequestMapping("/download/{id}")
	void downloadPdf(Model model, @PathVariable(value = "id") Long receiptId) {
    	//Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
		//esponse = sendequesttoseve
    	
    	//show nyugtaPdf
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
        
        try {
        	XmlNyugtaCreate xml = new XmlNyugtaCreate(receipt, getUserData());
			Utils.log(RequestHandler.getInstance().mashal(xml));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
        
        return "item";
    }
    
    @PostMapping("/saveitem/{id}")
	public String saveItem(Model model, @PathVariable(value = "id") Long receiptId, Item item) {
    	String method = "saveItem";
    	Utils.log(method, "started");
    	
    	Receipt receipt = receiptRepository.findById(receiptId)
				.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    	
    	Utils.log(method, "loaded " + receipt);
    	
    	/*if(item.getReceipt() != null) {
    		Long  receiptId = item.getReceipt().getId();
    		Receipt receipt = receiptRepository.findById(receiptId)
    				.orElseThrow(() -> new IllegalArgumentException("Receipt " + receiptId + " not found"));
    		item.setReceipt(receipt);
    	}else {
    		throw new IllegalArgumentException("Receipt not found");
    	}*/
    	
    	item.setReceipt(receipt);
    	
    	receipt.addItem(item);
		//item = itemRepository.save(item);
    	receipt = receiptRepository.save(receipt);
		
		Utils.log(method, "saved " + item);
		
		return "redirect:/receipt/" + item.getReceipt().getId();
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
    
    @PostMapping("/savepayment")
	public String savePayment(Model model, Payment payment) {
    	String method = "savePayment";
    	Utils.log(method, "started");
    	
		payment = paymentRepository.save(payment);
		Utils.log(method, "saved " + payment);
		
		return "redirect:/receipt/" + payment.getReceipt().getId();
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
        return "receipt";
    }
    
    //USERDATA
    
    @Autowired
    private UserDataRepository userDataRepository;
    
	@RequestMapping("/userdata")
    public String getUserData(Model model) {
		model.addAttribute("userdata", getUserData());
        return "userdata";
    }
	
	public UserData getUserData() {
		List<UserData> userDataList = userDataRepository.findAll();
		
		UserData userData = null;
		
		if(userDataList.isEmpty()){
			userData = new UserData();
		}else {
			userData = userDataList.get(0);
		}
		
		return userData;
	}
	
	@RequestMapping("/setuserdata")
    public String setUserData(Model model, UserData userData) {
		userDataRepository.save(userData);
		
		return "redirect:/userdata/";
	}
	
}
