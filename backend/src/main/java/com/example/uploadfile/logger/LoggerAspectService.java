//package com.example.uploadfile.logger;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//
//
//@Aspect
//@Component
//public class LoggerAspectService {
//
//    private final Logger log = LoggerFactory.getLogger(this.getClass());
//
//    @Pointcut("within(com.example.uploadfile.UploadController.*)"
//            + " || within(com.example.uploadfile.UserController.*)"
//            + " || within(com.example.uploadfile.UploadFileApplication.*)"
//            + " || within(@org.springframework.web.bind.annotation.RestController *)")
//    public void springBeanPointcut() {
//    }
//
//    @Pointcut("within(com.example.uploadfile.repo.*)"
//            + " || within(com.example.uploadfile.service.*)"
//            + " || within(com.example.uploadfile.controller.*)")
//    public void applicationPackagePointcut() {
//    }
//
//    @Pointcut("@annotation(com.example.uploadfile.logger.LogMePlease)")
//    public void logMePleaseAnnotationPointcut() {
//    }
//
//    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
//    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
//        log.error("Exception in {}.{}() with cause = {}", joinPoint.getSignature().getDeclaringTypeName(),
//                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL");
//    }
//
//    @Around("logMePleaseAnnotationPointcut()")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//        if (log.isDebugEnabled()) {
//            log.debug("Enter: {}.{}()",
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName()
//            );
//        }
//        try {
//            Object result = joinPoint.proceed();
//            if (log.isDebugEnabled()) {
//                log.debug("Exit: {}.{}()",
//                        joinPoint.getSignature().getDeclaringTypeName(),
//                        joinPoint.getSignature().getName()
//                );
//            }
//            return result;
//        } catch (IllegalArgumentException e) {
//            log.error("Illegal argument: {} in {}.{}()",
//                    Arrays.toString(joinPoint.getArgs()),
//                    joinPoint.getSignature().getDeclaringTypeName(),
//                    joinPoint.getSignature().getName()
//            );
//            throw e;
//        }
//    }
//
//
//}
