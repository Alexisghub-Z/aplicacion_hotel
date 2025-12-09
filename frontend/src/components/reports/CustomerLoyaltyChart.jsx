import { Card } from 'react-bootstrap';
import { FaUsers, FaStar } from 'react-icons/fa';
import PropTypes from 'prop-types';

const CustomerLoyaltyChart = ({ data }) => {
  const loyaltyConfig = {
    REGULAR: { label: 'Regular', color: '#6c757d', discount: '0%' },
    SILVER: { label: 'Silver', color: '#c0c0c0', discount: '5%' },
    GOLD: { label: 'Gold', color: '#ffd700', discount: '10%' },
    PLATINUM: { label: 'Platinum', color: '#e5e4e2', discount: '20%' }
  };

  const calculatePercentage = (count) => {
    if (!data?.totalCustomers || data.totalCustomers === 0) return 0;
    return Math.round((count / data.totalCustomers) * 100);
  };

  return (
    <Card className="shadow-sm h-100">
      <Card.Header className="bg-white border-bottom">
        <h5 className="mb-0">
          <FaUsers className="me-2 text-primary" />
          Clientes por Nivel de Lealtad
        </h5>
      </Card.Header>
      <Card.Body>
        {/* Total de Clientes */}
        <div className="text-center mb-4 p-3 bg-light rounded">
          <small className="text-muted d-block mb-1">Total de Clientes</small>
          <h2 className="text-primary mb-0">{data?.totalCustomers || 0}</h2>
        </div>

        {/* Desglose por Nivel */}
        <div className="loyalty-breakdown">
          {data?.customersByLoyalty && Object.entries(data.customersByLoyalty)
            .sort((a, b) => {
              const order = ['PLATINUM', 'GOLD', 'SILVER', 'REGULAR'];
              return order.indexOf(a[0]) - order.indexOf(b[0]);
            })
            .map(([level, count]) => {
              const config = loyaltyConfig[level];
              const percentage = calculatePercentage(count);

              return (
                <div key={level} className="mb-4">
                  <div className="d-flex justify-content-between align-items-center mb-2">
                    <div className="d-flex align-items-center">
                      <FaStar
                        className="me-2"
                        style={{ color: config.color }}
                        size={20}
                      />
                      <div>
                        <div className="fw-medium">{config.label}</div>
                        <small className="text-muted">Descuento: {config.discount}</small>
                      </div>
                    </div>
                    <div className="text-end">
                      <div className="fw-bold">{count}</div>
                      <small className="text-muted">{percentage}%</small>
                    </div>
                  </div>
                  <div className="progress" style={{ height: '8px' }}>
                    <div
                      className="progress-bar"
                      role="progressbar"
                      style={{
                        width: `${percentage}%`,
                        backgroundColor: config.color
                      }}
                      aria-valuenow={percentage}
                      aria-valuemin="0"
                      aria-valuemax="100"
                    />
                  </div>
                </div>
              );
            })}
        </div>

        {/* Estadísticas Adicionales */}
        {data?.topLoyaltyLevel && (
          <div className="mt-4 p-3 bg-light rounded">
            <small className="text-muted d-block mb-1">Nivel más común</small>
            <div className="d-flex align-items-center">
              <FaStar
                className="me-2"
                style={{ color: loyaltyConfig[data.topLoyaltyLevel]?.color }}
              />
              <strong>{loyaltyConfig[data.topLoyaltyLevel]?.label || data.topLoyaltyLevel}</strong>
            </div>
          </div>
        )}
      </Card.Body>
    </Card>
  );
};

CustomerLoyaltyChart.propTypes = {
  data: PropTypes.shape({
    totalCustomers: PropTypes.number,
    customersByLoyalty: PropTypes.object,
    topLoyaltyLevel: PropTypes.string
  })
};

export default CustomerLoyaltyChart;
