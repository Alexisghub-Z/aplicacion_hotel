import { Container, Row, Col, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { FaCalendarCheck, FaChevronDown } from 'react-icons/fa';

const Hero = () => {
  return (
    <div className="hero-section">
      <div className="hero-overlay">
        <Container>
          <Row className="align-items-center min-vh-75">
            <Col lg={8} className="text-white">
              <h1 className="display-3 fw-bold mb-4 hero-title">
                Bienvenido a Oaxaca Dreams
              </h1>
              <p className="lead mb-4 hero-subtitle">
                Experimenta la hospitalidad oaxaqueña en un ambiente de lujo y tradición.
                Disfruta de nuestras habitaciones premium y servicios excepcionales.
              </p>
              <div className="d-flex gap-3 flex-wrap">
                <Button
                  as={Link}
                  to="/rooms"
                  variant="primary"
                  size="lg"
                  className="px-4 py-3"
                >
                  <FaCalendarCheck className="me-2" />
                  Ver Habitaciones
                </Button>
                <Button
                  as={Link}
                  to="/contact"
                  variant="outline-light"
                  size="lg"
                  className="px-4 py-3"
                >
                  Contactar
                </Button>
              </div>
            </Col>
          </Row>
        </Container>

        <div className="scroll-indicator">
          <FaChevronDown size={32} className="text-white animate-bounce" />
        </div>
      </div>
    </div>
  );
};

export default Hero;
