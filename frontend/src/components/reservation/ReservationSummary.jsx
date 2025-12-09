import { Card, ListGroup, Badge } from 'react-bootstrap';
import { FaBed, FaCalendar, FaUsers, FaMoon, FaTag } from 'react-icons/fa';
import PropTypes from 'prop-types';

const ReservationSummary = ({ room, checkInDate, checkOutDate, numberOfGuests, customer, selectedServices }) => {
  const roomTypeNames = {
    SINGLE: 'Individual',
    DOUBLE: 'Doble',
    SUITE: 'Suite',
    PRESIDENTIAL: 'Presidencial'
  };

  const loyaltyDiscounts = {
    REGULAR: 0,
    SILVER: 0.05,
    GOLD: 0.10,
    PLATINUM: 0.20
  };

  const loyaltyLabels = {
    REGULAR: 'Regular (Sin descuento)',
    SILVER: 'Silver (5% descuento)',
    GOLD: 'Gold (10% descuento)',
    PLATINUM: 'Platinum (20% descuento)'
  };

  const calculateNights = () => {
    if (checkInDate && checkOutDate) {
      const checkIn = new Date(checkInDate);
      const checkOut = new Date(checkOutDate);
      const diffTime = checkOut - checkIn;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays > 0 ? diffDays : 0;
    }
    return 0;
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('es-MX', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const nights = calculateNights();
  const roomSubtotal = room ? room.price * nights : 0;
  const servicesSubtotal = selectedServices.reduce((sum, service) => sum + service.price, 0);
  const subtotal = roomSubtotal + servicesSubtotal;
  const discount = customer ? subtotal * loyaltyDiscounts[customer.loyaltyLevel] : 0;
  const total = subtotal - discount;

  return (
    <Card className="reservation-summary shadow-sm sticky-top" style={{ top: '100px' }}>
      <Card.Header className="bg-primary text-white">
        <h5 className="mb-0">Resumen de Reserva</h5>
      </Card.Header>

      <Card.Body>
        {room && (
          <div className="mb-3">
            <h6 className="text-primary mb-2">
              <FaBed className="me-2" />
              Habitación
            </h6>
            <div className="d-flex justify-content-between align-items-start mb-2">
              <div>
                <div className="fw-bold">Habitación {room.roomNumber}</div>
                <small className="text-muted">{roomTypeNames[room.roomType]}</small>
              </div>
              <Badge bg="primary">${room.price.toLocaleString('es-MX')}/noche</Badge>
            </div>
          </div>
        )}

        {checkInDate && checkOutDate && (
          <div className="mb-3">
            <h6 className="text-primary mb-2">
              <FaCalendar className="me-2" />
              Fechas
            </h6>
            <ListGroup variant="flush" className="small">
              <ListGroup.Item className="px-0 py-1">
                <strong>Check-in:</strong> {formatDate(checkInDate)}
              </ListGroup.Item>
              <ListGroup.Item className="px-0 py-1">
                <strong>Check-out:</strong> {formatDate(checkOutDate)}
              </ListGroup.Item>
              {nights > 0 && (
                <ListGroup.Item className="px-0 py-1">
                  <FaMoon className="me-2" />
                  <strong>{nights}</strong> {nights === 1 ? 'noche' : 'noches'}
                </ListGroup.Item>
              )}
            </ListGroup>
          </div>
        )}

        {numberOfGuests > 0 && (
          <div className="mb-3">
            <h6 className="text-primary mb-2">
              <FaUsers className="me-2" />
              Huéspedes
            </h6>
            <div>{numberOfGuests} {numberOfGuests === 1 ? 'persona' : 'personas'}</div>
          </div>
        )}

        {selectedServices.length > 0 && (
          <div className="mb-3">
            <h6 className="text-primary mb-2">Servicios Adicionales</h6>
            <ListGroup variant="flush" className="small">
              {selectedServices.map((service) => (
                <ListGroup.Item key={service.id} className="px-0 py-1 d-flex justify-content-between">
                  <span>{service.name}</span>
                  <span className="text-muted">${service.price.toLocaleString('es-MX')}</span>
                </ListGroup.Item>
              ))}
            </ListGroup>
          </div>
        )}

        <hr />

        <div className="pricing-breakdown">
          {room && nights > 0 && (
            <div className="d-flex justify-content-between mb-2">
              <span>Habitación ({nights} {nights === 1 ? 'noche' : 'noches'})</span>
              <span>${roomSubtotal.toLocaleString('es-MX')}</span>
            </div>
          )}

          {selectedServices.length > 0 && (
            <div className="d-flex justify-content-between mb-2">
              <span>Servicios adicionales</span>
              <span>${servicesSubtotal.toLocaleString('es-MX')}</span>
            </div>
          )}

          {subtotal > 0 && (
            <div className="d-flex justify-content-between mb-2">
              <strong>Subtotal</strong>
              <strong>${subtotal.toLocaleString('es-MX')}</strong>
            </div>
          )}

          {customer && discount > 0 && (
            <div className="d-flex justify-content-between mb-2 text-success">
              <span>
                <FaTag className="me-1" />
                Descuento {loyaltyLabels[customer.loyaltyLevel]}
              </span>
              <span>-${discount.toLocaleString('es-MX')}</span>
            </div>
          )}

          <hr />

          <div className="d-flex justify-content-between">
            <h5 className="mb-0">Total</h5>
            <h5 className="mb-0 text-primary">${total.toLocaleString('es-MX')} MXN</h5>
          </div>
        </div>
      </Card.Body>
    </Card>
  );
};

ReservationSummary.propTypes = {
  room: PropTypes.object,
  checkInDate: PropTypes.string,
  checkOutDate: PropTypes.string,
  numberOfGuests: PropTypes.number,
  customer: PropTypes.object,
  selectedServices: PropTypes.array
};

ReservationSummary.defaultProps = {
  selectedServices: []
};

export default ReservationSummary;
