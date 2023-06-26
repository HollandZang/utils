class Solution {

    public static void main(String[] args) {
        s += s1;

        for (String s2 : s.split("\n")) {
            final String[] split = s2.split("\t");
            String a = split[0];
            String c = a;
            String b = split[1];
            a = a.toUpperCase().charAt(0) + a.substring(1);
            if (b.equals("Double")) b = "float64";
            if (b.equals("String")) b = "string";
            if (b.equals("int")) b = "int32";
            /*打印结构体*/
//            System.out.printf("%s %s `json:\"%s\"`\n", a, b, c);

            /*打印转Map方法*/
            System.out.printf("parameters[\"%s\"] = callback.%s\n", c, a);
        }
    }

    static String s = "amount\tDouble\t必须\tSDK\t成交金额，单位元\n" +
            "gameOrder\tSring\t必须\t游戏\t游戏订单号\n" +
            "orderNo\tString\t必须\tSDK\t整合sdk订单号\n" +
            "status\tint\t必须\tSDK\t交易状态，0表示成功\n" +
            "selfDefine\tString\t可选\t游戏\t透传参数\n" +
            "channelUid\tString\t必须\tSDK\t渠道的用户id\n" +
            "payTime\tString\t必须\tSDK\t交易时间yyyy-MM-dd HH:mm:ss\n" +
            "channel\tString\t必须\tSDK\t渠道类型\n" +
            "channelId\tint\t必须\tSDK\t渠道ID\n" +
            "goodsId\tString\t必须\tSDK\t渠道的档位ID\n" +
            "goodsName\tString\t必须\tSDK\t商品名称\n";
    static String s1 = "yx_is_in_intro_offer_period\tString\t可选\tSDK\t订阅：是否处于介绍价格期仅英雄国内iOS渠道下有该字段\n" +
            "yx_is_trial_period\tString\t可选\tSDK\t订阅: 是否处于免费试用期 仅英雄国内iOS渠道下有该字段\n" +
            "iap_sub_expire\tString\t可选\tSDK\t订阅：过期时间 仅英雄国内iOS渠道、华为渠道下有该字段 示例 2021-07-18 18:39:58\n" +
            "iap_sub\tString\t可选\tSDK\t订阅：是否是订阅仅英雄国内渠道iOS、华为渠道下有该字段\n" +
            "paytype\tString\t可选\tSDK\t支付方式类型 仅英雄国内渠道、华为渠道下有该字段\n" +
            "yx_sub_type\tString\t可选\tSDK\t支付方式子类型 仅英雄国内渠道下有该字段\n" +
            "dealAmount\tString\t可选\tSDK\t使用了英雄SDK代金券实际支付的金额 仅英雄国内渠道、华为渠道下有该字段\n" +
            "qkChannelId\tString\t可选\tSDK\tquicksdk的id\n" +
            "quickChannelId\tString\t可选\tSDK\tquicksdk的id 等于 qkChannelId 值一样\n" +
            "sandbox\tString\t可选\tSDK\t是否是沙盒 等于 仅英雄(国内/全球)渠道、华为渠道下有该字段\n" +
            "iapSub\tString\t可选\tSDK\t订阅：是否是订阅仅英雄全球iOS渠道下有该字段\n" +
            "iapSubExpire\tString\t可选\tSDK\t订阅：订阅过期时间 仅英雄全球iOS渠道下有该字段\n" +
            "currency\tString\t可选\tSDK\t币种 仅英雄全球渠道下有该字段\n" +
            "payType\tString\t可选\tSDK\t支付方式类型 仅英雄全球渠道下有该字段";
}