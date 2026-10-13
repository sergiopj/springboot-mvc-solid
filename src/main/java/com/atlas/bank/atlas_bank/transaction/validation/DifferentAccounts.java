package com.atlas.bank.atlas_bank.transaction.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Anotación de validación personalizada que asegura que las cuentas origen y destino sean distintas
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifferentAccountsValidator.class)
public @interface DifferentAccounts {

    String message() default "Source and target accounts must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}