grammar C;

options { 
	output=AST;
	//ASTLabelType=pANTLR3_COMMON_TREE ;
	//language=C;
	language=Java;
	k=4;
}

tokens{
	WHILE_STATEMENT='while_statement';
	FOR_STATEMENT='for_statement';
	FUNCTION='1212function1212';
	IFPART='ifpart';
	ELSEPART='elsepart';
	THEN='then';
	ELSE='else';
	DECLARATION='declaration';
	PROCEDURE='procedure';
	PROCEDURE_PARAMETERS='1212param1212';
	POWER_EXPRESSION='power_expression';
	SIGN_EXPRESSION='sign_expression';
	PROGRAM='1212program1212';
	ARRAY_IDENTIFIER='array_identifier';
	ARRAY_DECLARE='array_declare';
	CAST_EXPRESSION='1212cast1212';
}

@header {
package Hackscript;
import Hackscript.Model.*;
}


start	:	main_program EOF!;

main_program	:	(program) (expression main_program)?;

//program	:	(procedure|declaration|loop|ifthen)*;
program	:	(allowed_single)*;


// $<Statements

whileStatement	:	WHILE LPAREN expression RPAREN (((SBLEFT) program (SBRIGHT))|allowed_single) -> ^('while_statement' expression (program)? (allowed_single)?);

forStatement	:	FOR LPAREN (assign_statement|declaration) (expression) (SEMICOLON) (assign_statement_sans_colon|increment_decrement_assign_statement_sans_colon) RPAREN (((SBLEFT) program (SBRIGHT))|allowed_single) -> ^('for_statement' (declaration)? (assign_statement)? expression (assign_statement_sans_colon)? (increment_decrement_assign_statement_sans_colon)? (program)? (allowed_single)?);

function:	type IDENTIFIER functionParameters SBLEFT program SBRIGHT -> ^('1212function1212' type IDENTIFIER functionParameters program);

procedure
	:	IDENTIFIER LPAREN procedureParameters RPAREN -> ^('procedure' IDENTIFIER procedureParameters);
	
procedure_statement
	:	IDENTIFIER (((LPAREN) procedureParameters (RPAREN))|procedureParameters) SEMICOLON -> ^('procedure' IDENTIFIER procedureParameters);
	
functionParameters
	:	LPAREN (type (typePair)(COMMA type (typePair))*)? RPAREN -> ^('1212param1212' (type)* (typePair)*);
	
typePair:	
	IDENTIFIER|complex_identifier;

procedureParameters
	: (expression (COMMA expression)*)? -> ^('1212param1212' expression*);
	
	
ifthen	:	IF (expression) (((SBLEFT) program (SBRIGHT))|allowed_single) (elsepart)?-> ^('ifpart' expression (program)? (allowed_single)? (elsepart)?);

elsepart	:	ELSE (((SBLEFT) program (SBRIGHT))|allowed_single) -> ^('elsepart' (program)? (allowed_single)?);

allowed_single
	:	(function|procedure_statement|declaration|whileStatement|forStatement|ifthen|assign_statement|increment_decrement_assign_statement);

arrayDeclare
	:	SBLEFT expression (COMMA expression)* SBRIGHT -> ^('array_declare' expression*);

// $<Expressions

primitiveElement
  :
  arrayDeclare|
  procedure |
  variableReference |
  (LPAREN! expression RPAREN!)
  ;
    
cast	:	LPAREN! type RPAREN!;

signExpression
	:  ((PLUS|MINUS|NOT)^)* (primitiveElement) ((INCREMENT|DECREMENT)^)?;
	
powerExpression
	:	(a=signExpression->$a) ((POWER) b=signExpression->^('power_expression' $powerExpression $b))*;

castExpression 
	:	(a=(cast powerExpression)->^('1212cast1212' cast powerExpression))|powerExpression;

multiplyingExpression
	:	castExpression ((TIMES|DIV|MOD)^ castExpression)*;
	
addingExpression
	:	multiplyingExpression ((PLUS|MINUS)^ multiplyingExpression)*;
	
relationalExpression
  : addingExpression ((GTE|EQUALS|NOT_EQUALS|GT|LT|LTE)^ addingExpression)*;

expression
 	:	(relationalExpression ((AND|OR)^ relationalExpression)*);
 	

// $>

// $<Declarations

declaration
	:	type (assign_assignment) SEMICOLON -> ^('declaration' type assign_assignment);

