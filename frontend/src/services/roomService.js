import api from './api';

/**
 * Room Service - Maneja todas las operaciones relacionadas con habitaciones
 */
const roomService = {
  /**
   * Obtiene todas las habitaciones
   * @returns {Promise} Lista de habitaciones
   */
  getAllRooms: async () => {
    const response = await api.get('/rooms');
    return response.data;
  },

  /**
   * Obtiene una habitación por ID
   * @param {number} id - ID de la habitación
   * @returns {Promise} Datos de la habitación
   */
  getRoomById: async (id) => {
    const response = await api.get(`/rooms/${id}`);
    return response.data;
  },

  /**
   * Obtiene solo las habitaciones disponibles
   * @returns {Promise} Lista de habitaciones disponibles
   */
  getAvailableRooms: async () => {
    const response = await api.get('/rooms/available');
    return response.data;
  },

  /**
   * Busca habitaciones de lujo para familias
   * (Suites o Presidenciales con capacidad >= 4)
   * @returns {Promise} Lista de habitaciones de lujo
   */
  searchLuxuryFamilies: async () => {
    const response = await api.get('/rooms/search/luxury-families');
    return response.data;
  },

  /**
   * Busca habitaciones por rango de precio
   * @param {number} min - Precio mínimo
   * @param {number} max - Precio máximo
   * @returns {Promise} Lista de habitaciones en el rango
   */
  searchByPrice: async (min, max) => {
    const response = await api.get('/rooms/search/price', {
      params: { min, max }
    });
    return response.data;
  },

  /**
   * Busca habitaciones por capacidad mínima
   * @param {number} guests - Número mínimo de huéspedes
   * @returns {Promise} Lista de habitaciones con capacidad suficiente
   */
  searchByCapacity: async (guests) => {
    const response = await api.get('/rooms/search/capacity', {
      params: { guests }
    });
    return response.data;
  },

  /**
   * Crea una nueva habitación
   * @param {Object} roomData - Datos de la habitación
   * @returns {Promise} Habitación creada
   */
  createRoom: async (roomData) => {
    const response = await api.post('/rooms', roomData);
    return response.data;
  },

  /**
   * Actualiza una habitación existente
   * @param {number} id - ID de la habitación
   * @param {Object} roomData - Datos actualizados
   * @returns {Promise} Habitación actualizada
   */
  updateRoom: async (id, roomData) => {
    const response = await api.put(`/rooms/${id}`, roomData);
    return response.data;
  },

  /**
   * Elimina una habitación
   * @param {number} id - ID de la habitación
   * @returns {Promise}
   */
  deleteRoom: async (id) => {
    const response = await api.delete(`/rooms/${id}`);
    return response.data;
  },

  /**
   * Clona una habitación (Prototype Pattern)
   * @param {number} id - ID de la habitación a clonar
   * @param {Object} newData - Datos para la nueva habitación
   * @returns {Promise} Habitación clonada
   */
  cloneRoom: async (id, newData) => {
    const response = await api.post(`/rooms/${id}/clone`, newData);
    return response.data;
  }
};

export default roomService;
