package ir.smmh.lingu.impl;

import ir.smmh.jile.common.Resource;
import ir.smmh.lingu.*;

import java.util.*;

public class CodeImpl implements Code {

    public final Language language;
    private Resource resource;

    public CodeImpl(Resource resource) {
        this(resource, Languages.singleton().getLanguageByExt(resource.getExt()));
    }

    public CodeImpl(String encoded, Language language) {
        this(Resource.of(encoded, ""), language);
    }

    public CodeImpl(String encoded, String ext) {
        this(Resource.of(encoded, ext));
    }

    public CodeImpl(Resource resource, Language language) {
        this.resource = Objects.requireNonNull(resource);
        this.language = language == null ? TextLanguage.singleton() : language;
        beProcessed();
    }

    private synchronized void beProcessed() {
        // String i = getIdentity();
        // System.out.println("\n" + i + "\n" + "<".repeat(i.length()) + "\n");

        mishaps.write(this, new HashMap<>());

        language.processor.process(this);

        // System.out.println("\n" + i + "\n" + ">".repeat(i.length()) + "\n");
    }

    public String getExt() {
        return resource.getExt();
    }

    public String getString() {
        return resource.getContents(false);
    }

    public boolean load() {
        if (resource.readFrom()) {
            beProcessed();
            return true;
        } else {
            return false;
        }
    }

    public boolean save() {
        return resource.writeTo(true, true);
    }

    public boolean saveAs(Resource resource) {
        return saveAs(resource, false);
    }

    public boolean saveAs(Resource resource, boolean overwrite) {
        if (resource != null) {
            // preparing to change resource
            if (!resource.equals(this.resource)) {
                // if there is nothing on disk, or
                if (this.resource == null
                        // if the contents on disk are the same as in memory
                        || this.resource.isSynchronized()) {

                    // cut ties with the previous file, as it is synchronized
                    this.resource = resource;
                    // write to the new file
                    return resource.writeTo(overwrite, true);
                }
            }
        }
        return false;
    }

    public Resource getResource() {
        return resource;
    }

    // TODO make this not public
    public class Process implements CodeProcess {

        private final String name;

        public Process(String name) {
            this.name = name;
            // String i = name + ": " + getCode().getIdentity();
            // System.out.println("\n\t" + i + "\n\t" + "<".repeat(i.length()) + "\n");
        }

        private boolean safe = true;

        private boolean finished = false;

        private final List<Mishap> myMishaps = new LinkedList<>();

        @Override
        public void issue(Mishap mishap) {
            Objects.requireNonNull(mishap, "cannot issue a null mishap.");
            if (finished) {
                System.out.println("Tried to add a mishap.");
            } else {
                mishap.setProcess(this);
                if (safe) {
                    if (mishap.isFatal()) {
                        safe = false;
                    }
                }
                myMishaps.add(mishap);
                // if (mishap.fatal) System.out.println("\t" + mishap.toString());
            }
        }

        public Code getCode() {
            return CodeImpl.this;
        }

        @Override
        public boolean finish() {

            finished = true;

            // String i = name + ": " + getCode().getIdentity();
            // System.out.println("\n\t" + i + "\n\t" + ">".repeat(i.length()) + "\n");

            if (safe) {
                return true;

            } else {
                System.out.println("" + myMishaps.size() + " mishap(s) during: '" + name + "' of: " + this);
                for (Mishap mishap : myMishaps)
                    System.out.println("\t" + mishap.toString());

                Map<Token.Individual, Set<Mishap>> map = mishaps.read(getCode());

                for (Mishap mishap : myMishaps) {
                    Token.Individual key = mishap.getToken();
                    if (!map.containsKey(key)) {
                        map.put(key, new HashSet<>());
                    }
                    map.get(key).add(mishap);
                }

                return false;
            }
        }
    }

    @Override
    public String toString() {
        return resource.toString() + ":" + language.primaryExt;
    }

    public int getSize() {
        return resource.getSize();
    }

    public static final Port<List<Token.Individual>> syntax = new Port<>("CodeView:syntax");

    public static final Port<Map<Token.Individual, Set<Mishap>>> mishaps = new Port<>("CodeView:mishaps");

    public static final Port<Map<Token.Individual, Resource>> links = new Port<>("CodeView:links");
}