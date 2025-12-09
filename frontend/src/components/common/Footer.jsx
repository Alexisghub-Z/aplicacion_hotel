import { Container, Row, Col } from 'react-bootstrap';
import { FaFacebook, FaInstagram, FaTwitter } from 'react-icons/fa';

const Footer = () => {
  return (
    <footer className="footer-custom mt-5">
      <Container>
        <Row className="py-4">
          <Col md={6} className="mb-3">
            <h5 className="text-primary mb-3">Hotel Oaxaca Dreams</h5>
            <p className="text-muted">
              Descubre la magia de Oaxaca en un ambiente de lujo y confort.
              Tu estadía perfecta te espera.
            </p>
            <div className="social-links">
              <a href="#" className="me-3"><FaFacebook size={24} /></a>
              <a href="#" className="me-3"><FaInstagram size={24} /></a>
              <a href="#" className="me-3"><FaTwitter size={24} /></a>
            </div>
          </Col>

          <Col md={6} className="mb-3">
            <h5 className="mb-3">Enlaces Rápidos</h5>
            <ul className="list-unstyled">
              <li className="mb-2"><a href="#" className="text-muted text-decoration-none">Sobre Nosotros</a></li>
              <li className="mb-2"><a href="#" className="text-muted text-decoration-none">Habitaciones</a></li>
              <li className="mb-2"><a href="#" className="text-muted text-decoration-none">Servicios</a></li>
              <li className="mb-2"><a href="#" className="text-muted text-decoration-none">Galería</a></li>
            </ul>
          </Col>
        </Row>

        <Row className="border-top pt-3">
          <Col className="text-center text-muted">
            <small>&copy; {new Date().getFullYear()} Hotel Oaxaca Dreams. Todos los derechos reservados.</small>
          </Col>
        </Row>
      </Container>
    </footer>
  );
};

export default Footer;
