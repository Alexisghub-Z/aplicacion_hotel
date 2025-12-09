import { useState, useEffect } from 'react';
import { Container, Row, Col, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { FaCog, FaUser, FaSearch } from 'react-icons/fa';
import NotificationSettings from '../components/settings/NotificationSettings';
import customerService from '../services/customerService';

const Settings = () => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [showSettings, setShowSettings] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchCustomers();
  }, []);

  const fetchCustomers = async () => {
    try {
      setLoading(true);
      setError('');
      const data = await customerService.getAllCustomers();
      setCustomers(data);
    } catch (err) {
      console.error('Error fetching customers:', err);
      setError('Error al cargar la lista de clientes');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');

    if (!selectedCustomer) {
      setError('Por favor selecciona un cliente');
      return;
    }

    setShowSettings(true);
  };

  const filteredCustomers = customers.filter(customer =>
    customer.fullName.toLowerCase().includes(searchTerm.toLowerCase()) ||
    customer.email.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <Container className="py-5">
      <Row className="mb-4">
        <Col>
          <h1 className="mb-2">
            <FaCog className="me-3 text-primary" />
            Configuración
          </h1>
          <p className="text-muted">Administra tus preferencias de notificación</p>
        </Col>
      </Row>

      {!showSettings ? (
        <Row className="justify-content-center">
          <Col md={8} lg={6}>
            <div className="bg-white p-4 rounded shadow-sm">
              <h5 className="mb-4">
                <FaUser className="me-2 text-primary" />
                Selecciona tu Perfil
              </h5>

              {error && (
                <Alert variant="danger" dismissible onClose={() => setError('')}>
                  {error}
                </Alert>
              )}

              {loading ? (
                <div className="text-center py-5">
                  <Spinner animation="border" variant="primary" />
                  <p className="mt-3 text-muted">Cargando clientes...</p>
                </div>
              ) : (
                <Form onSubmit={handleSubmit}>
                  <Form.Group className="mb-3">
                    <Form.Label>
                      <FaSearch className="me-2" />
                      Buscar Cliente
                    </Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="Buscar por nombre o email..."
                      value={searchTerm}
                      onChange={(e) => setSearchTerm(e.target.value)}
                      className="mb-3"
                    />
                  </Form.Group>

                  <Form.Group className="mb-4">
                    <Form.Label>Cliente</Form.Label>
                    <Form.Select
                      value={selectedCustomer?.id || ''}
                      onChange={(e) => {
                        const customer = customers.find(c => c.id === parseInt(e.target.value));
                        setSelectedCustomer(customer);
                      }}
                      required
                      size="lg"
                    >
                      <option value="">Selecciona un cliente...</option>
                      {filteredCustomers.map(customer => (
                        <option key={customer.id} value={customer.id}>
                          {customer.fullName} - {customer.email}
                        </option>
                      ))}
                    </Form.Select>
                    {filteredCustomers.length === 0 && searchTerm && (
                      <Form.Text className="text-danger">
                        No se encontraron clientes con ese criterio de búsqueda
                      </Form.Text>
                    )}
                  </Form.Group>

                  {selectedCustomer && (
                    <div className="customer-info-card p-3 mb-4 bg-light rounded">
                      <h6 className="text-primary mb-2">Cliente Seleccionado:</h6>
                      <div className="d-flex justify-content-between align-items-start">
                        <div>
                          <p className="mb-1"><strong>{selectedCustomer.fullName}</strong></p>
                          <p className="mb-1 text-muted small">
                            <i className="bi bi-envelope me-1"></i>
                            {selectedCustomer.email}
                          </p>
                          <p className="mb-0 text-muted small">
                            <i className="bi bi-telephone me-1"></i>
                            {selectedCustomer.phone}
                          </p>
                        </div>
                        <span className={`badge ${
                          selectedCustomer.loyaltyLevel === 'PLATINUM' ? 'bg-dark' :
                          selectedCustomer.loyaltyLevel === 'GOLD' ? 'bg-warning' :
                          selectedCustomer.loyaltyLevel === 'SILVER' ? 'bg-secondary' :
                          'bg-info'
                        }`}>
                          {selectedCustomer.loyaltyLevel}
                        </span>
                      </div>
                    </div>
                  )}

                  <div className="d-grid">
                    <Button
                      variant="primary"
                      type="submit"
                      size="lg"
                      disabled={!selectedCustomer}
                    >
                      Continuar a Configuración
                    </Button>
                  </div>
                </Form>
              )}
            </div>
          </Col>
        </Row>
      ) : (
        <Row className="justify-content-center">
          <Col lg={8}>
            <div className="mb-3 d-flex justify-content-between align-items-center">
              <Button
                variant="outline-secondary"
                size="sm"
                onClick={() => {
                  setShowSettings(false);
                  setSelectedCustomer(null);
                  setSearchTerm('');
                }}
              >
                ← Cambiar Cliente
              </Button>
              <div className="text-muted">
                <strong>{selectedCustomer?.fullName}</strong> - {selectedCustomer?.email}
              </div>
            </div>
            <NotificationSettings customerId={selectedCustomer.id} />
          </Col>
        </Row>
      )}
    </Container>
  );
};

export default Settings;
