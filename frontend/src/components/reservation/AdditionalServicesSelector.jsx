import { useState, useEffect } from 'react';
import { Card, Form, Badge, Row, Col, Spinner, Alert } from 'react-bootstrap';
import { FaUtensils, FaSpa, FaCar, FaMapMarkedAlt, FaConciergeBell } from 'react-icons/fa';
import PropTypes from 'prop-types';
import additionalServiceService from '../../services/additionalServiceService';

const AdditionalServicesSelector = ({ selectedServices, onSelectionChange }) => {
  const [services, setServices] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchServices();
  }, []);

  const fetchServices = async () => {
    try {
      setLoading(true);
      const data = await additionalServiceService.getAllServices();
      setServices(data);
    } catch (err) {
      console.error('Error fetching services:', err);
      setError('No se pudieron cargar los servicios adicionales');
    } finally {
      setLoading(false);
    }
  };

  const handleServiceToggle = (service) => {
    const isSelected = selectedServices.some(s => s.id === service.id);

    if (isSelected) {
      onSelectionChange(selectedServices.filter(s => s.id !== service.id));
    } else {
      onSelectionChange([...selectedServices, service]);
    }
  };

  const getServiceIcon = (type) => {
    switch (type) {
      case 'BREAKFAST': return <FaUtensils />;
      case 'SPA': return <FaSpa />;
      case 'TRANSPORT': return <FaCar />;
      case 'EXCURSION': return <FaMapMarkedAlt />;
      case 'ROOM_SERVICE': return <FaConciergeBell />;
      default: return <FaConciergeBell />;
    }
  };

  const getServiceTypeLabel = (type) => {
    const labels = {
      'BREAKFAST': 'Desayuno',
      'SPA': 'Spa & Wellness',
      'TRANSPORT': 'Transporte',
      'EXCURSION': 'Excursión',
      'ROOM_SERVICE': 'Servicio a Cuarto'
    };
    return labels[type] || type;
  };

  const getServiceTypeBadgeColor = (type) => {
    const colors = {
      'BREAKFAST': 'warning',
      'SPA': 'info',
      'TRANSPORT': 'primary',
      'EXCURSION': 'success',
      'ROOM_SERVICE': 'secondary'
    };
    return colors[type] || 'secondary';
  };

  const calculateTotal = () => {
    return selectedServices.reduce((sum, service) => sum + service.price, 0);
  };

  if (loading) {
    return (
      <Card className="mb-4">
        <Card.Body className="text-center py-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3 mb-0">Cargando servicios adicionales...</p>
        </Card.Body>
      </Card>
    );
  }

  if (error) {
    return (
      <Card className="mb-4">
        <Card.Body>
          <Alert variant="warning">{error}</Alert>
        </Card.Body>
      </Card>
    );
  }

  return (
    <div className="additional-services-selector">
      <h5 className="mb-3">Servicios Adicionales (Opcional)</h5>

      <Row>
        {services.map((service) => {
          const isSelected = selectedServices.some(s => s.id === service.id);

          return (
            <Col key={service.id} md={6} lg={4} className="mb-3">
              <Card
                className={`service-card h-100 ${isSelected ? 'selected' : ''}`}
                onClick={() => handleServiceToggle(service)}
                style={{ cursor: 'pointer' }}
              >
                <Card.Body>
                  <div className="d-flex justify-content-between align-items-start mb-2">
                    <div className="service-icon text-primary fs-3">
                      {getServiceIcon(service.serviceType)}
                    </div>
                    <Form.Check
                      type="checkbox"
                      checked={isSelected}
                      onChange={() => {}}
                      onClick={(e) => e.stopPropagation()}
                    />
                  </div>

                  <Badge bg={getServiceTypeBadgeColor(service.serviceType)} className="mb-2">
                    {getServiceTypeLabel(service.serviceType)}
                  </Badge>

                  <Card.Title className="h6 mb-2">{service.name}</Card.Title>

                  <Card.Text className="text-muted small mb-2">
                    {service.description}
                  </Card.Text>

                  <div className="service-price text-primary fw-bold">
                    ${service.price.toLocaleString('es-MX')} MXN
                  </div>
                </Card.Body>
              </Card>
            </Col>
          );
        })}
      </Row>

      {selectedServices.length > 0 && (
        <Card className="bg-light mt-3">
          <Card.Body>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <strong>{selectedServices.length}</strong> {selectedServices.length === 1 ? 'servicio seleccionado' : 'servicios seleccionados'}
              </div>
              <div className="text-primary fw-bold fs-5">
                +${calculateTotal().toLocaleString('es-MX')} MXN
              </div>
            </div>
          </Card.Body>
        </Card>
      )}
    </div>
  );
};

AdditionalServicesSelector.propTypes = {
  selectedServices: PropTypes.array.isRequired,
  onSelectionChange: PropTypes.func.isRequired
};

export default AdditionalServicesSelector;
