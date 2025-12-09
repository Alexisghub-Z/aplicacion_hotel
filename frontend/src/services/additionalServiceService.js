import api from './api';

/**
 * Additional Service Service - Maneja servicios adicionales del hotel
 */
const additionalServiceService = {
  /**
   * Obtiene todos los servicios adicionales
   * @returns {Promise} Lista de servicios
   */
  getAllServices: async () => {
    const response = await api.get('/services');
    return response.data;
  },

  /**
   * Obtiene un servicio por ID
   * @param {number} id - ID del servicio
   * @returns {Promise} Datos del servicio
   */
  getServiceById: async (id) => {
    const response = await api.get(`/services/${id}`);
    return response.data;
  },

  /**
   * Obtiene servicios por tipo
   * @param {string} type - Tipo de servicio (BREAKFAST, SPA, TRANSPORT, EXCURSION, ROOM_SERVICE)
   * @returns {Promise} Lista de servicios del tipo especificado
   */
  getServicesByType: async (type) => {
    const response = await api.get(`/services/type/${type}`);
    return response.data;
  },

  /**
   * Crea un nuevo servicio
   * @param {Object} serviceData - Datos del servicio
   * @returns {Promise} Servicio creado
   */
  createService: async (serviceData) => {
    const response = await api.post('/services', serviceData);
    return response.data;
  },

  /**
   * Actualiza un servicio existente
   * @param {number} id - ID del servicio
   * @param {Object} serviceData - Datos actualizados
   * @returns {Promise} Servicio actualizado
   */
  updateService: async (id, serviceData) => {
    const response = await api.put(`/services/${id}`, serviceData);
    return response.data;
  },

  /**
   * Elimina un servicio
   * @param {number} id - ID del servicio
   * @returns {Promise}
   */
  deleteService: async (id) => {
    const response = await api.delete(`/services/${id}`);
    return response.data;
  }
};

export default additionalServiceService;
