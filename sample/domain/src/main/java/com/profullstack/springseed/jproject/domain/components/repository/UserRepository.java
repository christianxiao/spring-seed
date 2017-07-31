package com.profullstack.springseed.jproject.domain.components.repository;

import com.profullstack.springseed.jproject.domain.components.model.QUser;
import com.profullstack.springseed.jproject.domain.components.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by christianxiao on 4/16/16.
 */
public interface UserRepository extends JpaRepository<User,Integer>, QueryDslPredicateExecutor<User>, UserRepositoryCustom {
    QUser qUser = QUser.user;

    User findUserByLoginIdAndPassword(String loginId, String password);

    @Query("SELECT user FROM User user WHERE user.loginId = ?1")
    List<User> getUserByLoginId(String loginId);

    @Query("SELECT user FROM User user WHERE user.loginId = ?1 AND user.password = ?2")
    List<User> getUserByLoginIdAndPassword(String loginId, String password);
}
