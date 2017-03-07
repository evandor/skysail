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
    }

   /*def "dashboard displays widgets and navbar"() {
        when:
        to DashboardHomePage

        then:
        assert navbar.brandLogo.isDisplayed()
        assert navbar.searchBar.isDisplayed()
        assert widgets.size() > 0
    }

    def "click on eformulare widget opens eformulare application"() {
        when:
        to DashboardHomePage

        and:
        eFormulareLink.click()

        then:
        at EformulareHomePage
    }

    def "click on navbar - menu - logout"() {
        when:
        to DashboardHomePage

        and:
        navbar.logout()

        then:
        at LoginPage
    }*/

}

