package ir.smmh.util.jile.impl;

import ir.smmh.util.jile.Cache;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CacheImpl<Wrapper, Contents> implements Cache.Extended<Wrapper, Contents> {

    private final Function<Contents, ? extends Wrapper> creator;
    private final Map<Contents, Wrapper> map = new HashMap<>();

    public CacheImpl(Function<Contents, ? extends Wrapper> creator) {
        super();
        this.creator = creator;
    }

    @Override
    public final @Nullable Wrapper find(Contents contents) {
        return map.get(contents);
    }

    @Override
    public final void create(Contents contents) {
        map.put(contents, creator.apply(contents));
    }

    @Override
    public final boolean exists(Contents contents) {
        return map.containsKey(contents);
    }
}
