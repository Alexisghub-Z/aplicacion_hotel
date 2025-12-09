import { Card, ProgressBar } from 'react-bootstrap';
import { FaBed, FaCheckCircle, FaTimesCircle } from 'react-icons/fa';
import PropTypes from 'prop-types';

const OccupancyChart = ({ data }) => {
  const roomTypes = {
    SINGLE: 'Individual',
    DOUBLE: 'Doble',
    SUITE: 'Suite',
    PRESIDENTIAL: 'Presidencial'
  };

  const calculateOccupancyRate = () => {
    if (!data || data.totalRooms === 0) return 0;
    return Math.round(((data.totalRooms - data.availableRooms) / data.totalRooms) * 100);
  };

  const occupancyRate = calculateOccupancyRate();

  return (
    <Card className="shadow-sm h-100">
      <Card.Header className="bg-white border-bottom">
        <h5 className="mb-0">
          <FaBed className="me-2 text-primary" />
          Ocupación de Habitaciones
        </h5>
      </Card.Header>
      <Card.Body>
        {/* Resumen General */}
        <div className="mb-4">
          <div className="d-flex justify-content-between align-items-center mb-2">
            <h2 className="mb-0">{occupancyRate}%</h2>
            <span className="text-muted">Tasa de Ocupación</span>
          </div>
          <ProgressBar
            now={occupancyRate}
            variant={occupancyRate > 75 ? 'success' : occupancyRate > 50 ? 'warning' : 'info'}
            className="mb-3"
            style={{ height: '10px' }}
          />
          <div className="row text-center">
            <div className="col-4">
              <div className="text-primary fw-bold fs-4">{data?.totalRooms || 0}</div>
              <small className="text-muted">Total</small>
            </div>
            <div className="col-4">
              <div className="text-success fw-bold fs-4">{data?.availableRooms || 0}</div>
              <small className="text-muted">Disponibles</small>
            </div>
            <div className="col-4">
              <div className="text-danger fw-bold fs-4">
                {(data?.totalRooms || 0) - (data?.availableRooms || 0)}
              </div>
              <small className="text-muted">Ocupadas</small>
            </div>
          </div>
        </div>

        {/* Desglose por Tipo */}
        <div>
          <h6 className="text-muted mb-3">Por Tipo de Habitación</h6>
          {data?.roomTypeBreakdown && Object.entries(data.roomTypeBreakdown).map(([type, stats]) => {
            const occupied = stats.total - stats.available;
            const typeOccupancy = stats.total > 0 ? Math.round((occupied / stats.total) * 100) : 0;

            return (
              <div key={type} className="mb-3">
                <div className="d-flex justify-content-between align-items-center mb-1">
                  <span className="fw-medium">{roomTypes[type] || type}</span>
                  <span className="text-muted small">
                    {occupied}/{stats.total} ocupadas ({typeOccupancy}%)
                  </span>
                </div>
                <ProgressBar
                  now={typeOccupancy}
                  variant="primary"
                  className="mb-1"
                  style={{ height: '6px' }}
                />
              </div>
            );
          })}
        </div>
      </Card.Body>
    </Card>
  );
};

OccupancyChart.propTypes = {
  data: PropTypes.shape({
    totalRooms: PropTypes.number,
    availableRooms: PropTypes.number,
    roomTypeBreakdown: PropTypes.object
  })
};

export default OccupancyChart;
