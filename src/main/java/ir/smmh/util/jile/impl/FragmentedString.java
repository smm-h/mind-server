package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Typeable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class FragmentedString implements Typeable {

    private final int CHUNK_SIZE;

    private final ArrayList<Fragment> fragments = new ArrayList<>(32);
    private @Nullable TypeableFragment typeable;
    private int typeableIndex;
    private int caret;

    public FragmentedString() {
        super();
        CHUNK_SIZE = 256;
    }

    public FragmentedString(int chunkSize) {
        super();
        CHUNK_SIZE = chunkSize;
    }

    public FragmentedString(String string) {
        super();
        CHUNK_SIZE = Math.max(32, Math.min(1024, string.length() / 16));
        append(string);
    }

    public FragmentedString(String string, int chunkSize) {
        super();
        CHUNK_SIZE = chunkSize;
        append(string);
    }

    public final void append(String string) {
        if (CHUNK_SIZE == 0) {
            fragments.add(new RealFragment(string));
        } else {
            int p = 0;
            while (p + CHUNK_SIZE < string.length())
                fragments.add(new RealFragment(string.substring(p, p += CHUNK_SIZE)));
            fragments.add(new RealFragment(string.substring(p)));
        }
    }

    public final String getString() {
        StringBuilder string = new StringBuilder();
        for (Fragment fragment : fragments)
            string.append(fragment.getString());
        return string.toString();
    }

    public final void defragment() {
        String string = getString();
        clear();
        append(string);
    }

    public final void defragment(int from, int to) {

        StringBuilder string = new StringBuilder();

        // find out which fragments each position belongs to
        int indexFirst = indexOfFragmentContaining(from);
        int indexLast = indexOfFragmentContaining(to);

        // remove all other fragments in between, but gather their strings
        for (int index = indexLast; index >= indexFirst; index--)
            string.insert(0, fragments.remove(index).getString());

        // insert back all the gathered string as a single fragment
        fragments.add(indexFirst, new RealFragment(string.toString()));
    }

    public final void clear() {
        fragments.clear();
    }

    private int indexOfFragmentContaining(int position) {
        int index = -1;
        while (position > 0)
            position -= fragments.get(++index).getLength();
        return index;
    }

    private int getFragmentStart(int index) {
        int position = 0;
        while (index > 0)
            position += fragments.get(--index).getLength();
        return position;
    }

    public final void insert(int position, String string) {

        // find out which fragment contains this position
        int index = indexOfFragmentContaining(position);

        // turn absolute position into relative position
        position -= getFragmentStart(index);

        // delete the fragment found so we can break it in half
        Fragment fragment = fragments.remove(index);

        // insert the left half
        if (fragment.getLengthOfFromStartToIndex(position) != 0)
            fragments.add(index++, fragment.fromStartToIndex(position));

        // insert the middle
        if (CHUNK_SIZE == 0) {
            fragments.add(index++, new RealFragment(string));
        } else {
            int p = 0;
            while (p + CHUNK_SIZE < string.length())
                fragments.add(index++, new RealFragment(string.substring(p, p += CHUNK_SIZE)));
            fragments.add(index++, new RealFragment(string.substring(p)));
        }

        // insert the right half
        if (fragment.getLengthOfFromIndexToEnd(position) != 0)
            fragments.add(index, fragment.fromIndexToEnd(position));
    }

    @NotNull
    @Override
    public final String toString() {
        StringBuilder string = new StringBuilder();
        for (Fragment fragment : fragments)
            string.append("[").append(fragment.getString()).append("]");
        return string.toString();
    }

    public final int getLength() {
        int length = 0;
        for (Fragment fragment : fragments)
            length += fragment.getLength();
        return length;
    }

    public final void delete(int from, int to) {

        // find out which fragments each position belongs to
        int indexFirst = indexOfFragmentContaining(from);
        int indexLast = indexOfFragmentContaining(to);

        // turn absolute positions into relative positions
        from -= getFragmentStart(indexFirst);
        to -= getFragmentStart(indexLast);

        // if they belong to the same fragment,
        if (indexFirst == indexLast) {

            // remove that fragment so we can break it into three parts
            Fragment fragment = fragments.remove(indexFirst);

            // insert back the third one-third
            if (fragment.getLengthOfFromIndexToEnd(to) != 0)
                fragments.add(indexFirst, fragment.fromIndexToEnd(to));

            // insert back the first one-third
            if (fragment.getLengthOfFromStartToIndex(from) != 0)
                fragments.add(indexFirst, fragment.fromStartToIndex(from));

        }
        // otherwise
        else {

            // remove all fragments involved from last to first, but keep a reference to the
            // first and last one so we can break them

            // remove the last fragment
            Fragment fragmentLast = fragments.remove(indexLast);

            // remove all other fragments in between
            if (indexLast > indexFirst + 1) {
                fragments.subList(indexFirst + 1, indexLast).clear();
            }

            // remove the first fragment
            Fragment fragmentFirst = fragments.remove(indexFirst);

            // insert back the second half of the last fragment
            if (fragmentLast.getLengthOfFromIndexToEnd(to) != 0)
                fragments.add(indexFirst, fragmentLast.fromIndexToEnd(to));

            // insert back the first half of the first fragment
            if (fragmentFirst.getLengthOfFromStartToIndex(from) != 0)
                fragments.add(indexFirst, fragmentFirst.fromStartToIndex(from));
        }
    }

    private boolean isTyping() {
        return typeable != null;
    }

    private void startTyping() {

        // find out in which fragment our caret is
        int index = indexOfFragmentContaining(caret);

        // if while typing we have moved into another fragment
        if (isTyping() && typeableIndex != index) {
            finishTyping();
        }

        // if we are not typing
        if (!isTyping()) {

            // if that fragment is not typeable, make it so
            if (!(fragments.get(index) instanceof TypeableFragment))
                fragments.set(index, new TypeableFragment(fragments.get(index).getString()));

            typeable = (TypeableFragment) fragments.get(index);

            typeableIndex = index;
        }
    }

    private void finishTyping() {

        // if was typing
        if (isTyping()) {

            // turn that typeable fragment into a non-typeable one
            fragments.set(typeableIndex, new RealFragment(fragments.get(typeableIndex).getString()));

            // signal that we are not typing anymore
            typeable = null;
        }
    }

    @Override
    public final void type(char c) {
        startTyping();
        int p = caret - getFragmentStart(typeableIndex);
        typeable.string = typeable.string.substring(0, p) + c + typeable.string.substring(p);
        typeable.length++;
        caret++;
    }

    @Override
    public final void pressBackspace() {
        startTyping();
        int p = caret - getFragmentStart(typeableIndex);
        typeable.string = typeable.string.substring(0, p - 1) + typeable.string.substring(p);
        typeable.length--;
        caret--;
    }

    @Override
    public final int getCaret() {
        return caret;
    }

    @Override
    public final void setCaret(int position) {
        finishTyping();
        caret = position;
    }

    private interface Fragment {
        int getLength();

        String getString();

        Fragment fromStartToIndex(int index);

        Fragment fromIndexToEnd(int index);

        int getLengthOfFromStartToIndex(int index);

        int getLengthOfFromIndexToEnd(int index);
    }

    private abstract static class AbstractFragment implements Fragment {
        protected int length;

        AbstractFragment(int length) {
            super();
            if (length == 0)
                throw new NullPointerException("String fragment length cannot be zero");
            this.length = length;
        }

        @Override
        public final int getLength() {
            return length;
        }
    }

    private class RealFragment extends AbstractFragment {
        final char[] array;

        RealFragment(String string) {
            super(string.length());
            int index = 0;
            array = new char[length];
            while (index < length)
                array[index] = string.charAt(index++);
        }

        @Override
        public final String getString() {
            return new String(array);
        }

        @Override
        public final Fragment fromStartToIndex(int index) {
            return new FakeFragment(this, 0, index);
        }

        @Override
        public final Fragment fromIndexToEnd(int index) {
            return new FakeFragment(this, index, length);
        }

        @Override
        public final int getLengthOfFromStartToIndex(int index) {
            return index;
        }

        @Override
        public final int getLengthOfFromIndexToEnd(int index) {
            return length - index;
        }
    }

    private class FakeFragment extends AbstractFragment {
        final RealFragment parent;
        final int offset;

        FakeFragment(RealFragment real, int start, int end) {
            super(end - start);
            parent = real;
            offset = start;
        }

        @Override
        public final String getString() {
            return new String(parent.array, offset, length);
        }

        @Override
        public final Fragment fromStartToIndex(int index) {
            return new FakeFragment(parent, offset, index);
        }

        @Override
        public final Fragment fromIndexToEnd(int index) {
            return new FakeFragment(parent, offset + index, offset + length);
        }

        @Override
        public final int getLengthOfFromStartToIndex(int index) {
            return index - offset;
        }

        @Override
        public final int getLengthOfFromIndexToEnd(int index) {
            return length - index;
        }
    }

    private class TypeableFragment extends AbstractFragment {

        String string;

        TypeableFragment(String string) {
            super(string.length());
            this.string = string;
        }

        @Override
        public final String getString() {
            return string;
        }

        @Override
        public final Fragment fromStartToIndex(int index) {
            return new RealFragment(string.substring(0, index));
        }

        @Override
        public final Fragment fromIndexToEnd(int index) {
            return new RealFragment(string.substring(index, length));
        }

        @Override
        public final int getLengthOfFromStartToIndex(int index) {
            return index;
        }

        @Override
        public final int getLengthOfFromIndexToEnd(int index) {
            return length - index;
        }
    }
}
