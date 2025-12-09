import { Form, Row, Col, Button } from 'react-bootstrap';
import { FaSearch } from 'react-icons/fa';
import PropTypes from 'prop-types';

const CustomerForm = ({ customer, onChange, onSearchByEmail, searching, errors }) => {
  const handleChange = (field, value) => {
    onChange({
      ...customer,
      [field]: value
    });
  };

  const handleSearchClick = () => {
    if (customer.email) {
      onSearchByEmail(customer.email);
    }
  };

  return (
    <div className="customer-form">
      <h5 className="mb-3">Información del Huésped</h5>

      <Row>
        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Email <span className="text-danger">*</span>
            </Form.Label>
            <div className="d-flex gap-2">
              <Form.Control
                type="email"
                placeholder="correo@ejemplo.com"
                value={customer.email}
                onChange={(e) => handleChange('email', e.target.value)}
                isInvalid={!!errors?.email}
                required
              />
              <Button
                variant="outline-primary"
                onClick={handleSearchClick}
                disabled={!customer.email || searching}
              >
                <FaSearch />
              </Button>
            </div>
            <Form.Control.Feedback type="invalid">
              {errors?.email}
            </Form.Control.Feedback>
            <Form.Text className="text-muted">
              Si ya eres cliente, ingresa tu email y presiona buscar
            </Form.Text>
          </Form.Group>
        </Col>

        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Teléfono <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              type="tel"
              placeholder="+52 951 123 4567"
              value={customer.phone}
              onChange={(e) => handleChange('phone', e.target.value)}
              isInvalid={!!errors?.phone}
              required
            />
            <Form.Control.Feedback type="invalid">
              {errors?.phone}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>

      <Row>
        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Nombre(s) <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              type="text"
              placeholder="Juan"
              value={customer.firstName}
              onChange={(e) => handleChange('firstName', e.target.value)}
              isInvalid={!!errors?.firstName}
              required
            />
            <Form.Control.Feedback type="invalid">
              {errors?.firstName}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>

        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Apellido(s) <span className="text-danger">*</span>
            </Form.Label>
            <Form.Control
              type="text"
              placeholder="García López"
              value={customer.lastName}
              onChange={(e) => handleChange('lastName', e.target.value)}
              isInvalid={!!errors?.lastName}
              required
            />
            <Form.Control.Feedback type="invalid">
              {errors?.lastName}
            </Form.Control.Feedback>
          </Form.Group>
        </Col>
      </Row>

      <Row>
        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>Nivel de Lealtad</Form.Label>
            <Form.Select
              value={customer.loyaltyLevel}
              onChange={(e) => handleChange('loyaltyLevel', e.target.value)}
            >
              <option value="REGULAR">Regular (Sin descuento)</option>
              <option value="SILVER">Silver (5% descuento)</option>
              <option value="GOLD">Gold (10% descuento)</option>
              <option value="PLATINUM">Platinum (20% descuento)</option>
            </Form.Select>
            <Form.Text className="text-muted">
              El nivel de lealtad aplica descuentos automáticos
            </Form.Text>
          </Form.Group>
        </Col>
      </Row>
    </div>
  );
};

CustomerForm.propTypes = {
  customer: PropTypes.shape({
    email: PropTypes.string.isRequired,
    phone: PropTypes.string.isRequired,
    firstName: PropTypes.string.isRequired,
    lastName: PropTypes.string.isRequired,
    loyaltyLevel: PropTypes.string.isRequired
  }).isRequired,
  onChange: PropTypes.func.isRequired,
  onSearchByEmail: PropTypes.func.isRequired,
  searching: PropTypes.bool,
  errors: PropTypes.object
};

export default CustomerForm;
