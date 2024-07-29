package org.ckzs.ckdp.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ckzs.ckdp.pojo.User;

@Mapper
public interface UserMapper {
    public User getByUsername(String username);

    void insert(User user);
}
