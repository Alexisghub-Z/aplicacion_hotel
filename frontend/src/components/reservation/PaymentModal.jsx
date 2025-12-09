import { useState } from 'react';
import { Modal, Button, Form, Alert, Spinner } from 'react-bootstrap';
import { FaCreditCard, FaPaypal, FaMoneyBill, FaTimes, FaCheckCircle } from 'react-icons/fa';
import PropTypes from 'prop-types';
import paymentService from '../../services/paymentService';

const PaymentModal = ({ show, onHide, reservation, onPaymentSuccess }) => {
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [processing, setProcessing] = useState(false);
  const [error, setError] = useState(null);
  const [cardNumber, setCardNumber] = useState('');
  const [cardName, setCardName] = useState('');
  const [expiryDate, setExpiryDate] = useState('');
  const [cvv, setCvv] = useState('');
  const [paypalEmail, setPaypalEmail] = useState('');

  if (!reservation) return null;

  const paymentMethods = [
    {
      value: 'CREDIT_CARD',
      label: 'Tarjeta de Crédito',
      icon: <FaCreditCard size={24} />,
      color: '#0d6efd'
    },
    {
      value: 'PAYPAL',
      label: 'PayPal',
      icon: <FaPaypal size={24} />,
      color: '#0070ba'
    },
    {
      value: 'CASH',
      label: 'Efectivo (en recepción)',
      icon: <FaMoneyBill size={24} />,
      color: '#198754'
    }
  ];

  const handlePaymentSubmit = async (e) => {
    e.preventDefault();
    setError(null);

    // Validaciones básicas
    if (paymentMethod === 'CREDIT_CARD') {
      if (!cardNumber || !cardName || !expiryDate || !cvv) {
        setError('Por favor, completa todos los campos de la tarjeta');
        return;
      }
      if (cardNumber.replace(/\s/g, '').length !== 16) {
        setError('El número de tarjeta debe tener 16 dígitos');
        return;
      }
      if (cvv.length !== 3) {
        setError('El CVV debe tener 3 dígitos');
        return;
      }
    }

    if (paymentMethod === 'PAYPAL' && !paypalEmail) {
      setError('Por favor, ingresa tu email de PayPal');
      return;
    }

    try {
      setProcessing(true);

      await paymentService.processPayment(reservation.id, paymentMethod);

      // Notificar éxito
      if (onPaymentSuccess) {
        onPaymentSuccess(reservation);
      }

      // Cerrar modal
      handleClose();
    } catch (err) {
      console.error('Error processing payment:', err);
      setError('Error al procesar el pago. Por favor, verifica tus datos e intenta nuevamente.');
    } finally {
      setProcessing(false);
    }
  };

  const handleClose = () => {
    // Limpiar formulario
    setPaymentMethod('CREDIT_CARD');
    setCardNumber('');
    setCardName('');
    setExpiryDate('');
    setCvv('');
    setPaypalEmail('');
    setError(null);
    onHide();
  };

  const formatCardNumber = (value) => {
    const cleaned = value.replace(/\s/g, '');
    const match = cleaned.match(/.{1,4}/g);
    return match ? match.join(' ') : cleaned;
  };

  const handleCardNumberChange = (e) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 16);
    setCardNumber(formatCardNumber(value));
  };

  const handleExpiryChange = (e) => {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length >= 2) {
      value = value.slice(0, 2) + '/' + value.slice(2, 4);
    }
    setExpiryDate(value.slice(0, 5));
  };

  const handleCvvChange = (e) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 3);
    setCvv(value);
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" centered>
      <Modal.Header closeButton className="bg-success text-white">
        <Modal.Title>
          <FaCreditCard className="me-2" />
          Procesar Pago - Reserva #{reservation.id}
        </Modal.Title>
      </Modal.Header>

      <Form onSubmit={handlePaymentSubmit}>
        <Modal.Body>
          {/* Resumen de Pago */}
          <div className="mb-4 p-3 bg-light rounded">
            <h6 className="text-muted mb-2">Resumen del Pago</h6>
            <div className="d-flex justify-content-between align-items-center">
              <span>Total a pagar:</span>
              <h4 className="mb-0 text-success">
                ${reservation.totalPrice?.toLocaleString('es-MX')} MXN
              </h4>
            </div>
          </div>

          {error && (
            <Alert variant="danger" dismissible onClose={() => setError(null)}>
              {error}
            </Alert>
          )}

          {/* Selección de Método de Pago */}
          <div className="mb-4">
            <h6 className="mb-3">Método de Pago</h6>
            <div className="payment-methods">
              {paymentMethods.map((method) => (
                <div
                  key={method.value}
                  className={`payment-method-card ${paymentMethod === method.value ? 'active' : ''}`}
                  onClick={() => setPaymentMethod(method.value)}
                  style={{ borderColor: paymentMethod === method.value ? method.color : '#dee2e6' }}
                >
                  <div className="d-flex align-items-center">
                    <div className="payment-icon me-3" style={{ color: method.color }}>
                      {method.icon}
                    </div>
                    <div>
                      <div className="fw-medium">{method.label}</div>
                      {method.value === 'CASH' && (
                        <small className="text-muted">Pagar al hacer check-in</small>
                      )}
                    </div>
                  </div>
                  {paymentMethod === method.value && (
                    <FaCheckCircle className="text-success" />
                  )}
                </div>
              ))}
            </div>
          </div>

          {/* Formulario de Tarjeta de Crédito */}
          {paymentMethod === 'CREDIT_CARD' && (
            <div className="payment-form">
              <h6 className="mb-3">Información de la Tarjeta</h6>
              <Form.Group className="mb-3">
                <Form.Label>Número de Tarjeta</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="1234 5678 9012 3456"
                  value={cardNumber}
                  onChange={handleCardNumberChange}
                  required
                />
              </Form.Group>

              <Form.Group className="mb-3">
                <Form.Label>Nombre en la Tarjeta</Form.Label>
                <Form.Control
                  type="text"
                  placeholder="JUAN PÉREZ"
                  value={cardName}
                  onChange={(e) => setCardName(e.target.value.toUpperCase())}
                  required
                />
              </Form.Group>

              <div className="row">
                <div className="col-md-6">
                  <Form.Group className="mb-3">
                    <Form.Label>Fecha de Expiración</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="MM/AA"
                      value={expiryDate}
                      onChange={handleExpiryChange}
                      required
                    />
                  </Form.Group>
                </div>
                <div className="col-md-6">
                  <Form.Group className="mb-3">
                    <Form.Label>CVV</Form.Label>
                    <Form.Control
                      type="text"
                      placeholder="123"
                      value={cvv}
                      onChange={handleCvvChange}
                      required
                    />
                  </Form.Group>
                </div>
              </div>
            </div>
          )}

          {/* Formulario de PayPal */}
          {paymentMethod === 'PAYPAL' && (
            <div className="payment-form">
              <h6 className="mb-3">Información de PayPal</h6>
              <Form.Group className="mb-3">
                <Form.Label>Email de PayPal</Form.Label>
                <Form.Control
                  type="email"
                  placeholder="tu-email@ejemplo.com"
                  value={paypalEmail}
                  onChange={(e) => setPaypalEmail(e.target.value)}
                  required
                />
              </Form.Group>
              <Alert variant="info">
                <small>Serás redirigido a PayPal para completar el pago de forma segura.</small>
              </Alert>
            </div>
          )}

          {/* Información de Efectivo */}
          {paymentMethod === 'CASH' && (
            <Alert variant="info">
              <FaMoneyBill className="me-2" />
              <strong>Pago en Efectivo:</strong> Podrás realizar el pago en efectivo al momento de hacer check-in en la recepción del hotel.
            </Alert>
          )}

          {/* Aviso de Seguridad */}
          <div className="mt-3">
            <small className="text-muted">
              🔒 Tu información de pago está protegida con encriptación de nivel bancario
            </small>
          </div>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose} disabled={processing}>
            <FaTimes className="me-2" />
            Cancelar
          </Button>
          <Button
            variant="success"
            type="submit"
            disabled={processing}
          >
            {processing ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                  className="me-2"
                />
                Procesando...
              </>
            ) : (
              <>
                <FaCreditCard className="me-2" />
                Pagar ${reservation.totalPrice?.toLocaleString('es-MX')} MXN
              </>
            )}
          </Button>
        </Modal.Footer>
      </Form>
    </Modal>
  );
};

PaymentModal.propTypes = {
  show: PropTypes.bool.isRequired,
  onHide: PropTypes.func.isRequired,
  reservation: PropTypes.object,
  onPaymentSuccess: PropTypes.func
};

export default PaymentModal;
