package com.covidsample.start.services;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.covidsample.start.model.LocationStats;

@Service
public class CovidDataService {

	private String covid_data_url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

	private List<LocationStats> stats = new ArrayList<LocationStats>();
	
	public List<LocationStats> getStats() {
		return stats;
	}

	@PostConstruct
	@Scheduled(cron = "* * 1 * * *")
	public void fetchCovidData() throws IOException, InterruptedException {
		List<LocationStats> newStats = new ArrayList<LocationStats>();

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(covid_data_url)).build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

		StringReader reader = new StringReader(response.body());
		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		for (CSVRecord record : records) {
			LocationStats stats = new LocationStats();
			stats.setState(record.get("Province/State"));
			stats.setCountry(record.get("Country/Region"));
			int latestRecords = Integer.parseInt(record.get(record.size() - 1));
			int prevRecords = Integer.parseInt(record.get(record.size() - 2));
			stats.setLatestTotalCases(latestRecords);
			stats.setDiffFromPrevDay(latestRecords-prevRecords);
			//System.out.println(stats);
			newStats.add(stats);
		}
		this.stats = newStats;
	}
}
