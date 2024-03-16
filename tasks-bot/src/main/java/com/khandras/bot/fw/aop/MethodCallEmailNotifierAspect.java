package com.khandras.bot.fw.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khandras.bot.adapter.email.EmailSender;
import com.khandras.bot.fw.annotation.NotifyMethodCall;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormat;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class MethodCallEmailNotifierAspect {
    private final EmailSender emailSender;
    private final ObjectMapper mapper;

    @Around("@annotation(com.khandras.bot.fw.annotation.NotifyMethodCall)")
    public Object notifyMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        var start = Instant.now();
        Object proceed = joinPoint.proceed();
        var timeDiff = new Interval(start, Instant.now());

        sendMessage(joinPoint, timeDiff);

        return proceed;
    }

    private void sendMessage(ProceedingJoinPoint joinPoint, Interval timeDiff) throws Exception {
        var methodCalled = ((MethodSignature) joinPoint.getSignature()).getMethod();

        var sendTo = methodCalled.getAnnotation(NotifyMethodCall.class).to();
        var message = String.format("Executed method %s, execution time: %s,\nArguments:\n%s",
                joinPoint.getSignature().toShortString(),
                PeriodFormat.getDefault().print(timeDiff.toPeriod()),
                mapper.writeValueAsString(joinPoint.getArgs()));

        emailSender.sendMessage(message, sendTo);
    }
}
