package ppzeff.tgm.bot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ppzeff.tgm.bot.dto.UserDetail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailService {
    private static UserDetailService instance;
    final Map<Long, UserDetail> userDetailMap = Collections.synchronizedMap(new HashMap<>());

    public UserDetail getUserDetailById(Long id) {
        return userDetailMap.get(id);
    }

    public String getVendorByUserId(Long id) {
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
