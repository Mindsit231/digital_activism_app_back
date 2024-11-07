package mindsit.digitalactivismapp.service.authentication;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TempDataService {
    private static final String VERIFICATION_CODE_KEY = "verificationCode";

    private final Map<String, Object> tempDataStore = new ConcurrentHashMap<>();

    public void putValue(String key, Object value) {
        tempDataStore.put(key, value);
    }

    public Object getValue(String key) {
        return tempDataStore.get(key);
    }

    public void removeValue(String key) {
        tempDataStore.remove(key);
    }

    public static String getVerificationCodeKey(String prefix) {
        return prefix + "_" + VERIFICATION_CODE_KEY;
    }
}
