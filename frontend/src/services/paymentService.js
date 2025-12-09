import api from './api';

/**
 * Payment Service - Maneja todas las operaciones relacionadas con pagos
 */
const paymentService = {
  /**
   * Obtiene un pago por ID
   * @param {number} id - ID del pago
   * @returns {Promise} Datos del pago
   */
  getPaymentById: async (id) => {
    const response = await api.get(`/payments/${id}`);
    return response.data;
  },

  /**
   * Obtiene todos los pagos de una reserva
   * @param {number} reservationId - ID de la reserva
   * @returns {Promise} Lista de pagos
   */
  getPaymentsByReservation: async (reservationId) => {
    const response = await api.get(`/payments/reservation/${reservationId}`);
    return response.data;
  },

  /**
   * Procesa un pago para una reserva
   * @param {number} reservationId - ID de la reserva
   * @param {string} paymentMethod - Método de pago (CREDIT_CARD, PAYPAL, CASH)
   * @returns {Promise} Pago procesado
   */
  processPayment: async (reservationId, paymentMethod) => {
    const response = await api.post('/payments', null, {
      params: {
        reservationId,
        paymentMethod
      }
    });
    return response.data;
  },

  /**
   * Procesa un reembolso
   * @param {number} paymentId - ID del pago
   * @returns {Promise} Pago reembolsado
   */
  refundPayment: async (paymentId) => {
    const response = await api.post(`/payments/${paymentId}/refund`);
    return response.data;
  }
};

export default paymentService;
