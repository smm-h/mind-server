package ir.smmh.mind;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A mind is an interface that allows you to imagine coherent ideas,
 * mutate them, and
 */
public interface Mind {
    /**
     * Find and return an idea with a given name.
     * @param name The name of an idea
     * @param create Create the idea if none is found with that name
     * @return The mutable idea in this mind with that name
     */
    @Nullable
    MutableIdea imagine(String name, boolean create);

    default boolean isMutated() {
        final LocalDateTime lastMutatedOn = getLastMutatedOn();
        if (lastMutatedOn == null) {
            return false;
        } else {
            final Version lastVersion = getLatestVersion(false);
            if (lastVersion == null) {
                return true;
            } else {
                return lastMutatedOn.compareTo(lastVersion.getCreatedOn()) >= 0;
            }
        }
    }

    void setLastMutatedOn(LocalDateTime mutatedOn);

    @Nullable
    LocalDateTime getLastMutatedOn();

    /**
     * Creates a new version of this mind with all the most recent
     * mutations taken into account, and adds it to the version list.
     */
    boolean update();

    /**
     * @return A list of all the available versions of this mind
     */
    @NotNull
    List<Version> getVersions();

    /**
     * If the mind was modified since its latest version was created,
     * a new version will be created that will reflect the new changes.
     *
     * @param update Pass true to make sure the returned version includes
     *               the most recent changes, but the result may be null
     * @return The most recent version of this mind
     */
    @Nullable
    default Version getLatestVersion(boolean update) {
        final List<Version> list = getVersions();
        if (update && (list.isEmpty() || isMutated())) {
            if (!update()) {
                return null;
            }
        }
        return list.isEmpty() ? null : list.get(list.size() - 1);
    }
}
