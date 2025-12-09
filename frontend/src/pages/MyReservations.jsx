import { useState, useEffect } from 'react';
import { Container, Row, Col, Alert, Spinner, Button, Modal, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { FaPlus, FaExclamationTriangle } from 'react-icons/fa';
import ReservationCard from '../components/reservation/ReservationCard';
import ReservationFilters from '../components/reservation/ReservationFilters';
import ReservationDetailsModal from '../components/reservation/ReservationDetailsModal';
import PaymentModal from '../components/reservation/PaymentModal';
import reservationService from '../services/reservationService';
import customerService from '../services/customerService';
import roomService from '../services/roomService';
import additionalServiceService from '../services/additionalServiceService';
import paymentService from '../services/paymentService';

const MyReservations = () => {
  const navigate = useNavigate();

  // Estados principales
  const [reservations, setReservations] = useState([]);
  const [filteredReservations, setFilteredReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Estados de UI
  const [selectedReservation, setSelectedReservation] = useState(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);
  const [showCancelModal, setShowCancelModal] = useState(false);
  const [showPaymentModal, setShowPaymentModal] = useState(false);
  const [reservationToCancel, setReservationToCancel] = useState(null);
  const [reservationToPay, setReservationToPay] = useState(null);
  const [cancelling, setCancelling] = useState(false);
  const [paymentStatuses, setPaymentStatuses] = useState({});

  // Filtros
  const [filters, setFilters] = useState({
    status: '',
    sortBy: 'newest',
    roomNumber: ''
  });

  // Estadísticas
  const [stats, setStats] = useState({
    total: 0,
    pending: 0,
    confirmed: 0,
    cancelled: 0,
    completed: 0
  });

  useEffect(() => {
    fetchReservations();
    loadPaymentStatuses();
  }, []);

  useEffect(() => {
    applyFiltersAndSort();
  }, [reservations, filters]);

  useEffect(() => {
    calculateStats();
  }, [filteredReservations]);

  const fetchReservations = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await reservationService.getAllReservations();
      setReservations(data);
    } catch (err) {
      console.error('Error fetching reservations:', err);
      setError('No se pudieron cargar las reservas. Verifica que el backend esté funcionando.');
    } finally {
      setLoading(false);
    }
  };

  const applyFiltersAndSort = () => {
    let filtered = [...reservations];

    // Filtrar por estado
    if (filters.status) {
      filtered = filtered.filter(r => r.status === filters.status);
    }

    // Filtrar por número de habitación
    if (filters.roomNumber) {
      filtered = filtered.filter(r =>
        r.room?.roomNumber?.toLowerCase().includes(filters.roomNumber.toLowerCase())
      );
    }

    // Ordenar
    switch (filters.sortBy) {
      case 'newest':
        filtered.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt));
        break;
      case 'oldest':
        filtered.sort((a, b) => new Date(a.createdAt) - new Date(b.createdAt));
        break;
      case 'checkin':
        filtered.sort((a, b) => new Date(a.checkInDate) - new Date(b.checkInDate));
        break;
      case 'price-high':
        filtered.sort((a, b) => b.totalPrice - a.totalPrice);
        break;
      case 'price-low':
        filtered.sort((a, b) => a.totalPrice - b.totalPrice);
        break;
      default:
        break;
    }

    setFilteredReservations(filtered);
  };

  const calculateStats = () => {
    setStats({
      total: filteredReservations.length,
      pending: filteredReservations.filter(r => r.status === 'PENDING').length,
      confirmed: filteredReservations.filter(r => r.status === 'CONFIRMED').length,
      cancelled: filteredReservations.filter(r => r.status === 'CANCELLED').length,
      completed: filteredReservations.filter(r => r.status === 'COMPLETED').length
    });
  };

  const handleViewDetails = async (reservation) => {
    try {
      // Cargar datos completos del cliente, habitación y servicios
      const [customer, room, allServices] = await Promise.all([
        customerService.getCustomerById(reservation.customerId),
        roomService.getRoomById(reservation.roomId),
        additionalServiceService.getAllServices()
      ]);

      // Si hay servicios adicionales, filtrar los que están en la reserva
      let services = [];
      if (reservation.additionalServiceIds && reservation.additionalServiceIds.length > 0) {
        services = allServices.filter(s =>
          reservation.additionalServiceIds.includes(s.id)
        );
      }

      // Crear objeto de reserva enriquecido
      const enrichedReservation = {
        ...reservation,
        customer,
        room,
        additionalServices: services
      };

      setSelectedReservation(enrichedReservation);
      setShowDetailsModal(true);
    } catch (err) {
      console.error('Error loading reservation details:', err);
      alert('Error al cargar los detalles de la reserva');
    }
  };

  const handleCancelClick = (reservation) => {
    setReservationToCancel(reservation);
    setShowCancelModal(true);
  };

  const handleConfirmCancel = async () => {
    if (!reservationToCancel) return;

    try {
      setCancelling(true);
      await reservationService.cancelReservation(reservationToCancel.id);

      // Actualizar la lista
      await fetchReservations();

      setShowCancelModal(false);
      setReservationToCancel(null);
    } catch (err) {
      console.error('Error cancelling reservation:', err);
      setError('No se pudo cancelar la reserva. Intenta nuevamente.');
    } finally {
      setCancelling(false);
    }
  };

  const handleResetFilters = () => {
    setFilters({
      status: '',
      sortBy: 'newest',
      roomNumber: ''
    });
  };

  const loadPaymentStatuses = async () => {
    try {
      const allReservations = await reservationService.getAllReservations();
      const statuses = {};

      for (const reservation of allReservations) {
        try {
          const payments = await paymentService.getPaymentsByReservation(reservation.id);
          statuses[reservation.id] = payments.length > 0;
        } catch (err) {
          statuses[reservation.id] = false;
        }
      }

      setPaymentStatuses(statuses);
    } catch (err) {
      console.error('Error loading payment statuses:', err);
    }
  };

  const handlePayClick = (reservation) => {
    setReservationToPay(reservation);
    setShowPaymentModal(true);
  };

  const handlePaymentSuccess = async (reservation) => {
    // Actualizar el estado de pago
    setPaymentStatuses(prev => ({
      ...prev,
      [reservation.id]: true
    }));

    // Recargar reservas
    await fetchReservations();

    // Mostrar mensaje de éxito
    alert('¡Pago procesado exitosamente!');
  };

  if (loading) {
    return (
      <Container className="py-5">
        <div className="text-center">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3">Cargando reservas...</p>
        </div>
      </Container>
    );
  }

  return (
    <div className="my-reservations-page bg-light py-5">
      <Container>
        {/* Header */}
        <Row className="mb-4">
          <Col>
            <div className="d-flex justify-content-between align-items-center">
              <div>
                <h1 className="display-6 fw-bold">Mis Reservas</h1>
                <p className="lead text-muted">
                  Gestiona y consulta tus reservas de hotel
                </p>
              </div>
              <Button
                variant="primary"
                size="lg"
                onClick={() => navigate('/rooms')}
              >
                <FaPlus className="me-2" />
                Nueva Reserva
              </Button>
            </div>
          </Col>
        </Row>

        {error && (
          <Alert variant="danger" dismissible onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {/* Estadísticas */}
        <Row className="mb-4">
          <Col md={2} xs={6} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm text-center">
              <h3 className="mb-1">{stats.total}</h3>
              <small className="text-muted">Total</small>
            </div>
          </Col>
          <Col md={2} xs={6} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm text-center">
              <h3 className="mb-1 text-warning">{stats.pending}</h3>
              <small className="text-muted">Pendientes</small>
            </div>
          </Col>
          <Col md={2} xs={6} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm text-center">
              <h3 className="mb-1 text-success">{stats.confirmed}</h3>
              <small className="text-muted">Confirmadas</small>
            </div>
          </Col>
          <Col md={2} xs={6} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm text-center">
              <h3 className="mb-1 text-danger">{stats.cancelled}</h3>
              <small className="text-muted">Canceladas</small>
            </div>
          </Col>
          <Col md={2} xs={6} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm text-center">
              <h3 className="mb-1 text-secondary">{stats.completed}</h3>
              <small className="text-muted">Completadas</small>
            </div>
          </Col>
        </Row>

        {/* Filtros */}
        <ReservationFilters
          filters={filters}
          onFilterChange={setFilters}
          onReset={handleResetFilters}
        />

        {/* Lista de Reservas */}
        {filteredReservations.length === 0 ? (
          <Alert variant="info" className="text-center py-5">
            <h4>No hay reservas para mostrar</h4>
            <p className="mb-4">
              {reservations.length === 0
                ? 'Aún no tienes ninguna reserva. ¡Haz tu primera reserva ahora!'
                : 'No se encontraron reservas con los filtros seleccionados.'}
            </p>
            <Button variant="primary" onClick={() => navigate('/rooms')}>
              <FaPlus className="me-2" />
              Crear Primera Reserva
            </Button>
          </Alert>
        ) : (
          <>
            <div className="mb-3">
              <small className="text-muted">
                Mostrando {filteredReservations.length} de {reservations.length} reservas
              </small>
            </div>
            {filteredReservations.map((reservation) => (
              <ReservationCard
                key={reservation.id}
                reservation={reservation}
                onViewDetails={handleViewDetails}
                onCancel={handleCancelClick}
                onPay={handlePayClick}
                isPaid={paymentStatuses[reservation.id] || false}
              />
            ))}
          </>
        )}
      </Container>

      {/* Modal de Detalles */}
      <ReservationDetailsModal
        show={showDetailsModal}
        onHide={() => setShowDetailsModal(false)}
        reservation={selectedReservation}
      />

      {/* Modal de Confirmación de Cancelación */}
      <Modal
        show={showCancelModal}
        onHide={() => !cancelling && setShowCancelModal(false)}
        centered
      >
        <Modal.Header closeButton={!cancelling}>
          <Modal.Title>
            <FaExclamationTriangle className="me-2 text-warning" />
            Confirmar Cancelación
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>¿Estás seguro de que deseas cancelar esta reserva?</p>
          {reservationToCancel && (
            <div className="bg-light p-3 rounded">
              <strong>Habitación {reservationToCancel.room?.roomNumber}</strong>
              <br />
              <small className="text-muted">
                Check-in: {reservationToCancel.checkInDate} •
                Check-out: {reservationToCancel.checkOutDate}
              </small>
              <br />
              <small className="text-primary">
                Total: ${reservationToCancel.totalPrice?.toLocaleString('es-MX')} MXN
              </small>
            </div>
          )}
          <Alert variant="warning" className="mt-3 mb-0">
            <small>Esta acción no se puede deshacer.</small>
          </Alert>
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            onClick={() => setShowCancelModal(false)}
            disabled={cancelling}
          >
            No, mantener reserva
          </Button>
          <Button
            variant="danger"
            onClick={handleConfirmCancel}
            disabled={cancelling}
          >
            {cancelling ? (
              <>
                <Spinner animation="border" size="sm" className="me-2" />
                Cancelando...
              </>
            ) : (
              'Sí, cancelar reserva'
            )}
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Modal de Pago */}
      <PaymentModal
        show={showPaymentModal}
        onHide={() => setShowPaymentModal(false)}
        reservation={reservationToPay}
        onPaymentSuccess={handlePaymentSuccess}
      />
    </div>
  );
};

export default MyReservations;
