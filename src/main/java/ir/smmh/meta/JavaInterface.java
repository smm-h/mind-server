package ir.smmh.meta;

import ir.smmh.util.impl.MultiValueBlankSpace;

public interface JavaInterface extends JavaType {
    BlankSpace SUPER_INTERFACES = new MultiValueBlankSpace("super-interfaces", "", ", ", "", "extends ", " ", "");
}
