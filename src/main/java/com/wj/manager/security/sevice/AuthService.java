package com.wj.manager.security.sevice;

import com.wj.manager.security.vo.AuthToken;

public interface AuthService {
    public AuthToken login(String username, String password);
    public Integer logout(Integer userid);

    public boolean unlock(Integer userId, String password);
}
