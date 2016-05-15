package io.skysail.client.testsupport;

public class BrowserTests<T extends ApplicationBrowser<?,?>, U>  extends TestsupportTestBase {

    // doesnt seem to work in integration tests
    //@Rule
    //public ExpectedException thrown = ExpectedException.none();

    protected T browser;

}
