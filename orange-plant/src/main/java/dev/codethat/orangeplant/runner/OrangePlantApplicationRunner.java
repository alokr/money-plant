package dev.codethat.orangeplant.runner;

import com.zerodhatech.kiteconnect.KiteConnect;
import dev.codethat.moneyplant.core.runner.MoneyPlantCoreApplicationRunner;
import dev.codethat.orangeplant.service.SessionServiceImpl;
import dev.codethat.orangeplant.to.request.SessionRequestTO;
import dev.codethat.orangeplant.to.response.SessionResponseTO;
import org.springframework.boot.ApplicationArguments;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.Optional;

@Named
public class OrangePlantApplicationRunner extends MoneyPlantCoreApplicationRunner {
    private final SessionServiceImpl sessionService;

    @Inject
    public OrangePlantApplicationRunner(SessionServiceImpl sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<String> requestToken = args.getNonOptionArgs().stream().findFirst();
        if (!requestToken.isPresent()) {
            throw new IllegalArgumentException("request token is missing!");
        }

        SessionRequestTO sessionRequestTO = new SessionRequestTO();
        sessionRequestTO.setRequestToken(requestToken.get());

        SessionResponseTO sessionResponseTO = null;

        File file = new File("sessionResponseTO.ser");
        if (file.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("sessionResponseTO.ser")));
            sessionResponseTO = (SessionResponseTO) ois.readObject();
        }

        if (sessionResponseTO == null) {
            sessionResponseTO = sessionService.login(sessionRequestTO);
            if (sessionResponseTO != null) {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("sessionResponseTO.ser")));
                oos.writeObject(sessionResponseTO);
            } else {
                throw new Exception();
            }
        }
    }
}
