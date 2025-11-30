package com.example.cats.featuretoggle.aspect;

import com.example.cats.featuretoggle.FeatureToggleService;
import com.example.cats.featuretoggle.FeatureToggles;
import com.example.cats.featuretoggle.annotation.FeatureToggle;
import com.example.cats.featuretoggle.exception.FeatureToggleNotEnabledException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class FeatureToggleAspect {

    private final FeatureToggleService featureToggleService;

    @Around(value = "@annotation(featureToggle)")
    public Object checkFeatureToggleAnnotation(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        return checkToggle(joinPoint, featureToggle);
    }

    private Object checkToggle(ProceedingJoinPoint joinPoint, FeatureToggle featureToggle) throws Throwable {
        FeatureToggles toggle = featureToggle.value();

        if (featureToggleService.check(toggle.getFeatureName())) {
            return joinPoint.proceed();
        }

        log.warn("Feature toggle {} is not enabled!", toggle.getFeatureName());
        throw new FeatureToggleNotEnabledException(toggle.getFeatureName());
    }
}
