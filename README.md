# 模拟电商秒杀
### 项目说明

利用SpringBoot简单的模仿电商秒杀项目的设计

1. Spring Boot环境搭建
2. 集成Thymeleaf，Result结果封装
3. 集成Mybatis+Druid
4. 集成Jedis+Redis安装+通用缓存Key封装

### Demo

测试账号：18786705103

测试密码：123456

### 秒杀系统的设计

> 秒杀的核心问题就是**极高并发处理**，库存只有一份或者几份，所有人会在集中的时间读和写这些数据。由于系统要在瞬时承受平时数十倍甚至上百倍的流量，这往往超出系统上限，因此处理秒杀的**核心思路是流控和性能优化**。

### 秒杀优化的方向

1. 将请求尽量拦截在系统上游：秒杀系统之所以挂，请求都压倒了后端数据层，数据读写锁冲突严重，并发高响应慢，几乎所有请求都超时，流量虽大，下单成功的有效流量甚小。
2. 充分利用缓存：这是一个典型的读多写少的应用场景【一趟火车其实只有2000张票，200w个人来买，最多2000个人下单成功，其他人都是查询库存，写比例只有0.1%，读比例占99.9%】，非常适合使用缓存
3. 并发性上不去是因为当多个线程同时访问一行数据时，产生了事务，因此产生写锁，每当一个获取了事务的线程把锁释放，另一个排队线程才能拿到写锁，QPS(Query Per Second每秒查询率)和事务执行的时间有密切关系，事务执行时间越短，并发性越高，这也是要将费时的I/O操作移出事务的原因。

### 秒杀的具体细节

1. 前端优化（请求流量拦截）

   > 对于高并发秒杀的过程中大量的用户访问，很多次点击是重复，所以要对访问的接口限制流量，此功能可以在前端JS实现，也可以在后台实现，后台具体思路：将用户的访问请求对应在redis设置数量，没访问一次就增加数量，设置缓存的时间为1分钟，1分钟内访问次数达到5次即限制访问。

   具体代码实现

   ```java
   		   int seconds = accessLimit.seconds();
               int maxCount = accessLimit.maxCount();
               boolean needLogin = accessLimit.needLogin();
               String key = request.getRequestURI();
               if (needLogin) {
                   if (user == null) {
                       render(response, CodeMsg.SESSION_ERROR);
                       return false;
                   }
                   key += "_" + user.getId();
               } else {
                   //do nothing
               }
               AccessKey ak = AccessKey.withExpire(seconds);
               Integer count = redisService.get(ak, key, Integer.class);
               if (count == null) {
                   redisService.set(ak, key, 1);
               } else if (count < maxCount) {
                   redisService.incr(ak, key);
               } else {
                   render(response, CodeMsg.ACCESS_LIMIT_REACHED);
                   return false;
               }
   ```

2. 接口验证码（减流量）

   > 秒杀的峰值时瞬时涌入的流量极大，为了减轻对后台服务器的请求，可以在前端抢购时候添加图片验证码，将集中的流量分散，减少峰值流量。思路是添加生成验证码的接口，在获取秒杀路径的时候，验证验证码。

   ```java
   	/**
        * 校验输入的验证码是否正确
        *
        * @param user
        * @param goodsId
        * @param verifyCode
        * @return
        */
       public boolean checkVerifyCode(MiaoshaUser user, long goodsId, int verifyCode) {
           if (user == null || goodsId <= 0) {
               return false;
           }
           Integer codeOld = redisService.get(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId, Integer.class);
           if (codeOld == null || codeOld - verifyCode != 0) {
               return false;
           }
           redisService.delete(MiaoshaKey.getMiaoshaVerifyCode, user.getId() + "," + goodsId);
           return true;
       }
   ```

   3. 秒杀接口地址隐藏

   > 秒杀开始之前，先去请求接口获取秒杀地址, 防止恶意利用端口抢购服务器，请求秒杀接口时候必须带上path的Token和验证码信息才能正常的进行秒杀。

