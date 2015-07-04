// $ANTLR 3.0.1 /Users/benjamincoe/HackWars/C.g 2009-01-01 16:21:20
package Hackscript;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class CLexer extends Lexer {
    public static final int COMMA=28;
    public static final int STRING_TYPE=54;
    public static final int MINUS=31;
    public static final int SIGN_EXPRESSION=15;
    public static final int DOUBLE=49;
    public static final int INCREMENT_ASSIGN=61;
    public static final int ELSEPART=8;
    public static final int PROCEDURE=12;
    public static final int BOOLEAN_LITERAL=50;
    public static final int THEN=9;
    public static final int INCREMENT=33;
    public static final int LBRACKET=51;
    public static final int MOD=38;
    public static final int INTEGER_TYPE=55;
    public static final int BECOMES=59;
    public static final int VOID_TYPE=57;
    public static final int OR=46;
    public static final int PROGRAM=16;
    public static final int NEWLINE=66;
    public static final int PROCEDURE_PARAMETERS=13;
    public static final int NOT_EQUALS=41;
    public static final int AND=45;
    public static final int DECLARATION=11;
    public static final int CAST_EXPRESSION=19;
    public static final int FUNCTION=6;
    public static final int GTE=39;
    public static final int STRING_LITERAL=47;
    public static final int RBRACKET=52;
    public static final int RPAREN=22;
    public static final int LPAREN=21;
    public static final int WHITE_SPACE=63;
    public static final int ML_COMMENT=65;
    public static final int PLUS=30;
    public static final int SBRIGHT=24;
    public static final int INTEGER_LITERAL=48;
    public static final int WHILE_STATEMENT=4;
    public static final int DECREMENT=34;
    public static final int TIMES=36;
    public static final int ARRAY_DECLARE=18;
    public static final int ARRAY_TYPE=58;
    public static final int DECREMENT_ASSIGN=62;
    public static final int POWER=35;
    public static final int WHILE=20;
    public static final int BOOLEAN_TYPE=56;
    public static final int LT=43;
    public static final int GT=42;
    public static final int COMMENT=64;
    public static final int MULTIPLY_ASSIGN=60;
    public static final int POWER_EXPRESSION=14;
    public static final int FLOAT_TYPE=53;
    public static final int FOR_STATEMENT=5;
    public static final int SBLEFT=23;
    public static final int EQUALS=40;
    public static final int LTE=44;
    public static final int ELSE=10;
    public static final int SEMICOLON=26;
    public static final int IF=29;
    public static final int EOF=-1;
    public static final int FOR=25;
    public static final int Tokens=67;
    public static final int ARRAY_IDENTIFIER=17;
    public static final int DIV=37;
    public static final int IFPART=7;
    public static final int IDENTIFIER=27;
    public static final int NOT=32;
    public CLexer() {;} 
    public CLexer(CharStream input) {
        super(input);
    }
    public String getGrammarFileName() { return "/Users/benjamincoe/HackWars/C.g"; }

    // $ANTLR start WHILE_STATEMENT
    public final void mWHILE_STATEMENT() throws RecognitionException {
        try {
            int _type = WHILE_STATEMENT;
            // /Users/benjamincoe/HackWars/C.g:7:17: ( 'while_statement' )
            // /Users/benjamincoe/HackWars/C.g:7:19: 'while_statement'
            {
            match("while_statement"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WHILE_STATEMENT

    // $ANTLR start FOR_STATEMENT
    public final void mFOR_STATEMENT() throws RecognitionException {
        try {
            int _type = FOR_STATEMENT;
            // /Users/benjamincoe/HackWars/C.g:8:15: ( 'for_statement' )
            // /Users/benjamincoe/HackWars/C.g:8:17: 'for_statement'
            {
            match("for_statement"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FOR_STATEMENT

    // $ANTLR start FUNCTION
    public final void mFUNCTION() throws RecognitionException {
        try {
            int _type = FUNCTION;
            // /Users/benjamincoe/HackWars/C.g:9:10: ( '1212function1212' )
            // /Users/benjamincoe/HackWars/C.g:9:12: '1212function1212'
            {
            match("1212function1212"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FUNCTION

    // $ANTLR start IFPART
    public final void mIFPART() throws RecognitionException {
        try {
            int _type = IFPART;
            // /Users/benjamincoe/HackWars/C.g:10:8: ( 'ifpart' )
            // /Users/benjamincoe/HackWars/C.g:10:10: 'ifpart'
            {
            match("ifpart"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IFPART

    // $ANTLR start ELSEPART
    public final void mELSEPART() throws RecognitionException {
        try {
            int _type = ELSEPART;
            // /Users/benjamincoe/HackWars/C.g:11:10: ( 'elsepart' )
            // /Users/benjamincoe/HackWars/C.g:11:12: 'elsepart'
            {
            match("elsepart"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ELSEPART

    // $ANTLR start THEN
    public final void mTHEN() throws RecognitionException {
        try {
            int _type = THEN;
            // /Users/benjamincoe/HackWars/C.g:12:6: ( 'then' )
            // /Users/benjamincoe/HackWars/C.g:12:8: 'then'
            {
            match("then"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end THEN

    // $ANTLR start DECLARATION
    public final void mDECLARATION() throws RecognitionException {
        try {
            int _type = DECLARATION;
            // /Users/benjamincoe/HackWars/C.g:13:13: ( 'declaration' )
            // /Users/benjamincoe/HackWars/C.g:13:15: 'declaration'
            {
            match("declaration"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DECLARATION

    // $ANTLR start PROCEDURE
    public final void mPROCEDURE() throws RecognitionException {
        try {
            int _type = PROCEDURE;
            // /Users/benjamincoe/HackWars/C.g:14:11: ( 'procedure' )
            // /Users/benjamincoe/HackWars/C.g:14:13: 'procedure'
            {
            match("procedure"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PROCEDURE

    // $ANTLR start PROCEDURE_PARAMETERS
    public final void mPROCEDURE_PARAMETERS() throws RecognitionException {
        try {
            int _type = PROCEDURE_PARAMETERS;
            // /Users/benjamincoe/HackWars/C.g:15:22: ( '1212param1212' )
            // /Users/benjamincoe/HackWars/C.g:15:24: '1212param1212'
            {
            match("1212param1212"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PROCEDURE_PARAMETERS

    // $ANTLR start POWER_EXPRESSION
    public final void mPOWER_EXPRESSION() throws RecognitionException {
        try {
            int _type = POWER_EXPRESSION;
            // /Users/benjamincoe/HackWars/C.g:16:18: ( 'power_expression' )
            // /Users/benjamincoe/HackWars/C.g:16:20: 'power_expression'
            {
            match("power_expression"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end POWER_EXPRESSION

    // $ANTLR start SIGN_EXPRESSION
    public final void mSIGN_EXPRESSION() throws RecognitionException {
        try {
            int _type = SIGN_EXPRESSION;
            // /Users/benjamincoe/HackWars/C.g:17:17: ( 'sign_expression' )
            // /Users/benjamincoe/HackWars/C.g:17:19: 'sign_expression'
            {
            match("sign_expression"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SIGN_EXPRESSION

    // $ANTLR start PROGRAM
    public final void mPROGRAM() throws RecognitionException {
        try {
            int _type = PROGRAM;
            // /Users/benjamincoe/HackWars/C.g:18:9: ( '1212program1212' )
            // /Users/benjamincoe/HackWars/C.g:18:11: '1212program1212'
            {
            match("1212program1212"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PROGRAM

    // $ANTLR start ARRAY_IDENTIFIER
    public final void mARRAY_IDENTIFIER() throws RecognitionException {
        try {
            int _type = ARRAY_IDENTIFIER;
            // /Users/benjamincoe/HackWars/C.g:19:18: ( 'array_identifier' )
            // /Users/benjamincoe/HackWars/C.g:19:20: 'array_identifier'
            {
            match("array_identifier"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARRAY_IDENTIFIER

    // $ANTLR start ARRAY_DECLARE
    public final void mARRAY_DECLARE() throws RecognitionException {
        try {
            int _type = ARRAY_DECLARE;
            // /Users/benjamincoe/HackWars/C.g:20:15: ( 'array_declare' )
            // /Users/benjamincoe/HackWars/C.g:20:17: 'array_declare'
            {
            match("array_declare"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARRAY_DECLARE

    // $ANTLR start CAST_EXPRESSION
    public final void mCAST_EXPRESSION() throws RecognitionException {
        try {
            int _type = CAST_EXPRESSION;
            // /Users/benjamincoe/HackWars/C.g:21:17: ( '1212cast1212' )
            // /Users/benjamincoe/HackWars/C.g:21:19: '1212cast1212'
            {
            match("1212cast1212"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end CAST_EXPRESSION

    // $ANTLR start WHITE_SPACE
    public final void mWHITE_SPACE() throws RecognitionException {
        try {
            int _type = WHITE_SPACE;
            // /Users/benjamincoe/HackWars/C.g:152:3: ( ( ' ' | '\\t' | '\\f' ) )
            // /Users/benjamincoe/HackWars/C.g:152:5: ( ' ' | '\\t' | '\\f' )
            {
            if ( input.LA(1)=='\t'||input.LA(1)=='\f'||input.LA(1)==' ' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse =
                    new MismatchedSetException(null,input);
                recover(mse);    throw mse;
            }

             channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WHITE_SPACE

    // $ANTLR start COMMENT
    public final void mCOMMENT() throws RecognitionException {
        try {
            int _type = COMMENT;
            // /Users/benjamincoe/HackWars/C.g:158:9: ( '//' (~ ( '\\r\\n' | '\\r' | '\\n' ) )* )
            // /Users/benjamincoe/HackWars/C.g:158:11: '//' (~ ( '\\r\\n' | '\\r' | '\\n' ) )*
            {
            match("//"); 

            // /Users/benjamincoe/HackWars/C.g:158:16: (~ ( '\\r\\n' | '\\r' | '\\n' ) )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0>='\u0000' && LA1_0<='\t')||(LA1_0>='\u000B' && LA1_0<='\f')||(LA1_0>='\u000E' && LA1_0<='\uFFFE')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // /Users/benjamincoe/HackWars/C.g:158:16: ~ ( '\\r\\n' | '\\r' | '\\n' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMENT

    // $ANTLR start ML_COMMENT
    public final void mML_COMMENT() throws RecognitionException {
        try {
            int _type = ML_COMMENT;
            // /Users/benjamincoe/HackWars/C.g:165:5: ( '/*' ( options {greedy=false; } : . )* '*/' )
            // /Users/benjamincoe/HackWars/C.g:165:9: '/*' ( options {greedy=false; } : . )* '*/'
            {
            match("/*"); 

            // /Users/benjamincoe/HackWars/C.g:165:14: ( options {greedy=false; } : . )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( (LA2_0=='*') ) {
                    int LA2_1 = input.LA(2);

                    if ( (LA2_1=='/') ) {
                        alt2=2;
                    }
                    else if ( ((LA2_1>='\u0000' && LA2_1<='.')||(LA2_1>='0' && LA2_1<='\uFFFE')) ) {
                        alt2=1;
                    }


                }
                else if ( ((LA2_0>='\u0000' && LA2_0<=')')||(LA2_0>='+' && LA2_0<='\uFFFE')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // /Users/benjamincoe/HackWars/C.g:165:41: .
            	    {
            	    matchAny(); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);

            match("*/"); 

            channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ML_COMMENT

    // $ANTLR start NEWLINE
    public final void mNEWLINE() throws RecognitionException {
        try {
            int _type = NEWLINE;
            // /Users/benjamincoe/HackWars/C.g:168:9: ( ( '\\r\\n' | '\\r' | '\\n' ) )
            // /Users/benjamincoe/HackWars/C.g:169:5: ( '\\r\\n' | '\\r' | '\\n' )
            {
            // /Users/benjamincoe/HackWars/C.g:169:5: ( '\\r\\n' | '\\r' | '\\n' )
            int alt3=3;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='\r') ) {
                int LA3_1 = input.LA(2);

                if ( (LA3_1=='\n') ) {
                    alt3=1;
                }
                else {
                    alt3=2;}
            }
            else if ( (LA3_0=='\n') ) {
                alt3=3;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("169:5: ( '\\r\\n' | '\\r' | '\\n' )", 3, 0, input);

                throw nvae;
            }
            switch (alt3) {
                case 1 :
                    // /Users/benjamincoe/HackWars/C.g:169:6: '\\r\\n'
                    {
                    match("\r\n"); 


                    }
                    break;
                case 2 :
                    // /Users/benjamincoe/HackWars/C.g:170:9: '\\r'
                    {
                    match('\r'); 

                    }
                    break;
                case 3 :
                    // /Users/benjamincoe/HackWars/C.g:171:9: '\\n'
                    {
                    match('\n'); 

                    }
                    break;

            }

            //input.setLine(input.getLine()+1); 
                  	channel=HIDDEN;

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NEWLINE

    // $ANTLR start IF
    public final void mIF() throws RecognitionException {
        try {
            int _type = IF;
            // /Users/benjamincoe/HackWars/C.g:177:4: ( 'if' )
            // /Users/benjamincoe/HackWars/C.g:177:6: 'if'
            {
            match("if"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IF

    // $ANTLR start ELSE
    public final void mELSE() throws RecognitionException {
        try {
            int _type = ELSE;
            // /Users/benjamincoe/HackWars/C.g:178:6: ( 'else' )
            // /Users/benjamincoe/HackWars/C.g:178:8: 'else'
            {
            match("else"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ELSE

    // $ANTLR start WHILE
    public final void mWHILE() throws RecognitionException {
        try {
            int _type = WHILE;
            // /Users/benjamincoe/HackWars/C.g:179:7: ( 'while' )
            // /Users/benjamincoe/HackWars/C.g:179:9: 'while'
            {
            match("while"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end WHILE

    // $ANTLR start FOR
    public final void mFOR() throws RecognitionException {
        try {
            int _type = FOR;
            // /Users/benjamincoe/HackWars/C.g:180:5: ( 'for' )
            // /Users/benjamincoe/HackWars/C.g:180:7: 'for'
            {
            match("for"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FOR

    // $ANTLR start FLOAT_TYPE
    public final void mFLOAT_TYPE() throws RecognitionException {
        try {
            int _type = FLOAT_TYPE;
            // /Users/benjamincoe/HackWars/C.g:183:2: ( 'float' )
            // /Users/benjamincoe/HackWars/C.g:183:4: 'float'
            {
            match("float"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end FLOAT_TYPE

    // $ANTLR start STRING_TYPE
    public final void mSTRING_TYPE() throws RecognitionException {
        try {
            int _type = STRING_TYPE;
            // /Users/benjamincoe/HackWars/C.g:185:2: ( 'string' | 'String' )
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0=='s') ) {
                alt4=1;
            }
            else if ( (LA4_0=='S') ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("184:1: STRING_TYPE : ( 'string' | 'String' );", 4, 0, input);

                throw nvae;
            }
            switch (alt4) {
                case 1 :
                    // /Users/benjamincoe/HackWars/C.g:185:4: 'string'
                    {
                    match("string"); 


                    }
                    break;
                case 2 :
                    // /Users/benjamincoe/HackWars/C.g:185:13: 'String'
                    {
                    match("String"); 


                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING_TYPE

    // $ANTLR start INTEGER_TYPE
    public final void mINTEGER_TYPE() throws RecognitionException {
        try {
            int _type = INTEGER_TYPE;
            // /Users/benjamincoe/HackWars/C.g:187:2: ( 'int' )
            // /Users/benjamincoe/HackWars/C.g:187:4: 'int'
            {
            match("int"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INTEGER_TYPE

    // $ANTLR start BOOLEAN_TYPE
    public final void mBOOLEAN_TYPE() throws RecognitionException {
        try {
            int _type = BOOLEAN_TYPE;
            // /Users/benjamincoe/HackWars/C.g:189:2: ( 'bool' | 'boolean' )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0=='b') ) {
                int LA5_1 = input.LA(2);

                if ( (LA5_1=='o') ) {
                    int LA5_2 = input.LA(3);

                    if ( (LA5_2=='o') ) {
                        int LA5_3 = input.LA(4);

                        if ( (LA5_3=='l') ) {
                            int LA5_4 = input.LA(5);

                            if ( (LA5_4=='e') ) {
                                alt5=2;
                            }
                            else {
                                alt5=1;}
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("188:1: BOOLEAN_TYPE : ( 'bool' | 'boolean' );", 5, 3, input);

                            throw nvae;
                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("188:1: BOOLEAN_TYPE : ( 'bool' | 'boolean' );", 5, 2, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("188:1: BOOLEAN_TYPE : ( 'bool' | 'boolean' );", 5, 1, input);

                    throw nvae;
                }
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("188:1: BOOLEAN_TYPE : ( 'bool' | 'boolean' );", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // /Users/benjamincoe/HackWars/C.g:189:4: 'bool'
                    {
                    match("bool"); 


                    }
                    break;
                case 2 :
                    // /Users/benjamincoe/HackWars/C.g:189:11: 'boolean'
                    {
                    match("boolean"); 


                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BOOLEAN_TYPE

    // $ANTLR start VOID_TYPE
    public final void mVOID_TYPE() throws RecognitionException {
        try {
            int _type = VOID_TYPE;
            // /Users/benjamincoe/HackWars/C.g:191:2: ( 'void' )
            // /Users/benjamincoe/HackWars/C.g:191:4: 'void'
            {
            match("void"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end VOID_TYPE

    // $ANTLR start ARRAY_TYPE
    public final void mARRAY_TYPE() throws RecognitionException {
        try {
            int _type = ARRAY_TYPE;
            // /Users/benjamincoe/HackWars/C.g:193:2: ( 'array' )
            // /Users/benjamincoe/HackWars/C.g:193:4: 'array'
            {
            match("array"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end ARRAY_TYPE

    // $ANTLR start COMMA
    public final void mCOMMA() throws RecognitionException {
        try {
            int _type = COMMA;
            // /Users/benjamincoe/HackWars/C.g:199:12: ( ',' )
            // /Users/benjamincoe/HackWars/C.g:199:14: ','
            {
            match(','); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end COMMA

    // $ANTLR start LBRACKET
    public final void mLBRACKET() throws RecognitionException {
        try {
            int _type = LBRACKET;
            // /Users/benjamincoe/HackWars/C.g:200:12: ( '[' )
            // /Users/benjamincoe/HackWars/C.g:200:14: '['
            {
            match('['); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LBRACKET

    // $ANTLR start RBRACKET
    public final void mRBRACKET() throws RecognitionException {
        try {
            int _type = RBRACKET;
            // /Users/benjamincoe/HackWars/C.g:201:12: ( ']' )
            // /Users/benjamincoe/HackWars/C.g:201:14: ']'
            {
            match(']'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RBRACKET

    // $ANTLR start LPAREN
    public final void mLPAREN() throws RecognitionException {
        try {
            int _type = LPAREN;
            // /Users/benjamincoe/HackWars/C.g:202:12: ( '(' )
            // /Users/benjamincoe/HackWars/C.g:202:14: '('
            {
            match('('); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LPAREN

    // $ANTLR start RPAREN
    public final void mRPAREN() throws RecognitionException {
        try {
            int _type = RPAREN;
            // /Users/benjamincoe/HackWars/C.g:203:12: ( ')' )
            // /Users/benjamincoe/HackWars/C.g:203:14: ')'
            {
            match(')'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end RPAREN

    // $ANTLR start NOT_EQUALS
    public final void mNOT_EQUALS() throws RecognitionException {
        try {
            int _type = NOT_EQUALS;
            // /Users/benjamincoe/HackWars/C.g:204:12: ( '!=' )
            // /Users/benjamincoe/HackWars/C.g:204:14: '!='
            {
            match("!="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NOT_EQUALS

    // $ANTLR start LTE
    public final void mLTE() throws RecognitionException {
        try {
            int _type = LTE;
            // /Users/benjamincoe/HackWars/C.g:205:12: ( '<=' )
            // /Users/benjamincoe/HackWars/C.g:205:14: '<='
            {
            match("<="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LTE

    // $ANTLR start LT
    public final void mLT() throws RecognitionException {
        try {
            int _type = LT;
            // /Users/benjamincoe/HackWars/C.g:206:12: ( '<' )
            // /Users/benjamincoe/HackWars/C.g:206:14: '<'
            {
            match('<'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end LT

    // $ANTLR start GTE
    public final void mGTE() throws RecognitionException {
        try {
            int _type = GTE;
            // /Users/benjamincoe/HackWars/C.g:207:12: ( '>=' )
            // /Users/benjamincoe/HackWars/C.g:207:14: '>='
            {
            match(">="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GTE

    // $ANTLR start GT
    public final void mGT() throws RecognitionException {
        try {
            int _type = GT;
            // /Users/benjamincoe/HackWars/C.g:208:12: ( '>' )
            // /Users/benjamincoe/HackWars/C.g:208:14: '>'
            {
            match('>'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end GT

    // $ANTLR start PLUS
    public final void mPLUS() throws RecognitionException {
        try {
            int _type = PLUS;
            // /Users/benjamincoe/HackWars/C.g:209:12: ( '+' )
            // /Users/benjamincoe/HackWars/C.g:209:14: '+'
            {
            match('+'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end PLUS

    // $ANTLR start MINUS
    public final void mMINUS() throws RecognitionException {
        try {
            int _type = MINUS;
            // /Users/benjamincoe/HackWars/C.g:210:12: ( '-' )
            // /Users/benjamincoe/HackWars/C.g:210:14: '-'
            {
            match('-'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MINUS

    // $ANTLR start TIMES
    public final void mTIMES() throws RecognitionException {
        try {
            int _type = TIMES;
            // /Users/benjamincoe/HackWars/C.g:211:12: ( '*' )
            // /Users/benjamincoe/HackWars/C.g:211:14: '*'
            {
            match('*'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end TIMES

    // $ANTLR start DIV
    public final void mDIV() throws RecognitionException {
        try {
            int _type = DIV;
            // /Users/benjamincoe/HackWars/C.g:212:12: ( '/' )
            // /Users/benjamincoe/HackWars/C.g:212:14: '/'
            {
            match('/'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DIV

    // $ANTLR start POWER
    public final void mPOWER() throws RecognitionException {
        try {
            int _type = POWER;
            // /Users/benjamincoe/HackWars/C.g:213:7: ( '^' )
            // /Users/benjamincoe/HackWars/C.g:213:9: '^'
            {
            match('^'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end POWER

    // $ANTLR start MOD
    public final void mMOD() throws RecognitionException {
        try {
            int _type = MOD;
            // /Users/benjamincoe/HackWars/C.g:214:5: ( '%' )
            // /Users/benjamincoe/HackWars/C.g:214:7: '%'
            {
            match('%'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MOD

    // $ANTLR start EQUALS
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            // /Users/benjamincoe/HackWars/C.g:215:12: ( '==' )
            // /Users/benjamincoe/HackWars/C.g:215:14: '=='
            {
            match("=="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end EQUALS

    // $ANTLR start BECOMES
    public final void mBECOMES() throws RecognitionException {
        try {
            int _type = BECOMES;
            // /Users/benjamincoe/HackWars/C.g:216:12: ( '=' )
            // /Users/benjamincoe/HackWars/C.g:216:14: '='
            {
            match('='); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BECOMES

    // $ANTLR start AND
    public final void mAND() throws RecognitionException {
        try {
            int _type = AND;
            // /Users/benjamincoe/HackWars/C.g:217:5: ( '&&' )
            // /Users/benjamincoe/HackWars/C.g:217:7: '&&'
            {
            match("&&"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end AND

    // $ANTLR start OR
    public final void mOR() throws RecognitionException {
        try {
            int _type = OR;
            // /Users/benjamincoe/HackWars/C.g:218:4: ( '||' )
            // /Users/benjamincoe/HackWars/C.g:218:6: '||'
            {
            match("||"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end OR

    // $ANTLR start SEMICOLON
    public final void mSEMICOLON() throws RecognitionException {
        try {
            int _type = SEMICOLON;
            // /Users/benjamincoe/HackWars/C.g:220:2: ( ';' )
            // /Users/benjamincoe/HackWars/C.g:220:4: ';'
            {
            match(';'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SEMICOLON

    // $ANTLR start SBLEFT
    public final void mSBLEFT() throws RecognitionException {
        try {
            int _type = SBLEFT;
            // /Users/benjamincoe/HackWars/C.g:221:8: ( '{' )
            // /Users/benjamincoe/HackWars/C.g:221:10: '{'
            {
            match('{'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SBLEFT

    // $ANTLR start SBRIGHT
    public final void mSBRIGHT() throws RecognitionException {
        try {
            int _type = SBRIGHT;
            // /Users/benjamincoe/HackWars/C.g:222:9: ( '}' )
            // /Users/benjamincoe/HackWars/C.g:222:11: '}'
            {
            match('}'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end SBRIGHT

    // $ANTLR start INCREMENT
    public final void mINCREMENT() throws RecognitionException {
        try {
            int _type = INCREMENT;
            // /Users/benjamincoe/HackWars/C.g:224:2: ( '++' )
            // /Users/benjamincoe/HackWars/C.g:224:4: '++'
            {
            match("++"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INCREMENT

    // $ANTLR start DECREMENT
    public final void mDECREMENT() throws RecognitionException {
        try {
            int _type = DECREMENT;
            // /Users/benjamincoe/HackWars/C.g:226:2: ( '--' )
            // /Users/benjamincoe/HackWars/C.g:226:4: '--'
            {
            match("--"); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DECREMENT

    // $ANTLR start INCREMENT_ASSIGN
    public final void mINCREMENT_ASSIGN() throws RecognitionException {
        try {
            int _type = INCREMENT_ASSIGN;
            // /Users/benjamincoe/HackWars/C.g:228:2: ( '+=' )
            // /Users/benjamincoe/HackWars/C.g:228:4: '+='
            {
            match("+="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INCREMENT_ASSIGN

    // $ANTLR start DECREMENT_ASSIGN
    public final void mDECREMENT_ASSIGN() throws RecognitionException {
        try {
            int _type = DECREMENT_ASSIGN;
            // /Users/benjamincoe/HackWars/C.g:230:2: ( '-=' )
            // /Users/benjamincoe/HackWars/C.g:230:4: '-='
            {
            match("-="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DECREMENT_ASSIGN

    // $ANTLR start MULTIPLY_ASSIGN
    public final void mMULTIPLY_ASSIGN() throws RecognitionException {
        try {
            int _type = MULTIPLY_ASSIGN;
            // /Users/benjamincoe/HackWars/C.g:232:2: ( '*=' )
            // /Users/benjamincoe/HackWars/C.g:232:4: '*='
            {
            match("*="); 


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end MULTIPLY_ASSIGN

    // $ANTLR start NOT
    public final void mNOT() throws RecognitionException {
        try {
            int _type = NOT;
            // /Users/benjamincoe/HackWars/C.g:233:5: ( '!' )
            // /Users/benjamincoe/HackWars/C.g:233:7: '!'
            {
            match('!'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end NOT

    // $ANTLR start BOOLEAN_LITERAL
    public final void mBOOLEAN_LITERAL() throws RecognitionException {
        try {
            int _type = BOOLEAN_LITERAL;
            // /Users/benjamincoe/HackWars/C.g:239:2: ( 'true' | 'false' )
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0=='t') ) {
                alt6=1;
            }
            else if ( (LA6_0=='f') ) {
                alt6=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("238:1: BOOLEAN_LITERAL : ( 'true' | 'false' );", 6, 0, input);

                throw nvae;
            }
            switch (alt6) {
                case 1 :
                    // /Users/benjamincoe/HackWars/C.g:239:4: 'true'
                    {
                    match("true"); 


                    }
                    break;
                case 2 :
                    // /Users/benjamincoe/HackWars/C.g:239:11: 'false'
                    {
                    match("false"); 


                    }
                    break;

            }
            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end BOOLEAN_LITERAL

    // $ANTLR start INTEGER_LITERAL
    public final void mINTEGER_LITERAL() throws RecognitionException {
        try {
            int _type = INTEGER_LITERAL;
            // /Users/benjamincoe/HackWars/C.g:242:2: ( ( '0' .. '9' )+ )
            // /Users/benjamincoe/HackWars/C.g:242:4: ( '0' .. '9' )+
            {
            // /Users/benjamincoe/HackWars/C.g:242:4: ( '0' .. '9' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>='0' && LA7_0<='9')) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // /Users/benjamincoe/HackWars/C.g:242:5: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end INTEGER_LITERAL

    // $ANTLR start IDENTIFIER
    public final void mIDENTIFIER() throws RecognitionException {
        try {
            int _type = IDENTIFIER;
            // /Users/benjamincoe/HackWars/C.g:244:2: ( ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) ) ( '_' | INTEGER_LITERAL | ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) ) )* )
            // /Users/benjamincoe/HackWars/C.g:244:4: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) ) ( '_' | INTEGER_LITERAL | ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) ) )*
            {
            // /Users/benjamincoe/HackWars/C.g:244:4: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( ((LA8_0>='a' && LA8_0<='z')) ) {
                alt8=1;
            }
            else if ( ((LA8_0>='A' && LA8_0<='Z')) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("244:4: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) )", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // /Users/benjamincoe/HackWars/C.g:244:5: ( 'a' .. 'z' )
                    {
                    // /Users/benjamincoe/HackWars/C.g:244:5: ( 'a' .. 'z' )
                    // /Users/benjamincoe/HackWars/C.g:244:6: 'a' .. 'z'
                    {
                    matchRange('a','z'); 

                    }


                    }
                    break;
                case 2 :
                    // /Users/benjamincoe/HackWars/C.g:244:16: ( 'A' .. 'Z' )
                    {
                    // /Users/benjamincoe/HackWars/C.g:244:16: ( 'A' .. 'Z' )
                    // /Users/benjamincoe/HackWars/C.g:244:17: 'A' .. 'Z'
                    {
                    matchRange('A','Z'); 

                    }


                    }
                    break;

            }

            // /Users/benjamincoe/HackWars/C.g:244:27: ( '_' | INTEGER_LITERAL | ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) ) )*
            loop10:
            do {
                int alt10=4;
                switch ( input.LA(1) ) {
                case '_':
                    {
                    alt10=1;
                    }
                    break;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    {
                    alt10=2;
                    }
                    break;
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'S':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                case 'Z':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z':
                    {
                    alt10=3;
                    }
                    break;

                }

                switch (alt10) {
            	case 1 :
            	    // /Users/benjamincoe/HackWars/C.g:244:28: '_'
            	    {
            	    match('_'); 

            	    }
            	    break;
            	case 2 :
            	    // /Users/benjamincoe/HackWars/C.g:244:32: INTEGER_LITERAL
            	    {
            	    mINTEGER_LITERAL(); 

            	    }
            	    break;
            	case 3 :
            	    // /Users/benjamincoe/HackWars/C.g:244:48: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) )
            	    {
            	    // /Users/benjamincoe/HackWars/C.g:244:48: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) )
            	    int alt9=2;
            	    int LA9_0 = input.LA(1);

            	    if ( ((LA9_0>='a' && LA9_0<='z')) ) {
            	        alt9=1;
            	    }
            	    else if ( ((LA9_0>='A' && LA9_0<='Z')) ) {
            	        alt9=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("244:48: ( ( 'a' .. 'z' ) | ( 'A' .. 'Z' ) )", 9, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt9) {
            	        case 1 :
            	            // /Users/benjamincoe/HackWars/C.g:244:49: ( 'a' .. 'z' )
            	            {
            	            // /Users/benjamincoe/HackWars/C.g:244:49: ( 'a' .. 'z' )
            	            // /Users/benjamincoe/HackWars/C.g:244:50: 'a' .. 'z'
            	            {
            	            matchRange('a','z'); 

            	            }


            	            }
            	            break;
            	        case 2 :
            	            // /Users/benjamincoe/HackWars/C.g:244:60: ( 'A' .. 'Z' )
            	            {
            	            // /Users/benjamincoe/HackWars/C.g:244:60: ( 'A' .. 'Z' )
            	            // /Users/benjamincoe/HackWars/C.g:244:61: 'A' .. 'Z'
            	            {
            	            matchRange('A','Z'); 

            	            }


            	            }
            	            break;

            	    }


            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);


            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end IDENTIFIER

    // $ANTLR start STRING_LITERAL
    public final void mSTRING_LITERAL() throws RecognitionException {
        try {
            int _type = STRING_LITERAL;
            // /Users/benjamincoe/HackWars/C.g:248:2: ( '\"' (~ ( '\"' | '\\n' | '\\r' ) )* '\"' )
            // /Users/benjamincoe/HackWars/C.g:248:4: '\"' (~ ( '\"' | '\\n' | '\\r' ) )* '\"'
            {
            match('\"'); 
            // /Users/benjamincoe/HackWars/C.g:248:9: (~ ( '\"' | '\\n' | '\\r' ) )*
            loop11:
            do {
                int alt11=2;
                int LA11_0 = input.LA(1);

                if ( ((LA11_0>='\u0000' && LA11_0<='\t')||(LA11_0>='\u000B' && LA11_0<='\f')||(LA11_0>='\u000E' && LA11_0<='!')||(LA11_0>='#' && LA11_0<='\uFFFE')) ) {
                    alt11=1;
                }


                switch (alt11) {
            	case 1 :
            	    // /Users/benjamincoe/HackWars/C.g:248:10: ~ ( '\"' | '\\n' | '\\r' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\t')||(input.LA(1)>='\u000B' && input.LA(1)<='\f')||(input.LA(1)>='\u000E' && input.LA(1)<='!')||(input.LA(1)>='#' && input.LA(1)<='\uFFFE') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse =
            	            new MismatchedSetException(null,input);
            	        recover(mse);    throw mse;
            	    }


            	    }
            	    break;

            	default :
            	    break loop11;
                }
            } while (true);

            match('\"'); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end STRING_LITERAL

    // $ANTLR start DOUBLE
    public final void mDOUBLE() throws RecognitionException {
        try {
            int _type = DOUBLE;
            // /Users/benjamincoe/HackWars/C.g:249:8: ( INTEGER_LITERAL '.' INTEGER_LITERAL )
            // /Users/benjamincoe/HackWars/C.g:249:10: INTEGER_LITERAL '.' INTEGER_LITERAL
            {
            mINTEGER_LITERAL(); 
            match('.'); 
            mINTEGER_LITERAL(); 

            }

            this.type = _type;
        }
        finally {
        }
    }
    // $ANTLR end DOUBLE

    public void mTokens() throws RecognitionException {
        // /Users/benjamincoe/HackWars/C.g:1:8: ( WHILE_STATEMENT | FOR_STATEMENT | FUNCTION | IFPART | ELSEPART | THEN | DECLARATION | PROCEDURE | PROCEDURE_PARAMETERS | POWER_EXPRESSION | SIGN_EXPRESSION | PROGRAM | ARRAY_IDENTIFIER | ARRAY_DECLARE | CAST_EXPRESSION | WHITE_SPACE | COMMENT | ML_COMMENT | NEWLINE | IF | ELSE | WHILE | FOR | FLOAT_TYPE | STRING_TYPE | INTEGER_TYPE | BOOLEAN_TYPE | VOID_TYPE | ARRAY_TYPE | COMMA | LBRACKET | RBRACKET | LPAREN | RPAREN | NOT_EQUALS | LTE | LT | GTE | GT | PLUS | MINUS | TIMES | DIV | POWER | MOD | EQUALS | BECOMES | AND | OR | SEMICOLON | SBLEFT | SBRIGHT | INCREMENT | DECREMENT | INCREMENT_ASSIGN | DECREMENT_ASSIGN | MULTIPLY_ASSIGN | NOT | BOOLEAN_LITERAL | INTEGER_LITERAL | IDENTIFIER | STRING_LITERAL | DOUBLE )
        int alt12=63;
        alt12 = dfa12.predict(input);
        switch (alt12) {
            case 1 :
                // /Users/benjamincoe/HackWars/C.g:1:10: WHILE_STATEMENT
                {
                mWHILE_STATEMENT(); 

                }
                break;
            case 2 :
                // /Users/benjamincoe/HackWars/C.g:1:26: FOR_STATEMENT
                {
                mFOR_STATEMENT(); 

                }
                break;
            case 3 :
                // /Users/benjamincoe/HackWars/C.g:1:40: FUNCTION
                {
                mFUNCTION(); 

                }
                break;
            case 4 :
                // /Users/benjamincoe/HackWars/C.g:1:49: IFPART
                {
                mIFPART(); 

                }
                break;
            case 5 :
                // /Users/benjamincoe/HackWars/C.g:1:56: ELSEPART
                {
                mELSEPART(); 

                }
                break;
            case 6 :
                // /Users/benjamincoe/HackWars/C.g:1:65: THEN
                {
                mTHEN(); 

                }
                break;
            case 7 :
                // /Users/benjamincoe/HackWars/C.g:1:70: DECLARATION
                {
                mDECLARATION(); 

                }
                break;
            case 8 :
                // /Users/benjamincoe/HackWars/C.g:1:82: PROCEDURE
                {
                mPROCEDURE(); 

                }
                break;
            case 9 :
                // /Users/benjamincoe/HackWars/C.g:1:92: PROCEDURE_PARAMETERS
                {
                mPROCEDURE_PARAMETERS(); 

                }
                break;
            case 10 :
                // /Users/benjamincoe/HackWars/C.g:1:113: POWER_EXPRESSION
                {
                mPOWER_EXPRESSION(); 

                }
                break;
            case 11 :
                // /Users/benjamincoe/HackWars/C.g:1:130: SIGN_EXPRESSION
                {
                mSIGN_EXPRESSION(); 

                }
                break;
            case 12 :
                // /Users/benjamincoe/HackWars/C.g:1:146: PROGRAM
                {
                mPROGRAM(); 

                }
                break;
            case 13 :
                // /Users/benjamincoe/HackWars/C.g:1:154: ARRAY_IDENTIFIER
                {
                mARRAY_IDENTIFIER(); 

                }
                break;
            case 14 :
                // /Users/benjamincoe/HackWars/C.g:1:171: ARRAY_DECLARE
                {
                mARRAY_DECLARE(); 

                }
                break;
            case 15 :
                // /Users/benjamincoe/HackWars/C.g:1:185: CAST_EXPRESSION
                {
                mCAST_EXPRESSION(); 

                }
                break;
            case 16 :
                // /Users/benjamincoe/HackWars/C.g:1:201: WHITE_SPACE
                {
                mWHITE_SPACE(); 

                }
                break;
            case 17 :
                // /Users/benjamincoe/HackWars/C.g:1:213: COMMENT
                {
                mCOMMENT(); 

                }
                break;
            case 18 :
                // /Users/benjamincoe/HackWars/C.g:1:221: ML_COMMENT
                {
                mML_COMMENT(); 

                }
                break;
            case 19 :
                // /Users/benjamincoe/HackWars/C.g:1:232: NEWLINE
                {
                mNEWLINE(); 

                }
                break;
            case 20 :
                // /Users/benjamincoe/HackWars/C.g:1:240: IF
                {
                mIF(); 

                }
                break;
            case 21 :
                // /Users/benjamincoe/HackWars/C.g:1:243: ELSE
                {
                mELSE(); 

                }
                break;
            case 22 :
                // /Users/benjamincoe/HackWars/C.g:1:248: WHILE
                {
                mWHILE(); 

                }
                break;
            case 23 :
                // /Users/benjamincoe/HackWars/C.g:1:254: FOR
                {
                mFOR(); 

                }
                break;
            case 24 :
                // /Users/benjamincoe/HackWars/C.g:1:258: FLOAT_TYPE
                {
                mFLOAT_TYPE(); 

                }
                break;
            case 25 :
                // /Users/benjamincoe/HackWars/C.g:1:269: STRING_TYPE
                {
                mSTRING_TYPE(); 

                }
                break;
            case 26 :
                // /Users/benjamincoe/HackWars/C.g:1:281: INTEGER_TYPE
                {
                mINTEGER_TYPE(); 

                }
                break;
            case 27 :
                // /Users/benjamincoe/HackWars/C.g:1:294: BOOLEAN_TYPE
                {
                mBOOLEAN_TYPE(); 

                }
                break;
            case 28 :
                // /Users/benjamincoe/HackWars/C.g:1:307: VOID_TYPE
                {
                mVOID_TYPE(); 

                }
                break;
            case 29 :
                // /Users/benjamincoe/HackWars/C.g:1:317: ARRAY_TYPE
                {
                mARRAY_TYPE(); 

                }
                break;
            case 30 :
                // /Users/benjamincoe/HackWars/C.g:1:328: COMMA
                {
                mCOMMA(); 

                }
                break;
            case 31 :
                // /Users/benjamincoe/HackWars/C.g:1:334: LBRACKET
                {
                mLBRACKET(); 

                }
                break;
            case 32 :
                // /Users/benjamincoe/HackWars/C.g:1:343: RBRACKET
                {
                mRBRACKET(); 

                }
                break;
            case 33 :
                // /Users/benjamincoe/HackWars/C.g:1:352: LPAREN
                {
                mLPAREN(); 

                }
                break;
            case 34 :
                // /Users/benjamincoe/HackWars/C.g:1:359: RPAREN
                {
                mRPAREN(); 

                }
                break;
            case 35 :
                // /Users/benjamincoe/HackWars/C.g:1:366: NOT_EQUALS
                {
                mNOT_EQUALS(); 

                }
                break;
            case 36 :
                // /Users/benjamincoe/HackWars/C.g:1:377: LTE
                {
                mLTE(); 

                }
                break;
            case 37 :
                // /Users/benjamincoe/HackWars/C.g:1:381: LT
                {
                mLT(); 

                }
                break;
            case 38 :
                // /Users/benjamincoe/HackWars/C.g:1:384: GTE
                {
                mGTE(); 

                }
                break;
            case 39 :
                // /Users/benjamincoe/HackWars/C.g:1:388: GT
                {
                mGT(); 

                }
                break;
            case 40 :
                // /Users/benjamincoe/HackWars/C.g:1:391: PLUS
                {
                mPLUS(); 

                }
                break;
            case 41 :
                // /Users/benjamincoe/HackWars/C.g:1:396: MINUS
                {
                mMINUS(); 

                }
                break;
            case 42 :
                // /Users/benjamincoe/HackWars/C.g:1:402: TIMES
                {
                mTIMES(); 

                }
                break;
            case 43 :
                // /Users/benjamincoe/HackWars/C.g:1:408: DIV
                {
                mDIV(); 

                }
                break;
            case 44 :
                // /Users/benjamincoe/HackWars/C.g:1:412: POWER
                {
                mPOWER(); 

                }
                break;
            case 45 :
                // /Users/benjamincoe/HackWars/C.g:1:418: MOD
                {
                mMOD(); 

                }
                break;
            case 46 :
                // /Users/benjamincoe/HackWars/C.g:1:422: EQUALS
                {
                mEQUALS(); 

                }
                break;
            case 47 :
                // /Users/benjamincoe/HackWars/C.g:1:429: BECOMES
                {
                mBECOMES(); 

                }
                break;
            case 48 :
                // /Users/benjamincoe/HackWars/C.g:1:437: AND
                {
                mAND(); 

                }
                break;
            case 49 :
                // /Users/benjamincoe/HackWars/C.g:1:441: OR
                {
                mOR(); 

                }
                break;
            case 50 :
                // /Users/benjamincoe/HackWars/C.g:1:444: SEMICOLON
                {
                mSEMICOLON(); 

                }
                break;
            case 51 :
                // /Users/benjamincoe/HackWars/C.g:1:454: SBLEFT
                {
                mSBLEFT(); 

                }
                break;
            case 52 :
                // /Users/benjamincoe/HackWars/C.g:1:461: SBRIGHT
                {
                mSBRIGHT(); 

                }
                break;
            case 53 :
                // /Users/benjamincoe/HackWars/C.g:1:469: INCREMENT
                {
                mINCREMENT(); 

                }
                break;
            case 54 :
                // /Users/benjamincoe/HackWars/C.g:1:479: DECREMENT
                {
                mDECREMENT(); 

                }
                break;
            case 55 :
                // /Users/benjamincoe/HackWars/C.g:1:489: INCREMENT_ASSIGN
                {
                mINCREMENT_ASSIGN(); 

                }
                break;
            case 56 :
                // /Users/benjamincoe/HackWars/C.g:1:506: DECREMENT_ASSIGN
                {
                mDECREMENT_ASSIGN(); 

                }
                break;
            case 57 :
                // /Users/benjamincoe/HackWars/C.g:1:523: MULTIPLY_ASSIGN
                {
                mMULTIPLY_ASSIGN(); 

                }
                break;
            case 58 :
                // /Users/benjamincoe/HackWars/C.g:1:539: NOT
                {
                mNOT(); 

                }
                break;
            case 59 :
                // /Users/benjamincoe/HackWars/C.g:1:543: BOOLEAN_LITERAL
                {
                mBOOLEAN_LITERAL(); 

                }
                break;
            case 60 :
                // /Users/benjamincoe/HackWars/C.g:1:559: INTEGER_LITERAL
                {
                mINTEGER_LITERAL(); 

                }
                break;
            case 61 :
                // /Users/benjamincoe/HackWars/C.g:1:575: IDENTIFIER
                {
                mIDENTIFIER(); 

                }
                break;
            case 62 :
                // /Users/benjamincoe/HackWars/C.g:1:586: STRING_LITERAL
                {
                mSTRING_LITERAL(); 

                }
                break;
            case 63 :
                // /Users/benjamincoe/HackWars/C.g:1:601: DOUBLE
                {
                mDOUBLE(); 

                }
                break;

        }

    }


    protected DFA12 dfa12 = new DFA12(this);
    static final String DFA12_eotS =
        "\1\uffff\2\45\1\54\7\45\1\uffff\1\73\1\uffff\3\45\5\uffff\1\100"+
        "\1\102\1\104\1\107\1\112\1\114\2\uffff\1\116\5\uffff\1\54\2\uffff"+
        "\4\45\1\54\2\uffff\1\45\1\126\11\45\3\uffff\3\45\20\uffff\2\45\1"+
        "\146\1\45\1\54\1\151\1\45\1\uffff\17\45\1\uffff\1\45\1\54\1\uffff"+
        "\1\45\1\u0080\1\u0081\1\u0082\7\45\1\u008b\1\u008c\1\u008e\1\u008f"+
        "\1\45\1\u0081\3\uffff\2\45\3\uffff\5\45\1\u009b\2\45\2\uffff\1\45"+
        "\2\uffff\1\45\2\uffff\1\u00a0\4\45\1\u00a5\2\45\1\uffff\1\u00a5"+
        "\3\45\1\uffff\4\45\1\uffff\3\45\1\u008b\2\45\1\u00b5\10\45\1\uffff"+
        "\1\45\1\u00bf\7\45\1\uffff\6\45\1\u00cd\6\45\1\uffff\5\45\1\u00d9"+
        "\3\45\1\u00dd\1\45\1\uffff\3\45\1\uffff\1\u00e2\1\45\1\u00e4\1\45"+
        "\1\uffff\1\u00e6\1\uffff\1\u00e7\2\uffff";
    static final String DFA12_eofS =
        "\u00e8\uffff";
    static final String DFA12_minS =
        "\1\11\1\150\1\141\1\56\1\146\1\154\1\150\1\145\1\157\1\151\1\162"+
        "\1\uffff\1\52\1\uffff\1\164\2\157\5\uffff\3\75\1\53\1\55\1\75\2"+
        "\uffff\1\75\5\uffff\1\56\2\uffff\1\151\1\157\1\162\1\154\1\56\2"+
        "\uffff\1\164\1\60\1\163\1\165\1\145\1\143\1\157\1\167\1\162\1\147"+
        "\1\162\3\uffff\1\162\1\157\1\151\20\uffff\1\154\1\141\1\60\1\163"+
        "\1\56\1\60\1\141\1\uffff\2\145\1\156\1\154\1\143\1\145\1\151\1\156"+
        "\1\141\1\151\1\154\1\144\1\145\1\164\1\163\1\uffff\1\145\1\56\1"+
        "\uffff\1\162\3\60\1\141\1\145\1\162\1\156\1\137\1\171\1\156\4\60"+
        "\1\164\1\60\1\141\2\uffff\1\164\1\141\3\uffff\1\162\1\144\1\137"+
        "\1\147\1\145\1\60\1\147\1\141\2\uffff\1\163\2\uffff\1\141\2\uffff"+
        "\1\60\1\162\1\141\1\165\1\145\1\60\1\170\1\144\1\uffff\1\60\1\156"+
        "\2\164\1\uffff\2\164\1\162\1\170\1\uffff\1\160\1\144\1\145\1\60"+
        "\1\141\1\145\1\60\1\151\1\145\1\160\1\162\1\145\1\143\1\164\1\155"+
        "\1\uffff\1\157\1\60\1\162\1\145\1\156\1\154\2\145\1\156\1\uffff"+
        "\1\145\1\163\1\164\1\141\1\155\1\156\1\60\2\163\1\151\1\162\1\145"+
        "\1\164\1\uffff\1\163\1\151\1\146\1\145\1\156\1\60\1\151\1\157\1"+
        "\151\1\60\1\164\1\uffff\1\157\1\156\1\145\1\uffff\1\60\1\156\1\60"+
        "\1\162\1\uffff\1\60\1\uffff\1\60\2\uffff";
    static final String DFA12_maxS =
        "\1\175\1\150\1\157\1\71\1\156\1\154\1\162\1\145\1\162\1\164\1\162"+
        "\1\uffff\1\57\1\uffff\1\164\2\157\5\uffff\6\75\2\uffff\1\75\5\uffff"+
        "\1\71\2\uffff\1\151\1\157\1\162\1\154\1\71\2\uffff\1\164\1\172\1"+
        "\163\1\165\1\145\1\143\1\157\1\167\1\162\1\147\1\162\3\uffff\1\162"+
        "\1\157\1\151\20\uffff\1\154\1\141\1\172\1\163\1\71\1\172\1\141\1"+
        "\uffff\2\145\1\156\1\154\1\143\1\145\1\151\1\156\1\141\1\151\1\154"+
        "\1\144\1\145\1\164\1\163\1\uffff\1\145\1\160\1\uffff\1\162\3\172"+
        "\1\141\1\145\1\162\1\156\1\137\1\171\1\156\4\172\1\164\1\172\1\162"+
        "\2\uffff\1\164\1\141\3\uffff\1\162\1\144\1\137\1\147\1\145\1\172"+
        "\1\147\1\141\2\uffff\1\163\2\uffff\1\141\2\uffff\1\172\1\162\1\141"+
        "\1\165\1\145\1\172\1\170\1\151\1\uffff\1\172\1\156\2\164\1\uffff"+
        "\2\164\1\162\1\170\1\uffff\1\160\1\144\1\145\1\172\1\141\1\145\1"+
        "\172\1\151\1\145\1\160\1\162\1\145\1\143\1\164\1\155\1\uffff\1\157"+
        "\1\172\1\162\1\145\1\156\1\154\2\145\1\156\1\uffff\1\145\1\163\1"+
        "\164\1\141\1\155\1\156\1\172\2\163\1\151\1\162\1\145\1\164\1\uffff"+
        "\1\163\1\151\1\146\1\145\1\156\1\172\1\151\1\157\1\151\1\172\1\164"+
        "\1\uffff\1\157\1\156\1\145\1\uffff\1\172\1\156\1\172\1\162\1\uffff"+
        "\1\172\1\uffff\1\172\2\uffff";
    static final String DFA12_acceptS =
        "\13\uffff\1\20\1\uffff\1\23\3\uffff\1\36\1\37\1\40\1\41\1\42\6\uffff"+
        "\1\54\1\55\1\uffff\1\60\1\61\1\62\1\63\1\64\1\uffff\1\75\1\76\5"+
        "\uffff\1\74\1\77\13\uffff\1\21\1\22\1\53\3\uffff\1\43\1\72\1\44"+
        "\1\45\1\46\1\47\1\65\1\67\1\50\1\70\1\66\1\51\1\71\1\52\1\56\1\57"+
        "\7\uffff\1\24\17\uffff\1\27\2\uffff\1\32\22\uffff\1\17\1\3\2\uffff"+
        "\1\25\1\73\1\6\10\uffff\1\33\1\34\1\uffff\1\26\1\30\1\uffff\1\11"+
        "\1\14\10\uffff\1\35\4\uffff\1\4\4\uffff\1\31\17\uffff\1\5\11\uffff"+
        "\1\10\15\uffff\1\7\13\uffff\1\2\3\uffff\1\16\4\uffff\1\1\1\uffff"+
        "\1\13\1\uffff\1\12\1\15";
    static final String DFA12_specialS =
        "\u00e8\uffff}>";
    static final String[] DFA12_transitionS = {
            "\1\13\1\15\1\uffff\1\13\1\15\22\uffff\1\13\1\26\1\46\2\uffff"+
            "\1\35\1\37\1\uffff\1\24\1\25\1\33\1\31\1\21\1\32\1\uffff\1\14"+
            "\1\44\1\3\10\44\1\uffff\1\41\1\27\1\36\1\30\2\uffff\22\45\1"+
            "\16\7\45\1\22\1\uffff\1\23\1\34\2\uffff\1\12\1\17\1\45\1\7\1"+
            "\5\1\2\2\45\1\4\6\45\1\10\2\45\1\11\1\6\1\45\1\20\1\1\3\45\1"+
            "\42\1\40\1\43",
            "\1\47",
            "\1\52\12\uffff\1\50\2\uffff\1\51",
            "\1\55\1\uffff\2\44\1\53\7\44",
            "\1\57\7\uffff\1\56",
            "\1\60",
            "\1\62\11\uffff\1\61",
            "\1\63",
            "\1\65\2\uffff\1\64",
            "\1\67\12\uffff\1\66",
            "\1\70",
            "",
            "\1\72\4\uffff\1\71",
            "",
            "\1\74",
            "\1\75",
            "\1\76",
            "",
            "",
            "",
            "",
            "",
            "\1\77",
            "\1\101",
            "\1\103",
            "\1\105\21\uffff\1\106",
            "\1\111\17\uffff\1\110",
            "\1\113",
            "",
            "",
            "\1\115",
            "",
            "",
            "",
            "",
            "",
            "\1\55\1\uffff\12\44",
            "",
            "",
            "\1\117",
            "\1\120",
            "\1\121",
            "\1\122",
            "\1\55\1\uffff\1\44\1\123\10\44",
            "",
            "",
            "\1\124",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45\1\125\12\45",
            "\1\127",
            "\1\130",
            "\1\131",
            "\1\132",
            "\1\133",
            "\1\134",
            "\1\135",
            "\1\136",
            "\1\137",
            "",
            "",
            "",
            "\1\140",
            "\1\141",
            "\1\142",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\143",
            "\1\144",
            "\12\45\7\uffff\32\45\4\uffff\1\145\1\uffff\32\45",
            "\1\147",
            "\1\55\1\uffff\2\44\1\150\7\44",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\152",
            "",
            "\1\153",
            "\1\154",
            "\1\155",
            "\1\156",
            "\1\157",
            "\1\160",
            "\1\161",
            "\1\162",
            "\1\163",
            "\1\164",
            "\1\165",
            "\1\166",
            "\1\167",
            "\1\170",
            "\1\171",
            "",
            "\1\172",
            "\1\55\1\uffff\12\44\51\uffff\1\174\2\uffff\1\175\11\uffff\1"+
            "\173",
            "",
            "\1\176",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\17\45\1\177\12\45",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u0083",
            "\1\u0084",
            "\1\u0085",
            "\1\u0086",
            "\1\u0087",
            "\1\u0088",
            "\1\u0089",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\4\45\1\u008a\25\45",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\12\45\7\uffff\32\45\4\uffff\1\u008d\1\uffff\32\45",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u0090",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u0091\20\uffff\1\u0092",
            "",
            "",
            "\1\u0093",
            "\1\u0094",
            "",
            "",
            "",
            "\1\u0095",
            "\1\u0096",
            "\1\u0097",
            "\1\u0098",
            "\1\u0099",
            "\12\45\7\uffff\32\45\4\uffff\1\u009a\1\uffff\32\45",
            "\1\u009c",
            "\1\u009d",
            "",
            "",
            "\1\u009e",
            "",
            "",
            "\1\u009f",
            "",
            "",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00a1",
            "\1\u00a2",
            "\1\u00a3",
            "\1\u00a4",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00a6",
            "\1\u00a8\4\uffff\1\u00a7",
            "",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00a9",
            "\1\u00aa",
            "\1\u00ab",
            "",
            "\1\u00ac",
            "\1\u00ad",
            "\1\u00ae",
            "\1\u00af",
            "",
            "\1\u00b0",
            "\1\u00b1",
            "\1\u00b2",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00b3",
            "\1\u00b4",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00b6",
            "\1\u00b7",
            "\1\u00b8",
            "\1\u00b9",
            "\1\u00ba",
            "\1\u00bb",
            "\1\u00bc",
            "\1\u00bd",
            "",
            "\1\u00be",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00c0",
            "\1\u00c1",
            "\1\u00c2",
            "\1\u00c3",
            "\1\u00c4",
            "\1\u00c5",
            "\1\u00c6",
            "",
            "\1\u00c7",
            "\1\u00c8",
            "\1\u00c9",
            "\1\u00ca",
            "\1\u00cb",
            "\1\u00cc",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00ce",
            "\1\u00cf",
            "\1\u00d0",
            "\1\u00d1",
            "\1\u00d2",
            "\1\u00d3",
            "",
            "\1\u00d4",
            "\1\u00d5",
            "\1\u00d6",
            "\1\u00d7",
            "\1\u00d8",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00da",
            "\1\u00db",
            "\1\u00dc",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00de",
            "",
            "\1\u00df",
            "\1\u00e0",
            "\1\u00e1",
            "",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00e3",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "\1\u00e5",
            "",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            "\12\45\7\uffff\32\45\4\uffff\1\45\1\uffff\32\45",
            "",
            ""
    };

    static final short[] DFA12_eot = DFA.unpackEncodedString(DFA12_eotS);
    static final short[] DFA12_eof = DFA.unpackEncodedString(DFA12_eofS);
    static final char[] DFA12_min = DFA.unpackEncodedStringToUnsignedChars(DFA12_minS);
    static final char[] DFA12_max = DFA.unpackEncodedStringToUnsignedChars(DFA12_maxS);
    static final short[] DFA12_accept = DFA.unpackEncodedString(DFA12_acceptS);
    static final short[] DFA12_special = DFA.unpackEncodedString(DFA12_specialS);
    static final short[][] DFA12_transition;

    static {
        int numStates = DFA12_transitionS.length;
        DFA12_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA12_transition[i] = DFA.unpackEncodedString(DFA12_transitionS[i]);
        }
    }

    class DFA12 extends DFA {

        public DFA12(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 12;
            this.eot = DFA12_eot;
            this.eof = DFA12_eof;
            this.min = DFA12_min;
            this.max = DFA12_max;
            this.accept = DFA12_accept;
            this.special = DFA12_special;
            this.transition = DFA12_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( WHILE_STATEMENT | FOR_STATEMENT | FUNCTION | IFPART | ELSEPART | THEN | DECLARATION | PROCEDURE | PROCEDURE_PARAMETERS | POWER_EXPRESSION | SIGN_EXPRESSION | PROGRAM | ARRAY_IDENTIFIER | ARRAY_DECLARE | CAST_EXPRESSION | WHITE_SPACE | COMMENT | ML_COMMENT | NEWLINE | IF | ELSE | WHILE | FOR | FLOAT_TYPE | STRING_TYPE | INTEGER_TYPE | BOOLEAN_TYPE | VOID_TYPE | ARRAY_TYPE | COMMA | LBRACKET | RBRACKET | LPAREN | RPAREN | NOT_EQUALS | LTE | LT | GTE | GT | PLUS | MINUS | TIMES | DIV | POWER | MOD | EQUALS | BECOMES | AND | OR | SEMICOLON | SBLEFT | SBRIGHT | INCREMENT | DECREMENT | INCREMENT_ASSIGN | DECREMENT_ASSIGN | MULTIPLY_ASSIGN | NOT | BOOLEAN_LITERAL | INTEGER_LITERAL | IDENTIFIER | STRING_LITERAL | DOUBLE );";
        }
    }
 

}