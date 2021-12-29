package com.yiying.excelhelper.web;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import com.yiying.excelhelper.entities.User;
import com.yiying.excelhelper.service.UserService;
/**
 * 服务类
 * @author yiying
 * @since 1.0.0
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    final
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据User的字段,自动生成条件,字段的值为null不生成条件
     * http://localhost:8080/user/?id=1
     * @param user 实体对象
     * @param pageable 分页/排序对象
     * @return 返回的是实体,里面涵盖分页信息及状态码
     */
     @GetMapping
     ResponseEntity<Object> search(User user, Pageable pageable) {
             return userService.search(user, pageable);
     }
     @PostMapping
     void create() {
     }
     @PutMapping
     void update(){
     }
     @DeleteMapping("/")
     void delete(){
     }
}
