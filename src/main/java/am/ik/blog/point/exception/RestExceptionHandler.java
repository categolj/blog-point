package am.ik.blog.point.exception;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import am.ik.blog.point.domain.DomainException;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
	private final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);
	private final MessageSourceAccessor messageSourceAccessor;

	public RestExceptionHandler(MessageSource messageSource) {
		this.messageSourceAccessor = new MessageSourceAccessor(messageSource,
				Locale.getDefault());
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Object responseBody = (body == null) ? new SimpleExceptionMessage(ex.getMessage())
				: body;
		if (log.isInfoEnabled()) {
			log.info("Exception(class={}, message={})", ex.getClass(), ex.getMessage());
		}
		return ResponseEntity.status(status).headers(headers).body(responseBody);
	}

	ResponseEntity<Object> handleBindingResul(Exception ex, BindingResult bindingResult,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ConstraintViolationExceptionMessage message = new ConstraintViolationExceptionMessage(
				bindingResult, messageSourceAccessor);
		return handleExceptionInternal(ex, message, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(BindException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return handleBindingResul(ex, ex.getBindingResult(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status,
			WebRequest request) {
		return handleBindingResul(ex, ex.getBindingResult(), headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String message = String.format("The given request (%s=%s) is not valid.",
				ex.getPropertyName(), ex.getValue());
		return handleExceptionInternal(ex, new SimpleExceptionMessage(message), headers,
				status, request);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e,
			WebRequest request) {
		return this.handleExceptionInternal(e, null, null, HttpStatus.BAD_REQUEST,
				request);
	}

	@ExceptionHandler(DomainException.class)
	ResponseEntity<Object> handleResourceDomainException(DomainException e,
			WebRequest request) {
		return this.handleExceptionInternal(e, null, null, HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException e,
			WebRequest request) {
		return this.handleExceptionInternal(e, null, null, HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler(RuntimeException.class)
	ResponseEntity<Object> handleRuntimeException(RuntimeException e,
			WebRequest request) {
		log.error("Unexpected Exception", e);
		return this.handleExceptionInternal(e,
				new SimpleExceptionMessage("Unexpected Exception"), null,
				HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}