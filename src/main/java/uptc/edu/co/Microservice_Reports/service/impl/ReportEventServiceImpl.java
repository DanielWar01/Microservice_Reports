package uptc.edu.co.Microservice_Reports.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import uptc.edu.co.Microservice_Reports.Commons.JasperReportManager;
import uptc.edu.co.Microservice_Reports.Enums.TipoReporteEnum;
import uptc.edu.co.Microservice_Reports.Model.ReportEventsDTO;
import uptc.edu.co.Microservice_Reports.service.api.ReporteVentasServiceAPI;

@Service
public class ReportEventServiceImpl implements ReporteVentasServiceAPI {

	@Autowired
	private JasperReportManager reportManager;

	@Autowired
	private DataSource dataSource;

	@Override
	public ReportEventsDTO obtenerReporteVentas(Map<String, Object> params)
			throws JRException, IOException, SQLException {
		String fileName = "Events_Report";
		ReportEventsDTO dto = new ReportEventsDTO();
		String extension = params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name()) ? ".xlsx"
				: ".pdf";
		dto.setFileName(fileName + extension);


		ByteArrayOutputStream stream = reportManager.export(fileName, params.get("tipo").toString(), params,
				dataSource.getConnection());

		byte[] bs = stream.toByteArray();
		dto.setStream(new ByteArrayInputStream(bs));
		dto.setLength(bs.length);

		return dto;
	}


}
