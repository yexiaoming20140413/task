package com.longdai.task.mybatis.mapper;




import com.longdai.task.mybatis.model.Task;

import java.util.List;

/**
 * @Copyright      Copyright (c) 2016,2016
 * @Title
 * @Description
 *
 *
 * @author         Jade ZHANG E-mail:pioneer_zcy@163.cpm
 * @date           16/02/21
 * @version        1.0
 */
public interface TaskMapper {

    List<Task> queryAllTask();//查询所有task

    int insertTask(Task task);//插入一条task

    int deleteTask(Task task);//删除一条task

    int updateTask(Task task);//更新一条task

    Task queryTask(Task task);//查询一条task



}
