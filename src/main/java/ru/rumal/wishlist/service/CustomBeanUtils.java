package ru.rumal.wishlist.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.ResolvableType;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class CustomBeanUtils extends BeanUtils {

    public static void copyProperties(Object source,
                                      Object target,
                                      @Nullable String... ignoreProperties) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        PropertyDescriptor[] targetPds = getPropertyDescriptors(target.getClass());
        List<String> ignoreList = ignoreProperties != null ? Arrays.asList(ignoreProperties) : null;

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && (ignoreList == null || !ignoreList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                        ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);
                        boolean isAssignable =
                                !sourceResolvableType.hasUnresolvableGenerics() && !targetResolvableType.hasUnresolvableGenerics()
                                        ? targetResolvableType.isAssignableFrom(sourceResolvableType)
                                        : ClassUtils.isAssignable(writeMethod.getParameterTypes()[0],
                                                                  readMethod.getReturnType());
                        if (isAssignable) {
                            try {
                                if (!Modifier.isPublic(readMethod
                                                               .getDeclaringClass()
                                                               .getModifiers()))
                                    readMethod.setAccessible(true);

                                Object value = readMethod.invoke(source);

                                if (value != null) {
                                    if (!Modifier.isPublic(writeMethod
                                                                   .getDeclaringClass()
                                                                   .getModifiers()))
                                        writeMethod.setAccessible(true);

                                    writeMethod.invoke(target, value);
                                }
                            } catch (Throwable e) {
                                throw new FatalBeanException(
                                        "Could not copy property '" + targetPd.getName() + "' from source to target",
                                        e);
                            }
                        }
                    }
                }
            }
        }
    }
}