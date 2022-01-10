package ir.smmh.lingu;

import ir.smmh.lingu.impl.Port;

public interface GrouperMaker extends Maker<Grouper> {
    Port<Grouper> port = new Port<>("GrouperMaker:port");
}
