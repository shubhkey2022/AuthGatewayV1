/**
 * 
 */
package com.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.service.constants.AppEntityCodes;

@RestController
@RequestMapping(AppEntityCodes.EMPLOYEE)
public class EmployeeAddressController {

	@Autowired
	private RestTemplate restTemplate;

	public EmployeeAddressController() {
	}

	@GetMapping("address/{id}")
	public Map<String, Object> getAddress(final HttpServletRequest httpServletRequest,
			@PathVariable("id") final Long id) {
		System.out.println("Employee id in service-2 --> " + id);

		Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap = Map.of("id", "1", "employeeId", "1", "Address", "University of Gujarat", "Country", "India",
					"State", "Gujarat");
		} else if (id == 2) {
			returnMap = Map.of("id", "2", "employeeId", "2", "Address", "University of Victoria", "Country",
					"Australia", "State", "Victoria");
		}
		return returnMap;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping("address/employee/{id}")
	public Map<String, Object> getAddressAndEmployee(final HttpServletRequest httpServletRequest,
			@PathVariable("id") final Long id) {

		Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap.putAll(Map.of("id", "1", "employeeId", "1", "Address", "University of Gujarat", "Country",
					"India", "State", "Gujarat"));
		} else if (id == 2) {
			returnMap.putAll(Map.of("id", "2", "employeeId", "2", "Address", "University of Victoria", "Country",
					"Australia", "State", "Victoria"));
		}

		// get employee from service-1
		final String url = "http://localhost:8180/api/service-1/employee/emp/" + id;
		final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", MediaType.APPLICATION_JSON.toString());
		headers.add("ApplicationName", "SMBEmployeeServices_UAT_9908");
		final HttpEntity<?> requestEntity = new HttpEntity<>(headers);
		final ResponseEntity<Map> response = this.restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
		final Map<String, Object> address = response.getBody();

		returnMap.put("employee", address);
		return returnMap;
	}
}
