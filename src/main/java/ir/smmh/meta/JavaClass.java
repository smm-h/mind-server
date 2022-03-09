package ir.smmh.meta;

import ir.smmh.util.impl.MultiValueBlankSpace;
import ir.smmh.util.impl.SingleValueBlankSpace;

public interface JavaClass extends JavaType {
    BlankSpace SUPER_CLASS = new SingleValueBlankSpace("super-class", "extends ", " ", "");
    BlankSpace SUPER_INTERFACES = new MultiValueBlankSpace("super-interfaces", "", ", ", "", "implements ", " ", "");
}