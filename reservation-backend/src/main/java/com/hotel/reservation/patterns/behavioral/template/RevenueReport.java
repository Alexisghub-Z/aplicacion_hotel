package com.hotel.reservation.patterns.behavioral.template;

import com.hotel.reservation.models.Payment;
import com.hotel.reservation.models.PaymentStatus;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import com.hotel.reservation.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Reporte de ingresos
 */
@RequiredArgsConstructor
public class RevenueReport extends ReportTemplate {

    private final PaymentRepository paymentRepository;

    @Override
    protected String getReportName() {
        return "REPORTE DE INGRESOS";
    }

    @Override
    protected String generateBody() {
        ConfigurationManager config = ConfigurationManager.INSTANCE;
        String currency = config.getCurrency();

        List<Payment> payments = paymentRepository.findAll();

        if (payments.isEmpty()) {
            return "No hay pagos registrados.\n";
        }

        StringBuilder body = new StringBuilder();

        BigDecimal totalRevenue = payments.stream()
            .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingRevenue = payments.stream()
            .filter(p -> p.getPaymentStatus() == PaymentStatus.PENDING)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal refundedAmount = payments.stream()
            .filter(p -> p.getPaymentStatus() == PaymentStatus.REFUNDED)
            .map(Payment::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        body.append(String.format("Total de pagos registrados: %d\n\n", payments.size()));

        body.append("Resumen financiero:\n");
        body.append(String.format("  💰 Ingresos completados: $%.2f %s\n", totalRevenue, currency));
        body.append(String.format("  ⏳ Ingresos pendientes: $%.2f %s\n", pendingRevenue, currency));
        body.append(String.format("  ↩️  Reembolsos: $%.2f %s\n\n", refundedAmount, currency));

        body.append("Desglose por método de pago:\n");
        body.append("-".repeat(60)).append("\n");

        payments.stream()
            .filter(p -> p.getPaymentStatus() == PaymentStatus.COMPLETED)
            .collect(java.util.stream.Collectors.groupingBy(Payment::getPaymentMethod))
            .forEach((method, paymentList) -> {
                BigDecimal methodTotal = paymentList.stream()
                    .map(Payment::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                body.append(String.format("%s: %d pagos - $%.2f %s\n",
                    method, paymentList.size(), methodTotal, currency));
            });

        body.append("-".repeat(60)).append("\n");

        return body.toString();
    }
}
