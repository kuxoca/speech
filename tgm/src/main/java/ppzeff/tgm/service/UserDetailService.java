package ppzeff.tgm.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ppzeff.shared.Constants;
import ppzeff.tgm.dto.UserDetail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailService {
    private static UserDetailService instance;
    final Map<Long, UserDetail> userDetailMap = Collections.synchronizedMap(new HashMap<>());

    public UserDetail getUserDetailById(Long id) {
        return userDetailMap.get(id);
    }

    public String getVendorByUserId(Long id) {

        if (!userDetailMap.containsKey(id)) {
//            log.info("default vendor {}", Constants.ROUTING_KEY_SBER);
            return Constants.ROUTING_KEY_SBER;
        }
        return userDetailMap.get(id).getVendor();
    }

    public void setUserDetail(long id, UserDetail userDetail) {
        userDetailMap.put(id, userDetail);
    }

    public static UserDetailService getInstance() {
        if (instance == null) {
            instance = new UserDetailService();
        }
        return instance;
    }
}
