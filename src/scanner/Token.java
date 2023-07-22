package scanner;

import java.util.List;
import java.util.Arrays;

public class Token {

    private final String value;

    public String getValue() {
        return value;
    }

    private static final List<String> operators
            = Arrays.asList("+", "-", "*", "/", "%", "<", ">", "==");

    public Token(String s) {
        value = new String(s);
    }

    public boolean isInteger() {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    similar method aboce to check if its string and boolean values

    public boolean isBoolean() {
        try {
            Boolean.parseBoolean(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean IsString() {
        try {
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }


    public boolean isKeyword() {
        return isKeywordRead() || isKeywordPrint() || isKeywordIf()
                || isKeywordThen() || isKeywordElse() || isKeywordEndif()
                || isKeywordWhile() || isKeywordDo() || isKeywordEnd();
    }

    public boolean isStatementKeyword() {
        return isKeywordRead() || isKeywordPrint() || isKeywordIf()
                || isKeywordWhile() || isIdentifier();
    }

    public boolean isOperator() {
        return operators.contains(value);
    }

    public boolean isAssignment() {
        return value.equals(":=");
    }

    public boolean isLeftParenthesis() {
        return value.equals("(");
    }

    public boolean isRightParenthesis() {
        return value.equals(")");
    }

    public boolean isIdentifier() {
        boolean result = false;
        if (Character.isLetter(value.charAt(0))) {
            if (!isKeyword()) {
                result = true;
                for (int i = 1; i < value.length(); i++) {
                    result = Character.isLetterOrDigit(value.charAt(i))
                            || value.charAt(i) == '_';
                }
            }
        }
        return result;
    }

    public boolean isKeywordRead() {
        return value.equals("read");
    }

    public boolean isKeywordPrint() {
        return value.equals("print");
    }

    public boolean isKeywordIf() {
        return value.equals("if");
    }

    public boolean isKeywordThen() {
        return value.equals("then");
    }

    public boolean isKeywordElse() {
        return value.equals("else");
    }

    public boolean isKeywordEndif() {
        return value.equals("endif");
    }

    public boolean isKeywordWhile() {
        return value.equals("while");
    }

    public boolean isKeywordDo() {
        return value.equals("do");
    }

    public boolean isKeywordEnd() {
        return value.equals("end");
    }
}
