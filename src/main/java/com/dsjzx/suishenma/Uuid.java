package com.dsjzx.suishenma;

import com.alibaba.fastjson.JSONObject;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Uuid extends UDF {

    public static String evaluate(String xm, String zjhm,String date) throws Exception {

        String url = "https://172.27.148.121/xid/api/labelgen";
        //memId
        //appId
        //regCode
        //idType
        //id
        //signature
        String memberId = "0000102";
        String appId = "10SJ1803091031127556";
        String regCode = "DK2UxmyhotDAmK7oiwhIeqoG5R4Z5HDVnetLvL0Ycg4KA318FNMzDeRsaEvltZwM";
        String idType = "ID010107";
        String id = xm + zjhm+date;


        String signature = MD5("4e78c74eb1c0b0ea8a5a56a3451be53b" + memberId + appId + regCode + idType + id);


        // 拼接请求参数Request（JSON格式）
        JSONObject req = new JSONObject();
        req.put("memId", memberId);
        req.put("appId", appId);
        req.put("regCode", regCode);
        req.put("idType", idType);
        req.put("id", id);
        req.put("signature", signature);

        // 调用httpPost请求返回JSON格式Response
        HttpPost httpPost = new HttpPost();
        //JSONObject response =  httpPost(url, req, false);

        HttpClient client = new SSLClient();
        HttpPost request = new HttpPost(url);

        StringEntity stringEntity = new StringEntity(req.toJSONString(), "UTF-8");
        stringEntity.setContentType("application/json");
        System.out.println("stringEntity :" +  stringEntity.getContentLength());

        request.setEntity(stringEntity);
        request.setHeader("Accept","application/json");

        HttpResponse response;
        JSONObject object = null;
        String resXid = "";


        try {
            //System.out.println("request : "+request.getEntity().getContent().toString());

            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity, "UTF-8");
                System.out.println(result);
                object = JSONObject.parseObject(result);
                resXid = object.get("resXID").toString();
                System.out.println(resXid);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resXid;
    }



    public static String MD5(String input) {
        if (input == null || input.length() == 0) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            byte[] byteArray = md5.digest();

            BigInteger bigInt = new BigInteger(1, byteArray);
            // 参数16表示16进制
            String result = bigInt.toString(16);
            // 不足32位高位补零
            while (result.length() < 32) {
                result = "0" + result;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }




    public static void main(String[] args) throws Exception {
        String memberId = "0000102";
        String appId = "10SJ1803091031127556";
        String regCode = "DK2UxmyhotDAmK7oiwhIeqoG5R4Z5HDVnetLvL0Ycg4KA318FNMzDeRsaEvltZwM";
        String idType = "ID010107";
        String id = "叶佳伟32128319950405563720200520";
        String s = MD5("4e78c74eb1c0b0ea8a5a56a3451be53b" + memberId + appId + regCode + idType + id);
        String xid = evaluate("叶佳伟", "321283199504055637","20200520");
        System.out.println(xid);
    }


}
