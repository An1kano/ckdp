package org.ckzs.ckdp.Service;

import org.ckzs.ckdp.DTO.UserDTO;
import org.ckzs.ckdp.pojo.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User login(UserDTO userDTO);
    void update(UserDTO userDTO);

    void insert(UserDTO userDTO);
}
