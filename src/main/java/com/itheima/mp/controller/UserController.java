package com.itheima.mp.controller;


import cn.hutool.core.bean.BeanUtil;
import com.itheima.mp.domain.VO.UserVO;
import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.query.UserQuery;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/users")
@Api(tags="用户管理接口")
public class UserController {
    @Autowired
    private IUserService userService;


    @ApiOperation("新增用户接口")
    @PostMapping
    public void saveUser(@RequestBody UserFormDTO userDTO){
      //1.将DTO拷贝到PO
        User user=new User();
        BeanUtils.copyProperties(userDTO,user);

        //2.新增
        userService.save(user);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("{id}")
    public void deleteUserById(@ApiParam("用户id") @PathVariable("id") Long id){
       userService.removeById(id);
    }

    @ApiOperation("根据id查询用户接口")
    @GetMapping("{id}")
    public UserVO queryById(@ApiParam("用户id") @PathVariable("id") Long id){
        //1.查询用户
        User user = userService.getById(id);
        //2.拷贝
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }


    @ApiOperation("批量查询用户接口")
    @GetMapping
    public List<UserVO>  queryUserByIds(@ApiParam("用户id集合") @RequestParam List<Long> ids) {
       //1.查询用户
        List<User> users = userService.listByIds(ids);
       //2.把PO拷贝到VO

        List<UserVO> list = BeanUtil.copyToList(users, UserVO.class);
        return list;

    }

    @ApiOperation("扣减用户余额接口")
    @DeleteMapping("/{id}/deduction/{money}")
    public void deductMoneyById(@ApiParam("用户id") @PathVariable("id") Long id,
                                      @ApiParam("扣减金额") @PathVariable("money") Integer money) {
     userService.deductBalance(id,money);
    }

    @ApiOperation("根据复杂条件查询用户余额接口")
    @GetMapping("/list")
    public List<UserVO> queryUsers(UserQuery query){
        //1.查询用户PO
        List<User> users = userService.queryUsers(query.getName(), query.getStatus(),query.getMinBalance(),query.getMaxBalance());
        //2.把PO拷贝到VO
        return BeanUtil.copyToList(users,UserVO.class);
    }


}
