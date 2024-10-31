package org.findy.findy_be.common.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidYoutuberId.Validator.class)
@Documented
public @interface ValidYoutuberId {
	String message() default "유튜버 ID는 비어있을 수 없으며 '@'로 시작해야 합니다.";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class Validator implements ConstraintValidator<ValidYoutuberId, String> {
		@Override
		public boolean isValid(String value, ConstraintValidatorContext context) {
			// null 또는 빈 문자열이 아닌지 확인하고, '@'로 시작하는지 검증
			return value != null && !value.isEmpty() && value.startsWith("@");
		}
	}
}

