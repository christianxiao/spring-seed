package com.profullstack.springseed.jproject.domain.components.service;

import com.profullstack.springseed.infrastructure.redis.ExpirableCacheable;
import com.profullstack.springseed.jproject.domain.components.model.User;
import com.profullstack.springseed.jproject.domain.components.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by christianxiao on 4/17/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository rep;

	@ExpirableCacheable(cacheName="userCache")
	public User findUserByLoginIdAndPassword(String loginId, String password){
		return rep.findUserByLoginIdAndPassword(loginId,password);
	}
}
