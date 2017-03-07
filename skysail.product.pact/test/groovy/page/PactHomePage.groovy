package page

import geb.Page
import org.openqa.selenium.By

class PactHomePage extends Page {

    static url = "/pact/v1"

    static at = { title == "" }

    static content = {
        navbarHeader { $(By.className("navbar-header")) }
    }

}

