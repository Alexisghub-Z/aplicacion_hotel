import { Container, Row, Col, Card } from 'react-bootstrap';
import {
  FaWifi,
  FaSwimmingPool,
  FaUtensils,
  FaSpa,
  FaCar,
  FaConciergeBell,
  FaDumbbell,
  FaShieldAlt
} from 'react-icons/fa';

const Features = () => {
  const features = [
    {
      icon: <FaWifi size={40} />,
      title: 'WiFi Gratis',
      description: 'Internet de alta velocidad en todo el hotel'
    },
    {
      icon: <FaSwimmingPool size={40} />,
      title: 'Alberca',
      description: 'Alberca climatizada con vista panorámica'
    },
    {
      icon: <FaUtensils size={40} />,
      title: 'Restaurante',
      description: 'Gastronomía oaxaqueña e internacional'
    },
    {
      icon: <FaSpa size={40} />,
      title: 'Spa & Wellness',
      description: 'Masajes relajantes y tratamientos faciales'
    },
    {
      icon: <FaCar size={40} />,
      title: 'Transporte',
      description: 'Traslado al aeropuerto y tours turísticos'
    },
    {
      icon: <FaConciergeBell size={40} />,
      title: 'Servicio 24/7',
      description: 'Recepción y servicio a cuarto las 24 horas'
    },
    {
      icon: <FaDumbbell size={40} />,
      title: 'Gimnasio',
      description: 'Centro de fitness completamente equipado'
    },
    {
      icon: <FaShieldAlt size={40} />,
      title: 'Seguridad',
      description: 'Vigilancia y seguridad privada'
    }
  ];

  return (
    <section className="features-section py-5 bg-light">
      <Container>
        <Row className="text-center mb-5">
          <Col>
            <h2 className="display-5 fw-bold mb-3">Servicios y Amenidades</h2>
            <p className="lead text-muted">
              Disfruta de comodidades excepcionales durante tu estancia
            </p>
          </Col>
        </Row>

        <Row className="g-4">
          {features.map((feature, index) => (
            <Col key={index} md={6} lg={3}>
              <Card className="h-100 border-0 shadow-sm feature-card text-center">
                <Card.Body className="d-flex flex-column align-items-center p-4">
                  <div className="feature-icon text-primary mb-3">
                    {feature.icon}
                  </div>
                  <Card.Title className="h5 mb-2">{feature.title}</Card.Title>
                  <Card.Text className="text-muted">
                    {feature.description}
                  </Card.Text>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>
    </section>
  );
};

export default Features;
