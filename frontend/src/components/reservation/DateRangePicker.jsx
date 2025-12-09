import { useState, useEffect } from 'react';
import { Form, Row, Col, Alert } from 'react-bootstrap';
import DatePicker from 'react-datepicker';
import PropTypes from 'prop-types';
import axios from 'axios';
import 'react-datepicker/dist/react-datepicker.css';
import './DateRangePicker.css';

const API_URL = 'http://localhost:8080/api';

const DateRangePicker = ({ checkInDate, checkOutDate, onCheckInChange, onCheckOutChange, error, roomId }) => {
  const [occupiedDates, setOccupiedDates] = useState([]);
  const [loading, setLoading] = useState(false);

  // Convertir string a Date object
  const checkInDateObj = checkInDate ? new Date(checkInDate + 'T12:00:00') : null;
  const checkOutDateObj = checkOutDate ? new Date(checkOutDate + 'T12:00:00') : null;

  useEffect(() => {
    if (roomId) {
      fetchOccupiedDates();
    }
  }, [roomId]);

  const fetchOccupiedDates = async () => {
    setLoading(true);
    try {
      const response = await axios.get(`${API_URL}/reservations/room/${roomId}/occupied-dates`);
      const dates = response.data.map(dateStr => new Date(dateStr + 'T12:00:00'));
      setOccupiedDates(dates);
    } catch (err) {
      console.error('Error fetching occupied dates:', err);
    } finally {
      setLoading(false);
    }
  };

  // Verificar si una fecha está ocupada
  const isDateOccupied = (date) => {
    return occupiedDates.some(occupiedDate =>
      date.getFullYear() === occupiedDate.getFullYear() &&
      date.getMonth() === occupiedDate.getMonth() &&
      date.getDate() === occupiedDate.getDate()
    );
  };

  // Deshabilitar fechas ocupadas y fechas pasadas
  const isDateDisabled = (date) => {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return date < today || isDateOccupied(date);
  };

  // Custom day classname para marcar fechas ocupadas en rojo
  const getDayClassName = (date) => {
    if (isDateOccupied(date)) {
      return 'occupied-date';
    }
    return undefined;
  };

  // Calcular número de noches
  const calculateNights = () => {
    if (checkInDateObj && checkOutDateObj) {
      const diffTime = checkOutDateObj - checkInDateObj;
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
      return diffDays > 0 ? diffDays : 0;
    }
    return 0;
  };

  const nights = calculateNights();

  const handleCheckInChange = (date) => {
    if (date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      onCheckInChange(`${year}-${month}-${day}`);
    } else {
      onCheckInChange('');
    }
  };

  const handleCheckOutChange = (date) => {
    if (date) {
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      onCheckOutChange(`${year}-${month}-${day}`);
    } else {
      onCheckOutChange('');
    }
  };

  return (
    <div className="date-range-picker">
      <Row>
        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Fecha de Check-in <span className="text-danger">*</span>
            </Form.Label>
            <DatePicker
              selected={checkInDateObj}
              onChange={handleCheckInChange}
              selectsStart
              startDate={checkInDateObj}
              endDate={checkOutDateObj}
              minDate={new Date()}
              filterDate={(date) => !isDateDisabled(date)}
              dayClassName={getDayClassName}
              dateFormat="dd/MM/yyyy"
              placeholderText="Selecciona fecha de entrada"
              className={`form-control ${error ? 'is-invalid' : ''}`}
              disabled={loading}
              required
              showPopperArrow={false}
              popperPlacement="bottom-start"
              calendarStartDay={1}
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
          </Form.Group>
        </Col>

        <Col md={6} className="mb-3">
          <Form.Group>
            <Form.Label>
              Fecha de Check-out <span className="text-danger">*</span>
            </Form.Label>
            <DatePicker
              selected={checkOutDateObj}
              onChange={handleCheckOutChange}
              selectsEnd
              startDate={checkInDateObj}
              endDate={checkOutDateObj}
              minDate={checkInDateObj || new Date()}
              filterDate={(date) => !isDateDisabled(date)}
              dayClassName={getDayClassName}
              dateFormat="dd/MM/yyyy"
              placeholderText="Selecciona fecha de salida"
              className={`form-control ${error ? 'is-invalid' : ''}`}
              disabled={loading || !checkInDateObj}
              required
              showPopperArrow={false}
              popperPlacement="bottom-start"
              calendarStartDay={1}
            />
            {error && <div className="invalid-feedback d-block">{error}</div>}
          </Form.Group>
        </Col>
      </Row>

      {nights > 0 && (
        <Alert variant="info" className="py-2 px-3 mb-3">
          <strong>{nights}</strong> {nights === 1 ? 'noche' : 'noches'} de estadía
        </Alert>
      )}

      {occupiedDates.length > 0 && (
        <Alert variant="warning" className="py-2 px-3 mb-0">
          <small>
            Las fechas marcadas en <span className="text-danger fw-bold">rojo</span> están ocupadas y no pueden seleccionarse.
          </small>
        </Alert>
      )}
    </div>
  );
};

DateRangePicker.propTypes = {
  checkInDate: PropTypes.string.isRequired,
  checkOutDate: PropTypes.string.isRequired,
  onCheckInChange: PropTypes.func.isRequired,
  onCheckOutChange: PropTypes.func.isRequired,
  error: PropTypes.string,
  roomId: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};

export default DateRangePicker;
