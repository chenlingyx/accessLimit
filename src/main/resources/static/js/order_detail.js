function render(detail){
    var goods = detail.goods;
    var order = detail.order;
    $("#goodsName").text(goods.goodsName);
    $("#goodsImg").attr("src", goods.goodsImg);
    $("#orderPrice").text(order.goodsPrice);
    $("#createDate").text(new Date(order.createDate).format("yyyy-MM-dd hh:mm:ss"));
    var status = "";
    if(order.status === 0){
        status = "未支付"
    }else if(order.status === 1){
        status = "待发货";
    }
    $("#orderStatus").text(status);

}

$(function(){
    getOrderDetail();
});

function getOrderDetail(){
    var orderId = g_getQueryString("orderId");
    $.ajax({
        url:"/order/detail",
        type:"GET",
        data:{
            orderId:orderId
        },
        success:function(data){
            if(data.code === 0){
                render(data.data);
            }else{
                layer.msg(data.msg);
            }
        },
        error:function(){
            layer.msg("客户端请求有误");
        }
    });
}