package com.cl.apiprotect.demo.rabbitmq;

import com.cl.apiprotect.demo.config.MQConfig;
import com.cl.apiprotect.demo.domain.eo.MiaoshaOrder;
import com.cl.apiprotect.demo.domain.eo.MiaoshaUser;
import com.cl.apiprotect.demo.domain.vo.GoodsVo;
import com.cl.apiprotect.demo.redis.RedisService;
import com.cl.apiprotect.demo.redis.impl.RedisServiceImpl;
import com.cl.apiprotect.demo.service.GoodsService;
import com.cl.apiprotect.demo.service.MiaoshaService;
import com.cl.apiprotect.demo.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***
 * @ClassName: MQReciver
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/13 23:59
 * @version : V1.0.0
 */
@Service
public class MQReciver {


    private static Logger log = LoggerFactory.getLogger(MQReciver.class);

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage mm = RedisServiceImpl.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        final MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }
}
