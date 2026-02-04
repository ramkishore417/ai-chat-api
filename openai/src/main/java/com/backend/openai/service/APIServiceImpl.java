package com.backend.openai.service;

import org.springframework.stereotype.Service;
import com.backend.openai.dto.GenericResponse;
import com.backend.openai.util.Constants;

@Service
public class APIServiceImpl implements APIService {

	@Override
	public GenericResponse<String> greet() {
		long startTime = System.currentTimeMillis();
		return new GenericResponse<>(
				Constants.RAM_200,
				Constants.SUCCESS,
				"Welcome to the OpenAI API! Successfully connected to the backend.",
				System.currentTimeMillis() - startTime);
	}
}
