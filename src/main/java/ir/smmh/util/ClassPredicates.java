package ir.smmh.util;

import java.util.function.Predicate;

public interface ClassPredicates {
    Predicate<Class<?>> ANNOTATION = Class::isAnnotation;
    Predicate<Class<?>> ANONYMOUS_CLASS = Class::isAnonymousClass;
    Predicate<Class<?>> ARRAY = Class::isArray;
    Predicate<Class<?>> ENUM = Class::isEnum;
    Predicate<Class<?>> INTERFACE = Class::isInterface;
    Predicate<Class<?>> LOCAL_CLASS = Class::isLocalClass;
    Predicate<Class<?>> MEMBER_CLASS = Class::isMemberClass;
    Predicate<Class<?>> PRIMITIVE = Class::isPrimitive;
    Predicate<Class<?>> SYNTHETIC = Class::isSynthetic;
}
