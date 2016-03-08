package com.longdai.task.controller;



import com.longdai.task.service.TaskService;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Copyright      Copyright (c) 2016,${year},
 * @Title
 * @Description 控制Scheduler的开启\关闭\暂停...
 *
 */
@RestController
public class TaskStatusController {

    @Resource
    HttpServletRequest request;
    @Resource(name = "uniScheduler")
    Scheduler scheduler;
    @Resource
    TaskService taskService;

    /**
     *  开启定时任务
     * @return
     */
    @RequestMapping("/startup")
    public String startup() {

        String message = "startup";
        try {
            System.out.println("scheduler.isInStandbyMode():"+scheduler.isInStandbyMode());
            System.out.println("scheduler.isStarted():"+scheduler.isStarted());
            if (scheduler.isInStandbyMode()) {//Standby Mode
                scheduler.start();
                taskService.scheduleAllJobs(scheduler);
                message = "Success: The Scheduler is in Standby-Mode, is being started...";
            } else{ //Shutdown Mode
                message = "Exception: The Scheduler is in Shutdown-Mode, can't be started!";
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        return message;
    }


    @RequestMapping("/standby")
    public String standby(){
        String message = "standby";
        try{
            if(scheduler.isStarted()){ //Started Mode
                scheduler.standby(); //使处于standby模式
                System.out.println("scheduler.isInStandbyMode():"+scheduler.isInStandbyMode());
                System.out.println("scheduler.isStarted():"+scheduler.isStarted());
                message = "Success: The Scheduler is in Started-Mode, is making it in Standby-Mode...";
            }else if (scheduler.isInStandbyMode()){// Standby Mode
                message = "Exception: The Scheduler is in Standby-Mode, can't be made in Standby-Mode again!";
            }else{//Shutdown Mode
                message = "Exception: The Scheduler is in Shutdown-Mode, can't be made in Standby-Mode!";
            }


        }catch (SchedulerException e){
            e.printStackTrace();
        }

        return message;
    }


    @RequestMapping("/shutdown")
    public String shutdown(){
        String message = "shutdown";
        try{
            if(scheduler.isStarted()){//Started Mode
                scheduler.shutdown(false);//true:等任务结束后停止;default\false:立即停止但等任务执行完;
                message = "Success: The Scheduler is in Started-Mode, is being shutdown...";
            }else if(scheduler.isInStandbyMode()){//Standby-Mode
                scheduler.shutdown(false);
                message = "Success: The Scheduler is in Standby-Mode, is being shutdown...";
            }else{//Shutdown-Mode
                message = "Exception: The Scheduler is in Shutdown-Mode, can't be shutdown again!";
            }

        }catch (SchedulerException e){
            e.printStackTrace();
        }

        return message;

    }

}




