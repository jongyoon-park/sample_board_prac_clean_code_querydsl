package com.sample.freeboard.global.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class PointCut {

    @Pointcut("execution(* com.sample.freeboard.domain.*.controller..*.*(..))")
    public void allController() {

    }
}
