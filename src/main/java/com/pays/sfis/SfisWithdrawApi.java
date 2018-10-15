package com.pays.sfis;

import com.pays.WithdrawApi;
import com.pays.WithdrawApiParam;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import sun.misc.BASE64Encoder;

public class SfisWithdrawApi
        extends WithdrawApi {
    private static Map<String, String[]> BANK_ADDRESS = new HashMap();

    static {
        BANK_ADDRESS.put("北京", new String[]{"北京"});
        BANK_ADDRESS.put("上海", new String[]{"上海"});
        BANK_ADDRESS.put("天津", new String[]{"天津"});
        BANK_ADDRESS.put("重庆", new String[]{"重庆"});
        BANK_ADDRESS.put("香港", new String[]{"香港"});
        BANK_ADDRESS.put("澳门", new String[]{"澳门"});
        BANK_ADDRESS.put("黑龙江", new String[]{"哈尔滨", "齐齐哈尔", "牡丹江", "大庆", "伊春", "双鸭山", "鹤岗", "鸡西", "佳木斯", "七台河", "黑河", "绥化", "大兴安岭"});
        BANK_ADDRESS.put("吉林", new String[]{"长春", "延吉", "吉林", "白山", "白城", "四平", "松原", "辽源", "大安", "通化"});
        BANK_ADDRESS.put("辽宁", new String[]{"沈阳", "大连", "葫芦岛", "盘锦", "本溪", "抚顺", "铁岭", "辽阳", "营口", "阜新", "朝阳", "锦州", "丹东", "鞍山"});
        BANK_ADDRESS.put("内蒙古", new String[]{"呼和浩特", "　呼伦贝尔", "锡林浩特", "包头", "赤峰", "海拉尔", "乌海", "鄂尔多斯", "通辽"});
        BANK_ADDRESS.put("河北", new String[]{"石家庄", "唐山", "张家口", "廊坊", "邢台", "邯郸", "沧州", "衡水", "承德", "保定", "秦皇岛"});
        BANK_ADDRESS.put("河南", new String[]{"郑州", "开封", "洛阳", "平顶山", "焦作", "鹤壁", "新乡", "安阳", "濮阳", "许昌", "漯河", "三门峡", "南阳", "商丘", "信阳", "周口", "驻马店"});
        BANK_ADDRESS.put("山东", new String[]{"济南", "青岛", "淄博", "威海", "曲阜", "临沂", "烟台", "枣庄", "聊城", "济宁", "菏泽", "泰安", "日照", "东营", "德州", "滨州", "莱芜", "潍坊"});
        BANK_ADDRESS.put("山西", new String[]{"太原", "阳泉", "晋城", "晋中", "临汾", "运城", "长治", "朔州", "忻州", "大同", "吕梁"});
        BANK_ADDRESS.put("江苏", new String[]{"南京", "苏州", "昆山", "南通", "太仓", "吴县", "徐州", "宜兴", "镇江", "淮安", "常熟", "盐城", "泰州", "无锡", "连云港", "扬州", "常州", "宿迁"});
        BANK_ADDRESS.put("安徽", new String[]{"合肥", "巢湖", "蚌埠", "安庆", "六安", "滁州", "马鞍山", "阜阳", "宣城", "铜陵", "淮北", "芜湖", "毫州", "宿州", "淮南", "池州"});
        BANK_ADDRESS.put("陕西", new String[]{"西安", "韩城", "安康", "汉中", "宝鸡", "咸阳", "榆林", "渭南", "商洛", "铜川", "延安"});
        BANK_ADDRESS.put("宁夏", new String[]{"银川", "固原", "中卫", "石嘴山", "吴忠"});
        BANK_ADDRESS.put("甘肃", new String[]{"兰州", "天水", "庆阳", "武威", "酒泉", "张掖", "白银", "定西", "平凉", "临夏", "金昌", "甘南"});
        BANK_ADDRESS.put("青海", new String[]{"黄南　西宁", "玉树", "海西", "海南", "海北", "海东", "果洛"});
        BANK_ADDRESS.put("湖北", new String[]{"黄石", "黄冈", "随州", "鄂州", "襄阳", "荆门", "荆州", "神农架", "武汉", "恩施", "宜昌", "孝感", "咸宁", "十堰"});
        BANK_ADDRESS.put("湖南", new String[]{"韶山", "长沙", "郴州", "邵阳", "衡阳", "益阳", "湘潭", "永州", "株洲", "怀化", "张家界", "常德", "岳阳", "娄底", "吉首"});
        BANK_ADDRESS.put("浙江", new String[]{"金华", "衢州", "舟山", "绍兴", "湖州", "温州", "杭州", "宁波", "嘉兴", "台州", "丽水"});
        BANK_ADDRESS.put("江西", new String[]{"鹰潭", "赣州", "萍乡", "景德镇", "新余", "抚州", "宜春", "吉安", "南昌", "九江", "上饶"});
        BANK_ADDRESS.put("福建", new String[]{"龙岩", "莆田", "福州", "漳州", "泉州", "宁德", "厦门", "南平", "三明"});
        BANK_ADDRESS.put("贵州", new String[]{"铜仁", "都匀", "遵义", "赤水", "贵阳", "毕节", "安顺", "凯里", "六盘水"});
        BANK_ADDRESS.put("四川", new String[]{"雅安", "阿坝", "遂宁", "达州", "资阳", "自贡", "绵阳", "眉山", "甘孜州", "泸州", "攀枝花", "成都", "德阳", "广安", "广元", "巴中", "宜宾", "南充", "凉山", "内江", "乐山"});
        BANK_ADDRESS.put("广东", new String[]{"韶关", "阳江", "茂名", "肇庆", "珠海", "潮州", "湛江", "清远", "深圳", "河源", "江门", "汕尾", "汕头", "梅州", "揭阳", "惠州", "德庆", "广州", "佛山", "云浮", "中山", "东莞"});
        BANK_ADDRESS.put("广西", new String[]{"阳朔", "防城港", "钦州", "贺州", "贵港", "百色", "玉林", "河池", "梧州", "桂林", "桂平", "柳州", "来宾", "崇左", "南宁", "北海"});
        BANK_ADDRESS.put("云南", new String[]{"红河", "玉溪", "楚雄", "曲靖", "昭通", "昆明", "文山", "思茅", "怒江", "德宏", "大理", "保山", "丽江", "临沧"});
        BANK_ADDRESS.put("海南", new String[]{"通什", "琼山", "海口", "文昌", "儋州", "三亚"});
        BANK_ADDRESS.put("新疆", new String[]{"阿勒泰", "阿克苏", "石河子", "昌吉", "库尔勒", "塔城", "喀什", "哈密", "和田", "吐鲁番", "克拉玛依", "伊宁", "乌鲁木齐"});
        BANK_ADDRESS.put("西藏", new String[]{"阿里", "林芝", "昌都", "日喀则", "拉萨", "山南", "那曲"});
        BANK_ADDRESS.put("台湾", new String[]{"高雄", "台北"});
    }

    protected void init() {
        addBank("ICBC", "ICBC");
        addBank("CMB", "CMB");
        addBank("CCB", "CCB");
        addBank("ABC", "ABC");
        addBank("BOC", "BOC");
        addBank("COMM", "BCM");
        addBank("CMBC", "CMBC");
        addBank("CITIC", "CNCB");
        addBank("SPDB", "SPDB");
        addBank("PSBC", "PSBC");
        addBank("CEB", "CEB");
        addBank("SPA", "PAB");
        addBank("GDB", "GDB");
        addBank("HXB", "HXB");
        addBank("CIB", "CIB");
        addBank("ADBC", "ADBC");
        addBank("RCU", "RCC");
        addBank("BEA", "HKBEA");
        addBank("BOB", "BOB");
        addBank("CBHB", "CBHB");
        addBank("CZB", "CZB");
        addBank("GZCB", "GZCB");
        addBank("HSB", "HSB");
        addBank("HZB", "HZB");
        addBank("NBCB", "NBCB");
        addBank("NJCB", "NJCB");
        addBank("SHB", "SHB");
    }

    public String encApply(Map<String, String> param, String key) {
        return null;
    }

    public String encConfirm(Map<String, String> param, String key) {
        return null;
    }

    public String encResult(Map<String, String> param, String key) {
        return enc((String) param.get("json"), key);
    }

    public boolean checkResult(String result) {
        try {
            JsonNode jn = new ObjectMapper().readTree(result);
            String status = jn.get("success").asText();
            if ("true".equals(status)) {
                return true;
            }
        } catch (Exception localException) {
        }
        return false;
    }

    public Map<String, String> getPostMap(WithdrawApiParam param, String key) {
        Map<String, String> map = new LinkedHashMap();
        try {
            String bankProvinces = "";
            String bankCity = "";
            String bankBranch = "";

            String bankAddress = param.getIssueBankAddress();
            for (String provinces : BANK_ADDRESS.keySet()) {
                if (bankAddress.startsWith(provinces)) {
                    bankProvinces = provinces;
                    bankAddress = bankAddress.substring(provinces.length());
                    String[] citys = (String[]) BANK_ADDRESS.get(provinces);
                    if (citys.length == 1) {
                        bankCity = citys[0];
                        break;
                    }
                    for (String city : citys) {
                        if (bankAddress.startsWith(city)) {
                            bankCity = city;
                            bankBranch = bankAddress.substring(city.length());
                            break;
                        }
                    }
                    break;
                }
            }
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode data = mapper.createObjectNode();
            data.put("module", "bankcard");
            data.put("method", "api_add_member_card");
            data.put("company_name", param.getMerchantCode());
            ObjectNode payload = mapper.createObjectNode();
            payload.put("card_number", param.getCardNum());
            payload.put("real_name", param.getCardName());
            payload.put("bank_city", bankCity);
            payload.put("bank_branch", bankBranch);
            payload.put("bank_flag", param.getBank());
            payload.put("bank_area", "中国");
            payload.put("bank_provinces", bankProvinces);
            payload.put("trans_mode", "out_trans");
            data.put("payload", payload);

            String addCardData = mapper.writeValueAsString(data);
            String addCardSign = enc(addCardData, key);

            data = mapper.createObjectNode();
            data.put("module", "order");
            data.put("method", "api_add_order");
            data.put("company_name", param.getMerchantCode());
            payload = mapper.createObjectNode();
            payload.put("card_number", param.getCardNum());
            payload.put("amount", param.getAmount());
            payload.put("order_number", param.getOrderId());
            payload.put("atfs_flag", "");
            payload.put("trans_mode", "out_trans");
            data.put("payload", payload);

            String addOrderData = mapper.writeValueAsString(data);
            String addOrderSign = enc(addOrderData, key);

            map.put("addCardData", addCardData);
            map.put("addCardSign", addCardSign);
            map.put("addOrderData", addOrderData);
            map.put("addOrderSign", addOrderSign);
        } catch (Exception localException) {
        }
        return map;
    }

    private String enc(String data, String key) {
        String HMAC_SHA1_ALGORITHM = "HmacSHA256";
        String sign;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.toString().getBytes("UTF-8"));
            sign = new BASE64Encoder().encode(rawHmac);
        } catch (Exception e) {
            //jd-gui
            //String sign;
            sign = "";
        }
        return sign;
    }

    public String getOrderId(Map<String, String> param) {
        try {
            return new ObjectMapper().readTree((String) param.get("json")).get("order_number").asText();
        } catch (Exception localException) {
        }
        return null;
    }

    public String getTradeNo(Map<String, String> param) {
        return getOrderId(param);
    }

    public boolean isSuccessAll(Map<String, String> param) {
        try {
            return "SUCCESS".equals(new ObjectMapper().readTree((String) param.get("json")).get("status").asText());
        } catch (Exception localException) {
        }
        return false;
    }

    public boolean isSuccessParts(Map<String, String> param) {
        return false;
    }
}
