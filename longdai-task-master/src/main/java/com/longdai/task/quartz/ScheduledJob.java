package com.longdai.task.quartz;

import com.squareup.okhttp.*;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Copyright: Copyright (c) 2016,${year},
 * @Description: ${todo}
 */

public class ScheduledJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);

    public ScheduledJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        OkHttpClient client = new OkHttpClient();
        String api = dataMap.getString("app_address");
        final String jobGroup = jobKey.getGroup();
        final String jobName = jobKey.getName();
        if(StringUtils.isEmpty(api)){
            logger.info("jobGroup"+jobGroup+"-jobName:"+jobName+"没有回调地址");
            return;
        }
        RequestBody formBody = new FormEncodingBuilder()
                .add("name","TaskScheduleServer")
                .add("task_server_port","9010")
                .add("job_name", jobKey.getName())
                .add("task_group",jobKey.getGroup())
                .build();

        Request request = new Request.Builder()
                .url(api)
                .post(formBody)
                .build();

        //--- 异步调用start ---
        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("服务器端错误: " + response);
                }
//                logger.info(response.body().string());
            }
        });
        logger.info("TaskScheduleServer:请求执行url:"+api
                +jobGroup+"_"+jobName+" --- Date:"
                +new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        //--- 异步调用end ---
    }
}
