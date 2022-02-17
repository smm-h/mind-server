package ir.smmh.util;

import ir.smmh.lingu.Code;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static ir.smmh.util.FunctionalUtil.with;

@ParametersAreNonnullByDefault
public interface MarkupWriter {

    @NotNull Document createDocument();

    @NotNull List createList(boolean ordered);

    MarkupFragment plain(String text);

    MarkupFragment bold(MarkupFragment fragment);

    MarkupFragment italic(MarkupFragment fragment);

    MarkupFragment underline(MarkupFragment fragment);

    MarkupFragment strike(MarkupFragment fragment);

    default MarkupFragment bold(String text) {
        return bold(plain(text));
    }

    default MarkupFragment italic(String text) {
        return italic(plain(text));
    }

    default MarkupFragment underline(String text) {
        return underline(plain(text));
    }

    default MarkupFragment strike(String text) {
        return strike(plain(text));
    }

    MarkupFragment code(String codeString);

    MarkupFragment link(MarkupFragment fragment, String url);

    default MarkupFragment link(String text, String url) {
        return link(plain(text), url);
    }

    interface MarkupFragmentBuilder {
        @NotNull MarkupFragment build();
    }

    interface Document extends Section {
        @NotNull Code toCode();
    }

    interface Section extends MarkupFragmentBuilder {
        @Contract("_->this")
        @NotNull Section sectionBegin(MarkupFragment title);

        @Contract("_->this")
        default @NotNull Section sectionBegin(String titleData) {
            return sectionBegin(new MarkupFragment(titleData));
        }

        @Contract("->this")
        @NotNull Section sectionEnd();

        @Contract("_->this")
        @NotNull Section writeParagraph(MarkupFragment fragment);

        @Contract("_->this")
        default @NotNull Section writeParagraph(String fragmentData) {
            return writeParagraph(new MarkupFragment(fragmentData));
        }

        @Contract("_->this")
        @NotNull Section writeCodeBlock(String codeString);

        @Contract("_->this")
        default @NotNull Section writeCodeBlock(Code code) {
            return writeCodeBlock(with(code.getOpenFile().getContents(), "..."));
        }

        @Contract("_,_->this")
        @NotNull Section writeList(List list, @Nullable MarkupFragment caption);

        @Contract("_,_->this")
        default @NotNull Section writeList(List list, @Nullable String caption) {
            //noinspection ConstantConditions
            return writeList(list, with(caption, MarkupFragment::new, null));
        }
    }

    interface List extends MarkupFragmentBuilder {
        @Contract("_->this")
        @NotNull List append(MarkupFragment item);

        @Contract("_->this")
        default @NotNull List append(String itemData) {
            return append(new MarkupFragment(itemData));
        }

        @Contract("_->this")
        @NotNull List nest(List innerList);
    }

    abstract class AbstractSection implements Section {

        private final StringBuilder builder = new StringBuilder();

        protected int depth = 0;

        @Override
        public @NotNull MarkupFragment build() {
            return new MarkupFragment(builder.toString());
        }

        protected void write(MarkupFragment fragment) {
            write(fragment.getData());
        }

        protected void write(String fragmentData) {
            builder.append(fragmentData);
        }
    }
}
