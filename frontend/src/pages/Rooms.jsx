import { useState, useEffect } from 'react';
import { Container, Row, Col, Alert, Spinner, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import RoomCard from '../components/rooms/RoomCard';
import RoomFilters from '../components/rooms/RoomFilters';
import roomService from '../services/roomService';

const Rooms = () => {
  const navigate = useNavigate();
  const [rooms, setRooms] = useState([]);
  const [filteredRooms, setFilteredRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [stats, setStats] = useState({
    total: 0,
    available: 0,
    occupied: 0
  });

  // Cargar habitaciones al montar el componente
  useEffect(() => {
    fetchRooms();
  }, []);

  // Actualizar estadísticas cuando cambien las habitaciones filtradas
  useEffect(() => {
    calculateStats();
  }, [filteredRooms]);

  const fetchRooms = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await roomService.getAllRooms();
      setRooms(data);
      setFilteredRooms(data);
    } catch (err) {
      console.error('Error fetching rooms:', err);
      setError('No se pudieron cargar las habitaciones. Verifica que el backend esté funcionando.');
    } finally {
      setLoading(false);
    }
  };

  const calculateStats = () => {
    const available = filteredRooms.filter(room => room.available).length;
    setStats({
      total: filteredRooms.length,
      available: available,
      occupied: filteredRooms.length - available
    });
  };

  const handleFilterChange = (filters) => {
    let filtered = [...rooms];

    // Filtrar por tipo de habitación
    if (filters.roomType) {
      filtered = filtered.filter(room => room.roomType === filters.roomType);
    }

    // Filtrar por precio
    if (filters.minPrice) {
      filtered = filtered.filter(room => room.price >= parseFloat(filters.minPrice));
    }
    if (filters.maxPrice) {
      filtered = filtered.filter(room => room.price <= parseFloat(filters.maxPrice));
    }

    // Filtrar por capacidad
    if (filters.minCapacity) {
      filtered = filtered.filter(room => room.capacity >= parseInt(filters.minCapacity));
    }

    // Filtrar solo disponibles
    if (filters.availableOnly) {
      filtered = filtered.filter(room => room.available);
    }

    setFilteredRooms(filtered);
  };

  const handleResetFilters = () => {
    setFilteredRooms(rooms);
  };

  const handleRoomSelect = (room) => {
    // Navegar a la página de reserva
    navigate(`/reservations/new?roomId=${room.id}`);
  };

  if (loading) {
    return (
      <Container className="py-5">
        <div className="text-center">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3">Cargando habitaciones...</p>
        </div>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          <Alert.Heading>Error al cargar habitaciones</Alert.Heading>
          <p>{error}</p>
          <hr />
          <div className="d-flex justify-content-end">
            <button className="btn btn-outline-danger" onClick={fetchRooms}>
              Reintentar
            </button>
          </div>
        </Alert>
      </Container>
    );
  }

  return (
    <div className="rooms-page bg-light py-5">
      <Container>
        {/* Header */}
        <Row className="mb-4">
          <Col>
            <h1 className="display-5 fw-bold text-primary mb-2">
              Catálogo de Habitaciones
            </h1>
            <p className="lead text-muted">
              Descubre nuestras habitaciones y encuentra la perfecta para tu estadía
            </p>
          </Col>
        </Row>

        {/* Estadísticas */}
        <Row className="mb-4">
          <Col md={4} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <p className="text-muted mb-1">Total de Habitaciones</p>
                  <h3 className="mb-0">{stats.total}</h3>
                </div>
                <Badge bg="primary" className="fs-4 p-3">
                  {stats.total}
                </Badge>
              </div>
            </div>
          </Col>
          <Col md={4} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <p className="text-muted mb-1">Disponibles</p>
                  <h3 className="mb-0 text-success">{stats.available}</h3>
                </div>
                <Badge bg="success" className="fs-4 p-3">
                  {stats.available}
                </Badge>
              </div>
            </div>
          </Col>
          <Col md={4} className="mb-3">
            <div className="stat-card bg-white p-3 rounded shadow-sm">
              <div className="d-flex justify-content-between align-items-center">
                <div>
                  <p className="text-muted mb-1">Ocupadas</p>
                  <h3 className="mb-0 text-danger">{stats.occupied}</h3>
                </div>
                <Badge bg="danger" className="fs-4 p-3">
                  {stats.occupied}
                </Badge>
              </div>
            </div>
          </Col>
        </Row>

        {/* Filtros */}
        <RoomFilters
          onFilterChange={handleFilterChange}
          onReset={handleResetFilters}
        />

        {/* Resultados */}
        {filteredRooms.length === 0 ? (
          <Alert variant="info">
            <p className="mb-0">
              No se encontraron habitaciones con los filtros seleccionados.
              Intenta ajustar los criterios de búsqueda.
            </p>
          </Alert>
        ) : (
          <>
            <div className="mb-3">
              <p className="text-muted">
                Mostrando {filteredRooms.length} {filteredRooms.length === 1 ? 'habitación' : 'habitaciones'}
              </p>
            </div>
            <Row>
              {filteredRooms.map((room) => (
                <Col key={room.id} md={6} lg={4} className="mb-4">
                  <RoomCard room={room} onSelect={handleRoomSelect} />
                </Col>
              ))}
            </Row>
          </>
        )}
      </Container>
    </div>
  );
};

export default Rooms;
