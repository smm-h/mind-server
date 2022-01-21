package ir.smmh.util;

import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TypeQualifierDefault(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@interface MutatingMethod {
}
