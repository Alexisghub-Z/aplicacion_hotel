import { Form, Row, Col, Button, Card } from 'react-bootstrap';
import { FaFilter, FaRedo } from 'react-icons/fa';
import PropTypes from 'prop-types';

const ReservationFilters = ({ filters, onFilterChange, onReset }) => {
  const handleChange = (field, value) => {
    onFilterChange({
      ...filters,
      [field]: value
    });
  };

  return (
    <Card className="mb-4 shadow-sm">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="mb-0">
            <FaFilter className="me-2 text-primary" />
            Filtrar Reservas
          </h5>
          <Button
            variant="outline-secondary"
            size="sm"
            onClick={onReset}
          >
            <FaRedo className="me-1" />
            Limpiar
          </Button>
        </div>

        <Row>
          <Col md={4} sm={6} className="mb-3">
            <Form.Group>
              <Form.Label>Estado</Form.Label>
              <Form.Select
                value={filters.status}
                onChange={(e) => handleChange('status', e.target.value)}
              >
                <option value="">Todos</option>
                <option value="PENDING">Pendiente</option>
                <option value="CONFIRMED">Confirmada</option>
                <option value="CANCELLED">Cancelada</option>
                <option value="COMPLETED">Completada</option>
              </Form.Select>
            </Form.Group>
          </Col>

          <Col md={4} sm={6} className="mb-3">
            <Form.Group>
              <Form.Label>Ordenar por</Form.Label>
              <Form.Select
                value={filters.sortBy}
                onChange={(e) => handleChange('sortBy', e.target.value)}
              >
                <option value="newest">Más recientes</option>
                <option value="oldest">Más antiguas</option>
                <option value="checkin">Próximas (Check-in)</option>
                <option value="price-high">Precio (Mayor a menor)</option>
                <option value="price-low">Precio (Menor a mayor)</option>
              </Form.Select>
            </Form.Group>
          </Col>

          <Col md={4} sm={12} className="mb-3">
            <Form.Group>
              <Form.Label>Buscar por habitación</Form.Label>
              <Form.Control
                type="text"
                placeholder="Ej: 203"
                value={filters.roomNumber}
                onChange={(e) => handleChange('roomNumber', e.target.value)}
              />
            </Form.Group>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

ReservationFilters.propTypes = {
  filters: PropTypes.shape({
    status: PropTypes.string,
    sortBy: PropTypes.string,
    roomNumber: PropTypes.string
  }).isRequired,
  onFilterChange: PropTypes.func.isRequired,
  onReset: PropTypes.func.isRequired
};

export default ReservationFilters;
