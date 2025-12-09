import { useState } from 'react';
import { Form, Row, Col, Button, Card } from 'react-bootstrap';
import { FaFilter, FaRedo } from 'react-icons/fa';
import PropTypes from 'prop-types';

const RoomFilters = ({ onFilterChange, onReset }) => {
  const [filters, setFilters] = useState({
    roomType: '',
    minPrice: '',
    maxPrice: '',
    minCapacity: '',
    availableOnly: false
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const newFilters = {
      ...filters,
      [name]: type === 'checkbox' ? checked : value
    };
    setFilters(newFilters);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onFilterChange(filters);
  };

  const handleReset = () => {
    const resetFilters = {
      roomType: '',
      minPrice: '',
      maxPrice: '',
      minCapacity: '',
      availableOnly: false
    };
    setFilters(resetFilters);
    onReset();
  };

  return (
    <Card className="mb-4 shadow-sm">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h5 className="mb-0">
            <FaFilter className="me-2 text-primary" />
            Filtrar Habitaciones
          </h5>
          <Button
            variant="outline-secondary"
            size="sm"
            onClick={handleReset}
          >
            <FaRedo className="me-1" />
            Limpiar
          </Button>
        </div>

        <Form onSubmit={handleSubmit}>
          <Row>
            <Col md={3} sm={6} className="mb-3">
              <Form.Group>
                <Form.Label>Tipo de Habitación</Form.Label>
                <Form.Select
                  name="roomType"
                  value={filters.roomType}
                  onChange={handleChange}
                >
                  <option value="">Todas</option>
                  <option value="SINGLE">Individual</option>
                  <option value="DOUBLE">Doble</option>
                  <option value="SUITE">Suite</option>
                  <option value="PRESIDENTIAL">Presidencial</option>
                </Form.Select>
              </Form.Group>
            </Col>

            <Col md={2} sm={6} className="mb-3">
              <Form.Group>
                <Form.Label>Precio Mínimo</Form.Label>
                <Form.Control
                  type="number"
                  name="minPrice"
                  value={filters.minPrice}
                  onChange={handleChange}
                  placeholder="$0"
                  min="0"
                />
              </Form.Group>
            </Col>

            <Col md={2} sm={6} className="mb-3">
              <Form.Group>
                <Form.Label>Precio Máximo</Form.Label>
                <Form.Control
                  type="number"
                  name="maxPrice"
                  value={filters.maxPrice}
                  onChange={handleChange}
                  placeholder="$10000"
                  min="0"
                />
              </Form.Group>
            </Col>

            <Col md={2} sm={6} className="mb-3">
              <Form.Group>
                <Form.Label>Capacidad Mínima</Form.Label>
                <Form.Control
                  type="number"
                  name="minCapacity"
                  value={filters.minCapacity}
                  onChange={handleChange}
                  placeholder="1"
                  min="1"
                  max="10"
                />
              </Form.Group>
            </Col>

            <Col md={2} sm={6} className="mb-3 d-flex align-items-end">
              <Form.Group>
                <Form.Check
                  type="checkbox"
                  name="availableOnly"
                  label="Solo disponibles"
                  checked={filters.availableOnly}
                  onChange={handleChange}
                />
              </Form.Group>
            </Col>

            <Col md={1} sm={6} className="mb-3 d-flex align-items-end">
              <Button
                type="submit"
                variant="primary"
                className="w-100"
              >
                <FaFilter />
              </Button>
            </Col>
          </Row>
        </Form>
      </Card.Body>
    </Card>
  );
};

RoomFilters.propTypes = {
  onFilterChange: PropTypes.func.isRequired,
  onReset: PropTypes.func.isRequired
};

export default RoomFilters;
