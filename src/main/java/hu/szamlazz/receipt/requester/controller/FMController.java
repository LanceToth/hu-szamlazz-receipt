package hu.szamlazz.receipt.requester.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class FMController {
	
    //RECEIPT
	
    @Autowired
    private ReceiptRepository receiptRepository;
    
    @RequestMapping("/list")
    public String getAllReceipts(Model model) {
    	model.addAttribute("receipts", receiptRepository.findAll());
        return "list";
    }
    
    @RequestMapping("/addreceipt")
    public String emptyReceipt(Model model) {
    	model.addAttribute("pdfSablonList", PdfSablon.values());
    	model.addAttribute("fizmodList", Fizmod.values());
    	model.addAttribute("receipt", new Receipt());
        return "receipt";
    }
    
    @PostMapping("/savereceipt")
	public String saveReceipt(Model model, Receipt receipt) {
		receipt = receiptRepository.save(receipt);
		
		return "redirect:/receipt/" + receipt.getId();
	}
    
    @RequestMapping("/receipt/{id}")
    public String getReceipt(Model model, @PathVariable(value = "id") Long receiptId) {
    	if(receiptId == null) {
    		return "addreceipt";
    	}
    	Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
        //return ResponseEntity.ok().body(phone);
        model.addAttribute("pdfSablonList", PdfSablon.values());
        model.addAttribute("fizmodList", Fizmod.values());
        model.addAttribute("receipt", receipt);
        return "receipt";
    }
    
    @PostMapping("/sendreceipt/{id}")
	public String sendReceipt(Model model, @PathVariable(value = "id") Long receiptId) {
    	Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
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
    	Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
		//esponse = sendequesttoseve
    	
    	//show nyugtaPdf
	}
    
    //ITEM
    
    @Autowired
    private ItemRepository itemRepository;
    
    @RequestMapping("/additem/{id}")
    public String addItem(Model model, @PathVariable(value = "id") Long receiptId) {
    	Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
    	Item item = new Item();
    	item.setReceipt(receipt);
    	
        model.addAttribute("item", item);
        return "item";
    }
    
    @PostMapping("/saveitem")
	public String saveItem(Model model, Item item) {
		item = itemRepository.save(item);
		
		return "redirect:/receipt/" + item.getReceipt().getId();
	}
    
    @RequestMapping("/item/{id}")
    public String getItem(Model model, @PathVariable(value = "id") Long itemId) {
    	Item item = itemRepository.findById(itemId).orElse(new Item());
        //return ResponseEntity.ok().body(phone);
        model.addAttribute("item", item);
        return "item";
    }

    //PAYMENT
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @RequestMapping("/addpayment/{id}")
    public String addPayment(Model model, @PathVariable(value = "id") Long receiptId) {
    	Receipt receipt = receiptRepository.findById(receiptId).orElse(new Receipt());
    	Payment payment = new Payment();
    	payment.setReceipt(receipt);
    	
        model.addAttribute("fizmodList", Fizmod.values());
        model.addAttribute("payment", payment);
        return "payment";
    }
    
    @PostMapping("/savepayment")
	public String savePayment(Model model, Payment payment) {
		payment = paymentRepository.save(payment);
		
		return "redirect:/receipt/" + payment.getReceipt().getId();
	}
    
    @RequestMapping("/payment/{id}")
    public String getPayment(Model model, @PathVariable(value = "id") Long paymentId) {
    	Payment payment = paymentRepository.findById(paymentId).orElse(new Payment());
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
		List<UserData> userDataList = userDataRepository.findAll();
		
		UserData userData = null;
		
		if(userDataList.isEmpty()){
			userData = new UserData();
		}else {
			userData = userDataList.get(0);
		}
		
		model.addAttribute("userdata", userData);
		
        return "userdata";
    }
	
	@RequestMapping("/setuserdata")
    public String setUserData(Model model, UserData userData) {
		userDataRepository.save(userData);
		
		return "redirect:/userdata/";
	}
	
}
