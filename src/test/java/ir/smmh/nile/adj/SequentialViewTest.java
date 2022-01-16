package ir.smmh.nile.adj;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static ir.smmh.util.CharacterPredicates.VOWEL;
import static ir.smmh.util.FunctionalUtil.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SequentialViewTest {
    private static final String[] array = {"apples", "oranges", "bananas"};
    Sequential.Mutable.VariableSize<String> data;

    @BeforeEach
    void beforeEach() {
        data = Sequential.Mutable.VariableSize.of(array);
    }

    @Test
    void viewTest() {
        System.out.println(data);
        AtomicReference<Sequential<String>> view = new AtomicReference<>();
        Sequential.View.Conditional<String> originalView = new Sequential.View.Conditional<>(data, s -> (is(s.charAt(0), VOWEL)));
        originalView.addExpirationHandler(view::set);
        view.set(originalView);
        System.out.println(view);
        data.append("watermelons");
        System.out.println(data);
        System.out.println(view);
        data.append("apricots");
        System.out.println(data);
        System.out.println(view);
        data.removeIndexFrom(1);
        System.out.println(data);
        System.out.println(view);
        data.append("grapes");
        System.out.println(data);
        System.out.println(view);
        assertFalse(data.contains("oranges"));
        assertTrue(view.get().contains("oranges"));
    }

}
