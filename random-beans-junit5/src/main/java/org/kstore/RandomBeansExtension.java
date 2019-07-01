package org.kstore;

import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

public class RandomBeansExtension implements TestInstancePostProcessor, ParameterResolver {

    private static int MIN_COLLECTION_SIZE = 1;
    private static int MAX_COLLECTION_SIZE = 10;

    private EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .collectionSizeRange(MIN_COLLECTION_SIZE, MAX_COLLECTION_SIZE).build();

    @Override
    public boolean supportsParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().isAnnotationPresent(RandomBean.class);
    }

    @Override
    public Object resolveParameter(final ParameterContext parameterContext, final ExtensionContext extensionContext) throws ParameterResolutionException {
        RandomBean randomBean = parameterContext.getParameter().getAnnotation(RandomBean.class);

        return createRandomBean(parameterContext.getParameter().getType(), parameterContext.getParameter().getParameterizedType(), randomBean);
    }

    @Override
    public void postProcessTestInstance(final Object testInstance, final ExtensionContext context) throws IllegalAccessException {
        for (Field declaredField : testInstance.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(RandomBean.class)) {
                RandomBean randomBean = declaredField.getAnnotation(RandomBean.class);
                boolean accessible = declaredField.isAccessible();

                declaredField.setAccessible(true);
                declaredField.set(testInstance, createRandomBean(declaredField.getType(), declaredField.getGenericType(), randomBean));
                declaredField.setAccessible(accessible);
            }
        }
    }

    private Object createRandomBean(Class<?> clazz, Type type, final RandomBean randomBean) {
        Object result = enhancedRandom.nextObject(clazz);
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            if (parameterizedType.getActualTypeArguments().length == 1) {
                if (Collection.class.isAssignableFrom(result.getClass())) {
                    for (int i = 0; i < randomBean.minCollectionElements() + enhancedRandom.nextInt(randomBean.maxCollectionElements() + randomBean.minCollectionElements()); i++) {
                        ((Collection) result).add(enhancedRandom.nextObject((Class) parameterizedType.getActualTypeArguments()[0]));
                    }
                }
            } else if (parameterizedType.getActualTypeArguments().length == 2) {
                if (Map.class.isAssignableFrom(result.getClass())) {
                    for (int i = 0; i < randomBean.minCollectionElements() + enhancedRandom.nextInt(randomBean.maxCollectionElements() + randomBean.minCollectionElements()); i++) {
                        ((Map) result).put(
                                enhancedRandom.nextObject((Class) parameterizedType.getActualTypeArguments()[0]),
                                enhancedRandom.nextObject((Class) parameterizedType.getActualTypeArguments()[1])
                        );
                    }
                }
            }
        }

        return result;
    }
}
