import { useState, useEffect } from 'react';
import { Card, Badge, Row, Col, Spinner, Alert, ListGroup } from 'react-bootstrap';
import { FaBox, FaPercent, FaCheckCircle, FaTags } from 'react-icons/fa';
import PropTypes from 'prop-types';
import packageService from '../../services/packageService';

const PackageSelector = ({ selectedPackage, onPackageChange }) => {
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchPackages();
  }, []);

  const fetchPackages = async () => {
    try {
      setLoading(true);
      const data = await packageService.getActivePackages();
      setPackages(data);
    } catch (err) {
      console.error('Error fetching packages:', err);
      setError('No se pudieron cargar los paquetes disponibles');
    } finally {
      setLoading(false);
    }
  };

  const handlePackageToggle = (pkg) => {
    // Si el paquete ya está seleccionado, deseleccionarlo
    if (selectedPackage && selectedPackage.id === pkg.id) {
      onPackageChange(null);
    } else {
      // Seleccionar el nuevo paquete
      onPackageChange(pkg);
    }
  };

  if (loading) {
    return (
      <div className="text-center py-3">
        <Spinner animation="border" variant="primary" size="sm" />
        <p className="mt-2 mb-0 text-muted small">Cargando paquetes...</p>
      </div>
    );
  }

  if (error) {
    return <Alert variant="warning" className="mb-0">{error}</Alert>;
  }

  if (packages.length === 0) {
    return (
      <Alert variant="info" className="mb-0">
        No hay paquetes disponibles en este momento.
      </Alert>
    );
  }

  return (
    <div className="package-selector">
      <h5 className="mb-3">
        <FaBox className="me-2" />
        Paquetes Especiales (Opcional)
      </h5>
      <p className="text-muted small mb-3">
        Selecciona un paquete para obtener múltiples servicios con descuento
      </p>

      <Row>
        {packages.map((pkg) => {
          const isSelected = selectedPackage && selectedPackage.id === pkg.id;

          return (
            <Col key={pkg.id} md={6} className="mb-3">
              <Card
                className={`package-card h-100 ${isSelected ? 'border-primary selected' : ''}`}
                onClick={() => handlePackageToggle(pkg)}
                style={{ cursor: 'pointer', transition: 'all 0.3s' }}
              >
                <Card.Body>
                  <div className="d-flex justify-content-between align-items-start mb-2">
                    <div>
                      <Card.Title className="h6 mb-1">{pkg.name}</Card.Title>
                      {pkg.discountPercentage > 0 && (
                        <Badge bg="danger" className="d-inline-flex align-items-center">
                          <FaPercent className="me-1" size={10} />
                          {pkg.discountPercentage}% OFF
                        </Badge>
                      )}
                    </div>
                    {isSelected && (
                      <FaCheckCircle className="text-primary" size={24} />
                    )}
                  </div>

                  <Card.Text className="text-muted small mb-2">
                    {pkg.description}
                  </Card.Text>

                  <div className="mb-2">
                    <small className="text-muted d-flex align-items-center mb-1">
                      <FaTags className="me-1" />
                      {pkg.serviceCount} Servicio{pkg.serviceCount !== 1 ? 's' : ''} incluido{pkg.serviceCount !== 1 ? 's' : ''}:
                    </small>
                    <ListGroup variant="flush" className="small">
                      {pkg.services.map((service, index) => (
                        <ListGroup.Item key={index} className="px-0 py-1 border-0 bg-transparent">
                          <span className="text-success me-1">✓</span>
                          {service.name}
                        </ListGroup.Item>
                      ))}
                    </ListGroup>
                  </div>

                  <hr className="my-2" />

                  <div>
                    {pkg.discountPercentage > 0 && (
                      <div className="mb-1">
                        <small className="text-muted text-decoration-line-through">
                          ${pkg.originalPrice.toFixed(2)} MXN
                        </small>
                      </div>
                    )}
                    <div className="d-flex justify-content-between align-items-center">
                      <div>
                        <div className="text-primary fw-bold fs-5">
                          {pkg.formattedFinalPrice}
                        </div>
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
          );
        })}
      </Row>

      {selectedPackage && (
        <Card className="bg-light border-primary mt-3">
          <Card.Body>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <FaCheckCircle className="text-primary me-2" />
                <strong>Paquete seleccionado:</strong> {selectedPackage.name}
              </div>
              <div className="text-primary fw-bold fs-5">
                +{selectedPackage.formattedFinalPrice}
              </div>
            </div>
          </Card.Body>
        </Card>
      )}
    </div>
  );
};

PackageSelector.propTypes = {
  selectedPackage: PropTypes.object,
  onPackageChange: PropTypes.func.isRequired
};

export default PackageSelector;
