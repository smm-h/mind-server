package ir.smmh.lingu;

import ir.smmh.lingu.IndividualTokenType.IndividualToken;
import ir.smmh.tree.jile.Tree;
import ir.smmh.tree.jile.impl.LinkedTree;

import java.util.LinkedList;
import java.util.List;

public abstract class CollectiveTokenType implements TokenType {
    public final String title;
    private final String collectiveData;

    public CollectiveTokenType(String title, String collectiveData) {
        this.title = title;
        this.collectiveData = collectiveData;
    }

    @Override
    public final String toString() {
        return title;
    }

    public class CollectiveToken implements Token {

        public final IndividualToken opener, closer;

        // private final LinkedList<Cell> cells = new LinkedList<Cell>();
        private final List<Token> children = new LinkedList<Token>();

        public CollectiveToken(IndividualToken opener, IndividualToken closer) {
            this.opener = opener;
            this.closer = closer;
        }

        public CollectiveToken() {
            // the top token group
            this.opener = null;
            this.closer = null;
        }

        @Override
        public String toString() {
            // return getType().openerData + "..." + getType().closerData;
            return getType().collectiveData;
            // TODO add interactibility to parts of texts
        }

        public String getTypeString() {
            return getType().toString();
        }

        public CollectiveTokenType getType() {
            return CollectiveTokenType.this;
        }

        @Override
        public Integer getPosition() {
            return opener == null ? 0 : opener.position;
        }

        @Override
        public String getData() {
            return opener.data + "..." + closer.data;
        }

        @Override
        public IndividualToken getFirstHandle() {
            return opener;
        }

        @Override
        public IndividualToken getLastHandle() {
            return closer;
        }

        public List<Token> getChildren() {
            return children;
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

        public Tree<Token> toTree() {
            LinkedTree<Token> tree = new LinkedTree<>();
            toTree(tree);
            // System.out.println(tree);
            return tree;
        }

        private void toTree(LinkedTree<Token> tree) {
            tree.addAndGoTo(this);
            for (Token child : children) {
                if (child instanceof CollectiveToken) {
                    ((CollectiveToken) child).toTree(tree);
                } else {
                    tree.add(child);
                }
            }
            tree.goBack();
        }

        // public void split() {
        // cells.add(new Cell());
        // }

        public void add(Token token) {
            children.add(token);
            // cells.getLast().add(token);
        }

        // private class Cell {
        // private final LinkedList<Token> tokens = new LinkedList<Token>();

        // public void add(Token token) {
        // tokens.add(token);
        // }
        // }
    }
}