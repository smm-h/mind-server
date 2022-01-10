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

    public OpenFileImpl(String contents, String ext) {
        this(contents, ext, null);
    }

    public OpenFileImpl(String filename) throws InvalidPathException {
        this(null, null, filename);
    }

    private OpenFileImpl(@Nullable String contents, @Nullable String ext, @Nullable String filename) {
        this.contents = contents;
        this.ext = ext;
        if (filename == null) {
            this.path = null;
        } else {
            this.path = Path.of(filename);
            if (this.ext == null)
                this.ext = FileUtil.getExt(this.path.getFileName().toString());
            forceRead();
        }
    }

    @Override
    public boolean exists() {
        return path != null && Files.exists(path);
    }

    @Override
    public boolean forceRead() {
        if (path == null)
            return false;
        try {
            contents = Files.readString(path);
            lastSynchronized = System.currentTimeMillis();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean forceWrite() {
        if (path == null)
            return false;
        try {
            Files.writeString(path, contents == null ? "" : contents);
            lastSynchronized = System.currentTimeMillis();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public @Nullable String getFullName() {
        return path == null ? null : path.toString();
    }

    @Override
    public void setFullName(String fullName) throws InvalidPathException {
        this.path = Path.of(fullName);
    }

    @Override
    public @Nullable String getExt() {
        return ext;
    }

    @Override
    public @Nullable String getContents() {
        return contents;
    }

    @Override
    public String getTitle() {
        return path == null ? "Untitled" + (ext == null ? "" : "." + ext) : path.getFileName().toString();
    }

    @Override
    public boolean dump(String filename) {
        if (contents == null)
            return false;
        try {
            Files.writeString(Path.of(filename), contents);
            return true;
        } catch (IOException | InvalidPathException e) {
            return false;
        }
    }

    @Override
    public long getSize() {
        if (path == null)
            return -1;
        try {
            return Files.size(path);
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public long getLastModifiedExternally() {
        try {
            return path == null ? -1 : Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public long getLastSynchronized() {
        return lastSynchronized;
    }

    public static class Writeable extends OpenFileImpl implements OpenFile.Writeable, Mutable.Injected {

        private final Mutable injectedMutable = new MutableImpl(this);
        private long lastSetContents = -1;

        public Writeable(String contents, String ext) {
            this(contents, ext, null);
        }

        public Writeable(String filename) throws InvalidPathException {
            this(null, null, filename);
        }

        private Writeable(@Nullable String contents, @Nullable String ext, @Nullable String filename) {
            super(contents, ext, filename);
            addOnCleanListener(this::synchronize);
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
        public @NotNull Mutable getInjectedMutable() {
            return injectedMutable;
        }
    }
}
