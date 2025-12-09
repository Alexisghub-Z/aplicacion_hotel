import api from './api';

/**
 * Reservation Service - Maneja todas las operaciones relacionadas con reservas
 */
const reservationService = {
  /**
   * Obtiene todas las reservas
   * @returns {Promise} Lista de reservas
   */
  getAllReservations: async () => {
    const response = await api.get('/reservations');
    return response.data;
  },

  /**
   * Obtiene una reserva por ID
   * @param {number} id - ID de la reserva
   * @returns {Promise} Datos de la reserva
   */
  getReservationById: async (id) => {
    const response = await api.get(`/reservations/${id}`);
    return response.data;
  },

  /**
   * Crea una nueva reserva
   * @param {Object} reservationData - Datos de la reserva
   * @returns {Promise} Reserva creada
   */
  createReservation: async (reservationData) => {
    const response = await api.post('/reservations', reservationData);
    return response.data;
  },

  /**
   * Confirma una reserva
   * @param {number} id - ID de la reserva
   * @returns {Promise} Reserva confirmada
   */
  confirmReservation: async (id) => {
    const response = await api.patch(`/reservations/${id}/confirm`);
    return response.data;
  },

  /**
   * Cancela una reserva
   * @param {number} id - ID de la reserva
   * @returns {Promise} Reserva cancelada
   */
  cancelReservation: async (id) => {
    const response = await api.patch(`/reservations/${id}/cancel`);
    return response.data;
  }
};

export default reservationService;
