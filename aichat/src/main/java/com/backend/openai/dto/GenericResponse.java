package com.backend.openai.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericResponse<T> implements Serializable {
	private static final long serialVersionUID = 7156526077883281625L;

	private String resultCode;

	private String resultDescription;

	private transient T resultObj;

	private long executionTime;

}