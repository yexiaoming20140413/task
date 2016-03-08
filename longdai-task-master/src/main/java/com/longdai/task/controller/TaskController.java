package com.longdai.task.controller;
import com.alibaba.fastjson.JSONObject;
import com.longdai.task.mybatis.mapper.TaskMapper;
import com.longdai.task.mybatis.model.Task;
import com.longdai.task.service.TaskService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
/**
 * @Copyright: Copyright (c) 2016,${year},
 * @Description: ${todo}
 */

@RestController
@RequestMapping("/task")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    HttpServletRequest request;

    @Resource(name = "uniScheduler")
    Scheduler scheduler;
    @Autowired
    TaskMapper taskDao;
    @Autowired
    TaskService taskService;

    @RequestMapping("/query")
    public String queryAllTask(){
        String json = "";
        try {
            json = JSONObject.toJSONString(taskDao.queryAllTask());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    @RequestMapping("/add")
    public String insertTask(){
        Task task = getTaskByRequest();
        Task dbTask = taskDao.queryTask(task);
        if(null != dbTask){
            return "job is exits!";
        }
        int status = taskDao.insertTask(task);
        try{
            if(scheduler.isStarted())
                taskService.scheduleAddOneJobWithTrigger(scheduler, task);
        }catch (SchedulerException e){
            e.printStackTrace();
        }
        logger.warn("--/task/insert--{}:",status);
        if(status == 1)
            return "Insert Success!";
        else
            return "Insert failure!";
    }

    @RequestMapping("/del")
    public String deleteTask(){
        Task task = getTaskByRequest();
        int status = taskDao.deleteTask(task);
        try{
            if(scheduler.isStarted())
                taskService.scheduleRemoveOneJob(scheduler, task);
        }catch (SchedulerException e){
            e.printStackTrace();
        }
        logger.warn("--/task/delete--{}:",status);
        if(status == 1)
            return "Delete Success!";
        else
            return "Delete failure!";
    }

    @RequestMapping("/set")
    public String updateTask(){
        Task task = getTaskByRequest();
        int status = taskDao.updateTask(task);
        try{
            if(scheduler.isStarted())
                taskService.scheduleUpdateOneJobWithTrigger(scheduler, task);
        }catch (SchedulerException e){
            e.printStackTrace();
        }
        logger.warn("--/task/update--{}:",status);
        if(status == 1)
            return "Update Success!";
        else
            return "Update failure!";
    }

    public Task getTaskByRequest(){
        return new Task(
                request.getParameter("task_group"),
                request.getParameter("job_name"),
                request.getParameter("trigger_name"),
                request.getParameter("cron_expression"),
                request.getParameter("app_address"));
    }


}
