package com.hotel.reservation.patterns.behavioral.template;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Template Method Pattern - Plantilla base para generación de reportes
 * Define el esqueleto del algoritmo de generación de reportes
 */
@Slf4j
public abstract class ReportTemplate {

    // Template Method - Define el flujo del algoritmo
    public final String generateReport() {
        log.info("📊 Iniciando generación de reporte: {}", getReportName());

        StringBuilder report = new StringBuilder();

        report.append(generateHeader());
        report.append(generateBody());
        report.append(generateFooter());

        log.info("✅ Reporte generado exitosamente");

        return report.toString();
    }

    // Métodos abstractos - Las subclases deben implementarlos
    protected abstract String getReportName();
    protected abstract String generateBody();

    // Métodos con implementación por defecto - Pueden ser sobreescritos
    protected String generateHeader() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return String.format("""
            =====================================
            %s
            =====================================
            Fecha de generación: %s

            """, getReportName(), LocalDateTime.now().format(formatter));
    }

    protected String generateFooter() {
        return """

            =====================================
            Fin del reporte
            =====================================
            """;
    }
}
