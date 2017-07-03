package forms

import play.api.data.Form;

/**
 * ログインフォーム
 */
case class LoginForm(account: String, password: String)
