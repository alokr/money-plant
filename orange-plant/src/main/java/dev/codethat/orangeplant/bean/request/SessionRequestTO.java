package dev.codethat.orangeplant.bean.request;

import dev.codethat.moneyplant.core.bean.request.SessionRequestCoreTO;

public class SessionRequestTO extends SessionRequestCoreTO {
    private String requestToken;

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }
}