```java
 public String createMiaoshaPath(MiaoshaUser user, long goodsId) {
        if (user == null || goodsId <= 0) {
            return null;
        }
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisService.set(MiaoshaKey.getMiaoshaPath, "" + user.getId() + "_" + goodsId, str);
        return str;
    }

	@AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping(value = "/path", method = RequestMethod.GET)
    @ResponseBody
    public Result<String> getMiaoshaPath(HttpServletRequest request, MiaoshaUser user,
                                         @RequestParam("goodsId") long goodsId,
                                         @RequestParam(value = "verifyCode", defaultValue = "0") int verifyCode
    ) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        boolean check = miaoshaService.checkVerifyCode(user, goodsId, verifyCode);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        String path = miaoshaService.createMiaoshaPath(user, goodsId);
        return Result.success(path);
    }
```

4. 前端页面静态化

   > 通过前端页面的静态化，服务请求响应靠Ajax完成，可以有效的将js，css，html页面与服务器分离，通过cdn服务将客户流量请求分散到cdn服务器网上。



5.后台服务请求的优化逻辑

> 上述的流控做法只能限制用户异常访问，如果正常访问的用户数量很多，就有后端系统压力过大甚至异常宕机的可能，因此需要后端系统流量控制。后端系统的访问限制可以通过异步处理、消息队列、并发限制等方式实现。核心思路是保证后端系统的压力维持在可以正常处理的水平。

我们采用的逻辑是利用redis缓存，把数据库的读操作放到redis之中，把库存的数量放入redis之中，通过预减库存的操作来减少后端系统的流量。

1. 系统初始化，把商品库存数量加载到Redis
2. 收到请求，Redis预减库存，库存不足，直接返回，否则进入3
3. 请求进入rabbitMq消息队列中，前台立即返回排队中
4. 请求出队，生成订单，数据库写入减少库存
5. 客户端向服务器接口轮询是否秒杀成功，是否秒杀成功

```java
	/**
     * @param model
     * @param user
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "/{path}/do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> miaosha(Model model, MiaoshaUser user,
                                   @RequestParam("goodsId") long goodsId,
                                   @PathVariable("path") String path) {
        model.addAttribute("user", user);
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        // 验证path
        boolean check = miaoshaService.checkPath(user, goodsId, path);
        if (!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }
        //内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over) {
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        // 预减库存---从缓存中取到库存
        long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock, "" + goodsId);
        if (stock < 0) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        //判断是否重复秒杀商品
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }
        // 入队
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        // 排队中
        return Result.success(0);
    }
	@RabbitListener(queues = MQconfig.QUEUE)
    public void receive(String message) {
        log.info("receive message:" + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if (stock <= 0) {
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
        if (order != null) {
            return;
        }
        //减库存 下订单 写入秒杀订单
        miaoshaService.miaosha(user, goods);
    }
```

6. 防止订单超卖问题

   > 任何商品都会有数量上限，**如何避免成功下订单买到商品的人数不超过商品数量的上限，这是每个抢购活动都要面临的难题。**

对于小库存的商品我们可以加锁采用数据库的悲观锁或者乐观锁来防止数据库的订单超卖。

这里采用事务和唯一索引的方式，在商品表对用户id和商品id添加唯一索引，同时在update表结构时候判断库存数量要大于一。具体的SQL语句如下：

```sql
CREATE TABLE `miaosha_order` (
`id` BIGINT ( 20 ) NOT NULL AUTO_INCREMENT,
`user_id` BIGINT ( 20 ) DEFAULT NULL,
`order_id` BIGINT ( 20 ) DEFAULT NULL,
`goods_id` BIGINT ( 20 ) DEFAULT NULL,
PRIMARY KEY ( `id` ),
UNIQUE KEY `u_uid_god` ( `user_id`, `order_id` ) USING BTREE 
) ENGINE = INNODB AUTO_INCREMENT = 6 DEFAULT CHARSET = utf8;

UPDATE miaosha_goods SET stock_count = stock_count - 1 WHERE goods_id = #{goodsId} AND stock_count > 0
```

