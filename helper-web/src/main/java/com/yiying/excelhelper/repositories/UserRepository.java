package com.yiying.excelhelper.repositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.yiying.excelhelper.entities.User;

/**
 * 服务类
 * @author yiying
 * @since 1.0.0
 */
@Repository
public interface UserRepository
        extends CrudRepository<User,Long>,JpaRepository<User,Long>,
        PagingAndSortingRepository<User,Long>,JpaSpecificationExecutor<User>{

}