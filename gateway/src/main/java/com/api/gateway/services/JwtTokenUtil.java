package com.api.gateway.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.api.gateway.constants.Constants;
import com.api.gateway.constants.Constants.JwtKeys;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JwtTokenUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6366567108855027683L;

	public Long getExpirationDate(final String token) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			return claims.get(JwtKeys.EXPIRATION) != null ? (objectToLong(claims.get(JwtKeys.EXPIRATION)) * 1000) : 0L;
		}
		return 0L;
	}

	public boolean validateDisplayName(final String token) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			if (null != claims.get(JwtKeys.APP_DISPLAY_NAME)) {
				final String displayName = claims.get(JwtKeys.APP_DISPLAY_NAME).toString();
				for (final String applicationName : Constants.APPLCIATION_DISPLAY_NAMES) {
					if (applicationName.equalsIgnoreCase(displayName)) {
						return true;
					}
				}
			}
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean validateRole(final String token) {
		final Map<String, Object> claims = getClaim(token);
		if (null != claims) {
			if (null != claims.get(JwtKeys.ROLES)) {
				final List<String> roles = (ArrayList<String>) claims.get(JwtKeys.ROLES);
				if (roles.stream().anyMatch(x -> x.equalsIgnoreCase(Constants.ROLES))) {
					return true;
				}
			}
			return false;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getClaim(final String token) {
		final String[] chunks = token.split("\\.");
		final Base64.Decoder decoder = Base64.getUrlDecoder();
		final String payload = new String(decoder.decode(chunks[1]));
		try {
			return new ObjectMapper().readValue(payload, HashMap.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Long objectToLong(final Object obj) {
		return Long.parseLong(obj.toString());
	}
}
