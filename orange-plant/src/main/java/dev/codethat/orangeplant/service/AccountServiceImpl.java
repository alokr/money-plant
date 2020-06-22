package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.adapter.AccountAdapter;
import dev.codethat.moneyplant.core.service.AccountService;
import dev.codethat.orangeplant.to.request.AccountRequestTO;
import dev.codethat.orangeplant.to.response.AccountResponseTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService<AccountRequestTO, AccountResponseTO> {
    private AccountAdapter adapter;

    @Inject
    public AccountServiceImpl(AccountAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public AccountResponseTO margin(AccountRequestTO accountRequestTO) throws Exception {
        return (AccountResponseTO) adapter.margin(accountRequestTO);
    }
}
