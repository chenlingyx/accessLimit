package com.cl.apiprotect.demo.rabbitmq;

import com.cl.apiprotect.demo.domain.eo.MiaoshaUser;

/***
 * @ClassName: MiaoshaMessage
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:04
 * @version : V1.0.0
 */
public class MiaoshaMessage {

    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
