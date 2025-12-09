import { useState, useEffect } from 'react';
import { Container, Row, Col, Alert, Spinner, Button, ButtonGroup, Card } from 'react-bootstrap';
import { FaBed, FaUsers, FaCalendarCheck, FaMoneyBillWave, FaDownload, FaChartBar, FaFileExcel } from 'react-icons/fa';
import StatsCard from '../components/reports/StatsCard';
import OccupancyChart from '../components/reports/OccupancyChart';
import RevenueCard from '../components/reports/RevenueCard';
import CustomerLoyaltyChart from '../components/reports/CustomerLoyaltyChart';
import reportService from '../services/reportService';

const Reports = () => {
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [exporting, setExporting] = useState(false);

  useEffect(() => {
    fetchStatistics();
  }, []);

  const fetchStatistics = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await reportService.getStatistics();
      setStatistics(data);
    } catch (err) {
      console.error('Error fetching statistics:', err);
      setError('Error al cargar las estadísticas. Por favor, intenta de nuevo.');
    } finally {
      setLoading(false);
    }
  };

  const handleExport = async (exportType) => {
    try {
      setExporting(true);
      let csvData;
      let filename;

      switch (exportType) {
        case 'rooms':
          csvData = await reportService.exportRooms();
          filename = 'habitaciones.csv';
          break;
        case 'customers':
          csvData = await reportService.exportCustomers();
          filename = 'clientes.csv';
          break;
        case 'reservations':
          csvData = await reportService.exportReservations();
          filename = 'reservas.csv';
          break;
        case 'payments':
          csvData = await reportService.exportPayments();
          filename = 'pagos.csv';
          break;
        default:
          return;
      }

      // Crear blob y descargar
      const blob = new Blob([csvData], { type: 'text/csv;charset=utf-8;' });
      const link = document.createElement('a');
      const url = URL.createObjectURL(blob);
      link.setAttribute('href', url);
      link.setAttribute('download', filename);
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (err) {
      console.error('Error exporting data:', err);
      alert('Error al exportar los datos. Por favor, intenta de nuevo.');
    } finally {
      setExporting(false);
    }
  };

  const handleExcelDownload = async (reportType) => {
    try {
      setExporting(true);
      let blob;
      let filename;

      switch (reportType) {
        case 'reservations':
          blob = await reportService.downloadReservationsExcel();
          filename = 'reporte-reservas.xlsx';
          break;
        case 'revenue':
          blob = await reportService.downloadRevenueExcel();
          filename = 'reporte-ingresos.xlsx';
          break;
        case 'occupancy':
          blob = await reportService.downloadOccupancyExcel();
          filename = 'reporte-ocupacion.xlsx';
          break;
        default:
          return;
      }

      // Crear link temporal y descargar
      const url = window.URL.createObjectURL(new Blob([blob]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error('Error al descargar Excel:', err);
      alert('Error al descargar el archivo Excel. Por favor, intenta de nuevo.');
    } finally {
      setExporting(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-5 text-center">
        <Spinner animation="border" variant="primary" />
        <p className="mt-3 text-muted">Cargando estadísticas...</p>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="py-5">
        <Alert variant="danger">
          {error}
          <Button variant="outline-danger" size="sm" className="ms-3" onClick={fetchStatistics}>
            Reintentar
          </Button>
        </Alert>
      </Container>
    );
  }

  return (
    <Container className="py-5">
      {/* Encabezado */}
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1 className="mb-2">
            <FaChartBar className="me-3 text-primary" />
            Panel de Reportes
          </h1>
          <p className="text-muted mb-0">Estadísticas y análisis del hotel</p>
        </div>
        <ButtonGroup>
          <Button
            variant="outline-primary"
            onClick={() => handleExport('rooms')}
            disabled={exporting}
          >
            <FaDownload className="me-2" />
            Habitaciones
          </Button>
          <Button
            variant="outline-primary"
            onClick={() => handleExport('customers')}
            disabled={exporting}
          >
            <FaDownload className="me-2" />
            Clientes
          </Button>
          <Button
            variant="outline-primary"
            onClick={() => handleExport('reservations')}
            disabled={exporting}
          >
            <FaDownload className="me-2" />
            Reservas
          </Button>
          <Button
            variant="outline-primary"
            onClick={() => handleExport('payments')}
            disabled={exporting}
          >
            <FaDownload className="me-2" />
            Pagos
          </Button>
        </ButtonGroup>
      </div>

      {/* Tarjetas de Estadísticas Principales */}
      <Row className="mb-4">
        <Col md={3} className="mb-3">
          <StatsCard
            icon={<FaBed size={24} />}
            title="Total de Habitaciones"
            value={statistics?.totalRooms || 0}
            subtitle={`${statistics?.availableRooms || 0} disponibles`}
            color="primary"
          />
        </Col>
        <Col md={3} className="mb-3">
          <StatsCard
            icon={<FaUsers size={24} />}
            title="Total de Clientes"
            value={statistics?.totalCustomers || 0}
            subtitle={`${statistics?.topLoyaltyLevel || 'N/A'} más común`}
            color="info"
          />
        </Col>
        <Col md={3} className="mb-3">
          <StatsCard
            icon={<FaCalendarCheck size={24} />}
            title="Total de Reservas"
            value={statistics?.totalReservations || 0}
            subtitle={`Promedio: $${statistics?.averageReservationPrice?.toLocaleString('es-MX') || '0'} MXN`}
            color="warning"
          />
        </Col>
        <Col md={3} className="mb-3">
          <StatsCard
            icon={<FaMoneyBillWave size={24} />}
            title="Ingresos Totales"
            value={`$${statistics?.totalPaymentAmount?.toLocaleString('es-MX') || '0'}`}
            subtitle="MXN"
            color="success"
          />
        </Col>
      </Row>

      {/* Sección de Descargas en Excel */}
      <Card className="mb-4 shadow-sm">
        <Card.Body>
          <div className="d-flex justify-content-between align-items-center mb-3">
            <div>
              <h5 className="mb-1">
                <FaFileExcel className="me-2 text-success" />
                Descargar Reportes en Excel
              </h5>
              <p className="text-muted small mb-0">
                Descarga reportes detallados en formato Excel (.xlsx)
              </p>
            </div>
          </div>
          <div className="d-flex gap-2 flex-wrap">
            <Button
              variant="success"
              onClick={() => handleExcelDownload('reservations')}
              disabled={exporting}
            >
              <FaFileExcel className="me-2" />
              Reporte de Reservas
            </Button>
            <Button
              variant="success"
              onClick={() => handleExcelDownload('revenue')}
              disabled={exporting}
            >
              <FaFileExcel className="me-2" />
              Reporte de Ingresos
            </Button>
            <Button
              variant="success"
              onClick={() => handleExcelDownload('occupancy')}
              disabled={exporting}
            >
              <FaFileExcel className="me-2" />
              Reporte de Ocupación
            </Button>
          </div>
        </Card.Body>
      </Card>

      {/* Gráficos y Análisis Detallados */}
      <Row className="mb-4">
        <Col lg={6} className="mb-4">
          <OccupancyChart data={statistics} />
        </Col>
        <Col lg={6} className="mb-4">
          <RevenueCard data={statistics} />
        </Col>
      </Row>

      <Row>
        <Col lg={12}>
          <CustomerLoyaltyChart data={statistics} />
        </Col>
      </Row>
    </Container>
  );
};

export default Reports;
