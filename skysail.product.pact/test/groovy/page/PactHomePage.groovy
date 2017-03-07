package page

import geb.Page
import org.openqa.selenium.By

class PactHomePage extends Page {

    static url = "/pact/v1"

    static at = { title == "Meine KVB" }

    static content = {
        widgets { $(By.className("grid-stack-item")) }
        eFormulareLink { $(By.id("eFormulareLink")) }
        //navbar(wait: true) { module Navbar }
    }

}

