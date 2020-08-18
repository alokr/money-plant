package dev.codethat.orangeplant.bean.response;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import com.zerodhatech.ticker.KiteTicker;
import dev.codethat.moneyplant.core.bean.response.SessionResponseCoreTO;
import lombok.Data;

import java.io.IOException;

@Data
public class SessionResponseTO extends SessionResponseCoreTO {
    private static final long serialVersionUID = 4489356047088770010L;

    transient private KiteConnect kiteConnect;
    transient private KiteTicker kiteTicker;
    transient private User user;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // kiteConnect
        out.writeUTF(kiteConnect.getApiKey());
        out.writeUTF(kiteConnect.getPublicToken());
        out.writeUTF(kiteConnect.getAccessToken());
        // kiteTicker
        out.writeUTF(kiteConnect.getAccessToken());
        out.writeUTF(kiteConnect.getApiKey());
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
        // kiteTicker
        KiteTicker kiteTicker = new KiteTicker(in.readUTF(), in.readUTF());
        this.setKiteTicker(kiteTicker);
        // user
        User user = new User();
        // TODO refreshToken is blank
        user.refreshToken = in.readUTF();
        this.setUser(user);
    }
}
