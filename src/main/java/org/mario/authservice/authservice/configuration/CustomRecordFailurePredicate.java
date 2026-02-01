package org.mario.authservice.authservice.configuration;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import java.util.function.Predicate;

/**
 * The CustomRecordFailurePredicate class is a custom implementation of the {@link Predicate}
 * interface used to determine whether a specific {@link Throwable} should trigger
 * a failure in a system, particularly when used in conjunction with fault-tolerant mechanisms
 * such as a Circuit Breaker.
 *
 * Functionality:
 * - Evaluates a supplied {@link Throwable} instance to decide whether it should be logged
 *   as a failure.
 * - If the exception is a {@link FeignException}, the decision is based on its HTTP status code.
 *   - HTTP 404 (Not Found), 401 (Unauthorized), and 400 (Bad Request) are considered functional errors
 *     and do not activate the Circuit Breaker (do not count as failures).
 *   - HTTP 5xx errors (server-specific errors) are considered system failures and are treated as failures.
 * - Any other {@link Throwable} is automatically classified as a failure.
 *
 * Purpose:
 * - This class provides a filtering mechanism to distinguish between functional errors
 *   (e.g., business-level errors) and system-level failures, ensuring that only critical failures
 *   are tracked in systems like Circuit Breakers.
 *
 * Usage Context:
 * - Intended for use in scenarios where selective failure tracking is required, such as service-to-service
 *   communication with resilience patterns (e.g., when using Feign clients with Spring Cloud Resilience4J).
 * - Ensures that non-critical errors do not trigger system-wide failure mechanisms unnecessarily.
 */
public class CustomRecordFailurePredicate implements Predicate<Throwable> {
    @Override
    public boolean test(Throwable throwable) {
        // Si es un FeignException, filtramos por código HTTP
        if (throwable instanceof FeignException feignException) {
            int status = feignException.status();
            // Ignorar errores funcionales conocidos (no se consideran fallos del sistema)
            if (status == HttpStatus.NOT_FOUND.value() ||
                    status == HttpStatus.UNAUTHORIZED.value() ||
                    status == HttpStatus.BAD_REQUEST.value()) {
                return false; // NO activa el CircuitBreaker
            }
            // Si es 5xx, sí queremos que cuente como fallo
            if (status >= 500) {
                return true;
            }
        }
        // Si es cualquier otra excepción técnica (timeouts, etc.), la registramos como fallo
        return true;
    }
}
