import { Card, ListGroup, Badge } from 'react-bootstrap';
import { FaMoneyBillWave, FaCreditCard, FaPaypal, FaMoneyBill } from 'react-icons/fa';
import PropTypes from 'prop-types';

const RevenueCard = ({ data }) => {
  const paymentMethodIcons = {
    CREDIT_CARD: <FaCreditCard />,
    PAYPAL: <FaPaypal />,
    CASH: <FaMoneyBill />
  };

  const paymentMethodLabels = {
    CREDIT_CARD: 'Tarjeta de Crédito',
    PAYPAL: 'PayPal',
    CASH: 'Efectivo'
  };

  const paymentMethodColors = {
    CREDIT_CARD: 'primary',
    PAYPAL: 'info',
    CASH: 'success'
  };

  return (
    <Card className="shadow-sm h-100">
      <Card.Header className="bg-white border-bottom">
        <h5 className="mb-0">
          <FaMoneyBillWave className="me-2 text-success" />
          Ingresos y Pagos
        </h5>
      </Card.Header>
      <Card.Body>
        {/* Total de Ingresos */}
        <div className="text-center mb-4 p-3 bg-light rounded">
          <small className="text-muted d-block mb-1">Total de Ingresos</small>
          <h2 className="text-success mb-0">
            ${data?.totalPaymentAmount?.toLocaleString('es-MX') || '0'} MXN
          </h2>
        </div>

        {/* Desglose por Método de Pago */}
        <div className="mb-4">
          <h6 className="text-muted mb-3">Por Método de Pago</h6>
          {data?.paymentsByMethod && Object.entries(data.paymentsByMethod).map(([method, amount]) => (
            <div key={method} className="mb-2">
              <div className="d-flex justify-content-between align-items-center">
                <div className="d-flex align-items-center">
                  <Badge bg={paymentMethodColors[method]} className="me-2 p-2">
                    {paymentMethodIcons[method]}
                  </Badge>
                  <span>{paymentMethodLabels[method] || method}</span>
                </div>
                <strong className="text-success">
                  ${amount?.toLocaleString('es-MX')} MXN
                </strong>
              </div>
            </div>
          ))}
        </div>

        {/* Estadísticas de Reservas */}
        <div>
          <h6 className="text-muted mb-3">Estadísticas de Reservas</h6>
          <ListGroup variant="flush">
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Total de Reservas</span>
              <strong>{data?.totalReservations || 0}</strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between">
              <span className="text-muted">Precio Promedio</span>
              <strong className="text-primary">
                ${data?.averageReservationPrice?.toLocaleString('es-MX') || '0'} MXN
              </strong>
            </ListGroup.Item>
            <ListGroup.Item className="px-0 d-flex justify-content-between border-bottom-0">
              <span className="text-muted">Precio Más Alto</span>
              <strong className="text-success">
                ${data?.highestReservationPrice?.toLocaleString('es-MX') || '0'} MXN
              </strong>
            </ListGroup.Item>
          </ListGroup>
        </div>
      </Card.Body>
    </Card>
  );
};

RevenueCard.propTypes = {
  data: PropTypes.shape({
    totalPaymentAmount: PropTypes.number,
    paymentsByMethod: PropTypes.object,
    totalReservations: PropTypes.number,
    averageReservationPrice: PropTypes.number,
    highestReservationPrice: PropTypes.number
  })
};

export default RevenueCard;
