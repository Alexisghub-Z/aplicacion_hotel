import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Badge, ListGroup, Spinner, Alert, Button, Modal, Form } from 'react-bootstrap';
import { FaBox, FaPercent, FaCheckCircle, FaTags, FaPlus } from 'react-icons/fa';
import packageService from '../services/packageService';
import additionalServiceService from '../services/additionalServiceService';

function Packages() {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Estados para el modal de creación
  const [showModal, setShowModal] = useState(false);
  const [availableServices, setAvailableServices] = useState([]);
  const [loadingServices, setLoadingServices] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  // Estados del formulario
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    discount: 0,
    selectedServiceIds: [],
    active: true
  });
  const [formErrors, setFormErrors] = useState({});

  useEffect(() => {
    loadPackages();
  }, []);

  const loadPackages = async () => {
    try {
      setLoading(true);
      const data = await packageService.getActivePackages();
      setPackages(data);
      setError(null);
    } catch (err) {
      setError('Error al cargar los paquetes. Por favor, intenta de nuevo.');
      console.error('Error loading packages:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadServices = async () => {
    try {
      setLoadingServices(true);
      const data = await additionalServiceService.getAllServices();
      setAvailableServices(data);
    } catch (err) {
      console.error('Error loading services:', err);
      alert('Error al cargar los servicios disponibles');
    } finally {
      setLoadingServices(false);
    }
  };

  const handleShowModal = () => {
    loadServices();
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setFormData({
      name: '',
      description: '',
      discount: 0,
      selectedServiceIds: [],
      active: true
    });
    setFormErrors({});
  };

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleServiceToggle = (serviceId) => {
    setFormData(prev => ({
      ...prev,
      selectedServiceIds: prev.selectedServiceIds.includes(serviceId)
        ? prev.selectedServiceIds.filter(id => id !== serviceId)
        : [...prev.selectedServiceIds, serviceId]
    }));
  };

  const validateForm = () => {
    const errors = {};

    if (!formData.name.trim()) {
      errors.name = 'El nombre es obligatorio';
    }

    if (!formData.description.trim()) {
      errors.description = 'La descripción es obligatoria';
    }

    if (formData.discount < 0 || formData.discount > 100) {
      errors.discount = 'El descuento debe estar entre 0 y 100';
    }

    if (formData.selectedServiceIds.length === 0) {
      errors.services = 'Debes seleccionar al menos un servicio';
    }

    setFormErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      return;
    }

    try {
      setSubmitting(true);

      const packageData = {
        name: formData.name,
        description: formData.description,
        discount: parseFloat(formData.discount),
        serviceIds: formData.selectedServiceIds,
        active: formData.active
      };

      await packageService.createPackage(packageData);

      // Recargar paquetes
      await loadPackages();

      // Cerrar modal y mostrar éxito
      handleCloseModal();
      alert('Paquete creado exitosamente!');

    } catch (err) {
      console.error('Error creating package:', err);
      alert(err.response?.data?.message || 'Error al crear el paquete. Por favor intenta nuevamente.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Container className="mt-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="mt-3">Cargando paquetes...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">{error}</Alert>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h2>
            <FaBox className="me-2" />
            Paquetes de Servicios
          </h2>
          <p className="text-muted">Ahorra con nuestros paquetes especiales</p>
        </div>
        <Button variant="primary" onClick={handleShowModal}>
          <FaPlus className="me-2" />
          Crear Nuevo Paquete
        </Button>
      </div>

      {packages.length === 0 ? (
        <Alert variant="info">
          No hay paquetes disponibles en este momento.
        </Alert>
      ) : (
        <Row>
          {packages.map((pkg) => (
            <Col key={pkg.id} md={6} lg={4} className="mb-4">
              <Card className="h-100 shadow-sm hover-shadow">
                <Card.Body>
                  <div className="d-flex justify-content-between align-items-start mb-3">
                    <Card.Title className="mb-0">{pkg.name}</Card.Title>
                    {pkg.discountPercentage > 0 && (
                      <Badge bg="danger" className="d-flex align-items-center">
                        <FaPercent className="me-1" size={12} />
                        {pkg.discountPercentage}% OFF
                      </Badge>
                    )}
                  </div>

                  <Card.Text className="text-muted small mb-3">
                    {pkg.description}
                  </Card.Text>

                  <div className="mb-3">
                    <div className="d-flex justify-content-between align-items-center mb-2">
                      <span className="text-muted">
                        <FaTags className="me-1" />
                        {pkg.serviceCount} Servicio{pkg.serviceCount !== 1 ? 's' : ''}
                      </span>
                      {pkg.active && (
                        <Badge bg="success" className="d-flex align-items-center">
                          <FaCheckCircle className="me-1" size={12} />
                          Activo
                        </Badge>
                      )}
                    </div>

                    <ListGroup variant="flush" className="small">
                      {pkg.services.map((service, index) => (
                        <ListGroup.Item key={index} className="px-0 py-1 border-0">
                          <span className="text-success me-1">✓</span>
                          {service.name}
                        </ListGroup.Item>
                      ))}
                    </ListGroup>
                  </div>

                  <div className="mt-auto">
                    {pkg.discountPercentage > 0 && (
                      <div className="mb-1">
                        <small className="text-muted text-decoration-line-through">
                          ${pkg.originalPrice.toFixed(2)} MXN
                        </small>
                      </div>
                    )}
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <h4 className="mb-0 text-primary">
                          {pkg.formattedFinalPrice}
                        </h4>
                        {pkg.savings > 0 && (
                          <small className="text-success">
                            Ahorras ${pkg.savings.toFixed(2)} MXN
                          </small>
                        )}
                      </div>
                    </div>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      )}

      {/* Modal para crear paquete */}
      <Modal show={showModal} onHide={handleCloseModal} size="lg">
        <Modal.Header closeButton>
          <Modal.Title>
            <FaBox className="me-2" />
            Crear Nuevo Paquete
          </Modal.Title>
        </Modal.Header>

        <Form onSubmit={handleSubmit}>
          <Modal.Body>
            {/* Nombre */}
            <Form.Group className="mb-3">
              <Form.Label>Nombre del Paquete <span className="text-danger">*</span></Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={formData.name}
                onChange={handleInputChange}
                isInvalid={!!formErrors.name}
                placeholder="Ej: Paquete Romántico"
              />
              <Form.Control.Feedback type="invalid">
                {formErrors.name}
              </Form.Control.Feedback>
            </Form.Group>

            {/* Descripción */}
            <Form.Group className="mb-3">
              <Form.Label>Descripción <span className="text-danger">*</span></Form.Label>
              <Form.Control
                as="textarea"
                rows={3}
                name="description"
                value={formData.description}
                onChange={handleInputChange}
                isInvalid={!!formErrors.description}
                placeholder="Describe el paquete y sus beneficios..."
              />
              <Form.Control.Feedback type="invalid">
                {formErrors.description}
              </Form.Control.Feedback>
            </Form.Group>

            {/* Descuento */}
            <Form.Group className="mb-3">
              <Form.Label>Descuento (%) <span className="text-danger">*</span></Form.Label>
              <Form.Control
                type="number"
                name="discount"
                value={formData.discount}
                onChange={handleInputChange}
                isInvalid={!!formErrors.discount}
                min="0"
                max="100"
                step="0.01"
                placeholder="Ej: 15"
              />
              <Form.Control.Feedback type="invalid">
                {formErrors.discount}
              </Form.Control.Feedback>
              <Form.Text className="text-muted">
                Porcentaje de descuento sobre el precio total de los servicios
              </Form.Text>
            </Form.Group>

            {/* Servicios incluidos */}
            <Form.Group className="mb-3">
              <Form.Label>Servicios Incluidos <span className="text-danger">*</span></Form.Label>
              {formErrors.services && (
                <div className="text-danger small mb-2">{formErrors.services}</div>
              )}

              {loadingServices ? (
                <div className="text-center py-3">
                  <Spinner animation="border" size="sm" variant="primary" />
                  <span className="ms-2">Cargando servicios...</span>
                </div>
              ) : availableServices.length === 0 ? (
                <Alert variant="warning" className="mb-0">
                  No hay servicios disponibles. Crea servicios primero.
                </Alert>
              ) : (
                <div className="border rounded p-3" style={{ maxHeight: '250px', overflowY: 'auto' }}>
                  {availableServices.map(service => (
                    <Form.Check
                      key={service.id}
                      type="checkbox"
                      id={`service-${service.id}`}
                      className="mb-2"
                      label={
                        <div className="d-flex justify-content-between align-items-center w-100">
                          <span>{service.name}</span>
                          <Badge bg="secondary">${service.price.toFixed(2)} MXN</Badge>
                        </div>
                      }
                      checked={formData.selectedServiceIds.includes(service.id)}
                      onChange={() => handleServiceToggle(service.id)}
                    />
                  ))}
                </div>
              )}
            </Form.Group>

            {/* Activo */}
            <Form.Group className="mb-3">
              <Form.Check
                type="checkbox"
                id="active-checkbox"
                name="active"
                label="Paquete activo (visible para los clientes)"
                checked={formData.active}
                onChange={handleInputChange}
              />
            </Form.Group>

            {/* Resumen del paquete */}
            {formData.selectedServiceIds.length > 0 && (
              <Alert variant="info">
                <strong>Resumen:</strong>
                <ul className="mb-0 mt-2">
                  <li>{formData.selectedServiceIds.length} servicio(s) seleccionado(s)</li>
                  <li>
                    Precio total sin descuento: $
                    {availableServices
                      .filter(s => formData.selectedServiceIds.includes(s.id))
                      .reduce((sum, s) => sum + s.price, 0)
                      .toFixed(2)} MXN
                  </li>
                  {formData.discount > 0 && (
                    <li>
                      Precio final con {formData.discount}% descuento: $
                      {(availableServices
                        .filter(s => formData.selectedServiceIds.includes(s.id))
                        .reduce((sum, s) => sum + s.price, 0) *
                        (1 - formData.discount / 100))
                        .toFixed(2)} MXN
                    </li>
                  )}
                </ul>
              </Alert>
            )}
          </Modal.Body>

          <Modal.Footer>
            <Button variant="secondary" onClick={handleCloseModal} disabled={submitting}>
              Cancelar
            </Button>
            <Button variant="primary" type="submit" disabled={submitting}>
              {submitting ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Creando...
                </>
              ) : (
                <>
                  <FaPlus className="me-2" />
                  Crear Paquete
                </>
              )}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>
    </Container>
  );
}

export default Packages;
