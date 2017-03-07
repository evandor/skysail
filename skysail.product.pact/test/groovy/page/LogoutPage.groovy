package page

import geb.Page

class LogoutPage extends Page {

    static url = "/_logout?targetUri=/"

    static at = { title == "" }


}