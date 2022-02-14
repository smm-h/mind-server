package ir.smmh.lingu;

import ir.smmh.tree.Tree;
import ir.smmh.tree.impl.NodedTreeImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CollectiveTokenType implements Token.Type.Collective {
    public final String title;
    private final String collectiveData;

    public CollectiveTokenType(String title, String collectiveData) {
        this.title = title;
        this.collectiveData = collectiveData;
    }

    @NotNull
    @Override
    public final String toString() {
        return title;
    }

    public class CollectiveToken implements Token.Collective {

        public final Token.Individual opener, closer;

        // private final LinkedList<Cell> cells = new ArrayList<>();
        private final List<Token> children = new ArrayList<>();
        private NodedTreeImpl<Token>.Node treeMakingPointer;

        public CollectiveToken(Token.Individual opener, Token.Individual closer) {
            this.opener = opener;
            this.closer = closer;
        }

        public CollectiveToken() {
            // the top token group
            this.opener = null;
            this.closer = null;
        }

        @NotNull
        @Override
        public String toString() {
            // return getType().openerData + "..." + getType().closerData;
            return getType().collectiveData;
            // TODO add interactability to parts of texts
        }

        public String getTypeString() {
            return getType().toString();
        }

        public CollectiveTokenType getType() {
            return CollectiveTokenType.this;
        }

        @Override
        public int getPosition() {
            return opener == null ? -1 : opener.getPosition();
        }

        @Override
        public String getData() {
            // TODO nullplay
            assert opener != null && closer != null;
            return opener.getData() + "..." + closer.getData();
        }

        @Override
        public Token.Individual getFirstHandle() {
            return opener;
        }

        @Override
        public Token.Individual getLastHandle() {
            return closer;
        }

        @Override
        public List<Token> getChildren() {
            return children;
        }

        @Override
        public Individual getCloser() {
            return closer;
        }

        // public Group(LinkedTree<Group> tree) {
        // this(0, 0);
        // // vtree.add(token);
        // addFrom(tree, tree.getRoot());
        // }

        // private void addFrom(LinkedTree<Group> tree, Group node) {
        // for (Group child : tree.getChildren(node)) {
        // // children.add
        // }
        // }

        @Override
        public Individual getOpener() {
            return opener;
        }

        public Tree<Token> toTree() {
            NodedTreeImpl<Token> tree = new NodedTreeImpl<>();
            toTree(tree);
            // System.out.println(tree);
            return tree;
        }

        private void toTree(NodedTreeImpl<Token> tree) {
            treeMakingPointer = tree.new Node(this, treeMakingPointer);
            for (Token child : children) {
                if (child instanceof CollectiveToken) {
                    ((CollectiveToken) child).toTree(tree);
                } else {
                    treeMakingPointer.getChildren().append(tree.new Node(child, treeMakingPointer));
                }
            }
            treeMakingPointer = treeMakingPointer.getParent();
        }

        // public void split() {
        // cells.add(new Cell());
        // }

        public void add(Token token) {
            children.add(token);
            // cells.getLast().add(token);
        }

        // private class Cell {
        // private final LinkedList<Token> tokens = new ArrayList<Token>();

        // public void add(Token token) {
        // tokens.add(token);
        // }
        // }
    }
}