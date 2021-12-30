package ir.smmh.lingu;

import ir.smmh.lingu.CollectiveTokenType.CollectiveToken;

public interface Grouper {

    // public Tree<Group> aggroup(Code code);

    Port<CollectiveToken> grouped = new Port<CollectiveToken>("Grouper:grouped");

}
