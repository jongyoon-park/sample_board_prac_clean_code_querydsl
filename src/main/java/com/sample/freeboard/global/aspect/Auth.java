package com.sample.freeboard.global.aspect;

import com.sample.freeboard.domain.account.service.AuthService;
import com.sample.freeboard.global.annotation.AnyoneCallable;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@AllArgsConstructor
public class Auth {

    private final AuthService authService;

    @Before("com.sample.freeboard.global.aspect.PointCut.allController()")
    public void isAnyoneCallable(JoinPoint joinPoint) {
        if (hasAnyoneCallable(joinPoint)) {
            return;
        }

        authService.authenticate(getRequestServlet().getHeader("Authentication"));
    }

    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }

    private boolean hasAnyoneCallable(JoinPoint joinPoint) {
        return Objects.nonNull(getMethod(joinPoint).getDeclaredAnnotation(AnyoneCallable.class));
    }

    private HttpServletRequest getRequestServlet() {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

        return requestAttributes.getRequest();
    }
}
