package com.easytournament.webapp.validation;

import static java.lang.annotation.ElementType.*;  
import static java.lang.annotation.RetentionPolicy.*;  
  
import java.lang.annotation.Documented;  
import java.lang.annotation.Retention;  
import java.lang.annotation.Target;  
import javax.validation.Constraint;  
import javax.validation.Payload;  
import org.primefaces.validate.bean.ClientConstraint;  
  
@Target({METHOD,FIELD,ANNOTATION_TYPE})  
@Retention(RUNTIME)  
@Constraint(validatedBy=EmailConstraintValidator.class)  
@ClientConstraint(resolvedBy=EmailClientValidationConstraint.class)  
@Documented  
public @interface Email {  
      
    String message() default "{com.easytournament.webapp.validation}";  
      
    Class<?>[] groups() default {};  
  
    Class<? extends Payload>[] payload() default {};  
}