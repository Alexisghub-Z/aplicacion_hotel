import api from './api';

/**
 * Customer Service - Maneja todas las operaciones relacionadas con clientes
 */
const customerService = {
  /**
   * Obtiene todos los clientes
   * @returns {Promise} Lista de clientes
   */
  getAllCustomers: async () => {
    const response = await api.get('/customers');
    return response.data;
  },

  /**
   * Obtiene un cliente por ID
   * @param {number} id - ID del cliente
   * @returns {Promise} Datos del cliente
   */
  getCustomerById: async (id) => {
    const response = await api.get(`/customers/${id}`);
    return response.data;
  },

  /**
   * Busca un cliente por email
   * @param {string} email - Email del cliente
   * @returns {Promise} Datos del cliente
   */
  getCustomerByEmail: async (email) => {
    const response = await api.get(`/customers/email/${email}`);
    return response.data;
  },

  /**
   * Crea un nuevo cliente
   * @param {Object} customerData - Datos del cliente
   * @returns {Promise} Cliente creado
   */
  createCustomer: async (customerData) => {
    const response = await api.post('/customers', customerData);
    return response.data;
  },

  /**
   * Actualiza un cliente existente
   * @param {number} id - ID del cliente
   * @param {Object} customerData - Datos actualizados
   * @returns {Promise} Cliente actualizado
   */
  updateCustomer: async (id, customerData) => {
    const response = await api.put(`/customers/${id}`, customerData);
    return response.data;
  },

  /**
   * Elimina un cliente
   * @param {number} id - ID del cliente
   * @returns {Promise}
   */
  deleteCustomer: async (id) => {
    const response = await api.delete(`/customers/${id}`);
    return response.data;
  }
};

export default customerService;
