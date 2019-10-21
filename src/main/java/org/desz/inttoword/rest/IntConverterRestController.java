package org.desz.inttoword.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.desz.inttoword.conversion.functions.ConversionFunction;
import org.desz.inttoword.exceptions.AppConversionException;
import org.desz.inttoword.language.ProvLang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntConverterRestController {
	private static final Logger logger = LoggerFactory.getLogger(IntConverterRestController.class);
	private ConversionFunction conversionFunction;

	@Autowired
	public IntConverterRestController(ConversionFunction conversionFunction) {

		this.conversionFunction = conversionFunction;
	}

	@PostMapping(path = "/convert-int")
	public ResponseEntity<String> convertToWord(@Valid @RequestParam final Integer input, @RequestParam String lang) {

		String str = StringUtils.EMPTY;
		try {
			str = conversionFunction.convertIntToWord(input, ProvLang.valueOf(lang));
		} catch (AppConversionException e) {
			logger.error(e.getMessage());
			return new ResponseEntity<>(str, BAD_REQUEST);
		}
		return new ResponseEntity<>(str, OK);
	}

	/**
	 * Validates input arguments.
	 * 
	 * @param ex the MethodArgumentNotValidException.
	 * @return the Map of field name to error.
	 */
	@ResponseStatus(BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}

}
