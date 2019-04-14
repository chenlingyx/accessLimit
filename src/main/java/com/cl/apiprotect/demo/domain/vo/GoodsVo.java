package com.cl.apiprotect.demo.domain.vo;

import com.cl.apiprotect.demo.domain.eo.Goods;
import lombok.Data;

import java.util.Date;

/***
 * @ClassName: GoodsVo
 * @Description:TODO
 * @author: chenling
 * @Date: 2019/4/14 0:09
 * @version : V1.0.0
 */
@Data
public class GoodsVo  extends Goods {

    private Double miaoshaPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
