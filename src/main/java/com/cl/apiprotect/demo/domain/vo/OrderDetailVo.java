package com.cl.apiprotect.demo.domain.vo;

import com.cl.apiprotect.demo.domain.eo.OrderInfo;
import lombok.Data;
@Data
public class OrderDetailVo {
	private GoodsVo goods;
	private OrderInfo order;
}
