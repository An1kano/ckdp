package org.ckzs.ckdp.Service.Impl;

import org.ckzs.ckdp.DTO.UserDTO;
import org.ckzs.ckdp.Mapper.UserMapper;
import org.ckzs.ckdp.Service.UserService;
import org.ckzs.ckdp.pojo.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public User login(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();

        User user = userMapper.getByUsername(username);
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        if(user==null){
            throw new RuntimeException("用户'"+username+"'不存在！！");
        }
        if(!password.equals(user.getPassword())){
            throw new RuntimeException("用户'"+username+"'密码错误！！");
        }
        return user;
    }

    @Override
    public void update(UserDTO userDTO) {

    }
    @Override
    public void insert(UserDTO userDTO) {
        userDTO.setPassword(DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes()));
        User user =new User();
        BeanUtils.copyProperties(userDTO,user);
        try {
            userMapper.insert(user);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof SQLException) {
                throw new RuntimeException("用户'" + user.getUsername()+"'已经存在！！");
            } else {
                throw e; // 重新抛出其他类型的异常
            }
        }
    }
}
