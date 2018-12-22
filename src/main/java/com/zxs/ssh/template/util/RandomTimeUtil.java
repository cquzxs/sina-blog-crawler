package com.zxs.ssh.template.util;

import java.util.Random;

/**
 * Created by zxs on 2018/12/22.
 * 创建随机时间，每次请求后都休息一会，休息时间即此创建的随机时间
 */
public class RandomTimeUtil {

    /**
     * 获取随机时间
     *
     * @return 随机时间
     */
    public static long randomTime(){
        long res = 0;
        //等待数据加载的时间
        //为了防止服务器封锁，这里的时间要模拟人的行为，随机且不能太短
        long waitLoadBaseTime = 1000;
        int waitLoadRandomTime = 300;
        Random random = new Random(System.currentTimeMillis());
        res = waitLoadBaseTime + random.nextInt(waitLoadRandomTime);
        return res;
    }

}