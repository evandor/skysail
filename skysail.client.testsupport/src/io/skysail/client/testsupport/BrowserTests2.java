package io.skysail.client.testsupport;

public class BrowserTests2<T extends ApplicationBrowser2>  extends TestsupportTestBase {

    // doesnt seem to work in integration tests
    //@Rule
    //public ExpectedException thrown = ExpectedException.none();

    protected T browser;

}
