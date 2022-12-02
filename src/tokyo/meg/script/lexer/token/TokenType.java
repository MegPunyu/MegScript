package tokyo.meg.script.lexer.token;

/**
 * Token Types
 */
public enum TokenType {
  NULL, //
  APOS, // '
  BSOL, // \
  COLON, // :
  COMMA, // ,
  COMMAT, // @
  DOLLAR, // $
  LBRACE, // {
  LBRACK, // [
  LPAR, // (
  NUM, // #
  PERIOD, // .
  QUEST, // ?
  RBRACE, // }
  RBRACK, // ]
  RPAR, // )
  SEMI, // ;

  IDENTIFIER, // a
  OPERATOR, // +
  INT_LITERAL, // 0
  REAL_LITERAL, // 0.0
  STR_LITERAL, // ""
}
