import { Card } from 'react-bootstrap';
import PropTypes from 'prop-types';

const StatsCard = ({ icon, title, value, subtitle, color = 'primary', trend }) => {
  return (
    <Card className="stats-card shadow-sm h-100 border-0">
      <Card.Body>
        <div className="d-flex justify-content-between align-items-start mb-3">
          <div className={`stats-icon bg-${color} bg-opacity-10 text-${color} rounded-3 p-3`}>
            {icon}
          </div>
          {trend && (
            <div className={`trend ${trend.direction === 'up' ? 'text-success' : 'text-danger'}`}>
              <small>{trend.value}</small>
            </div>
          )}
        </div>
        <h3 className="mb-1 fw-bold">{value}</h3>
        <p className="text-muted mb-1">{title}</p>
        {subtitle && <small className="text-muted">{subtitle}</small>}
      </Card.Body>
    </Card>
  );
};

StatsCard.propTypes = {
  icon: PropTypes.element.isRequired,
  title: PropTypes.string.isRequired,
  value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]).isRequired,
  subtitle: PropTypes.string,
  color: PropTypes.string,
  trend: PropTypes.shape({
    direction: PropTypes.oneOf(['up', 'down']),
    value: PropTypes.string
  })
};

export default StatsCard;
