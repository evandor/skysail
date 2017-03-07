package page

import geb.Page

class LoginPage extends Page {

    static url = "/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login"

    static at = { title == "" }

    static content = {
        usernameField { $("input[id=login-username]") }
        passwordField { $("input[id=login-password]") }
        submitField { $("input[id=submitButton]") }
    }

    void login(String username, String password) {
        usernameField.value username
        passwordField.value password
        submitField.click()
    }

}