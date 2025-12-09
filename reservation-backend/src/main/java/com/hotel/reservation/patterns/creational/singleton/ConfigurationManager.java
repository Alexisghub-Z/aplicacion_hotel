package com.hotel.reservation.patterns.creational.singleton;

/**
 * PATRÓN SINGLETON - ConfigurationManager
 *
 * Propósito: Garantiza que solo existe una instancia de la configuración global
 * del sistema durante toda la ejecución de la aplicación.
 *
 * Beneficios:
 * - Una única fuente de verdad para la configuración
 * - Thread-safe usando enum
 * - Acceso global controlado
 * - Inicialización perezosa automática (lazy initialization)
 *
 * Uso: ConfigurationManager.INSTANCE.getCurrency()
 */
public enum ConfigurationManager {
    INSTANCE;

    // Configuración del hotel
    private final String currency = "MXN";
    private final String language = "ES";
    private final double taxRate = 0.16; // 16% IVA en México
    private final String hotelName = "Hotel Oaxaca Dreams";
    private final String hotelAddress = "Av. Juárez 123, Centro, Oaxaca de Juárez, Oaxaca";
    private final String hotelPhone = "+52 951 123 4567";
    private final String hotelEmail = "info@hoteloaxaca.com";

    // Configuración de precios
    private final double seasonalPriceIncrease = 0.30; // 30% en temporada alta
    private final double weekendPriceIncrease = 0.20; // 20% fines de semana

    // Niveles de descuento por lealtad (ya definidos en Customer, pero centralizados aquí)
    private final int regularDiscount = 0;
    private final int silverDiscount = 5;
    private final int goldDiscount = 10;
    private final int platinumDiscount = 20;

    /**
     * Obtiene la moneda del sistema
     * @return código de moneda (MXN)
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Obtiene el idioma del sistema
     * @return código de idioma (ES)
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Obtiene la tasa de impuestos
     * @return tasa de impuesto (0.16 = 16%)
     */
    public double getTaxRate() {
        return taxRate;
    }

    /**
     * Obtiene el nombre del hotel
     * @return nombre del hotel
     */
    public String getHotelName() {
        return hotelName;
    }

    /**
     * Obtiene la dirección del hotel
     * @return dirección del hotel
     */
    public String getHotelAddress() {
        return hotelAddress;
    }

    /**
     * Obtiene el teléfono del hotel
     * @return teléfono del hotel
     */
    public String getHotelPhone() {
        return hotelPhone;
    }

    /**
     * Obtiene el email del hotel
     * @return email del hotel
     */
    public String getHotelEmail() {
        return hotelEmail;
    }

    /**
     * Obtiene el incremento de precio por temporada alta
     * @return porcentaje de incremento (0.30 = 30%)
     */
    public double getSeasonalPriceIncrease() {
        return seasonalPriceIncrease;
    }

    /**
     * Obtiene el incremento de precio por fin de semana
     * @return porcentaje de incremento (0.20 = 20%)
     */
    public double getWeekendPriceIncrease() {
        return weekendPriceIncrease;
    }

    /**
     * Obtiene el descuento por nivel de lealtad
     * @param level nivel de lealtad (REGULAR, SILVER, GOLD, PLATINUM)
     * @return porcentaje de descuento
     */
    public int getDiscountByLoyaltyLevel(String level) {
        return switch (level.toUpperCase()) {
            case "SILVER" -> silverDiscount;
            case "GOLD" -> goldDiscount;
            case "PLATINUM" -> platinumDiscount;
            default -> regularDiscount;
        };
    }

    /**
     * Formatea un precio con la moneda del sistema
     * @param amount monto a formatear
     * @return precio formateado (ej: "$1,234.56 MXN")
     */
    public String formatPrice(double amount) {
        return String.format("$%,.2f %s", amount, currency);
    }

    /**
     * Muestra la configuración completa del sistema
     * Útil para debugging
     * @return configuración en formato String
     */
    @Override
    public String toString() {
        return "ConfigurationManager{" +
                "currency='" + currency + '\'' +
                ", language='" + language + '\'' +
                ", taxRate=" + taxRate +
                ", hotelName='" + hotelName + '\'' +
                ", hotelAddress='" + hotelAddress + '\'' +
                ", hotelPhone='" + hotelPhone + '\'' +
                ", hotelEmail='" + hotelEmail + '\'' +
                '}';
    }
}
