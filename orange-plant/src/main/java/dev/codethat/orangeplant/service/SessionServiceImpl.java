package dev.codethat.orangeplant.service;

import dev.codethat.moneyplant.core.adapter.SessionAdapter;
import dev.codethat.moneyplant.core.service.SessionService;
import dev.codethat.orangeplant.bean.request.SessionRequestTO;
import dev.codethat.orangeplant.bean.response.SessionResponseTO;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class SessionServiceImpl implements SessionService<SessionRequestTO, SessionResponseTO> {
    private SessionAdapter<SessionRequestTO, SessionResponseTO> adapter;

    @Inject
    public SessionServiceImpl(SessionAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public SessionResponseTO login(SessionRequestTO sessionRequestTO) throws Exception {
        SessionResponseTO sessionResponseTO = adapter.login(sessionRequestTO);
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
