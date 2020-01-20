package dev.codethat.orangeplant.to.response;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class SessionResponseTO extends SessionResponseCoreTO {
    private KiteConnect kiteConnect;
    private User user;

    public KiteConnect getKiteConnect() {
        return kiteConnect;
    }

    public void setKiteConnect(KiteConnect kiteConnect) {
        this.kiteConnect = kiteConnect;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this);
        out.writeObject(kiteConnect);
        out.writeObject(user);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        in.readObject();
    }
}
