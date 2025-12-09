import api from './api';

/**
 * Report Service - Maneja todas las operaciones relacionadas con reportes y estadísticas
 */
const reportService = {
  /**
   * Obtiene estadísticas generales del sistema
   * @returns {Promise} Estadísticas completas
   */
  getStatistics: async () => {
    const response = await api.get('/reports/statistics');
    return response.data;
  },

  /**
   * Obtiene el dashboard resumen
   * @returns {Promise} Dashboard con métricas principales
   */
  getDashboard: async () => {
    const response = await api.get('/reports/dashboard');
    return response.data;
  },

  /**
   * Valida la integridad de los datos del sistema
   * @returns {Promise} Resultado de validación
   */
  validateData: async () => {
    const response = await api.get('/reports/validate');
    return response.data;
  },

  /**
   * Obtiene reporte de ocupación (texto)
   * @returns {Promise} Reporte de ocupación en formato texto
   */
  getOccupancyReport: async () => {
    const response = await api.get('/reports/occupancy');
    return response.data;
  },

  /**
   * Obtiene reporte de ingresos (texto)
   * @returns {Promise} Reporte de ingresos en formato texto
   */
  getRevenueReport: async () => {
    const response = await api.get('/reports/revenue');
    return response.data;
  },

  /**
   * Obtiene reporte de reservas (texto)
   * @returns {Promise} Reporte de reservas en formato texto
   */
  getReservationsReport: async () => {
    const response = await api.get('/reports/reservations');
    return response.data;
  },

  /**
   * Exporta habitaciones a CSV
   * @returns {Promise} Datos de habitaciones en CSV
   */
  exportRooms: async () => {
    const response = await api.get('/reports/export/rooms');
    return response.data;
  },

  /**
   * Exporta clientes a CSV
   * @returns {Promise} Datos de clientes en CSV
   */
  exportCustomers: async () => {
    const response = await api.get('/reports/export/customers');
    return response.data;
  },

  /**
   * Exporta reservas a CSV
   * @returns {Promise} Datos de reservas en CSV
   */
  exportReservations: async () => {
    const response = await api.get('/reports/export/reservations');
    return response.data;
  },

  /**
   * Exporta pagos a CSV
   * @returns {Promise} Datos de pagos en CSV
   */
  exportPayments: async () => {
    const response = await api.get('/reports/export/payments');
    return response.data;
  },

  /**
   * Descarga reporte de reservas en Excel
   * @returns {Promise} Blob del archivo Excel
   */
  downloadReservationsExcel: async () => {
    const response = await api.get('/reports/reservations/excel', {
      responseType: 'blob'
    });
    return response.data;
  },

  /**
   * Descarga reporte de ingresos en Excel
   * @returns {Promise} Blob del archivo Excel
   */
  downloadRevenueExcel: async () => {
    const response = await api.get('/reports/revenue/excel', {
      responseType: 'blob'
    });
    return response.data;
  },

  /**
   * Descarga reporte de ocupación en Excel
   * @returns {Promise} Blob del archivo Excel
   */
  downloadOccupancyExcel: async () => {
    const response = await api.get('/reports/occupancy/excel', {
      responseType: 'blob'
    });
    return response.data;
  }
};

export default reportService;
