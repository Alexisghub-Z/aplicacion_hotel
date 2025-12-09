import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Spinner } from 'react-bootstrap';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { FaCheckCircle, FaArrowLeft } from 'react-icons/fa';
import DateRangePicker from '../components/reservation/DateRangePicker';
import CustomerForm from '../components/reservation/CustomerForm';
import AdditionalServicesSelector from '../components/reservation/AdditionalServicesSelector';
import PackageSelector from '../components/reservation/PackageSelector';
import ReservationSummary from '../components/reservation/ReservationSummary';
import roomService from '../services/roomService';
import customerService from '../services/customerService';
import reservationService from '../services/reservationService';

const NewReservation = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const roomId = searchParams.get('roomId');

  // Estados principales
  const [room, setRoom] = useState(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  // Estados del formulario
  const [checkInDate, setCheckInDate] = useState('');
  const [checkOutDate, setCheckOutDate] = useState('');
  const [numberOfGuests, setNumberOfGuests] = useState(1);
  const [customer, setCustomer] = useState({
    id: null,
    email: '',
    phone: '',
    firstName: '',
    lastName: '',
    loyaltyLevel: 'REGULAR'
  });
  const [selectedServices, setSelectedServices] = useState([]);
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [searchingCustomer, setSearchingCustomer] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (roomId) {
      fetchRoom(roomId);
    } else {
      setLoading(false);
      setError('No se especificó una habitación');
    }
  }, [roomId]);

  const fetchRoom = async (id) => {
    try {
      setLoading(true);
      const data = await roomService.getRoomById(id);
      setRoom(data);
      setNumberOfGuests(1);
    } catch (err) {
      console.error('Error fetching room:', err);
      setError('No se pudo cargar la información de la habitación');
    } finally {
      setLoading(false);
    }
  };

  const handleSearchCustomerByEmail = async (email) => {
    try {
      setSearchingCustomer(true);
      const data = await customerService.getCustomerByEmail(email);
      setCustomer({
        id: data.id,
        email: data.email,
        phone: data.phone,
        firstName: data.firstName,
        lastName: data.lastName,
        loyaltyLevel: data.loyaltyLevel
      });
      setError(null);
      setErrors({});
    } catch (err) {
      // Cliente no encontrado, mantener el email y permitir crear nuevo
      console.log('Cliente no encontrado, se creará uno nuevo');
      setCustomer(prev => ({ ...prev, id: null }));
    } finally {
      setSearchingCustomer(false);
    }
  };

  const handlePackageChange = (pkg) => {
    setSelectedPackage(pkg);
    // Si selecciona un paquete, limpiar servicios individuales
    if (pkg) {
      setSelectedServices([]);
    }
  };

  const handleServicesChange = (services) => {
    setSelectedServices(services);
    // Si selecciona servicios individuales, limpiar paquete
    if (services.length > 0) {
      setSelectedPackage(null);
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!checkInDate) {
      newErrors.checkInDate = 'La fecha de check-in es obligatoria';
    }

    if (!checkOutDate) {
      newErrors.checkOutDate = 'La fecha de check-out es obligatoria';
    }

    if (checkInDate && checkOutDate && new Date(checkOutDate) <= new Date(checkInDate)) {
      newErrors.checkOutDate = 'La fecha de check-out debe ser posterior al check-in';
    }

    if (!customer.email) {
      newErrors.email = 'El email es obligatorio';
    } else if (!/\S+@\S+\.\S+/.test(customer.email)) {
      newErrors.email = 'Email inválido';
    }

    if (!customer.phone) {
      newErrors.phone = 'El teléfono es obligatorio';
    }

    if (!customer.firstName) {
      newErrors.firstName = 'El nombre es obligatorio';
    }

    if (!customer.lastName) {
      newErrors.lastName = 'El apellido es obligatorio';
    }

    if (numberOfGuests < 1) {
      newErrors.numberOfGuests = 'Debe haber al menos 1 huésped';
    }

    if (room && numberOfGuests > room.capacity) {
      newErrors.numberOfGuests = `La habitación solo admite hasta ${room.capacity} personas`;
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validateForm()) {
      setError('Por favor corrige los errores en el formulario');
      return;
    }

    try {
      setSubmitting(true);
      setError(null);

      // Crear o actualizar cliente si es necesario
      let customerId = customer.id;
      if (!customerId) {
        const newCustomer = await customerService.createCustomer({
          email: customer.email,
          phone: customer.phone,
          firstName: customer.firstName,
          lastName: customer.lastName,
          loyaltyLevel: customer.loyaltyLevel
        });
        customerId = newCustomer.id;
      }

      // Crear la reserva
      const reservationData = {
        customerId: customerId,
        roomId: parseInt(roomId),
        checkInDate: checkInDate,
        checkOutDate: checkOutDate,
        numberOfGuests: numberOfGuests,
        packageId: selectedPackage ? selectedPackage.id : null,
        additionalServiceIds: selectedPackage ? [] : selectedServices.map(s => s.id)
      };

      const reservation = await reservationService.createReservation(reservationData);

      setSuccess(true);

      // Redirigir después de 2 segundos
      setTimeout(() => {
        navigate('/reservations');
      }, 2000);

    } catch (err) {
      console.error('Error creating reservation:', err);
      setError(err.response?.data?.message || 'Error al crear la reserva. Por favor intenta nuevamente.');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-5">
        <div className="text-center">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3">Cargando información...</p>
        </div>
      </Container>
    );
  }

  if (!room) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          {error || 'No se encontró la habitación'}
          <hr />
          <Button variant="outline-danger" onClick={() => navigate('/rooms')}>
            Volver al catálogo
          </Button>
        </Alert>
      </Container>
    );
  }

  if (success) {
    return (
      <Container className="py-5">
        <Alert variant="success" className="text-center">
          <FaCheckCircle size={64} className="mb-3" />
          <h4>¡Reserva Creada Exitosamente!</h4>
          <p>Redirigiendo a tus reservas...</p>
        </Alert>
      </Container>
    );
  }

  return (
    <div className="new-reservation-page bg-light py-5">
      <Container>
        <Button
          variant="link"
          className="mb-3 p-0"
          onClick={() => navigate('/rooms')}
        >
          <FaArrowLeft className="me-2" />
          Volver al catálogo
        </Button>

        <Row className="mb-4">
          <Col>
            <h1 className="display-6 fw-bold">Nueva Reserva</h1>
            <p className="lead text-muted">
              Completa los datos para confirmar tu reserva
            </p>
          </Col>
        </Row>

        {error && (
          <Alert variant="danger" dismissible onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        <Form onSubmit={handleSubmit}>
          <Row>
            <Col lg={8}>
              {/* Información de la habitación */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <h5 className="mb-3">Habitación Seleccionada</h5>
                  <div className="d-flex align-items-center">
                    <div className="flex-grow-1">
                      <h6 className="mb-1">Habitación {room.roomNumber}</h6>
                      <p className="text-muted mb-0">{room.description}</p>
                    </div>
                    <div className="text-end">
                      <div className="text-primary fw-bold fs-5">
                        ${room.price.toLocaleString('es-MX')}
                      </div>
                      <small className="text-muted">MXN / noche</small>
                    </div>
                  </div>
                </Card.Body>
              </Card>

              {/* Fechas */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <DateRangePicker
                    checkInDate={checkInDate}
                    checkOutDate={checkOutDate}
                    onCheckInChange={setCheckInDate}
                    onCheckOutChange={setCheckOutDate}
                    error={errors.checkInDate || errors.checkOutDate}
                    roomId={roomId}
                  />
                </Card.Body>
              </Card>

              {/* Número de huéspedes */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <h5 className="mb-3">Número de Huéspedes</h5>
                  <Form.Group>
                    <Form.Label>
                      ¿Cuántas personas se hospedarán? <span className="text-danger">*</span>
                    </Form.Label>
                    <Form.Control
                      type="number"
                      min="1"
                      max={room.capacity}
                      value={numberOfGuests}
                      onChange={(e) => setNumberOfGuests(parseInt(e.target.value))}
                      isInvalid={!!errors.numberOfGuests}
                      required
                    />
                    <Form.Control.Feedback type="invalid">
                      {errors.numberOfGuests}
                    </Form.Control.Feedback>
                    <Form.Text className="text-muted">
                      Capacidad máxima: {room.capacity} personas
                    </Form.Text>
                  </Form.Group>
                </Card.Body>
              </Card>

              {/* Información del cliente */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <CustomerForm
                    customer={customer}
                    onChange={setCustomer}
                    onSearchByEmail={handleSearchCustomerByEmail}
                    searching={searchingCustomer}
                    errors={errors}
                  />
                </Card.Body>
              </Card>

              {/* Paquetes especiales */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <PackageSelector
                    selectedPackage={selectedPackage}
                    onPackageChange={handlePackageChange}
                  />
                </Card.Body>
              </Card>

              {/* Servicios adicionales */}
              <Card className="mb-4 shadow-sm">
                <Card.Body>
                  <AdditionalServicesSelector
                    selectedServices={selectedServices}
                    onSelectionChange={handleServicesChange}
                  />
                  {selectedPackage && (
                    <Alert variant="info" className="mt-3 mb-0">
                      <small>Has seleccionado un paquete. Los servicios individuales están deshabilitados.</small>
                    </Alert>
                  )}
                </Card.Body>
              </Card>
            </Col>

            <Col lg={4}>
              <ReservationSummary
                room={room}
                checkInDate={checkInDate}
                checkOutDate={checkOutDate}
                numberOfGuests={numberOfGuests}
                customer={customer}
                selectedServices={selectedServices}
              />

              <Button
                type="submit"
                variant="primary"
                size="lg"
                className="w-100 mt-3"
                disabled={submitting}
              >
                {submitting ? (
                  <>
                    <Spinner animation="border" size="sm" className="me-2" />
                    Procesando...
                  </>
                ) : (
                  'Confirmar Reserva'
                )}
              </Button>
            </Col>
          </Row>
        </Form>
      </Container>
    </div>
  );
};

export default NewReservation;
