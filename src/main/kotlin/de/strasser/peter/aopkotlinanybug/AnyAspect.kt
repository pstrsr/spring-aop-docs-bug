package de.strasser.peter.aopkotlinanybug

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class AnyAspect {
    private val DEFAULT_MAX_RETRIES = 2
    private var maxRetries = DEFAULT_MAX_RETRIES

    /**
     * Taken from https://docs.spring.io/spring-framework/reference/core/aop/ataspectj/example.html
     */
    @Around("execution(* de.strasser.peter.aopkotlinanybug.AopAnyExample.failsBecauseOfAny(..))")
    fun doConcurrentOperation(pjp: ProceedingJoinPoint): Any {
        var numAttempts = 0
        var lockFailureException: Exception
        do {
            numAttempts++
            try {
                return pjp.proceed()
            } catch (ex: Exception) {
                lockFailureException = ex
            }

        } while (numAttempts <= this.maxRetries)
        throw lockFailureException
    }
}