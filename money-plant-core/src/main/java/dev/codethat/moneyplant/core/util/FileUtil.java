package dev.codethat.moneyplant.core.util;

import dev.codethat.moneyplant.core.bean.response.SessionResponseCoreTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Named;
import java.io.*;
import java.util.Calendar;

@Named
@Slf4j
public class FileUtil {
    public SessionResponseCoreTO deserializeSession(final String sessionFile) throws IOException, ClassNotFoundException {
        SessionResponseCoreTO sessionResponseTO = null;
        File file = new File(sessionFile);
        if (file.exists()) {
            // old session
            Calendar sessionCal = Calendar.getInstance();
            sessionCal.setTimeInMillis(file.lastModified());
            if (Math.abs(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                    - sessionCal.get(Calendar.DAY_OF_MONTH)) > 0) {
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

    public void serializeSession(final SessionResponseCoreTO responseCoreTO, String sessionFile) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(sessionFile)));
        oos.writeObject(responseCoreTO);
        log.info("Serialized session");
    }
}
