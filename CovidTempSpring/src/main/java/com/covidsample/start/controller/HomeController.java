package com.covidsample.start.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.covidsample.start.model.LocationStats;
import com.covidsample.start.services.CovidDataService;

@Controller
public class HomeController {
	
	@Autowired
	CovidDataService service;

	@GetMapping("/")
	public String home(Model model) {
		List<LocationStats> list = service.getStats();
		int totalCases = list.stream().mapToInt(s -> s.getLatestTotalCases()).sum();
		int newCases = list.stream().mapToInt(s -> s.getDiffFromPrevDay()).sum();
		model.addAttribute("locationStats",list);
		model.addAttribute("totalReportedCases",totalCases);
		model.addAttribute("newReportedCases",newCases);
		return "home";
	}
}
