package ir.smmh.apps.plotbot;

public interface Figure {
    Iterable<Part> getParts();

    int getPartCount();

    String getReport();

    interface Part {
        String getColor();

        double getAlpha();

        String getStroke();

        Expression getExpression();

        String getTitle();
    }
}
