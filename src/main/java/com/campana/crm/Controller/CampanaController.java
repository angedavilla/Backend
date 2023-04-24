package com.campana.crm.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.campana.crm.Entity.CampanaEntity;
import com.campana.crm.Service.CampanaService;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/campanas")
public class CampanaController {

	@Autowired
	private CampanaService campanaService;
	
	@PostMapping("/upload")
	public void uploadCampana(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws IOException, CsvValidationException {
        campanaService.uploadCampana(file, response);
    }
	
	@GetMapping("/all")
	public ResponseEntity<List<CampanaEntity>> getCampanas(){
		List<CampanaEntity> Campanas = campanaService.getAllCampana();
		return ResponseEntity.ok(Campanas);
	}
	
	@GetMapping("/count")
	public ResponseEntity<Long> getAllCampanas(){
		Long total = campanaService.getTotalCampanas();
		return ResponseEntity.ok(total);
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<CampanaEntity>> searchCampana(@RequestParam("nombres") String nombres){
		List<CampanaEntity> campanas = campanaService.getCampanasByName(nombres);
		return ResponseEntity.ok(campanas);
	}
	
	@GetMapping("/range")
	public ResponseEntity<List<CampanaEntity>> getCampanasRange(
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end){
		List<CampanaEntity> campanas = campanaService.getCampanasInRange(start, end);
		return ResponseEntity.ok(campanas);
	}
}
