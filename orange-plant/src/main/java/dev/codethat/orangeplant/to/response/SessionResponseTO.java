package dev.codethat.orangeplant.to.response;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;

import java.io.IOException;

public class SessionResponseTO extends SessionResponseCoreTO {
    private static final long serialVersionUID = 4489356047088770010L;

    transient private KiteConnect kiteConnect;
    transient private User user;

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

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // kiteConnect
        out.writeUTF(kiteConnect.getApiKey());
        out.writeUTF(kiteConnect.getPublicToken());
        out.writeUTF(kiteConnect.getAccessToken());
        // user
        out.writeUTF(user.refreshToken);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // kiteConnect
        KiteConnect kiteConnect = new KiteConnect(in.readUTF());
        kiteConnect.setPublicToken(in.readUTF());
        kiteConnect.setAccessToken(in.readUTF());
        this.setKiteConnect(kiteConnect);
        // user
        User user = new User();
        // TODO refreshToken is blank
        user.refreshToken = in.readUTF();
        this.setUser(user);
    }
}
