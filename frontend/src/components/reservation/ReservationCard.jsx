import { Card, Badge, Button, Row, Col } from 'react-bootstrap';
import { FaBed, FaCalendar, FaUsers, FaMoon, FaEye, FaTimes, FaCheckCircle, FaCreditCard } from 'react-icons/fa';
import PropTypes from 'prop-types';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

const ReservationCard = ({ reservation, onViewDetails, onCancel, onPay, isPaid }) => {
  const statusConfig = {
    PENDING: {
      label: 'Pendiente',
      color: 'warning',
      icon: <FaCalendar />
    },
    CONFIRMED: {
      label: 'Confirmada',
      color: 'success',
      icon: <FaCheckCircle />
    },
    CANCELLED: {
      label: 'Cancelada',
      color: 'danger',
      icon: <FaTimes />
    },
    COMPLETED: {
      label: 'Completada',
      color: 'secondary',
      icon: <FaCheckCircle />
    }
  };

  const config = statusConfig[reservation.status] || statusConfig.PENDING;

  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), "dd 'de' MMMM, yyyy", { locale: es });
    } catch {
      return dateString;
    }
  };

  const calculateNights = () => {
    const checkIn = new Date(reservation.checkInDate);
    const checkOut = new Date(reservation.checkOutDate);
    const diffTime = checkOut - checkIn;
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  };

  const canBeCancelled = reservation.status === 'PENDING' || reservation.status === 'CONFIRMED';
  const canBePaid = (reservation.status === 'PENDING' || reservation.status === 'CONFIRMED') && !isPaid;
  const nights = calculateNights();

  return (
    <Card className="reservation-card shadow-sm mb-3">
      <Card.Body>
        <Row>
          <Col lg={8}>
            <div className="d-flex justify-content-between align-items-start mb-3">
              <div>
                <h5 className="mb-1">
                  <FaBed className="me-2 text-primary" />
                  Habitación {reservation.room?.roomNumber || 'N/A'}
                </h5>
                <small className="text-muted">
                  Reserva #{reservation.id} • {reservation.customer?.firstName} {reservation.customer?.lastName}
                </small>
              </div>
              <Badge bg={config.color} className="d-flex align-items-center gap-1">
                {config.icon}
                {config.label}
              </Badge>
            </div>

            <Row className="mb-3">
              <Col sm={6} className="mb-2">
                <div className="d-flex align-items-center text-muted">
                  <FaCalendar className="me-2" />
                  <div>
                    <small className="d-block">Check-in</small>
                    <strong className="text-dark">{formatDate(reservation.checkInDate)}</strong>
                  </div>
                </div>
              </Col>
              <Col sm={6} className="mb-2">
                <div className="d-flex align-items-center text-muted">
                  <FaCalendar className="me-2" />
                  <div>
                    <small className="d-block">Check-out</small>
                    <strong className="text-dark">{formatDate(reservation.checkOutDate)}</strong>
                  </div>
                </div>
              </Col>
            </Row>

            <div className="d-flex gap-3 mb-3">
              <Badge bg="light" text="dark" className="px-3 py-2">
                <FaMoon className="me-1" />
                {nights} {nights === 1 ? 'noche' : 'noches'}
              </Badge>
              <Badge bg="light" text="dark" className="px-3 py-2">
                <FaUsers className="me-1" />
                {reservation.numberOfGuests} {reservation.numberOfGuests === 1 ? 'huésped' : 'huéspedes'}
              </Badge>
            </div>

            {reservation.additionalServices && reservation.additionalServices.length > 0 && (
              <div className="mb-2">
                <small className="text-muted d-block mb-1">Servicios adicionales:</small>
                <div className="d-flex flex-wrap gap-1">
                  {reservation.additionalServices.slice(0, 3).map((service, idx) => (
                    <Badge key={idx} bg="info" className="text-white">
                      {service.name}
                    </Badge>
                  ))}
                  {reservation.additionalServices.length > 3 && (
                    <Badge bg="secondary">
                      +{reservation.additionalServices.length - 3} más
                    </Badge>
                  )}
                </div>
              </div>
            )}
          </Col>

          <Col lg={4} className="d-flex flex-column justify-content-between align-items-end">
            <div className="text-end mb-3">
              <div className="text-muted small">Total</div>
              <div className="h4 mb-0 text-primary">
                ${reservation.totalPrice?.toLocaleString('es-MX')} MXN
              </div>
              {isPaid && (
                <Badge bg="success" className="mt-2">
                  <FaCheckCircle className="me-1" />
                  Pagado
                </Badge>
              )}
              {!isPaid && canBePaid && (
                <Badge bg="warning" className="mt-2">
                  Pendiente de Pago
                </Badge>
              )}
            </div>

            <div className="d-flex gap-2 flex-wrap justify-content-end">
              <Button
                variant="outline-primary"
                size="sm"
                onClick={() => onViewDetails(reservation)}
              >
                <FaEye className="me-1" />
                Ver Detalles
              </Button>
              {canBePaid && onPay && (
                <Button
                  variant="success"
                  size="sm"
                  onClick={() => onPay(reservation)}
                >
                  <FaCreditCard className="me-1" />
                  Pagar
                </Button>
              )}
              {canBeCancelled && (
                <Button
                  variant="outline-danger"
                  size="sm"
                  onClick={() => onCancel(reservation)}
                >
                  <FaTimes className="me-1" />
                  Cancelar
                </Button>
              )}
            </div>
          </Col>
        </Row>
      </Card.Body>
    </Card>
  );
};

ReservationCard.propTypes = {
  reservation: PropTypes.shape({
    id: PropTypes.number.isRequired,
    status: PropTypes.string.isRequired,
    checkInDate: PropTypes.string.isRequired,
    checkOutDate: PropTypes.string.isRequired,
    numberOfGuests: PropTypes.number.isRequired,
    totalPrice: PropTypes.number.isRequired,
    room: PropTypes.object,
    customer: PropTypes.object,
    additionalServices: PropTypes.array
  }).isRequired,
  onViewDetails: PropTypes.func.isRequired,
  onCancel: PropTypes.func.isRequired,
  onPay: PropTypes.func,
  isPaid: PropTypes.bool
};

export default ReservationCard;
