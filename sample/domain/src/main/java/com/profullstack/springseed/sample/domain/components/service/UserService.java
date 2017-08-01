package com.profullstack.springseed.sample.domain.components.service;

import com.profullstack.springseed.sample.domain.components.model.User;

/**
 * Created by christianxiao on 4/14/16.
 */
public interface UserService {
	User findUserByLoginIdAndPassword(String loginId, String password);
}
