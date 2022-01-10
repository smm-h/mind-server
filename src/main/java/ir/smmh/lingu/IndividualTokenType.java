package ir.smmh.lingu;

public class IndividualTokenType implements Token.Type.Individual {
    public final String title;

    public IndividualTokenType(String title) {
        this.title = title;
    }

    @Override
    public final String toString() {
        return title;
    }

    public class IndividualToken implements Token.Individual {
        public final String data;
        public final int position;

        public IndividualToken(String data, int position) {
            this.data = data;
            this.position = position;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Token.Individual) {
                Token.Individual token = (Token.Individual) other;
                return getType().equals(token.getType()) && getData().equals(token.getData());
            } else {
                return false;
            }
        }

        @Override
        public String toString() {
            return "<" + getData() + ">";
        }

        public IndividualTokenType getType() {
            return IndividualTokenType.this;
        }

        @Override
        public int getPosition() {
            return position;
        }

        @Override
        public String getData() {
            return data;
        }

        @Override
        public Token.Individual getFirstHandle() {
            return this;
        }

        @Override
        public Token.Individual getLastHandle() {
            return this;
        }

        private class BrokenPiece extends IndividualToken {
            private BrokenPiece(int start, int end) {
                super(IndividualToken.this.getData().substring(start, end), IndividualToken.this.getPosition() + start);
            }

            private BrokenPiece(int start) {
                super(IndividualToken.this.getData().substring(start), IndividualToken.this.getPosition() + start);
            }
        }
    }
}
