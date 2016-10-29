package io.skysail.server.app.starmoney;

import java.util.List;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.ext.starmoney.api.StarMoneyApi;
import io.skysail.server.ext.starmoney.domain.Account;

@Component(immediate = true)
public class StarMoneyApiImpl implements StarMoneyApi {

    @Override
    public List<Account> getAccounts() {
        return Import2MemoryProcessor.getAccounts();
    }

}
