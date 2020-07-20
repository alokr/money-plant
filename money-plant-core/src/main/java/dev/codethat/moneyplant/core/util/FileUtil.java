package dev.codethat.moneyplant.core.util;

import dev.codethat.moneyplant.core.to.response.SessionResponseCoreTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.*;
import java.util.Calendar;

@Named
@Slf4j
public class FileUtil {
    public SessionResponseCoreTO serializeSession(final String sessionFile) throws IOException, ClassNotFoundException {
        SessionResponseCoreTO sessionResponseTO = null;
        File file = new File(sessionFile);
        if (file.exists()) {
            Calendar sessionCal = Calendar.getInstance();
            sessionCal.setTimeInMillis(file.lastModified());
            // old session
            if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) > sessionCal.get(Calendar.DAY_OF_MONTH)) {
                boolean deleted = file.delete();
                log.warn("Found invalid session. SessionDeleteStatus={}", deleted);
                return null;
            }
            log.info("Retrieving session");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(sessionFile)));
            sessionResponseTO = (SessionResponseCoreTO) ois.readObject();
        }
        return sessionResponseTO;
    }

    public void deserializeSession(final SessionResponseCoreTO responseCoreTO, String sessionFile) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(sessionFile)));
        oos.writeObject(responseCoreTO);
        log.info("Serialized session");
    }
}
