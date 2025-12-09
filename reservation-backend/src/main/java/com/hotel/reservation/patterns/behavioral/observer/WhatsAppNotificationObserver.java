package com.hotel.reservation.patterns.behavioral.observer;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Observer para enviar notificaciones por WhatsApp
 * Implementa el patrón Observer para notificaciones WhatsApp
 */
@Slf4j
@Component
public class WhatsAppNotificationObserver implements ReservationObserver {

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(new Locale("es", "MX"));
    private final ConfigurationManager config = ConfigurationManager.INSTANCE;

    @Override
    public void onReservationCreated(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String customerName = reservation.getCustomer().getFirstName();

        String message = String.format(
            "🏨 *Hotel Oaxaca Dreams*\n\n" +
            "Hola %s! 👋\n\n" +
            "Tu reserva ha sido creada exitosamente:\n\n" +
            "📋 *Reserva #%d*\n" +
            "🛏️ Habitación: %s\n" +
            "📅 Check-in: %s\n" +
            "📅 Check-out: %s\n" +
            "👥 Huéspedes: %d\n" +
            "💰 Total: $%.2f %s\n\n" +
            "¡Gracias por elegirnos! Estamos listos para recibirte. 🌟",
            customerName,
            reservation.getId(),
            reservation.getRoom().getRoomNumber(),
            reservation.getCheckInDate().format(DATE_FORMATTER),
            reservation.getCheckOutDate().format(DATE_FORMATTER),
            reservation.getNumberOfGuests(),
            reservation.getTotalPrice(),
            config.getCurrency()
        );

        sendWhatsApp(phone, message);
        log.info("💬 WhatsApp enviado a {}: Reserva #{} creada",
            maskPhone(phone), reservation.getId());
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String customerName = reservation.getCustomer().getFirstName();

        String message = String.format(
            "🏨 *Hotel Oaxaca Dreams*\n\n" +
            "¡Hola %s! ✅\n\n" +
            "Tu reserva ha sido *CONFIRMADA*:\n\n" +
            "📋 *Reserva #%d*\n" +
            "🛏️ Habitación: %s\n" +
            "📅 Check-in: %s\n" +
            "📅 Check-out: %s\n\n" +
            "🎉 ¡Todo listo! Te esperamos con los brazos abiertos.\n\n" +
            "📍 Ubicación: Centro Histórico de Oaxaca\n" +
            "📞 Contacto: (951) 123-4567\n" +
            "⏰ Check-in desde las 15:00 hrs",
            customerName,
            reservation.getId(),
            reservation.getRoom().getRoomNumber(),
            reservation.getCheckInDate().format(DATE_FORMATTER),
            reservation.getCheckOutDate().format(DATE_FORMATTER)
        );

        sendWhatsApp(phone, message);
        log.info("💬 WhatsApp enviado a {}: Reserva #{} confirmada",
            maskPhone(phone), reservation.getId());
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        String phone = reservation.getCustomer().getPhone();
        String customerName = reservation.getCustomer().getFirstName();

        String message = String.format(
            "🏨 *Hotel Oaxaca Dreams*\n\n" +
            "Hola %s,\n\n" +
            "Tu reserva #%d ha sido *cancelada* exitosamente. ❌\n\n" +
            "Si tienes alguna duda o deseas hacer una nueva reserva, " +
            "estamos para ayudarte:\n\n" +
            "📞 (951) 123-4567\n" +
            "📧 reservas@hoteloaxacadreams.com\n\n" +
            "¡Esperamos verte pronto! 🌺",
            customerName,
            reservation.getId()
        );

        sendWhatsApp(phone, message);
        log.info("💬 WhatsApp enviado a {}: Reserva #{} cancelada",
            maskPhone(phone), reservation.getId());
    }

    /**
     * Simula el envío de WhatsApp (en producción se integraría con WhatsApp Business API)
     */
    private void sendWhatsApp(String phone, String message) {
        // En producción, aquí se integraría con:
        // - WhatsApp Business API oficial
        // - Twilio API for WhatsApp
        // - MessageBird WhatsApp API
        // - 360dialog
        log.debug("💬 WhatsApp Gateway - Enviando a {}: {}", phone, message);

        // Simulación de envío
        try {
            Thread.sleep(150); // Simula latencia de red
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
