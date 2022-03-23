package ir.smmh.mpg.rule;

import ir.smmh.mpg.rule.impl.SimulationImpl;

public class RuleAPI {

    private final Simulation simulation = new SimulationImpl();

    public Simulation getSimulation() {
        return simulation;
    }
}
