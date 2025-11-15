
public class ParserImpl extends Parser {

    /*
     * Implements a recursive-descent parser for the following CFG:
     * * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        if (tokens == null) {
            throw new Exception("No tokens provided");
        }
        return parseT();
    }

    // T -> F AddOp T | F
    private Expr parseT() throws Exception {
        Expr f = parseF();

        // Checking if we have AddOp (PLUS or MINUS)
        if (peek(TokenType.PLUS, 0)) {
            consume(TokenType.PLUS);
            Expr t = parseT();
            return new PlusExpr(f, t);
        } else if (peek(TokenType.MINUS, 0)) {
            consume(TokenType.MINUS);
            Expr t = parseT();
            return new MinusExpr(f, t);
        } else {
            // T -> F
            return f;
        }
    }

    // F -> Lit MulOp F | Lit
    private Expr parseF() throws Exception {
        Expr lit = parseLit();

        // Checking if we have MulOp (TIMES or DIV)
        if (peek(TokenType.TIMES, 0)) {
            consume(TokenType.TIMES);
            Expr f = parseF();
            return new TimesExpr(lit, f);
        } else if (peek(TokenType.DIV, 0)) {
            consume(TokenType.DIV);
            Expr f = parseF();
            return new DivExpr(lit, f);
        } else {
            // F -> Lit
            return lit;
        }
    }

    // Lit -> NUM | LPAREN T RPAREN
    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            Token t = consume(TokenType.NUM);
            return new FloatExpr(Float.parseFloat(t.lexeme));
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr t = parseT();
            consume(TokenType.RPAREN);
            return t;
        } else {
            throw new Exception("Expected NUM or LPAREN");
        }
    }
}
