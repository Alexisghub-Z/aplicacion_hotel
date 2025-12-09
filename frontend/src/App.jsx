import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';

// Páginas públicas
import Home from './pages/Home';
import Rooms from './pages/Rooms';
import Packages from './pages/Packages';
import NewReservation from './pages/NewReservation';
import ClientReservations from './pages/ClientReservations';

// Páginas de administración
import Login from './pages/Login';
import MyReservations from './pages/MyReservations';
import Reports from './pages/Reports';
import Settings from './pages/Settings';
import RoomManagement from './pages/RoomManagement';

import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';

function AppContent() {
  const location = useLocation();
  const isAdminRoute = location.pathname.startsWith('/admin');
  const isLoginPage = location.pathname === '/admin/login';

  return (
    <div className="app-container d-flex flex-column min-vh-100">
      {/* Mostrar Navbar solo si NO es la página de login */}
      {!isLoginPage && <Navbar />}

      <main className="flex-grow-1">
        <Routes>
          {/* Rutas públicas - accesibles sin login (solo para clientes) */}
          <Route path="/" element={<Home />} />
          <Route path="/rooms" element={<Rooms />} />
          <Route path="/reservations/new" element={<NewReservation />} />
          <Route path="/reservations" element={<ClientReservations />} />
          <Route path="/contact" element={<div className="container mt-5"><h2>Contacto (Próximamente)</h2></div>} />

          {/* Ruta de login */}
          <Route path="/admin/login" element={<Login />} />

          {/* Rutas protegidas de administración */}
          <Route
            path="/admin/dashboard"
            element={
              <ProtectedRoute>
                <Home />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/reservations"
            element={
              <ProtectedRoute>
                <MyReservations />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/rooms"
            element={
              <ProtectedRoute>
                <RoomManagement />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/packages"
            element={
              <ProtectedRoute>
                <Packages />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/reports"
            element={
              <ProtectedRoute>
                <Reports />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/settings"
            element={
              <ProtectedRoute>
                <Settings />
              </ProtectedRoute>
            }
          />
        </Routes>
      </main>

      {/* Mostrar Footer solo si NO es la página de login */}
      {!isLoginPage && <Footer />}
    </div>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

export default App;
