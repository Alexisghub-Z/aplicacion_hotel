package com.hotel.reservation.controller;

import com.hotel.reservation.dto.PaymentDTO;
import com.hotel.reservation.models.PaymentMethod;
import com.hotel.reservation.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByReservation(@PathVariable Long reservationId) {
        return ResponseEntity.ok(paymentService.getPaymentsByReservation(reservationId));
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(
            @RequestParam Long reservationId,
            @RequestParam PaymentMethod paymentMethod) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.processPayment(reservationId, paymentMethod));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<PaymentDTO> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.refundPayment(id));
    }
}