increment_decrement_assign_statement
		:	(IDENTIFIER|complex_identifier) increment_decrement_type SEMICOLON -> ^('declaration' (IDENTIFIER)? (complex_identifier)? increment_decrement_type);

increment_decrement_assign_statement_sans_colon
		:	(IDENTIFIER|complex_identifier) increment_decrement_type -> ^('declaration' (IDENTIFIER)? (complex_identifier)? increment_decrement_type);


assign_statement
	:	(IDENTIFIER|complex_identifier) assignment_type expression SEMICOLON -> ^('declaration' (IDENTIFIER)? (complex_identifier)? assignment_type expression);
	
assign_statement_sans_colon
	:	(IDENTIFIER|complex_identifier) assignment_type expression -> ^('declaration' (IDENTIFIER)? (complex_identifier)? assignment_type expression);


assign_assignment
	:	(IDENTIFIER|complex_identifier) (assignment_type)? (expression)? -> ^('declaration' (IDENTIFIER)? (complex_identifier)? (assignment_type)? (expression)?);
	

variableReference:	(IDENTIFIER|complex_identifier|STRING_LITERAL|INTEGER_LITERAL|DOUBLE|BOOLEAN_LITERAL);


complex_identifier
	:	IDENTIFIER (LBRACKET (expression)? RBRACKET) -> ^('array_identifier' IDENTIFIER (expression)?);
	

type	:	(FLOAT_TYPE|STRING_TYPE|INTEGER_TYPE|BOOLEAN_TYPE|VOID_TYPE)^;

assignment_type
	:	(BECOMES|MULTIPLY_ASSIGN|INCREMENT_ASSIGN|DECREMENT_ASSIGN)^;
increment_decrement_type
	:	(INCREMENT)|(DECREMENT)^;

WHITE_SPACE
  : ( ' '
    | '\t'
    | '\f'
    ){ $channel=HIDDEN;}
  ;
  
 COMMENT	:	'//' ~('\r\n'  // DOS/Windows
      | '\r'    // Macintosh
      | '\n'    // Unix
      )*{$channel=HIDDEN;};
      	
// multiple-line comments
ML_COMMENT
    :   '/*' (options {greedy=false;} : .)* '*/' {channel=HIDDEN;}
    ;

NEWLINE	:	    // handle newlines
    ('\r\n'  // DOS/Windows
      | '\r'    // Macintosh
      | '\n'    // Unix
      ){//input.setLine(input.getLine()+1); 
      	$channel=HIDDEN;};

// $<Keywords

IF	:	'if';
ELSE	:	'else';
WHILE	:	'while';
FOR	:	'for';

FLOAT_TYPE
	:	'float';
STRING_TYPE
	:	'string'|'String';
INTEGER_TYPE
	:	'int';
BOOLEAN_TYPE
	:	'bool'|'boolean';
VOID_TYPE
	:	'void';

// $>
 
// $<Operators

COMMA      : ','   ;
LBRACKET   : '['   ;
RBRACKET   : ']'   ;
LPAREN     : '('   ;
RPAREN     : ')'   ;
NOT_EQUALS : '!='  ;
LTE        : '<='  ;
LT         : '<'   ;
GTE        : '>='  ;
GT         : '>'   ;
PLUS       : '+'   ;
MINUS      : '-'   ;
TIMES      : '*'   ;
DIV        : '/'   ;
POWER	:	'^';
MOD	:	'%';
EQUALS     : '=='   ;
BECOMES    : '='  ;
AND	:	'&&';
OR	:	'||';
SEMICOLON
	:	';';
SBLEFT	:	'{';
SBRIGHT	:	'}';
INCREMENT
	:	'++';
DECREMENT
	:	'--';
INCREMENT_ASSIGN
	:	'+=';
DECREMENT_ASSIGN
	:	'-=';
MULTIPLY_ASSIGN
	:	'*=';
NOT	:	'!';
	
	
// $>

BOOLEAN_LITERAL
	:	'true'|'false';

INTEGER_LITERAL
	:	('0'..'9')+;
IDENTIFIER
	:	(('a'..'z')|('A'..'Z'))('_'|INTEGER_LITERAL|(('a'..'z')|('A'..'Z')))*;

// $<Literals
STRING_LITERAL
	: '"'! (~('"'|'\n'|'\r'))* '"'!;
DOUBLE	:	INTEGER_LITERAL '.' INTEGER_LITERAL;

// $>



