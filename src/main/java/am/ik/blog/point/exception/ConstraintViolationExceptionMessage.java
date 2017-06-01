/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package am.ik.blog.point.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Jon Brisbin
 * @author Oliver Gierke
 */
public class ConstraintViolationExceptionMessage {

	private final List<ValidationError> errors = new ArrayList<>();

	public ConstraintViolationExceptionMessage(BindingResult bindingResult,
			MessageSourceAccessor accessor) {

		Assert.notNull(bindingResult, "BindingResult must not be null!");
		Assert.notNull(accessor, "MessageSourceAccessor must not be null!");

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			this.errors.add(ValidationError.of(fieldError.getObjectName(),
					fieldError.getField(), fieldError.getRejectedValue(),
					accessor.getMessage(fieldError)));
		}
	}

	@JsonProperty("errors")
	public List<ValidationError> getErrors() {
		return errors;
	}

	public static class ValidationError {
		@JsonProperty("entity")
		String entity;
		@JsonProperty("property")
		String property;
		@JsonProperty("invalidValue")
		Object invalidValue;
		@JsonProperty("message")
		String message;

		static ValidationError of(String entity, String property, Object invalidValue,
				String message) {
			ValidationError error = new ValidationError();
			error.entity = entity;
			error.property = property;
			error.invalidValue = invalidValue;
			error.message = message;
			return error;
		}
	}
}