package com.bookmytrain;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @PostMapping("/process") public ResponseEntity<?> process(@RequestBody PaymentRequest r) {
        try {
            PaymentManager pm=new PaymentManager();
            PaymentManager.PaymentMethod method=PaymentManager.PaymentMethod.valueOf(r.method());
            PaymentManager.PaymentRequest p=new PaymentManager.PaymentRequest(r.bookingId(),r.amount(),method);
            if(r.cardNumber()!=null) p.setCardDetails(r.cardNumber(),r.cardHolderName(),r.expiryDate(),r.cvv());
            if(r.upiId()!=null) p.setUpiId(r.upiId());
            PaymentManager.PaymentResult x=pm.processPayment(p);
            return ResponseEntity.ok(Map.of("success",x.isSuccess(),"transactionId",x.getTransactionId()==null?"":x.getTransactionId(),"message",x.getMessage(),"status",x.getStatus().toString(),"paymentId",x.getPaymentId()));
        } catch(Exception e){return ResponseEntity.internalServerError().body(Map.of("success",false,"message",e.getMessage()==null?"Payment error":e.getMessage()));}
    }
    public record PaymentRequest(int bookingId,BigDecimal amount,String method,String cardNumber,String cardHolderName,String expiryDate,String cvv,String upiId){}
}
