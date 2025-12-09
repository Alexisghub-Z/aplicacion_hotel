import { useState } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Badge, Spinner, Modal } from 'react-bootstrap';
import { FaSearch, FaCalendarAlt, FaUser, FaBed, FaDollarSign, FaCreditCard, FaPaypal, FaMoneyBillWave, FaCheckCircle, FaTimes, FaExclamationTriangle } from 'react-icons/fa';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const statusColors = {
  PENDING: 'warning',
  CONFIRMED: 'success',
  CANCELLED: 'danger',
  COMPLETED: 'info'
};

const statusLabels = {
  PENDING: 'Pendiente',
  CONFIRMED: 'Confirmada',
  CANCELLED: 'Cancelada',
  COMPLETED: 'Completada'
};

function ClientReservations() {
  const [email, setEmail] = useState('');
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [searched, setSearched] = useState(false);

  // Estados para el modal de pago
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [selectedReservation, setSelectedReservation] = useState(null);
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState('');
  const [paymentLoading, setPaymentLoading] = useState(false);
  const [paymentSuccess, setPaymentSuccess] = useState(false);
  const [paymentError, setPaymentError] = useState('');

  // Estados para el modal de cancelación
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [cancelLoading, setCancelLoading] = useState(false);
  const [cancelSuccess, setCancelSuccess] = useState(false);
  const [cancelError, setCancelError] = useState('');

  const handleSearch = async (e) => {
    e.preventDefault();

    if (!email || !email.includes('@')) {
      setError('Por favor ingresa un email válido');
      return;
    }

    setLoading(true);
    setError('');
    setSearched(true);

    try {
      const response = await axios.get(`${API_URL}/reservations/search`, {
        params: { email: email.trim() }
      });
      setReservations(response.data);
    } catch (err) {
      setError('Error al buscar reservaciones. Por favor intenta de nuevo.');
      console.error('Error searching reservations:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-MX', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  const handleOpenPaymentModal = (reservation) => {
    setSelectedReservation(reservation);
    setSelectedPaymentMethod('');
    setPaymentSuccess(false);
    setPaymentError('');
    setShowPaymentModal(true);
  };

  const handleClosePaymentModal = () => {
    setShowPaymentModal(false);
    setSelectedReservation(null);
    setSelectedPaymentMethod('');
    setPaymentSuccess(false);
    setPaymentError('');
  };

  const handlePayment = async () => {
    if (!selectedPaymentMethod) {
      setPaymentError('Por favor selecciona un método de pago');
      return;
    }

    setPaymentLoading(true);
    setPaymentError('');

    try {
      const response = await axios.post(`${API_URL}/payments`, null, {
        params: {
          reservationId: selectedReservation.id,
          paymentMethod: selectedPaymentMethod
        }
      });

      setPaymentSuccess(true);

      // Actualizar el estado de la reservación en la lista
      const updatedReservations = reservations.map(r =>
        r.id === selectedReservation.id
          ? { ...r, status: 'CONFIRMED' }
          : r
      );
      setReservations(updatedReservations);

      // Cerrar modal después de 2 segundos
      setTimeout(() => {
        handleClosePaymentModal();
      }, 2000);
    } catch (err) {
      setPaymentError('Error al procesar el pago. Por favor intenta de nuevo.');
      console.error('Error processing payment:', err);
    } finally {
      setPaymentLoading(false);
    }
  };

  const paymentMethods = [
    { value: 'CREDIT_CARD', label: 'Tarjeta de Crédito/Débito', icon: FaCreditCard },
    { value: 'PAYPAL', label: 'PayPal', icon: FaPaypal },
    { value: 'CASH', label: 'Efectivo (en recepción)', icon: FaMoneyBillWave }
  ];

  const handleOpenCancelModal = (reservation) => {
    setSelectedReservation(reservation);
    setCancelSuccess(false);
    setCancelError('');
    setShowCancelModal(true);
  };

  const handleCloseCancelModal = () => {
    setShowCancelModal(false);
    setSelectedReservation(null);
    setCancelSuccess(false);
    setCancelError('');
  };

  const handleCancelReservation = async () => {
    setCancelLoading(true);
    setCancelError('');

    try {
      await axios.patch(`${API_URL}/reservations/${selectedReservation.id}/cancel`);

      setCancelSuccess(true);

      // Actualizar el estado de la reservación en la lista
      const updatedReservations = reservations.map(r =>
        r.id === selectedReservation.id
          ? { ...r, status: 'CANCELLED' }
          : r
      );
      setReservations(updatedReservations);

      // Cerrar modal después de 2 segundos
      setTimeout(() => {
        handleCloseCancelModal();
      }, 2000);
    } catch (err) {
      setCancelError('Error al cancelar la reservación. Por favor intenta de nuevo.');
      console.error('Error cancelling reservation:', err);
    } finally {
      setCancelLoading(false);
    }
  };

  return (
    <Container className="py-5">
      <Row className="mb-4">
        <Col>
          <h1 className="text-center mb-2">Mis Reservaciones</h1>
          <p className="text-center text-muted">
            Consulta tus reservaciones ingresando tu email
          </p>
        </Col>
      </Row>

      <Row className="justify-content-center mb-4">
        <Col md={8} lg={6}>
          <Card className="shadow-sm">
            <Card.Body>
              <Form onSubmit={handleSearch}>
                <Form.Group className="mb-3">
                  <Form.Label>Email de Reservación</Form.Label>
                  <div className="d-flex gap-2">
                    <Form.Control
                      type="email"
                      placeholder="tu@email.com"
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                    <Button
                      type="submit"
                      variant="primary"
                      disabled={loading}
                    >
                      {loading ? (
                        <Spinner animation="border" size="sm" />
                      ) : (
                        <>
                          <FaSearch className="me-1" /> Buscar
                        </>
                      )}
                    </Button>
                  </div>
                  <Form.Text className="text-muted">
                    Ingresa el email que usaste al hacer la reservación
                  </Form.Text>
                </Form.Group>
              </Form>

              {error && (
                <Alert variant="danger" className="mb-0">
                  {error}
                </Alert>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {searched && !loading && (
        <>
          {reservations.length === 0 ? (
            <Row className="justify-content-center">
              <Col md={8}>
                <Alert variant="info" className="text-center">
                  No se encontraron reservaciones para el email <strong>{email}</strong>
                </Alert>
              </Col>
            </Row>
          ) : (
            <>
              <Row className="mb-3">
                <Col>
                  <h4>
                    Se encontraron {reservations.length} reservación{reservations.length !== 1 ? 'es' : ''}
                  </h4>
                </Col>
              </Row>

              <Row>
                {reservations.map((reservation) => (
                  <Col key={reservation.id} md={6} lg={4} className="mb-4">
                    <Card className="h-100 shadow-sm">
                      <Card.Header className="d-flex justify-content-between align-items-center">
                        <span className="fw-bold">Reservación #{reservation.id}</span>
                        <Badge bg={statusColors[reservation.status]}>
                          {statusLabels[reservation.status]}
                        </Badge>
                      </Card.Header>
                      <Card.Body>
                        <div className="mb-3">
                          <small className="text-muted d-flex align-items-center mb-1">
                            <FaCalendarAlt className="me-2" /> Check-in
                          </small>
                          <div className="fw-semibold">
                            {formatDate(reservation.checkInDate)}
                          </div>
                        </div>

                        <div className="mb-3">
                          <small className="text-muted d-flex align-items-center mb-1">
                            <FaCalendarAlt className="me-2" /> Check-out
                          </small>
                          <div className="fw-semibold">
                            {formatDate(reservation.checkOutDate)}
                          </div>
                        </div>

                        <div className="mb-3">
                          <small className="text-muted d-flex align-items-center mb-1">
                            <FaBed className="me-2" /> Habitación
                          </small>
                          <div className="fw-semibold">
                            Habitación #{reservation.roomId}
                          </div>
                        </div>

                        <div className="mb-3">
                          <small className="text-muted d-flex align-items-center mb-1">
                            <FaUser className="me-2" /> Huéspedes
                          </small>
                          <div className="fw-semibold">
                            {reservation.numberOfGuests} persona{reservation.numberOfGuests !== 1 ? 's' : ''}
                          </div>
                        </div>

                        <div className="mb-3">
                          <small className="text-muted d-flex align-items-center mb-1">
                            <FaCalendarAlt className="me-2" /> Noches
                          </small>
                          <div className="fw-semibold">
                            {reservation.numberOfNights} noche{reservation.numberOfNights !== 1 ? 's' : ''}
                          </div>
                        </div>

                        <hr />

                        <div className="d-flex justify-content-between align-items-center mb-3">
                          <span className="text-muted d-flex align-items-center">
                            <FaDollarSign className="me-1" /> Total
                          </span>
                          <span className="fs-4 fw-bold text-primary">
                            ${reservation.totalPrice.toFixed(2)}
                          </span>
                        </div>

                        {reservation.status === 'PENDING' && (
                          <>
                            <Button
                              variant="success"
                              className="w-100 mb-2"
                              onClick={() => handleOpenPaymentModal(reservation)}
                            >
                              <FaCreditCard className="me-2" />
                              Pagar Ahora
                            </Button>
                            <Button
                              variant="outline-danger"
                              className="w-100"
                              onClick={() => handleOpenCancelModal(reservation)}
                            >
                              <FaTimes className="me-2" />
                              Cancelar Reservación
                            </Button>
                          </>
                        )}

                        {reservation.status === 'CONFIRMED' && (
                          <>
                            <Alert variant="success" className="mb-2 py-2 text-center">
                              <FaCheckCircle className="me-2" />
                              Pagada
                            </Alert>
                            <Button
                              variant="outline-danger"
                              className="w-100"
                              onClick={() => handleOpenCancelModal(reservation)}
                            >
                              <FaTimes className="me-2" />
                              Cancelar Reservación
                            </Button>
                          </>
                        )}

                        {reservation.status === 'CANCELLED' && (
                          <Alert variant="danger" className="mb-0 py-2 text-center">
                            <FaTimes className="me-2" />
                            Cancelada
                          </Alert>
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                ))}
              </Row>
            </>
          )}
        </>
      )}

      {/* Modal de Pago */}
      <Modal show={showPaymentModal} onHide={handleClosePaymentModal} centered>
        <Modal.Header closeButton>
          <Modal.Title>Pagar Reservación #{selectedReservation?.id}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {paymentSuccess ? (
            <div className="text-center py-4">
              <FaCheckCircle size={60} className="text-success mb-3" />
              <h4 className="text-success mb-2">¡Pago Exitoso!</h4>
              <p className="text-muted">
                Tu reservación ha sido confirmada. Recibirás un email de confirmación.
              </p>
            </div>
          ) : (
            <>
              <div className="mb-4 p-3 bg-light rounded">
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Check-in:</span>
                  <span className="fw-semibold">{formatDate(selectedReservation?.checkInDate)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Check-out:</span>
                  <span className="fw-semibold">{formatDate(selectedReservation?.checkOutDate)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Noches:</span>
                  <span className="fw-semibold">{selectedReservation?.numberOfNights}</span>
                </div>
                <hr />
                <div className="d-flex justify-content-between align-items-center">
                  <span className="fs-5 fw-bold">Total a Pagar:</span>
                  <span className="fs-4 fw-bold text-success">
                    ${selectedReservation?.totalPrice.toFixed(2)}
                  </span>
                </div>
              </div>

              <Form.Group className="mb-3">
                <Form.Label className="fw-semibold">Método de Pago</Form.Label>
                {paymentMethods.map((method) => {
                  const Icon = method.icon;
                  return (
                    <Card
                      key={method.value}
                      className={`mb-2 cursor-pointer ${selectedPaymentMethod === method.value ? 'border-success border-2' : ''}`}
                      onClick={() => setSelectedPaymentMethod(method.value)}
                      style={{ cursor: 'pointer' }}
                    >
                      <Card.Body className="d-flex align-items-center py-2">
                        <Form.Check
                          type="radio"
                          name="paymentMethod"
                          checked={selectedPaymentMethod === method.value}
                          onChange={() => setSelectedPaymentMethod(method.value)}
                          className="me-3"
                        />
                        <Icon size={24} className="me-3 text-primary" />
                        <span>{method.label}</span>
                      </Card.Body>
                    </Card>
                  );
                })}
              </Form.Group>

              {paymentError && (
                <Alert variant="danger" className="mb-3">
                  {paymentError}
                </Alert>
              )}
            </>
          )}
        </Modal.Body>
        {!paymentSuccess && (
          <Modal.Footer>
            <Button variant="secondary" onClick={handleClosePaymentModal}>
              Cancelar
            </Button>
            <Button
              variant="success"
              onClick={handlePayment}
              disabled={paymentLoading || !selectedPaymentMethod}
            >
              {paymentLoading ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Procesando...
                </>
              ) : (
                <>
                  <FaCreditCard className="me-2" />
                  Confirmar Pago
                </>
              )}
            </Button>
          </Modal.Footer>
        )}
      </Modal>

      {/* Modal de Cancelación */}
      <Modal show={showCancelModal} onHide={handleCloseCancelModal} centered>
        <Modal.Header closeButton className="border-0">
          <Modal.Title>
            <FaExclamationTriangle className="text-warning me-2" />
            Cancelar Reservación #{selectedReservation?.id}
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {cancelSuccess ? (
            <div className="text-center py-4">
              <FaCheckCircle size={60} className="text-success mb-3" />
              <h4 className="text-success mb-2">¡Reservación Cancelada!</h4>
              <p className="text-muted">
                Tu reservación ha sido cancelada exitosamente. Recibirás un email de confirmación.
              </p>
            </div>
          ) : (
            <>
              <div className="alert alert-warning">
                <FaExclamationTriangle className="me-2" />
                <strong>¿Estás seguro de que deseas cancelar esta reservación?</strong>
              </div>

              <div className="mb-3 p-3 bg-light rounded">
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Reservación:</span>
                  <span className="fw-semibold">#{selectedReservation?.id}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Check-in:</span>
                  <span className="fw-semibold">{formatDate(selectedReservation?.checkInDate)}</span>
                </div>
                <div className="d-flex justify-content-between mb-2">
                  <span className="text-muted">Check-out:</span>
                  <span className="fw-semibold">{formatDate(selectedReservation?.checkOutDate)}</span>
                </div>
                <div className="d-flex justify-content-between">
                  <span className="text-muted">Total:</span>
                  <span className="fw-semibold">${selectedReservation?.totalPrice.toFixed(2)}</span>
                </div>
              </div>

              <p className="text-muted mb-0">
                <small>Esta acción no se puede deshacer. Si ya realizaste el pago, el proceso de reembolso se iniciará automáticamente.</small>
              </p>

              {cancelError && (
                <Alert variant="danger" className="mt-3 mb-0">
                  {cancelError}
                </Alert>
              )}
            </>
          )}
        </Modal.Body>
        {!cancelSuccess && (
          <Modal.Footer className="border-0">
            <Button variant="secondary" onClick={handleCloseCancelModal}>
              No, Mantener Reservación
            </Button>
            <Button
              variant="danger"
              onClick={handleCancelReservation}
              disabled={cancelLoading}
            >
              {cancelLoading ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Cancelando...
                </>
              ) : (
                <>
                  <FaTimes className="me-2" />
                  Sí, Cancelar Reservación
                </>
              )}
            </Button>
          </Modal.Footer>
        )}
      </Modal>
    </Container>
  );
}

export default ClientReservations;
