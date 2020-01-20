package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.service.SessionService;
import dev.codethat.orangeplant.adapter.SessionAdapterImpl;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SessionServiceImpl implements SessionService<SessionRequestTO, SessionResponseTO> {
    private SessionAdapterImpl sessionAdapter;

    @Inject
    public SessionServiceImpl(SessionAdapterImpl sessionAdapter) {
        this.sessionAdapter = sessionAdapter;
    }

    @Override
    public SessionResponseTO login(SessionRequestTO sessionRequestTO) {
        SessionResponseTO sessionResponseTO = sessionAdapter.login(sessionRequestTO);
        return sessionResponseTO;
    }

    @Override
    public SessionResponseTO refresh(SessionRequestTO sessionRequestTO) {
        return null;
    }

    @Override
    public SessionResponseTO logout(SessionRequestTO sessionRequestTO) {
        return null;
    }
}
