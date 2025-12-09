import { Container, Row, Col, Card, Button, Badge } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaUsers, FaBed, FaArrowRight } from 'react-icons/fa';

const RoomTypes = () => {
  const roomTypes = [
    {
      type: 'SINGLE',
      name: 'Habitación Individual',
      price: 800,
      capacity: 1,
      description: 'Perfecta para viajeros de negocios o solitarios. Cómoda y acogedora.',
      features: ['Cama individual', 'Escritorio', 'TV HD', 'Baño privado'],
      image: 'https://images.unsplash.com/photo-1611892440504-42a792e24d32?w=400&h=300&fit=crop'
    },
    {
      type: 'DOUBLE',
      name: 'Habitación Doble',
      price: 1200,
      capacity: 2,
      description: 'Ideal para parejas o viajeros que buscan más espacio y comodidad.',
      features: ['Cama matrimonial', 'Minibar', 'TV HD', 'Vista a la ciudad'],
      image: 'https://images.unsplash.com/photo-1590490360182-c33d57733427?w=400&h=300&fit=crop',
      popular: true
    },
    {
      type: 'SUITE',
      name: 'Suite Premium',
      price: 2500,
      capacity: 4,
      description: 'Espaciosa suite con sala de estar separada para familias y grupos.',
      features: ['2 habitaciones', 'Sala de estar', 'Balcón privado', 'Jacuzzi'],
      image: 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?w=400&h=300&fit=crop'
    },
    {
      type: 'PRESIDENTIAL',
      name: 'Suite Presidencial',
      price: 5000,
      capacity: 6,
      description: 'Máximo lujo y exclusividad. La experiencia definitiva en Oaxaca.',
      features: ['3 habitaciones', 'Comedor', 'Terraza', 'Servicio personalizado'],
      image: 'https://images.unsplash.com/photo-1578683010236-d716f9a3f461?w=400&h=300&fit=crop',
      luxury: true
    }
  ];

  return (
    <section className="room-types-section py-5">
      <Container>
        <Row className="text-center mb-5">
          <Col>
            <h2 className="display-5 fw-bold mb-3">Nuestras Habitaciones</h2>
            <p className="lead text-muted">
              Encuentra el espacio perfecto para tu estadía en Oaxaca
            </p>
          </Col>
        </Row>

        <Row className="g-4">
          {roomTypes.map((room, index) => (
            <Col key={index} md={6} lg={6}>
              <Card className="h-100 border-0 shadow-sm room-card overflow-hidden">
                <div className="room-image-container position-relative">
                  <Card.Img
                    variant="top"
                    src={room.image}
                    alt={room.name}
                    className="room-image"
                  />
                  {room.popular && (
                    <Badge bg="success" className="position-absolute top-0 end-0 m-3">
                      Más Popular
                    </Badge>
                  )}
                  {room.luxury && (
                    <Badge bg="warning" text="dark" className="position-absolute top-0 end-0 m-3">
                      Lujo
                    </Badge>
                  )}
                </div>

                <Card.Body className="d-flex flex-column">
                  <div className="d-flex justify-content-between align-items-start mb-2">
                    <Card.Title className="h4 mb-0">{room.name}</Card.Title>
                    <div className="text-end">
                      <div className="text-primary fw-bold h4 mb-0">
                        ${room.price.toLocaleString()}
                      </div>
                      <small className="text-muted">MXN / noche</small>
                    </div>
                  </div>

                  <div className="mb-3">
                    <Badge bg="light" text="dark" className="me-2">
                      <FaUsers className="me-1" />
                      Hasta {room.capacity} {room.capacity === 1 ? 'persona' : 'personas'}
                    </Badge>
                    <Badge bg="light" text="dark">
                      <FaBed className="me-1" />
                      {room.type}
                    </Badge>
                  </div>

                  <Card.Text className="text-muted mb-3">
                    {room.description}
                  </Card.Text>

                  <div className="mb-3">
                    <strong className="d-block mb-2">Características:</strong>
                    <ul className="list-unstyled">
                      {room.features.map((feature, idx) => (
                        <li key={idx} className="text-muted mb-1">
                          <small>✓ {feature}</small>
                        </li>
                      ))}
                    </ul>
                  </div>

                  <Button
                    as={Link}
                    to="/rooms"
                    variant="primary"
                    className="mt-auto w-100"
                  >
                    Ver Disponibilidad
                    <FaArrowRight className="ms-2" />
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>

        <Row className="mt-5">
          <Col className="text-center">
            <Button
              as={Link}
              to="/rooms"
              variant="outline-primary"
              size="lg"
              className="px-5"
            >
              Ver Todas las Habitaciones
            </Button>
          </Col>
        </Row>
      </Container>
    </section>
  );
};

export default RoomTypes;
