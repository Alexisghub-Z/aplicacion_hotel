import { useState, useEffect } from 'react';
import { Card, Form, Button, Alert, Spinner, Badge } from 'react-bootstrap';
import { FaBell, FaEnvelope, FaSms, FaWhatsapp, FaSave, FaCheckCircle } from 'react-icons/fa';
import PropTypes from 'prop-types';
import notificationService from '../../services/notificationService';

const NotificationSettings = ({ customerId }) => {
  const [preferences, setPreferences] = useState({
    emailEnabled: true,
    smsEnabled: false,
    whatsappEnabled: false
  });
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    if (customerId) {
      fetchPreferences();
    }
  }, [customerId]);

  const fetchPreferences = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await notificationService.getPreferences(customerId);
      setPreferences({
        emailEnabled: data.emailEnabled,
        smsEnabled: data.smsEnabled,
        whatsappEnabled: data.whatsappEnabled
      });
    } catch (err) {
      console.error('Error fetching preferences:', err);
      setError('Error al cargar las preferencias de notificación');
    } finally {
      setLoading(false);
    }
  };

  const handleToggle = (channel) => {
    setPreferences(prev => ({
      ...prev,
      [channel]: !prev[channel]
    }));
    setSuccess(false);
  };

  const handleSave = async () => {
    try {
      setSaving(true);
      setError(null);
      setSuccess(false);

      await notificationService.updatePreferences(customerId, preferences);
      setSuccess(true);

      // Ocultar mensaje de éxito después de 3 segundos
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      console.error('Error saving preferences:', err);
      setError('Error al guardar las preferencias. Por favor, intenta nuevamente.');
    } finally {
      setSaving(false);
    }
  };

  const getActiveChannelsCount = () => {
    let count = 0;
    if (preferences.emailEnabled) count++;
    if (preferences.smsEnabled) count++;
    if (preferences.whatsappEnabled) count++;
    return count;
  };

  if (loading) {
    return (
      <Card className="shadow-sm">
        <Card.Body className="text-center py-5">
          <Spinner animation="border" variant="primary" />
          <p className="mt-3 text-muted">Cargando preferencias...</p>
        </Card.Body>
      </Card>
    );
  }

  return (
    <Card className="shadow-sm">
      <Card.Header className="bg-white border-bottom">
        <div className="d-flex justify-content-between align-items-center">
          <h5 className="mb-0">
            <FaBell className="me-2 text-primary" />
            Preferencias de Notificación
          </h5>
          <Badge bg="info">
            {getActiveChannelsCount()} {getActiveChannelsCount() === 1 ? 'canal activo' : 'canales activos'}
          </Badge>
        </div>
      </Card.Header>

      <Card.Body>
        {error && (
          <Alert variant="danger" dismissible onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert variant="success" className="d-flex align-items-center">
            <FaCheckCircle className="me-2" />
            ¡Preferencias guardadas exitosamente!
          </Alert>
        )}

        <p className="text-muted mb-4">
          Elige cómo deseas recibir notificaciones sobre tus reservas
        </p>

        {/* Email */}
        <div className="notification-channel mb-4">
          <div className="d-flex align-items-start">
            <div className="channel-icon me-3">
              <div className={`icon-circle ${preferences.emailEnabled ? 'bg-primary' : 'bg-secondary'} bg-opacity-10`}>
                <FaEnvelope size={24} className={preferences.emailEnabled ? 'text-primary' : 'text-secondary'} />
              </div>
            </div>
            <div className="flex-grow-1">
              <div className="d-flex justify-content-between align-items-center mb-2">
                <div>
                  <h6 className="mb-1">Correo Electrónico</h6>
                  <small className="text-muted">
                    Recibe notificaciones detalladas en tu email
                  </small>
                </div>
                <Form.Check
                  type="switch"
                  id="email-switch"
                  checked={preferences.emailEnabled}
                  onChange={() => handleToggle('emailEnabled')}
                  className="custom-switch-lg"
                />
              </div>
              {preferences.emailEnabled && (
                <Badge bg="success" className="mt-2">
                  <FaCheckCircle className="me-1" />
                  Activo
                </Badge>
              )}
            </div>
          </div>
        </div>

        {/* SMS */}
        <div className="notification-channel mb-4">
          <div className="d-flex align-items-start">
            <div className="channel-icon me-3">
              <div className={`icon-circle ${preferences.smsEnabled ? 'bg-warning' : 'bg-secondary'} bg-opacity-10`}>
                <FaSms size={24} className={preferences.smsEnabled ? 'text-warning' : 'text-secondary'} />
              </div>
            </div>
            <div className="flex-grow-1">
              <div className="d-flex justify-content-between align-items-center mb-2">
                <div>
                  <h6 className="mb-1">SMS</h6>
                  <small className="text-muted">
                    Recibe mensajes de texto cortos
                  </small>
                </div>
                <Form.Check
                  type="switch"
                  id="sms-switch"
                  checked={preferences.smsEnabled}
                  onChange={() => handleToggle('smsEnabled')}
                  className="custom-switch-lg"
                />
              </div>
              {preferences.smsEnabled && (
                <Badge bg="success" className="mt-2">
                  <FaCheckCircle className="me-1" />
                  Activo
                </Badge>
              )}
            </div>
          </div>
        </div>

        {/* WhatsApp */}
        <div className="notification-channel mb-4">
          <div className="d-flex align-items-start">
            <div className="channel-icon me-3">
              <div className={`icon-circle ${preferences.whatsappEnabled ? 'bg-success' : 'bg-secondary'} bg-opacity-10`}>
                <FaWhatsapp size={24} className={preferences.whatsappEnabled ? 'text-success' : 'text-secondary'} />
              </div>
            </div>
            <div className="flex-grow-1">
              <div className="d-flex justify-content-between align-items-center mb-2">
                <div>
                  <h6 className="mb-1">WhatsApp</h6>
                  <small className="text-muted">
                    Recibe mensajes instantáneos con formato enriquecido
                  </small>
                </div>
                <Form.Check
                  type="switch"
                  id="whatsapp-switch"
                  checked={preferences.whatsappEnabled}
                  onChange={() => handleToggle('whatsappEnabled')}
                  className="custom-switch-lg"
                />
              </div>
              {preferences.whatsappEnabled && (
                <Badge bg="success" className="mt-2">
                  <FaCheckCircle className="me-1" />
                  Activo
                </Badge>
              )}
            </div>
          </div>
        </div>

        {/* Información adicional */}
        <Alert variant="info" className="mb-4">
          <small>
            <strong>📌 Nota:</strong> Las notificaciones se enviarán cuando crees, confirmes o canceles una reserva.
            Puedes activar o desactivar los canales en cualquier momento.
          </small>
        </Alert>

        {/* Botón de guardar */}
        <div className="d-grid">
          <Button
            variant="primary"
            size="lg"
            onClick={handleSave}
            disabled={saving}
          >
            {saving ? (
              <>
                <Spinner animation="border" size="sm" className="me-2" />
                Guardando...
              </>
            ) : (
              <>
                <FaSave className="me-2" />
                Guardar Preferencias
              </>
            )}
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};

NotificationSettings.propTypes = {
  customerId: PropTypes.number.isRequired
};

export default NotificationSettings;
