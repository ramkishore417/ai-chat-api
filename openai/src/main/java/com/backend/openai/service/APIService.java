package com.backend.openai.service;

import com.backend.openai.dto.GenericResponse;

public interface APIService {
	/**
	 * Returns a welcome message for the OpenAI API.
	 * @return GenericResponse with welcome message
	 */
	GenericResponse<String> greet();
}
