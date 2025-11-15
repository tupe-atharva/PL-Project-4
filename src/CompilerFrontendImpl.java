
public class CompilerFrontendImpl extends CompilerFrontend {

    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        lex = new LexerImpl();

        //NUM: [0-9]*\.[0-9]+ 
        Automaton num = new AutomatonImpl();
        //Start state, loops on digits
        num.addState(0, true, false);
        // Intermediate state, reached after '.'
        num.addState(1, false, false);
        //Accept state, reached after at least one digit following '.'
        num.addState(2, false, true);

        // 0 -> 0 on 0-9
        addRange(num, 0, 0, '0', '9');
        // 0 -> 1 on '.'
        num.addTransition(0, '.', 1);
        // 1 -> 2 on 0-9
        addRange(num, 1, 2, '0', '9');
        // 2 -> 2 on 0-9
        addRange(num, 2, 2, '0', '9');

        lex.add_automaton(TokenType.NUM, num);

        //PLUS: \+
        Automaton plus = new AutomatonImpl();
        plus.addState(0, true, false);
        plus.addState(1, false, true);
        plus.addTransition(0, '+', 1);
        lex.add_automaton(TokenType.PLUS, plus);

        // MINUS: -
        Automaton minus = new AutomatonImpl();
        minus.addState(0, true, false);
        minus.addState(1, false, true);
        minus.addTransition(0, '-', 1);
        lex.add_automaton(TokenType.MINUS, minus);

        // TIMES: \*
        Automaton times = new AutomatonImpl();
        times.addState(0, true, false);
        times.addState(1, false, true);
        times.addTransition(0, '*', 1);
        lex.add_automaton(TokenType.TIMES, times);

        // DIV: / 
        Automaton div = new AutomatonImpl();
        div.addState(0, true, false);
        div.addState(1, false, true);
        div.addTransition(0, '/', 1);
        lex.add_automaton(TokenType.DIV, div);

        // LPAREN: \( 
        Automaton lparen = new AutomatonImpl();
        lparen.addState(0, true, false);
        lparen.addState(1, false, true);
        lparen.addTransition(0, '(', 1);
        lex.add_automaton(TokenType.LPAREN, lparen);

        //RPAREN: \)
        Automaton rparen = new AutomatonImpl();
        rparen.addState(0, true, false);
        rparen.addState(1, false, true);
        rparen.addTransition(0, ')', 1);
        lex.add_automaton(TokenType.RPAREN, rparen);

        //WHITE_SPACE: (' '|\n|\r|\t)*
        Automaton ws = new AutomatonImpl();
        ws.addState(0, true, true);
        ws.addTransition(0, ' ', 0);
        ws.addTransition(0, '\n', 0);
        ws.addTransition(0, '\r', 0);
        ws.addTransition(0, '\t', 0);
        lex.add_automaton(TokenType.WHITE_SPACE, ws);
    }

    // Helper to add ranges of transitions (e.g. 0-9)
    private void addRange(Automaton a, int start, int end, char from, char to) {
        for (char c = from; c <= to; c++) {
            a.addTransition(start, c, end);
        }
    }

}
