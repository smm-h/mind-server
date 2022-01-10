package ir.smmh.lingu;

import ir.smmh.lingu.CollectiveTokenType.CollectiveToken;
import ir.smmh.lingu.impl.Port;

public interface Grouper {

    // public Tree<Group> aggroup(Code code);

    Port<CollectiveToken> grouped = new Port<>("Grouper:grouped");

}
