import org.openqa.selenium.Dimension
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.phantomjs.PhantomJSDriver
import org.openqa.selenium.remote.DesiredCapabilities

waiting {
    timeout = 5
}

environments {
    environments {

        // run via "./gradlew chromeTest"
        // See: http://code.google.com/p/selenium/wiki/ChromeDriver
        chrome {
            driver = { new ChromeDriver() }
        }

        // run via "./gradlew firefoxTest"
        // See: http://code.google.com/p/selenium/wiki/FirefoxDriver
        firefox {
            driver = { new FirefoxDriver() }
        }

        // run via "./gradlew phantomJs"
        phantomJs {
            driver = {
                def phantomDriver = new PhantomJSDriver(new DesiredCapabilities(
                        'browserName': 'phantomjs',
                        'phantomjs.cli.args': [
                                "--ignore-ssl-errors=yes",
                                "--web-security=no",
                                "--proxy-type=none",
                                "--ssl-protocol=tlsv2"
                        ] as String[]
                ))
                phantomDriver.manage().window().size = new Dimension(1280, 1024)
                return phantomDriver
            }
        }

    }
}

// Clear the cookies automatically at new test class
autoClearCookies = true

// Wait at page navigation until the new page is available
baseNavigatorWaiting = true

// Wait for timeout to check the "at" specifications inside pages
atCheckWaiting = true

reportsDir = new File("build/runtime_reports_dir")
baseUrl = "https://dienste.kvb.sn3.ig1.local/"
