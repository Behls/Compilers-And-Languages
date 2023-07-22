package parser;

import tree.*;
import scanner.*;
import java.util.ArrayList;

public class Parser {
    
    private final Lexer lexer;
    private final NodeBuilder builder;
    private final boolean debug;
    
    public Parser(Lexer l, NodeBuilder b, boolean d) {
        debug = d;
        lexer = l;
        builder = b;
        parseBlock();
        if (!lexer.endOfInput()){
            syntaxError("Trailing garbage in input stream");
        }
    }
    
    public void parseExpression() {
        if (debug) { System.err.println("Parser: Entering expression"); }
        parseTerm();
        if (lexer.getItem().isOperator()) {
            Expression left = (Expression) builder.getRootNode();
            String operator = lexer.getItem().getValue();
            parseTerm();
            Expression right = (Expression) builder.getRootNode();
            builder.makeExpression(operator, left, right);
        }
        if (debug) { System.err.println("Parser: Leaving expression"); }
    }

//  added the boolean and string values here

    public void parseTerm(){
        if (debug) { System.err.println("Parser: Entering term"); }
        lexer.next();
        if (lexer.getItem().isInteger()){
            builder.makeIntegerValue(Integer.parseInt(lexer.getItem().getValue()));
            //lexer.next();
        } else if(lexer.getItem().IsString()){
            builder.makeStringValue(lexer.getItem().getValue());
            if(!lexer.getItem().IsString()){
                System.out.println("String value expected");
            }
        } else if(lexer.getItem().isBoolean()){
            builder.makeBooleanValue(Boolean.parseBoolean(lexer.getItem().getValue()));
            if(!lexer.getItem().isBoolean()){
                System.out.println("Boolean value expected");
            }
        } else if (lexer.getItem().isLeftParenthesis()){
            parseExpression();
            if (!lexer.getItem().isRightParenthesis()){
                syntaxError("Right parenthesis expected");
            }
            //lexer.next();
        } else if (lexer.getItem().isIdentifier()){
            builder.makeVariableReference(lexer.getItem().getValue());
            //lexer.next();
        } else {
            syntaxError("Integer, variable or '(' expected");
        }
        if (debug) { System.err.println("Parser: Leaving term"); }
        lexer.next();
    }
    
    public void parseStatement(){
        if (lexer.getItem().isKeywordRead()){
            parseRead();
        } else if (lexer.getItem().isKeywordPrint()){
            parsePrint();
        } else if (lexer.getItem().isKeywordIf()){
            parseConditional();
        } else if (lexer.getItem().isKeywordWhile()){
            parseWhile();
        } else if (lexer.getItem().isIdentifier()){
            parseAssignment();
        } else {
            syntaxError("Assignment, read, print, if or while statement expected");
        }
    }
    
    public void parseRead() {
        if (debug) { System.err.println("Parser: Entering read"); }
        lexer.next();
        if (lexer.getItem().isIdentifier()) {
            builder.makeVariableReference(lexer.getItem().getValue());
            VariableRef target = (VariableRef) builder.getRootNode();
            builder.makeReadStatement(target);
        } else {
            syntaxError("Identifier expected");
        }
        if (debug) { System.err.println("Parser: Leaving read"); }
        lexer.next();
    }
    
    public void parsePrint() {
        if (debug) { System.err.println("Parser: Entering print"); }
        parseExpression();
        Expression argument = (Expression) builder.getRootNode();
        builder.makePrintStatement(argument);
        if (debug) { System.err.println("Parser: Leaving print"); }
    }
    
    public void parseAssignment() {
        if (debug) { System.err.println("Parser: Entering assignment"); }
        builder.makeVariableReference(lexer.getItem().getValue());
        VariableRef target = (VariableRef) builder.getRootNode();
        lexer.next();
        if (lexer.getItem().isAssignment()){
            parseExpression();
            Expression rhs = (Expression) builder.getRootNode();
            builder.makeAssignmentStatement(target, rhs);
        } else {
            syntaxError(":= expected");
        }
        if (debug) { System.err.println("Parser: Leaving assignment"); }
    }
    
    public void parseConditional() {
        if (debug) { System.err.println("Parser: Entering conditional"); }
        parseExpression();
        Expression condition = (Expression) builder.getRootNode();
        if (!lexer.getItem().isKeywordThen()){
            syntaxError("Keyword THEN expected");
        }
        parseBlock();
        Block thenPart = (Block) builder.getRootNode();
        Block elsePart = null;
        if (lexer.getItem().isKeywordElse()){
            parseBlock();
            elsePart = (Block) builder.getRootNode();
        }
        if (lexer.getItem().isKeywordEndif()){
            builder.makeConditionalStatement(condition, thenPart, elsePart);
            //lexer.next();
        } else {
            syntaxError("Keyword ENDIF expected");
        }
        if (debug) { System.err.println("Parser: Leaving conditional"); }
        lexer.next();
    }
    
    public void parseWhile() {
        if (debug) { System.err.println("Parser: Entering while"); }
        parseExpression();
        Expression condition = (Expression) builder.getRootNode();
        if (!lexer.getItem().isKeywordDo()){
            syntaxError("Keyword DO expected");
        }
        parseBlock();
        Block body = (Block) builder.getRootNode();
        if (lexer.getItem().isKeywordEnd()){
            builder.makeWhileStatement(condition, body);
            //lexer.next();
        } else {
            syntaxError("Keyword END expected");
        }
        if (debug) { System.err.println("Parser: Levaing while"); }
        lexer.next();
    }
    
    public void parseBlock() {
        if (debug) { System.err.println("Parser: Entering block"); }
        ArrayList<Statement> statements = new ArrayList<Statement>();
        lexer.next();
        while (!lexer.endOfInput() && lexer.getItem().isStatementKeyword()){
            parseStatement();
            Statement last = (Statement) builder.getRootNode();
            statements.add(last);
        }
        builder.makeBlock(statements);
        if (debug) { System.err.println("Parser: Leaving block"); }
    }
    
    private void syntaxError(String msg) {
        System.err.println("Syntax error: " + msg);
        System.err.println("Last token read = " + lexer.getItem().getValue());
        System.exit(1);
    }        
}
