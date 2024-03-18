package uptc.edu.co.Microservice_Reports.service.api;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import uptc.edu.co.Microservice_Reports.Model.ReportEventsDTO;

import net.sf.jasperreports.engine.JRException;

public interface ReporteVentasServiceAPI {

	ReportEventsDTO obtenerReporteVentas(Map<String, Object> params) throws JRException, IOException, SQLException;

}
