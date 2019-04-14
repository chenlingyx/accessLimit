package com.cl.apiprotect.demo.rabbitmq;

import com.cl.apiprotect.demo.config.MQConfig;
import com.cl.apiprotect.demo.redis.impl.RedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @ClassName: MQSender
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:03
 * @version : V1.0.0
 */
@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(MQReciver.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage mm) {
        String msg = RedisServiceImpl.beanToString(mm);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    }
}
