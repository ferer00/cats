package com.example.cats.service;

import com.example.cats.featuretoggle.FeatureToggleService;
import com.example.cats.featuretoggle.FeatureToggles;
import com.example.cats.featuretoggle.aspect.FeatureToggleAspect;
import com.example.cats.featuretoggle.exception.FeatureToggleNotEnabledException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CosmoCatService Unit Tests (AOP)")
class CosmoCatServiceTest {

    private CosmoCatService cosmoCatService;
    private CosmoCatService proxiedService;

    @Mock
    private FeatureToggleService featureToggleService;

    private FeatureToggleAspect featureToggleAspect;

    @BeforeEach
    void setUp() {
        cosmoCatService = new CosmoCatService();
        featureToggleAspect = new FeatureToggleAspect(featureToggleService);
        AspectJProxyFactory proxyFactory = new AspectJProxyFactory(cosmoCatService);
        proxyFactory.addAspect(featureToggleAspect);
        proxiedService = proxyFactory.getProxy();
    }

    @Test
    @DisplayName("Should return cosmo cats when feature is enabled")
    void getCosmoCats_WhenEnabled_ReturnsData() {
        String featureName = FeatureToggles.COSMO_CATS.getFeatureName();
        when(featureToggleService.check(featureName)).thenReturn(true);

        List<String> cats = proxiedService.getCosmoCats();

        assertThat(cats).isNotNull();
        assertThat(cats).contains("Captain Meow");
        verify(featureToggleService).check(featureName);
    }

    @Test
    @DisplayName("Should throw exception when feature is disabled")
    void getCosmoCats_WhenDisabled_ThrowsException() {
        String featureName = FeatureToggles.COSMO_CATS.getFeatureName();
        when(featureToggleService.check(featureName)).thenReturn(false);

        assertThatThrownBy(() -> proxiedService.getCosmoCats())
                .isInstanceOf(FeatureToggleNotEnabledException.class)
                .hasMessage("Feature 'cosmoCatsEnabled' is not enabled.");

        verify(featureToggleService).check(featureName);
    }
}