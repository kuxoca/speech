package ppzeff.tgm.service;

import ppzeff.tgm.dto.UserDetail;

public interface UserDetailService {
    String getVendorByUserId(Long id);

    UserDetail getUserDetailById(Long id);

    void setUserDetail(long id, UserDetail userDetail);
}
