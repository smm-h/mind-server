package ir.smmh.util.jile;

import ir.smmh.util.Mutable;
import ir.smmh.util.StringUtil;
import ir.smmh.util.jile.impl.OpenFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.InvalidPathException;

import static ir.smmh.util.StringUtil.RADIX_MAX;

/**
 * A high-level way to work with files whose contents you want to
 * handle internally but also may need to handle changes that
 * were made to the file externally. This is not suitable for
 * appending to files. This is not suitable for binary files.
 */
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public interface OpenFile {

    static OpenFile of(String filename) throws InvalidPathException {
        return new OpenFileImpl(filename);
    }

    static OpenFile of(String contents, String ext) {
        return new OpenFileImpl(contents, ext);
    }

    /**
     * @return Whether or not the file exists on disk
     */
    boolean exists();

    /**
     * Reads contents from file. Creates the file if it does not exist.
     *
     * @return Success in reading the contents from file
     * @implNote Be warned that this will discard any internal changes that
     * may have been made to the contents after the last synchronization
     */
    boolean forceRead();

    /**
     * Writes contents to file. Creates the file if it does not exist.
     * If it does exist, it overwrites it.
     *
     * @return Success in writing the contents to file
     * @implNote Be warned that this will discard any external changes that
     * may have been made to the contents after the last synchronization
     */
    boolean forceWrite();

    @Nullable String getFullName();

    void setFullName(String fullName) throws InvalidPathException;

    @Nullable String getExt();

    /**
     * Merely a plain getter with no file operations
     *
     * @return Contents, potentially out of date
     */
    @Nullable String getContents();

    /**
     * Makes sure the string returned is the same as the
     * contents of the file
     *
     * @return The contents
     */
    default @NotNull String read() {
        String c = getContents();
        return synchronize() ? (c == null ? "" : c) : "";
    }

    String getTitle();

    default String getTempFilename() {
        return "C:/t/" + StringUtil.stringOfValue(hashCode(), RADIX_MAX) + "." + getExt();
    }

    /**
     * Creates a local temporary file with a given filename and dumps the
     * contents to it.
     *
     * @param filename The name of the temporary file
     * @return Success on writing the file
     */
    boolean dump(String filename);

    /**
     * Creates a local temporary file with a friendly filename and dumps the
     * contents to it.
     *
     * @return Success on writing the file
     */
    default boolean dump() {
        return dump(getTempFilename());
    }

    /**
     * @return The size of the file in bytes
     * @see #getLength
     */
    long getSize();

    /**
     * @return The number of characters in its content
     * @see #getSize
     */
    default int getLength() {
        return read().length();
    }

    /**
     * This will choose to either write internal changes to disk or read external changes
     * to memory or neither. In case both external and internal changes have been made
     * since the last synchronization, {@link #synchronizationFailed()} is called to
     * choose which contents are legitimate.
     *
     * @return Success on synchronizing contents
     */
    default boolean synchronize() {
        boolean modifiedInternally = getLastModifiedInternally() > getLastSynchronized();
        boolean modifiedExternally = getLastModifiedExternally() > getLastSynchronized();
        if (modifiedInternally && modifiedExternally)
            return synchronizationFailed();
        else if (modifiedInternally)
            return forceWrite();
        else if (modifiedExternally)
            return forceRead();
        else
            return true;
    }

    /**
     * This is called when it fails to synchronize because contents have been modified
     * both on disk and in memory since the last synchronization. In it, you should
     * choose to call either {@link #forceRead()} or {@link #forceWrite()} and return
     * its result. You may choose to have additional functionality in this, like asking
     * the user to decide on which should happen.
     *
     * @return Success on synchronizing contents
     */
    default boolean synchronizationFailed() {
        if (forceWrite())
            return true;
        else
            return forceRead();
    }

    /**
     * @return Whether or not the contents in memory match the contents in file
     */
    default boolean isSynchronized() {
        return getLastSynchronized() >= getLastModifiedInternally() && getLastSynchronized() >= getLastModifiedExternally();
    }

    /**
     * @return The last moment in milliseconds when the contents on disk were modified
     * @implNote This method will probably get help from the operating system
     */
    long getLastModifiedExternally();

    /**
     * @return The last moment in milliseconds when the contents in memory were modified
     * @implNote This is overridden {@link Writeable} to indicate when it was last edited
     */
    default long getLastModifiedInternally() {
        return -1;
    }

    /**
     * @return The last moment in milliseconds when the contents on disk and in memory
     * were ensured to be in-sync.
     */
    long getLastSynchronized();

    interface Writeable extends OpenFile, Mutable {

        static Writeable of(String filename) {
            return new OpenFileImpl.Writeable(filename);
        }

        static Writeable of(String contents, String ext) {
            return new OpenFileImpl.Writeable(contents, ext);
        }

        default void write(String contents) {
            if (!contents.equals(getContents())) {
                setContents(contents);
                postMutate();
            }
        }

        /**
         * Merely a plain setter with no immediate effects on file
         */
        void setContents(String contents);

        @Override
        default long getLastModifiedInternally() {
            return getLastSetContents();
        }

        long getLastSetContents();
    }
}
