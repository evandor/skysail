package io.skysail.server.ext.starmoney.api;

import java.util.List;

import io.skysail.server.ext.starmoney.domain.Account;

public interface StarMoneyApi {

    List<Account> getAccounts();

}
