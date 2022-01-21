package ir.smmh.util.jile.impl;

import ir.smmh.util.FileUtil;
import ir.smmh.util.Mutable;
import ir.smmh.util.impl.MutableImpl;
import ir.smmh.util.jile.OpenFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@ParametersAreNonnullByDefault
public class OpenFileImpl implements OpenFile {
    protected @Nullable String contents;
    private @Nullable String ext;
    private @Nullable Path path;
    private long lastSynchronized;

    private OpenFileImpl(String contents, String ext) {
        this(contents, ext, null);
    }

    private OpenFileImpl(String filename) {
        this(null, null, filename);
    }

    private OpenFileImpl(@Nullable String contents, @Nullable String ext, @Nullable String filename) {
        super();
        this.contents = contents;
        this.ext = ext;
        if (filename == null) {
            path = null;
        } else {
            path = Path.of(filename);
            if (this.ext == null)
                this.ext = FileUtil.getExt(path.getFileName().toString());
            forceRead();
        }
    }

    public static OpenFile of(String contents, String ext) {
        return new OpenFileImpl(contents, ext);
    }

    /**
     * @param filename the full path of the file as a string
     * @return a file object open for reading
     * @throws InvalidPathException the given path was invalid
     */
    public static OpenFile of(String filename) {
        return new OpenFileImpl(filename);
    }

    @Override
    public final boolean exists() {
        return path != null && Files.exists(path);
    }

    @Override
    public final boolean forceRead() {
        if (path != null) {
            try {
                contents = Files.readString(path);
                lastSynchronized = System.currentTimeMillis();
                return true;
            } catch (IOException ignored) {
            }
        }
        return false;
    }

    @Override
    public final boolean forceWrite() {
        if (path != null) {
            try {
                Files.writeString(path, contents == null ? "" : contents);
                lastSynchronized = System.currentTimeMillis();
                return true;
            } catch (IOException ignored) {
            }
        }
        return false;
    }

    @Override
    public final @Nullable String getFullName() {
        return path == null ? null : path.toString();
    }

    /**
     * @param fullName the new full path of the file as a string
     * @throws InvalidPathException if the given path was invalid
     */
    @Override
    public final void setFullName(String fullName) {
        path = Path.of(fullName);
    }

    @Override
    public final @Nullable String getExt() {
        return ext;
    }

    @Override
    public final @Nullable String getContents() {
        return contents;
    }

    @SuppressWarnings("NestedConditionalExpression")
    @Override
    public final String getTitle() {
        return path == null ? "Untitled" + (ext == null ? "" : "." + ext) : path.getFileName().toString();
    }

    @Override
    public final boolean dump(String filename) {
        if (contents != null) {
            try {
                Files.writeString(Path.of(filename), contents);
                return true;
            } catch (IOException | InvalidPathException ignored) {
            }
        }
        return false;
    }

    @Override
    public final long getSize() {
        if (path != null) {
            try {
                return Files.size(path);
            } catch (IOException ignored) {
            }
        }
        return -1;
    }

    @Override
    public final long getLastModifiedExternally() {
        try {
            return path == null ? -1 : Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public final long getLastSynchronized() {
        return lastSynchronized;
    }

    @SuppressWarnings("MethodOverridesStaticMethodOfSuperclass")
    public static final class Writeable extends OpenFileImpl implements OpenFile.Writeable, Mutable.WithListeners.Injected {

        private final Mutable.WithListeners injectedMutable = MutableImpl.blank();
        private long lastSetContents = -1;

        private Writeable(String contents, String ext) {
            this(contents, ext, null);
        }

        private Writeable(String filename) {
            this(null, null, filename);
        }

        private Writeable(@Nullable String contents, @Nullable String ext, @Nullable String filename) {
            super(contents, ext, filename);
            getOnCleanListeners().add(this::synchronize);
        }

        public static OpenFile.Writeable of(String contents, String ext) {
            return new OpenFileImpl.Writeable(contents, ext);
        }

        /**
         * @param filename the full path of the file as a string
         * @return a file object open for reading and writing
         * @throws InvalidPathException if the given path was invalid
         */
        public static OpenFile.Writeable of(String filename) {
            return new OpenFileImpl.Writeable(filename);
        }

        @Override
        public void setContents(String contents) {
            this.contents = contents;
            lastSetContents = System.currentTimeMillis();
        }

        @Override
        public long getLastSetContents() {
            return lastSetContents;
        }

        @Override
        public @NotNull Mutable.WithListeners getInjectedMutable() {
            return injectedMutable;
        }
    }
}
