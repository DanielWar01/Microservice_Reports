package uptc.edu.co.Microservice_Reports.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import net.sf.jasperreports.engine.JRException;
import uptc.edu.co.Microservice_Reports.Enums.TipoReporteEnum;
import uptc.edu.co.Microservice_Reports.Model.ReportEventsDTO;
import uptc.edu.co.Microservice_Reports.service.api.ReporteVentasServiceAPI;

@RestController
@RequestMapping("/report")
public class ReportEventsController {

	@Autowired
	private ReporteVentasServiceAPI reporteVentasServiceAPI;

	@GetMapping(path = "/ventas/download")
	public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
			throws JRException, IOException, SQLException {

		System.out.println(params);
		params.put("fecha_inicio", convertirATimestamp(params.get("fecha_inicio").toString()));
		params.put("fecha_fin", convertirATimestamp(params.get("fecha_fin").toString()));
		ReportEventsDTO dto = reporteVentasServiceAPI.obtenerReporteVentas(params);

		InputStreamResource streamResource = new InputStreamResource(dto.getStream());
		MediaType mediaType = null;
		if (params.get("tipo").toString().equalsIgnoreCase(TipoReporteEnum.EXCEL.name())) {
			mediaType = MediaType.APPLICATION_OCTET_STREAM;
		} else {
			mediaType = MediaType.APPLICATION_PDF;
		} 

		return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
				.contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
	}

	public Timestamp convertirATimestamp(String fecha) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(fecha);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Timestamp timestamp = new Timestamp(date.getTime());

		return timestamp;

	}

}
