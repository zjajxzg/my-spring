package com.zjajxzg.test.bean;

import com.zjajxzg.spring.annotation.Scope;
import com.zjajxzg.spring.annotation.Service;

/**
 * @author xuzhigang
 * @date 2023/2/18 14:37
 **/
@Service("userService")
@Scope("prototype")
public class UserService {
}
