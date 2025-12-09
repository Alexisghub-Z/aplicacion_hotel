package com.hotel.reservation.patterns.behavioral.observer;

import com.hotel.reservation.models.Reservation;
import com.hotel.reservation.patterns.creational.singleton.ConfigurationManager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationObserver implements ReservationObserver {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name}")
    private String fromName;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ConfigurationManager config = ConfigurationManager.INSTANCE;

    @Override
    public void onReservationCreated(Reservation reservation) {
        String to = reservation.getCustomer().getEmail();
        String subject = "✅ Reserva Creada - Hotel Oaxaca Dreams";

        String body = buildCreatedEmailBody(reservation);

        try {
            sendEmail(to, subject, body);
            log.info("📧 Email enviado exitosamente: Reserva #{} creada para {}",
                reservation.getId(), to);
        } catch (Exception e) {
            log.error("❌ Error al enviar email de reserva creada: {}", e.getMessage());
        }
    }

    @Override
    public void onReservationConfirmed(Reservation reservation) {
        String to = reservation.getCustomer().getEmail();
        String subject = "✓ Reserva Confirmada - Hotel Oaxaca Dreams";

        String body = buildConfirmedEmailBody(reservation);

        try {
            sendEmail(to, subject, body);
            log.info("📧 Email enviado exitosamente: Reserva #{} confirmada para {}",
                reservation.getId(), to);
        } catch (Exception e) {
            log.error("❌ Error al enviar email de reserva confirmada: {}", e.getMessage());
        }
    }

    @Override
    public void onReservationCancelled(Reservation reservation) {
        String to = reservation.getCustomer().getEmail();
        String subject = "✗ Reserva Cancelada - Hotel Oaxaca Dreams";

        String body = buildCancelledEmailBody(reservation);

        try {
            sendEmail(to, subject, body);
            log.info("📧 Email enviado exitosamente: Reserva #{} cancelada para {}",
                reservation.getId(), to);
        } catch (Exception e) {
            log.error("❌ Error al enviar email de reserva cancelada: {}", e.getMessage());
        }
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true = HTML

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email: " + e.getMessage(), e);
        }
    }

    private String buildCreatedEmailBody(Reservation reservation) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }
                    .details { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; }
                    .detail-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #eee; }
                    .label { font-weight: bold; color: #667eea; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🏨 Hotel Oaxaca Dreams</h1>
                        <h2>¡Tu reserva ha sido creada!</h2>
                    </div>
                    <div class="content">
                        <p>Estimado/a <strong>%s</strong>,</p>
                        <p>Nos complace confirmar que hemos recibido tu reserva. A continuación encontrarás los detalles:</p>

                        <div class="details">
                            <div class="detail-row">
                                <span class="label">Número de Reserva:</span>
                                <span>#%d</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Habitación:</span>
                                <span>%s - %s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Check-in:</span>
                                <span>%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Check-out:</span>
                                <span>%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Huéspedes:</span>
                                <span>%d</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Estado:</span>
                                <span style="color: #ffc107;">%s</span>
                            </div>
                            <div class="detail-row" style="border-bottom: none; font-size: 18px;">
                                <span class="label">Total:</span>
                                <span style="color: #667eea; font-weight: bold;">$%.2f %s</span>
                            </div>
                        </div>

                        <p style="margin-top: 20px;">
                            <strong>Próximos pasos:</strong><br>
                            • Espera la confirmación de tu reserva por parte de nuestro equipo<br>
                            • Recibirás un email de confirmación cuando tu reserva sea aprobada<br>
                            • Si tienes alguna pregunta, no dudes en contactarnos
                        </p>

                        <p>¡Esperamos verte pronto!</p>

                        <div class="footer">
                            <p>Hotel Oaxaca Dreams - Oaxaca, México</p>
                            <p>Este es un correo automático, por favor no respondas a este mensaje.</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
            reservation.getCustomer().getFullName(),
            reservation.getId(),
            reservation.getRoom().getRoomNumber(),
            reservation.getRoom().getRoomType(),
            reservation.getCheckInDate().format(DATE_FORMATTER),
            reservation.getCheckOutDate().format(DATE_FORMATTER),
            reservation.getNumberOfGuests(),
            reservation.getStatus(),
            reservation.getTotalPrice(),
            config.getCurrency()
        );
    }

    private String buildConfirmedEmailBody(Reservation reservation) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #28a745 0%%, #20c997 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }
                    .details { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; }
                    .detail-row { display: flex; justify-content: space-between; padding: 10px 0; border-bottom: 1px solid #eee; }
                    .label { font-weight: bold; color: #28a745; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                    .success-badge { background: #28a745; color: white; padding: 10px 20px; border-radius: 20px; display: inline-block; margin: 10px 0; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✓ Reserva Confirmada</h1>
                        <div class="success-badge">¡TODO LISTO!</div>
                    </div>
                    <div class="content">
                        <p>Estimado/a <strong>%s</strong>,</p>
                        <p>¡Excelentes noticias! Tu reserva ha sido <strong>confirmada</strong>.</p>

                        <div class="details">
                            <div class="detail-row">
                                <span class="label">Número de Reserva:</span>
                                <span>#%d</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Check-in:</span>
                                <span>%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="label">Check-out:</span>
                                <span>%s</span>
                            </div>
                        </div>

                        <p><strong>Te esperamos el %s. ¡Que tengas un excelente viaje!</strong></p>

                        <div class="footer">
                            <p>Hotel Oaxaca Dreams - Oaxaca, México</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
            reservation.getCustomer().getFullName(),
            reservation.getId(),
            reservation.getCheckInDate().format(DATE_FORMATTER),
            reservation.getCheckOutDate().format(DATE_FORMATTER),
            reservation.getCheckInDate().format(DATE_FORMATTER)
        );
    }

    private String buildCancelledEmailBody(Reservation reservation) {
        return String.format("""
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                    .header { background: linear-gradient(135deg, #dc3545 0%%, #c82333 100%%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }
                    .content { background: #f8f9fa; padding: 30px; border-radius: 0 0 10px 10px; }
                    .details { background: white; padding: 20px; border-radius: 8px; margin: 20px 0; }
                    .footer { text-align: center; margin-top: 20px; color: #666; font-size: 12px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>✗ Reserva Cancelada</h1>
                    </div>
                    <div class="content">
                        <p>Estimado/a <strong>%s</strong>,</p>
                        <p>Lamentamos informarte que tu reserva <strong>#%d</strong> ha sido cancelada.</p>

                        <p>Si tienes alguna pregunta o deseas realizar una nueva reserva, no dudes en contactarnos.</p>
                        <p>¡Esperamos verte pronto!</p>

                        <div class="footer">
                            <p>Hotel Oaxaca Dreams - Oaxaca, México</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """,
            reservation.getCustomer().getFullName(),
            reservation.getId()
        );
    }
}
