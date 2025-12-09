import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Navbar as BootstrapNavbar, Nav, Container, Button, Badge } from 'react-bootstrap';
import { FaHotel, FaBed, FaBox, FaCalendarCheck, FaChartBar, FaCog, FaPhone, FaSignOutAlt, FaUser, FaHome } from 'react-icons/fa';
import { useAuth } from '../../context/AuthContext';

const Navbar = () => {
  const [expanded, setExpanded] = useState(false);
  const location = useLocation();
  const navigate = useNavigate();
  const { isAuthenticated, user, logout } = useAuth();

  const isAdminArea = location.pathname.startsWith('/admin');

  const handleLogout = () => {
    logout();
    navigate('/');
    setExpanded(false);
  };

  const handleClose = () => setExpanded(false);

  return (
    <BootstrapNavbar
      expand="lg"
      className="navbar-custom shadow-sm"
      expanded={expanded}
      onToggle={setExpanded}
      sticky="top"
      bg={isAdminArea ? "dark" : "light"}
      variant={isAdminArea ? "dark" : "light"}
    >
      <Container>
        <BootstrapNavbar.Brand as={Link} to={isAdminArea ? "/admin/dashboard" : "/"} className="brand-logo">
          <FaHotel className="me-2" size={28} />
          <span className="brand-name">
            Oaxaca Dreams
            {isAdminArea && <Badge bg="warning" text="dark" className="ms-2">Admin</Badge>}
          </span>
        </BootstrapNavbar.Brand>

        <BootstrapNavbar.Toggle aria-controls="basic-navbar-nav" />

        <BootstrapNavbar.Collapse id="basic-navbar-nav">
          <Nav className="ms-auto">
            {isAdminArea ? (
              // Menú de Administración
              <>
                <Nav.Link as={Link} to="/admin/dashboard" onClick={handleClose}>
                  <FaHome className="me-1" /> Dashboard
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/reservations" onClick={handleClose}>
                  <FaCalendarCheck className="me-1" /> Reservas
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/rooms" onClick={handleClose}>
                  <FaBed className="me-1" /> Habitaciones
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/packages" onClick={handleClose}>
                  <FaBox className="me-1" /> Paquetes
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/reports" onClick={handleClose}>
                  <FaChartBar className="me-1" /> Reportes
                </Nav.Link>
                <Nav.Link as={Link} to="/admin/settings" onClick={handleClose}>
                  <FaCog className="me-1" /> Configuración
                </Nav.Link>
                {isAuthenticated && (
                  <>
                    <Nav.Item className="d-flex align-items-center mx-2">
                      <span className="text-light">
                        <FaUser className="me-1" />
                        {user?.name}
                      </span>
                    </Nav.Item>
                    <Button
                      variant="outline-light"
                      size="sm"
                      onClick={handleLogout}
                      className="ms-2"
                    >
                      <FaSignOutAlt className="me-1" /> Salir
                    </Button>
                  </>
                )}
              </>
            ) : (
              // Menú Público
              <>
                <Nav.Link as={Link} to="/" onClick={handleClose}>
                  Inicio
                </Nav.Link>
                <Nav.Link as={Link} to="/rooms" onClick={handleClose}>
                  <FaBed className="me-1" /> Habitaciones
                </Nav.Link>
                <Nav.Link as={Link} to="/reservations" onClick={handleClose}>
                  <FaCalendarCheck className="me-1" /> Mis Reservaciones
                </Nav.Link>
                <Nav.Link as={Link} to="/contact" onClick={handleClose}>
                  <FaPhone className="me-1" /> Contacto
                </Nav.Link>
                <Button
                  variant="outline-primary"
                  size="sm"
                  as={Link}
                  to="/admin/login"
                  onClick={handleClose}
                  className="ms-2"
                >
                  <FaUser className="me-1" /> Admin
                </Button>
              </>
            )}
          </Nav>
        </BootstrapNavbar.Collapse>
      </Container>
    </BootstrapNavbar>
  );
};

export default Navbar;
