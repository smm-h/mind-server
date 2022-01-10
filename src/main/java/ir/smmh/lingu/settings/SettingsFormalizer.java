package ir.smmh.lingu.settings;

import ir.smmh.lingu.CodeProcess;
import ir.smmh.lingu.Language;

public interface SettingsFormalizer<T extends Settings> extends Language {
    FormalizationBlueprint findType(String type);

    T wrap(FormalSettings src);

    /**
     * TODO
     * <p>
     * In case {@code parentForValues} is of a different type than this node, call
     * this method with a fallback instead, which is another {@code
     * AbsoluteSettings} whose name is the value of the key {@code 'like'} in any
     * node, and must have the same type as this node.
     *
     * <p>
     * If that name is not yet absolved the node enters a queue with that name, and
     * waits to be absolved. If the tree is traversed and that name is not absolved
     * either that name does not exist or there is a cycle which includes this node
     * and its fallback.
     */

    FormalSettings absolve(InformalSettings subj, FormalSettings parentForName, FormalSettings parentForValues, CodeProcess process);

//    <T2 extends Settings> Maker<Map<T2, FormalSettings>> getMapMaker();

    void setDefaultValues();

}
