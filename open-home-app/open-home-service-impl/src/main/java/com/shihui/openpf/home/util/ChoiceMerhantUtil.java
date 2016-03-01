package com.shihui.openpf.home.util;



import java.util.*;

/**
 * Created by zhoutc on 2015/11/18.
 */
public class ChoiceMerhantUtil {

    /**
     * 从商品提供商户中选择一个服务商
     *
     * @param choiceMap 可选择商户信息   key-商户ID  value-商户权重
     * @return 选出的商户 -1表示没有找到商户
     */
    public static int choiceMerchant(Map<Integer, Integer> choiceMap) {

        Random random = new Random();
        int total = 0;
        if (choiceMap == null || choiceMap.size() == 0) return -1;

        if (choiceMap.size() == 1) {
            for (Map.Entry entry : choiceMap.entrySet()) {
                return (Integer) entry.getKey();
            }
        }

        List<String> rangeList = new ArrayList<>();
        for (Map.Entry entry : choiceMap.entrySet()) {
            Integer key = (Integer) entry.getKey();
            Integer value = (Integer) entry.getValue();
            total = total + value;
            String rangevalue = String.valueOf(key) + "," + String.valueOf(total);
            rangeList.add(rangevalue);
        }

        int randomNum = random.nextInt(total);
        for (String rangevalue : rangeList) {
            String[] values = rangevalue.split("\\,");
            int range = Integer.parseInt(values[1]);
            int merchantId = Integer.parseInt(values[0]);
            if (randomNum <= range) {
                return merchantId;
            }

        }
        return -1;


    }

    public static void main(String[] args) {
        try {
            Map<Integer, Integer> test = new HashMap<>();
            test.put(1, 1000);
            test.put(2, 2000);
            test.put(3, 5000);
            test.put(4, 9000);
            test.put(5, 10000);
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            int e = 0;
            for (int i = 0; i < 2000; i++) {

                int result = choiceMerchant(test);
                switch (result) {
                    case 1:
                        a++;
                        break;
                    case 2:
                        b++;
                        break;
                    case 3:
                        c++;
                        break;
                    case 4:
                        d++;
                        break;
                    case 5:
                        e++;
                        break;
                }
            }
            System.out.println("a:" + a + " b:" + b + " c:" + c + " d:" + d + " e:" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
