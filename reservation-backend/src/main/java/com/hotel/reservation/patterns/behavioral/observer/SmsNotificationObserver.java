package com.hotel.reservation.patterns.behavioral.observer;

import com.hotel.reservation.models.Reservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Observer para enviar notificaciones por SMS
 * Implementa el patrón Observer para notificaciones SMS
 */
@Slf4j
@Component
public class SmsNotificationObserver implements ReservationObserver {

    @Override
    public void onReservationCreated(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String message = String.format(
            "Hotel Oaxaca Dreams: Su reserva #%d ha sido creada exitosamente. " +
            "Check-in: %s. Gracias por elegirnos!",
            reservation.getId(),
            reservation.getCheckInDate()
        );
        sendSms(phone, message);
        log.info("📱 SMS enviado a {}: Reserva #{} creada",
            maskPhone(phone), reservation.getId());
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String message = String.format(
            "Hotel Oaxaca Dreams: Su reserva #%d ha sido CONFIRMADA. " +
            "Check-in: %s, Check-out: %s. Le esperamos!",
            reservation.getId(),
            reservation.getCheckInDate(),
            reservation.getCheckOutDate()
        );
        sendSms(phone, message);
        log.info("📱 SMS enviado a {}: Reserva #{} confirmada",
            maskPhone(phone), reservation.getId());
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String message = String.format(
            "Hotel Oaxaca Dreams: Su reserva #%d ha sido cancelada. " +
            "Si tiene dudas, contactenos al (951) 123-4567",
            reservation.getId()
        );
        sendSms(phone, message);
        log.info("📱 SMS enviado a {}: Reserva #{} cancelada",
            maskPhone(phone), reservation.getId());
    }

    /**
     * Simula el envío de SMS (en producción se integraría con Twilio, AWS SNS, etc.)
     */
    private void sendSms(String phone, String message) {
        // En producción, aquí se integraría con un proveedor de SMS como:
        // - Twilio
        // - AWS SNS
        // - Vonage (Nexmo)
        // - MessageBird
        log.debug("🔔 SMS Gateway - Enviando a {}: {}", phone, message);

        // Simulación de envío
        try {
            Thread.sleep(100); // Simula latencia de red
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Enmascara el número de teléfono para los logs (privacidad)
     */
    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 4) {
            return "***";
        }
        return "***" + phone.substring(phone.length() - 4);
    }
}
