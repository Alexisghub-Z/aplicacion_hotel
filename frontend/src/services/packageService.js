import api from './api';

const packageService = {
  // Obtener todos los paquetes
  getAllPackages: async () => {
    const response = await api.get('/packages');
    return response.data;
  },

  // Obtener solo paquetes activos
  getActivePackages: async () => {
    const response = await api.get('/packages?activeOnly=true');
    return response.data;
  },

  // Obtener paquete por ID
  getPackageById: async (id) => {
    const response = await api.get(`/packages/${id}`);
    return response.data;
  },

  // Crear nuevo paquete
  createPackage: async (packageData) => {
    const response = await api.post('/packages', packageData);
    return response.data;
  },

  // Actualizar paquete
  updatePackage: async (id, packageData) => {
    const response = await api.put(`/packages/${id}`, packageData);
    return response.data;
  },

  // Eliminar paquete
  deletePackage: async (id) => {
    await api.delete(`/packages/${id}`);
  },

  // Activar/desactivar paquete
  togglePackageActive: async (id) => {
    const response = await api.patch(`/packages/${id}/toggle-active`);
    return response.data;
  },

  // Buscar paquetes por nombre
  searchPackages: async (name) => {
    const response = await api.get(`/packages/search?name=${name}`);
    return response.data;
  }
};

export default packageService;
