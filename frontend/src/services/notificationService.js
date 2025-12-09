import api from './api';

/**
 * Notification Service - Maneja las preferencias de notificación
 */
const notificationService = {
  /**
   * Obtiene las preferencias de notificación de un cliente
   * @param {number} customerId - ID del cliente
   * @returns {Promise} Preferencias de notificación
   */
  getPreferences: async (customerId) => {
    const response = await api.get(`/notifications/preferences/${customerId}`);
    return response.data;
  },

  /**
   * Actualiza las preferencias de notificación
   * @param {number} customerId - ID del cliente
   * @param {Object} preferences - Preferencias a actualizar
   * @returns {Promise} Preferencias actualizadas
   */
  updatePreferences: async (customerId, preferences) => {
    const response = await api.put(`/notifications/preferences/${customerId}`, preferences);
    return response.data;
  }
};

export default notificationService;
