import { Card, Button, Badge } from 'react-bootstrap';
import { FaUsers, FaBed, FaMapMarkerAlt, FaCheckCircle, FaTimesCircle } from 'react-icons/fa';
import PropTypes from 'prop-types';

const RoomCard = ({ room, onSelect }) => {
  // Mapeo de tipos de habitación a nombres en español
  const roomTypeNames = {
    SINGLE: 'Individual',
    DOUBLE: 'Doble',
    SUITE: 'Suite',
    PRESIDENTIAL: 'Presidencial'
  };

  // Imágenes por defecto según el tipo de habitación
  const defaultImages = {
    SINGLE: 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=400&h=300&fit=crop',
    DOUBLE: 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=400&h=300&fit=crop',
    SUITE: 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=400&h=300&fit=crop',
    PRESIDENTIAL: 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=400&h=300&fit=crop'
  };

  const getImageUrl = () => {
    if (room.imageUrl && !room.imageUrl.startsWith('/images/')) {
      return room.imageUrl;
    }
    return defaultImages[room.roomType] || defaultImages.DOUBLE;
  };

  const getRoomTypeBadgeColor = () => {
    switch (room.roomType) {
      case 'SINGLE':
        return 'info';
      case 'DOUBLE':
        return 'primary';
      case 'SUITE':
        return 'success';
      case 'PRESIDENTIAL':
        return 'warning';
      default:
        return 'secondary';
    }
  };

  return (
    <Card className="h-100 room-card-catalog shadow-sm">
      <div className="room-image-wrapper">
        <Card.Img
          variant="top"
          src={getImageUrl()}
          alt={`Habitación ${room.roomNumber}`}
          className="room-image"
        />
        <div className="room-badges">
          <Badge bg={getRoomTypeBadgeColor()} className="me-2">
            {roomTypeNames[room.roomType]}
          </Badge>
          {room.available ? (
            <Badge bg="success">
              <FaCheckCircle className="me-1" />
              Disponible
            </Badge>
          ) : (
            <Badge bg="danger">
              <FaTimesCircle className="me-1" />
              Ocupada
            </Badge>
          )}
        </div>
      </div>

      <Card.Body className="d-flex flex-column">
        <div className="d-flex justify-content-between align-items-start mb-3">
          <div>
            <Card.Title className="h5 mb-1">
              <FaBed className="me-2 text-primary" />
              Habitación {room.roomNumber}
            </Card.Title>
            <small className="text-muted">
              <FaMapMarkerAlt className="me-1" />
              Piso {room.floor}
            </small>
          </div>
          <div className="text-end">
            <div className="price-tag">
              ${room.price.toLocaleString('es-MX')}
            </div>
            <small className="text-muted">MXN / noche</small>
          </div>
        </div>

        <div className="mb-3">
          <Badge bg="light" text="dark" className="me-2 mb-2">
            <FaUsers className="me-1" />
            Hasta {room.capacity} {room.capacity === 1 ? 'persona' : 'personas'}
          </Badge>
        </div>

        {room.description && (
          <Card.Text className="text-muted mb-3 flex-grow-1">
            {room.description}
          </Card.Text>
        )}

        {room.amenities && room.amenities.length > 0 && (
          <div className="amenities-list mb-3">
            <strong className="d-block mb-2 text-muted">Amenidades:</strong>
            <div className="d-flex flex-wrap gap-1">
              {room.amenities.slice(0, 4).map((amenity, index) => (
                <Badge key={index} bg="light" text="dark" className="amenity-badge">
                  {amenity}
                </Badge>
              ))}
              {room.amenities.length > 4 && (
                <Badge bg="light" text="dark">
                  +{room.amenities.length - 4} más
                </Badge>
              )}
            </div>
          </div>
        )}

        <Button
          variant={room.available ? 'primary' : 'secondary'}
          className="w-100 mt-auto"
          onClick={() => onSelect(room)}
          disabled={!room.available}
        >
          {room.available ? 'Reservar Ahora' : 'No Disponible'}
        </Button>
      </Card.Body>
    </Card>
  );
};

RoomCard.propTypes = {
  room: PropTypes.shape({
    id: PropTypes.number.isRequired,
    roomNumber: PropTypes.string.isRequired,
    roomType: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired,
    capacity: PropTypes.number.isRequired,
    available: PropTypes.bool.isRequired,
    floor: PropTypes.number.isRequired,
    imageUrl: PropTypes.string,
    description: PropTypes.string,
    amenities: PropTypes.arrayOf(PropTypes.string)
  }).isRequired,
  onSelect: PropTypes.func.isRequired
};

export default RoomCard;
