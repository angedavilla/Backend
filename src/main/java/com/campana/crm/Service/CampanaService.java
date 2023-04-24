package com.campana.crm.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.campana.crm.Entity.CampanaEntity;
import com.campana.crm.Repository.CampanaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class CampanaService {

	@Autowired
	private CampanaRepository campanaRepository;

	public void uploadCampana(MultipartFile file, HttpServletResponse response) throws IOException, CsvValidationException {
	    List<CampanaEntity> campanas = new ArrayList<>();

	    // Lee el archivo y convierte a lista de objetos CampanaEntity según la
	    // estructura del archivo
	    if (file.getOriginalFilename().endsWith(".csv")) {
	        campanas = readCsvFile(file.getInputStream());
	    } else if (file.getOriginalFilename().endsWith(".txt")) {
	        campanas = readTxtFile(file.getInputStream());
	    }

	    // Guarda los objetos CampanaEntity en la base de datos
	    campanaRepository.saveAll(campanas);

	    ObjectMapper mapper = new ObjectMapper();
	    mapper.registerModule(new JavaTimeModule());
	    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	    String jsonString = mapper.writeValueAsString(campanas);
	    response.setContentType("application/json");
	    response.getWriter().write(jsonString);
	}

	private List<CampanaEntity> readCsvFile(InputStream is) throws IOException, CsvValidationException {
		List<CampanaEntity> campanas = new ArrayList<>();
		InputStreamReader isr = new InputStreamReader(is);
		com.opencsv.CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
		CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(csvParser).build();
		String[] fields;
		while ((fields = reader.readNext()) != null) {
			if (fields.length == 4) {
				CampanaEntity campana = new CampanaEntity();
				campana.setNombres(fields[0]);
				campana.setApellidos(fields[1]);
				campana.setTelefono(fields[2]);
				campana.setDireccion(fields[3]);
				campanas.add(campana);
			}
		}
		return campanas;
	}

	private List<CampanaEntity> readTxtFile(InputStream is) throws IOException {
		List<CampanaEntity> campanas = new ArrayList<>();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		boolean firstLine = true;
		while ((line = br.readLine()) != null) {
			if (firstLine) { // omitimos la primera línea que contiene los nombres de los campos
				firstLine = false;
				continue;
			}
			String[] fields = line.split(",");
			if (fields.length == 4) {
				CampanaEntity campana = new CampanaEntity();
				campana.setNombres(fields[0]);
				campana.setApellidos(fields[1]);
				campana.setTelefono(fields[2]);
				campana.setDireccion(fields[3]);
				campanas.add(campana);
			}
		}
		return campanas;
	}

	public List<CampanaEntity> getAllCampana() {
		return campanaRepository.findAll();
	}

	public List<CampanaEntity> getCampanasInRange(LocalDate startDate, LocalDate endDate) {
		return campanaRepository.findByFechaBetween(startDate, endDate);
	}

	public long getTotalCampanas() {
		return campanaRepository.count();
	}

	public List<CampanaEntity> getCampanasByName(String nombres) {
		return campanaRepository.findByNombresContainingIgnoreCase(nombres);
	}
	
}
