import Constants
import page.GebHomePage
import page.LoginPage
import page.LogoutPage
import page.PactHomePage
import geb.spock.GebReportingSpec
import spock.lang.Ignore
import spock.lang.Stepwise

@Stepwise
class PactTest extends GebReportingSpec {

    def setupSpec() {
        reportGroup("pact.int")
        //at LogoutPage
        //browser.clearCookies("argus-signon")
    }

    @Ignore
    def "can access The Book of Geb via homepage"() {
        when:
        to GebHomePage

        and:
        highlights.jQueryLikeApi.click()

        then:
        sectionTitles == ["Navigating Content", "Form Control Shortcuts"]
        //highlights.jQueryLikeApi.selected
    }

    def "login to pact"() {
        given:
        //via LogoutPage
        to LoginPage
        //go "http://pact.int.skysail.io/io.skysail.server.um.shiro.app.ShiroUmApplication/v1/_login"
        report "login page"

        when:
        login('admin', 'skysail')

        then:
        at PactHomePage
        println navbarHeader
        assert navbarHeader.contains("Pact Backend") 
    }

}

