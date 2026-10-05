package com.atlas.bank.atlas_bank.transaction.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Validador custom: evita que hagas una transferencia a tu misma cuenta.
// Se pone a nivel de clase porque necesita comparar 2 campos: source y target.
@Target(ElementType.TYPE) // Solo se puede usar en clases, no en campos
@Retention(RetentionPolicy.RUNTIME) // Guarda la anotación cuando corre la app, si no Bean Validation no la ve
@Constraint(validatedBy = DifferentAccountsValidator.class) // Aquí está la lógica real
public @interface DifferentAccounts {

    String message() default "Source and target accounts must be different";

    Class<?>[] groups() default {}; // Requerido por Bean Validation, déjalo vacío

    Class<? extends Payload>[] payload() default {}; // Requerido por Bean Validation, déjalo vacío
}