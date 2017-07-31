package com.profullstack.springseed.jproject.domain.components.repository;

import com.profullstack.springseed.jproject.domain.components.model.User;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import java.util.List;

/**
 * Created by christianxiao on 6/25/17.
 */
public class UserRepositoryImpl extends QueryDslRepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public List<User> findUserByLoginIdAndPassword2(String id, String password) {
        return from(UserRepository.qUser).where(UserRepository.qUser.loginId.eq(id).and(UserRepository.qUser.password.eq(password))).list(UserRepository.qUser);
    }
}
