import { Modal, Button, Row, Col, ListGroup, Badge } from 'react-bootstrap';
import { FaTimes, FaBed, FaCalendar, FaUsers, FaMapMarkerAlt, FaTag, FaMoneyBillWave } from 'react-icons/fa';
import PropTypes from 'prop-types';
import { format } from 'date-fns';
import { es } from 'date-fns/locale';

const ReservationDetailsModal = ({ show, onHide, reservation }) => {
  if (!reservation) return null;

  const formatDate = (dateString) => {
    try {
      return format(new Date(dateString), "EEEE, dd 'de' MMMM yyyy", { locale: es });
    } catch {
      return dateString;
    }
  };

  const formatDateTime = (dateString) => {
    try {
      return format(new Date(dateString), "dd/MM/yyyy HH:mm", { locale: es });
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

  const statusConfig = {
    PENDING: { label: 'Pendiente', color: 'warning' },
    CONFIRMED: { label: 'Confirmada', color: 'success' },
    CANCELLED: { label: 'Cancelada', color: 'danger' },
    COMPLETED: { label: 'Completada', color: 'secondary' }
  };

  const loyaltyLabels = {
    REGULAR: 'Regular',
    SILVER: 'Silver',
    GOLD: 'Gold',
    PLATINUM: 'Platinum'
  };

  const roomTypeNames = {
    SINGLE: 'Individual',
    DOUBLE: 'Doble',
    SUITE: 'Suite',
    PRESIDENTIAL: 'Presidencial'
  };

  const nights = calculateNights();
  const status = statusConfig[reservation.status] || statusConfig.PENDING;

  return (
    <Modal show={show} onHide={onHide} size="lg" centered>
      <Modal.Header closeButton className="bg-primary text-white">
        <Modal.Title>
          Detalles de Reserva #{reservation.id}
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>
        {/* Estado */}
        <div className="mb-4 text-center">
          <Badge bg={status.color} className="px-4 py-2 fs-6">
            {status.label}
          </Badge>
        </div>

        <Row>
          {/* Información de la Habitación */}
          <Col md={6} className="mb-4">
            <h6 className="text-primary mb-3">
              <FaBed className="me-2" />
              Habitación
            </h6>
            <ListGroup variant="flush">
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Número:</span>
                <strong>Habitación {reservation.room?.roomNumber}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Tipo:</span>
                <strong>{roomTypeNames[reservation.room?.roomType] || 'N/A'}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Piso:</span>
                <strong>Piso {reservation.room?.floor}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Precio/noche:</span>
                <strong>${reservation.room?.price?.toLocaleString('es-MX')} MXN</strong>
              </ListGroup.Item>
            </ListGroup>
          </Col>

          {/* Información del Huésped */}
          <Col md={6} className="mb-4">
            <h6 className="text-primary mb-3">
              <FaUsers className="me-2" />
              Huésped
            </h6>
            <ListGroup variant="flush">
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Nombre:</span>
                <strong>{reservation.customer?.firstName} {reservation.customer?.lastName}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Email:</span>
                <strong className="text-break">{reservation.customer?.email}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Teléfono:</span>
                <strong>{reservation.customer?.phone}</strong>
              </ListGroup.Item>
              <ListGroup.Item className="px-0 d-flex justify-content-between">
                <span className="text-muted">Nivel:</span>
                <Badge bg="info">{loyaltyLabels[reservation.customer?.loyaltyLevel] || 'N/A'}</Badge>
              </ListGroup.Item>
            </ListGroup>
          </Col>
        </Row>

        {/* Fechas de Estadía */}
        <div className="mb-4">
          <h6 className="text-primary mb-3">
            <FaCalendar className="me-2" />
            Estadía
          </h6>
          <ListGroup variant="flush">
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Check-in:</span>
              <strong>{formatDate(reservation.checkInDate)}</strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Check-out:</span>
              <strong>{formatDate(reservation.checkOutDate)}</strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Noches:</span>
              <strong>{nights} {nights === 1 ? 'noche' : 'noches'}</strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Huéspedes:</span>
              <strong>{reservation.numberOfGuests} {reservation.numberOfGuests === 1 ? 'persona' : 'personas'}</strong>
            </ListGroup.Item>
          </ListGroup>
        </div>

        {/* Servicios Adicionales */}
        {reservation.additionalServices && reservation.additionalServices.length > 0 && (
          <div className="mb-4">
            <h6 className="text-primary mb-3">
              <FaTag className="me-2" />
              Servicios Adicionales
            </h6>
            <ListGroup variant="flush">
              {reservation.additionalServices.map((service, idx) => (
                <ListGroup.Item key={idx} className="px-0 d-flex justify-content-between">
                  <span>{service.name}</span>
                  <strong className="text-muted">${service.price?.toLocaleString('es-MX')} MXN</strong>
                </ListGroup.Item>
              ))}
            </ListGroup>
          </div>
        )}

        {/* Precio Total */}
        <div className="mb-4">
          <h6 className="text-primary mb-3">
            <FaMoneyBillWave className="me-2" />
            Desglose de Precio
          </h6>
          <ListGroup variant="flush">
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Subtotal:</span>
              <strong>${reservation.totalPrice?.toLocaleString('es-MX')} MXN</strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between border-top-2">
              <span className="fw-bold">Total:</span>
              <h5 className="mb-0 text-primary">${reservation.totalPrice?.toLocaleString('es-MX')} MXN</h5>
            </ListGroup.Item>
          </ListGroup>
        </div>

        {/* Fechas de Sistema */}
        <div className="border-top pt-3">
          <small className="text-muted d-block">
            Creada: {formatDateTime(reservation.createdAt)}
          </small>
          <small className="text-muted d-block">
            Última actualización: {formatDateTime(reservation.updatedAt)}
          </small>
        </div>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onHide}>
          <FaTimes className="me-2" />
          Cerrar
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

ReservationDetailsModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  reservation: PropTypes.object
};

export default ReservationDetailsModal;
