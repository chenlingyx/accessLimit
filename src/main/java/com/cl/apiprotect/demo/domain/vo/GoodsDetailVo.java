package com.cl.apiprotect.demo.domain.vo;

import com.cl.apiprotect.demo.domain.eo.MiaoshaUser;
import lombok.Data;

/***
 * @ClassName: GoodsDetailVo
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:08
 * @version : V1.0.0
 */
@Data
public class GoodsDetailVo {

    private int miaoshaStatus = 0;
    private int remainSeconds = 0;
    private GoodsVo goods;
    private MiaoshaUser user;

}
