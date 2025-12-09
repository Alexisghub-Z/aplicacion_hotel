import { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Form, Button, Alert, Badge, Modal, Table, Spinner } from 'react-bootstrap';
import { FaBed, FaPlus, FaEdit, FaTrash, FaCheck, FaTimes, FaDoorOpen } from 'react-icons/fa';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const roomTypeLabels = {
  SINGLE: 'Individual',
  DOUBLE: 'Doble',
  SUITE: 'Suite',
  PRESIDENTIAL: 'Presidencial'
};

const RoomManagement = () => {
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Estados para crear habitación
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [creating, setCreating] = useState(false);
  const [newRoom, setNewRoom] = useState({
    roomType: 'SINGLE',
    roomNumber: '',
    floor: 1
  });

  // Estados para eliminar habitación
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [roomToDelete, setRoomToDelete] = useState(null);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    fetchRooms();
  }, []);

  const fetchRooms = async () => {
    try {
      setLoading(true);
      const response = await axios.get(`${API_URL}/rooms`);
      setRooms(response.data);
    } catch (err) {
      setError('Error al cargar las habitaciones');
      console.error('Error fetching rooms:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateRoom = async (e) => {
    e.preventDefault();
    setCreating(true);
    setError('');

    try {
      await axios.post(`${API_URL}/rooms`, null, {
        params: {
          roomType: newRoom.roomType,
          roomNumber: newRoom.roomNumber,
          floor: newRoom.floor
        }
      });

      setSuccess(`Habitación ${newRoom.roomNumber} creada exitosamente`);
      setShowCreateModal(false);
      setNewRoom({ roomType: 'SINGLE', roomNumber: '', floor: 1 });
      fetchRooms();

      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al crear la habitación');
    } finally {
      setCreating(false);
    }
  };

  const handleDeleteRoom = async () => {
    if (!roomToDelete) return;

    setDeleting(true);
    setError('');

    try {
      await axios.delete(`${API_URL}/rooms/${roomToDelete.id}`);
      setSuccess(`Habitación ${roomToDelete.roomNumber} eliminada exitosamente`);
      setShowDeleteModal(false);
      setRoomToDelete(null);
      fetchRooms();

      setTimeout(() => setSuccess(''), 3000);
    } catch (err) {
      setError(err.response?.data?.message || 'Error al eliminar la habitación');
    } finally {
      setDeleting(false);
    }
  };

  const handleToggleAvailability = async (roomId) => {
    try {
      await axios.patch(`${API_URL}/rooms/${roomId}/toggle-availability`);
      fetchRooms();
      setSuccess('Disponibilidad actualizada');
      setTimeout(() => setSuccess(''), 2000);
    } catch (err) {
      setError('Error al cambiar disponibilidad');
    }
  };

  const openDeleteModal = (room) => {
    setRoomToDelete(room);
    setShowDeleteModal(true);
  };

  return (
    <Container className="py-5">
      <Row className="mb-4">
        <Col>
          <div className="d-flex justify-content-between align-items-center">
            <div>
              <h1 className="display-6 fw-bold">
                <FaBed className="me-3 text-primary" />
                Gestión de Habitaciones
              </h1>
              <p className="text-muted">Administra las habitaciones del hotel</p>
            </div>
            <Button
              variant="primary"
              size="lg"
              onClick={() => setShowCreateModal(true)}
            >
              <FaPlus className="me-2" />
              Nueva Habitación
            </Button>
          </div>
        </Col>
      </Row>

      {success && (
        <Alert variant="success" dismissible onClose={() => setSuccess('')}>
          {success}
        </Alert>
      )}

      {error && (
        <Alert variant="danger" dismissible onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      {loading ? (
        <div className="text-center py-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3">Cargando habitaciones...</p>
        </div>
      ) : (
        <Row>
          <Col>
            <Card className="shadow-sm">
              <Card.Body>
                <div className="table-responsive">
                  <Table hover>
                    <thead>
                      <tr>
                        <th>Número</th>
                        <th>Tipo</th>
                        <th>Piso</th>
                        <th>Capacidad</th>
                        <th>Precio/Noche</th>
                        <th>Disponible</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>
                    <tbody>
                      {rooms.map((room) => (
                        <tr key={room.id}>
                          <td className="fw-bold">{room.roomNumber}</td>
                          <td>{roomTypeLabels[room.roomType]}</td>
                          <td>{room.floor}</td>
                          <td>{room.capacity} personas</td>
                          <td className="text-success fw-semibold">
                            ${room.price}
                          </td>
                          <td>
                            <Badge bg={room.available ? 'success' : 'danger'}>
                              {room.available ? 'Disponible' : 'No disponible'}
                            </Badge>
                          </td>
                          <td>
                            <div className="d-flex gap-2">
                              <Button
                                variant={room.available ? 'warning' : 'success'}
                                size="sm"
                                onClick={() => handleToggleAvailability(room.id)}
                                title={room.available ? 'Marcar como no disponible' : 'Marcar como disponible'}
                              >
                                <FaDoorOpen />
                              </Button>
                              <Button
                                variant="danger"
                                size="sm"
                                onClick={() => openDeleteModal(room)}
                              >
                                <FaTrash />
                              </Button>
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </Table>
                </div>

                {rooms.length === 0 && (
                  <Alert variant="info" className="text-center mb-0">
                    No hay habitaciones registradas. Crea una nueva habitación para comenzar.
                  </Alert>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}

      {/* Modal para crear habitación */}
      <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>
            <FaPlus className="me-2" />
            Nueva Habitación
          </Modal.Title>
        </Modal.Header>
        <Form onSubmit={handleCreateRoom}>
          <Modal.Body>
            <Form.Group className="mb-3">
              <Form.Label>Tipo de Habitación <span className="text-danger">*</span></Form.Label>
              <Form.Select
                value={newRoom.roomType}
                onChange={(e) => setNewRoom({ ...newRoom, roomType: e.target.value })}
                required
              >
                <option value="SINGLE">Individual</option>
                <option value="DOUBLE">Doble</option>
                <option value="SUITE">Suite</option>
                <option value="PRESIDENTIAL">Presidencial</option>
              </Form.Select>
              <Form.Text className="text-muted">
                El tipo define el precio y capacidad automáticamente
              </Form.Text>
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Número de Habitación <span className="text-danger">*</span></Form.Label>
              <Form.Control
                type="text"
                placeholder="Ej: 101, 201, 301"
                value={newRoom.roomNumber}
                onChange={(e) => setNewRoom({ ...newRoom, roomNumber: e.target.value })}
                required
              />
            </Form.Group>

            <Form.Group className="mb-3">
              <Form.Label>Piso <span className="text-danger">*</span></Form.Label>
              <Form.Control
                type="number"
                min="1"
                max="20"
                value={newRoom.floor}
                onChange={(e) => setNewRoom({ ...newRoom, floor: parseInt(e.target.value) })}
                required
              />
            </Form.Group>

            {error && (
              <Alert variant="danger" className="mb-0">
                {error}
              </Alert>
            )}
          </Modal.Body>
          <Modal.Footer>
            <Button variant="secondary" onClick={() => setShowCreateModal(false)}>
              Cancelar
            </Button>
            <Button variant="primary" type="submit" disabled={creating}>
              {creating ? (
                <>
                  <Spinner animation="border" size="sm" className="me-2" />
                  Creando...
                </>
              ) : (
                <>
                  <FaCheck className="me-2" />
                  Crear Habitación
                </>
              )}
            </Button>
          </Modal.Footer>
        </Form>
      </Modal>

      {/* Modal para eliminar habitación */}
      <Modal show={showDeleteModal} onHide={() => setShowDeleteModal(false)} centered>
        <Modal.Header closeButton className="border-0">
          <Modal.Title className="text-danger">
            <FaTrash className="me-2" />
            Eliminar Habitación
          </Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Alert variant="warning">
            <strong>¿Estás seguro de eliminar esta habitación?</strong>
          </Alert>

          {roomToDelete && (
            <div className="p-3 bg-light rounded">
              <p className="mb-2">
                <strong>Número:</strong> {roomToDelete.roomNumber}
              </p>
              <p className="mb-2">
                <strong>Tipo:</strong> {roomTypeLabels[roomToDelete.roomType]}
              </p>
              <p className="mb-0">
                <strong>Piso:</strong> {roomToDelete.floor}
              </p>
            </div>
          )}

          <p className="text-muted mt-3 mb-0">
            <small>Esta acción no se puede deshacer. Asegúrate de que no hay reservas activas para esta habitación.</small>
          </p>
        </Modal.Body>
        <Modal.Footer className="border-0">
          <Button variant="secondary" onClick={() => setShowDeleteModal(false)}>
            Cancelar
          </Button>
          <Button variant="danger" onClick={handleDeleteRoom} disabled={deleting}>
            {deleting ? (
              <>
                <Spinner animation="border" size="sm" className="me-2" />
                Eliminando...
              </>
            ) : (
              <>
                <FaTrash className="me-2" />
                Eliminar
              </>
            )}
          </Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
};

export default RoomManagement;
