/**
 * 
 */
package com.service.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.constants.AppEntityCodes;

@RestController
@RequestMapping(AppEntityCodes.EMPLOYEE)
public class EmployeeAddressController {

	public EmployeeAddressController() {
	}

	@GetMapping("address/{id}")
	public Map<String, Object> getAddress(final HttpServletRequest httpServletRequest,
			@PathVariable("id") final Long id) {
		System.out.println("Employee id in service-2 --> " + id);

		Map<String, Object> returnMap = new HashMap<>();

		if (id == 1) {
			returnMap = Map.of("employeeId", "1", "Address", "University of Gujarat", "Country", "India", "State",
					"Gujarat");
		} else if (id == 2) {
			returnMap = Map.of("employeeId", "2", "Address", "University of Victoria", "Country", "Australia", "State",
					"Victoria");
		}
		return returnMap;
	}
}
