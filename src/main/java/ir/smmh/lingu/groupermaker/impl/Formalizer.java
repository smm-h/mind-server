package ir.smmh.lingu.groupermaker.impl;

import ir.smmh.lingu.Maker;
import ir.smmh.lingu.groupermaker.GrouperMakerImpl;
import ir.smmh.lingu.processors.Multiprocessor;
import ir.smmh.lingu.settings.FormalSettings;
import ir.smmh.lingu.settings.FormalizationBlueprint;
import ir.smmh.lingu.settings.Length;
import ir.smmh.lingu.settings.impl.FormalizationBlueprintImpl;
import ir.smmh.lingu.settings.impl.SettingsFormalizerImpl;

import java.io.FileNotFoundException;
import java.util.Map;

public class Formalizer extends SettingsFormalizerImpl<GrouperMakerImpl.Definition> {

    public final Maker<Map<GrouperMakerImpl.Definition, FormalSettings>> maker = code -> Formalizer.super.getMapMaker().makeFromCode(code);
    final FormalizationBlueprint blueprintOfStreak = new FormalizationBlueprintImpl(this, "streak", true) {
        @Override
        public void defineBlueprint() {
            addRange("length");
            add("consists-of"); // 'characters'
            add("can-start-with", DEFAULT_VALUE_STRING_SINGLE_QUOTES); // 'characters'
            add("can-end-with", DEFAULT_VALUE_STRING_SINGLE_QUOTES); // 'characters'
            add("cannot-start-with", DEFAULT_VALUE_STRING_SINGLE_QUOTES); // 'characters'
            add("cannot-end-with", DEFAULT_VALUE_STRING_SINGLE_QUOTES); // 'characters'
            addBoolean("ignore", false);
        }
    };
    private final FormalizationBlueprint blueprintOfPattern = new FormalizationBlueprintImpl(this, "pattern", true) {
        @Override
        public void defineBlueprint() {
            addBoolean("precedence", false);
            add("pattern", Length.ONE_OR_MORE);
        }
    };
    private final FormalizationBlueprint blueprintOfMultitude = new FormalizationBlueprintImpl(this, "multitude", true) {

        @Override
        public void defineBlueprint() {

            // number of elements/cells
            addRange("count");

            // cell boundary verbatim
            add("separator", DEFAULT_VALUE_NONE); // ','

            // is this multitude defining a Kept or a Group?
            addBoolean("opaque", false);

            // opener verbatim
            add("starts-with");

            // closer verbatim
            add("ends-with");

            // whether to ignore this or not
            addBoolean("ignore", false);
        }
    };
    private final FormalizationBlueprint blueprintOfRelativePattern = new FormalizationBlueprintImpl(this, "rpattern", false) {
        @Override
        public void defineBlueprint() {
            // TODO inherit from 'blueprintOfPattern';
        }
    };
    private final FormalizationBlueprint blueprintOfMetadata = new FormalizationBlueprintImpl(this, "metadata", false) {
        @Override
        public void defineBlueprint() {
            add("ext", Length.ONE_OR_MORE);
            add("root");
            // add("path");
        }
    };

    public Formalizer() throws FileNotFoundException, Maker.MakingException {
        super("Grouper Maker Formalizer", "ncx");
        ((Multiprocessor) getProcessor()).seal();
    }

    @Override
    public FormalizationBlueprint findType(String type) {
        switch (type) {
            case "streak":
                return blueprintOfStreak;
            case "pattern":
                return blueprintOfPattern;
            case "multitude":
                return blueprintOfMultitude;
            case "rpattern":
                return blueprintOfRelativePattern;
            case "metadata":
                return blueprintOfMetadata;
            default:
                // return super.findType(type);
                return null;
        }
    }

    @Override
    public GrouperMakerImpl.DefinitionImpl wrap(FormalSettings src) {
        switch (src.getSrc().getType().getData()) {
            case "streak":
                return new Streak(this, src);
            case "pattern":
                return new Pattern(src);
            case "multitude":
                return new Multitude(src);
            case "rpattern":
                return new RelativePattern(src);
            case "metadata":
                return new Metadata(src);
            default:
                return null;
        }
    }

}
