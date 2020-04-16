package com.wj.manager;

import com.alibaba.fastjson.JSON;
import com.wj.manager.common.properties.MBSecurityProperties;
import com.wj.manager.common.util.SpringContextHolder;
import com.wj.manager.security.SecurityKit;
import com.wj.manager.security.sevice.AuthService;
import com.wj.manager.security.vo.AuthToken;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SecurityTest {



    @Test
    public void testService(){
        AuthService bean = SpringContextHolder.getBean(AuthService.class);
        try{
            AuthToken authToken = bean.login("boss", "111111");
            System.out.println(authToken.getJwtToken());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * 生成jwt令牌
     */
    @Test
    public void testCreateJwt(){
        String keyStore = "mb.keystore";
        //密钥库的密码
        String keystore_password = "mbstorepass";
        ClassPathResource resource = new ClassPathResource(keyStore);

        //密钥别名
        String alias  = "mbkey";
        //密钥的访问密码
        String key_password = "mbpass";
        //密钥库
        KeyStoreKeyFactory keyFactory = new KeyStoreKeyFactory(resource, keystore_password.toCharArray());
        KeyPair keyPair = keyFactory.getKeyPair(alias, key_password.toCharArray());
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
        //自定义属性
        Map<String,String> body = new HashMap<>();
        body.put("name","test");
        body.put("role","Admin");
        String bodyString = JSON.toJSONString(body);
        //RSA非对称签名算法RsaSigner，用私钥签名
        Jwt jwt = JwtHelper.encode(bodyString, new RsaSigner(privateKey));
        //生成jwt令牌编码
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }

    //校验jwt令牌
    @Test
    public void testVerify(){
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp7mF/7WCQs68BA06qNezfV8QKpSpBui5YcuKJj8bxmFtxp6guUH2Nk3Y+isngbOtx9c7oN+7zAsekiah3phiv7rUIXg3i/HLiu0V28xLBYrjHGHc6sHIdY+slRN+pwG6uknl82BuqwsXBBpX8Dpyvk6ABB+ud2zPySAeyz3KFwnhgVJs3PFurh6/H5Ci63Xan/Z8LiS1Ff47buACLtG3LAan1t3hqGa2PeriHbW7WbT4yUxT2KQcP9WRDOgzn3iY+7R2sm+gDDtQNJk+mq3hpGQ+qLaNjDRNMT5EdZZboO7ZljpO241JervXpeNFUaojHSR5wO8uKkrAuZyYlbS8uQIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJib3NzIiwic2NvcGUiOlsiYWxsIl0sIm5hbWUiOiJzZGFzZGFzZCIsImV4cCI6MTU1MjgxNzc4NCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIwOWU0NDMxYS03MTkzLTQxNmYtOGE3NS01OTUxN2FiZmMzN2UiLCJjbGllbnRfaWQiOiJ0ZXN0In0.il1X4OaC2myVqCFQ9hGTxVC_QCTZDdjdbWu6Zexlz2PO_mzbeoLMdVgyTNhG5mu2HXGN76K6JH40eTOHktDK16D-qlVV7skpREetV4li2lTjurSgYCxY8kkXuTlL5NUwIsqFftNW-maSzhY2dIDKHVRyALgYjw7CqsRlII2LhFY8vetRsBJkrGvZfmyJ10kmCUdSEwJOBLCpMSYhbFHA386AoTai84IukMhI8P2ClJwafd_co-fIBLbfQOd7ocq5-MUALpqjknMBjS5kB1biP-Y4gaNohxJXGbGLOT7ebobCH7xxjmAVoV-detJVW9LxV1CX_uknZbkzIpSsZDtcPg";
        //校验jwt令牌并解析令牌，报错则说明校验不通过
        Jwt jwt = JwtHelper.decodeAndVerify(jwtString, new RsaVerifier(publickey));
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        System.out.println(claims);
        String format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()+1552817784));
        System.out.println(format);
    }

    @Test
    public void testVerify2(){
        //公钥
        String publickey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp7mF/7WCQs68BA06qNezfV8QKpSpBui5YcuKJj8bxmFtxp6guUH2Nk3Y+isngbOtx9c7oN+7zAsekiah3phiv7rUIXg3i/HLiu0V28xLBYrjHGHc6sHIdY+slRN+pwG6uknl82BuqwsXBBpX8Dpyvk6ABB+ud2zPySAeyz3KFwnhgVJs3PFurh6/H5Ci63Xan/Z8LiS1Ff47buACLtG3LAan1t3hqGa2PeriHbW7WbT4yUxT2KQcP9WRDOgzn3iY+7R2sm+gDDtQNJk+mq3hpGQ+qLaNjDRNMT5EdZZboO7ZljpO241JervXpeNFUaojHSR5wO8uKkrAuZyYlbS8uQIDAQAB-----END PUBLIC KEY-----";
        //jwt令牌
        String jwtString = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJub3RlIjoiMWY3YmYiLCJhdWQiOlsicmVzdHNlcnZpY2UiXSwidXNlcl9uYW1lIjoiYm9zcyIsInNjb3BlIjpbImFsbCJdLCJpZCI6NDUsImV4cCI6MTU1MzIzMjQ3NCwiYXV0aG9yaXRpZXMiOlsiL21nci9yZXNldCIsIi9tZW51L2FkZCIsIiMiLCIvbWdyL3VuZnJlZXplIiwiL3JvbGUvZWRpdCIsIi9tZW51L2VkaXQiLCIvbWdyL2ZyZWV6ZSIsIi9tZ3IvYWRkIiwiL21lbnUiLCIvcm9sZS9zZXRBdXRob3JpdHkiLCIvbWVudS9saXN0IiwiL3JvbGUvYWRkIiwiL21nci9kZWxldGUiLCIvcm9sZSIsIi9tZ3IvZWRpdCIsIi9tZW51L21lbnVfZWRpdCIsIi9tZ3Ivc2V0Um9sZSIsIi9yb2xlL3JlbW92ZSIsIi9tZW51L3JlbW92ZSIsIi9tZ3IiXSwianRpIjoiMGMwYzg0ZjYtOTVhNi00YWI5LWIyM2UtZjk0NTU2YWRjY2NjIiwiY2xpZW50X2lkIjoidGVzdCJ9.jspGE7CJizzjFc1cEBhkjFFi52KUYEXWAaCK6XVuGHg";
        //校验jwt令牌并解析令牌，报错则说明校验不通过
        Jwt jwt = JwtHelper.decode(jwtString);
        //拿到jwt令牌中自定义的内容
        String claims = jwt.getClaims();
        JsonParser objectMapper = JsonParserFactory.create();
        Map<String, Object> map = objectMapper.parseMap(claims);
        System.out.println(claims);
    }




    /**
     * 通过网络远程调用/oauth/token生成token
     * 会2此调用UserDetailsServiceImpl得loadUserByUsername()方法，第一次先用client得id和secret登陆，
     * 第二次再用用户得用户名和密码登陆详情
     */
    @Test
    public void testNetGetJwt(){
        //restTemplate用于发送远程请求
        RestTemplate restTemplate = new RestTemplate();
        //SpringSecurity认证错误时，错误码为401或400，restTemplate发现错误就不执行，拿不回结果，
        //所以得设置错误处理setErrorHandler
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode() != 400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        String url = "http://localhost:8080/manager-boot/oauth/token";

        //头部带的参数
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String basic = getBasic("test", "test");
        header.add("Authorization",basic);

        //body带的参数
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username","312sda1");
        body.add("password","1111");
        body.add("scope","all");


        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, header);

        ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        Map bodyMap = exchange.getBody();
        System.out.println(exchange);
    }

    /**
     * 访问/oauth/token时，头部要带上base64编码后的clientId和clientSecret
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String getBasic(String clientId,String clientSecret){
        String value = clientId+":"+clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic "+new String(encode);
    }
    @Autowired
    MBSecurityProperties properties;
    @Test
    public void testPro(){
        System.out.println(properties.getGetTokenUrl());
        System.out.println(properties.getRedirectUris());

    }


    /**
     * 用URL方式发送post请求，与testNetGetJwt()中restTemplate同样可达到效果
     *
     */
    @Test
    public void testURLPOST(){
        try {
            String url11 = "http://localhost:8080/manager-boot/oauth/token";
            //传递参数
            String Parma = "?cardType={}&cardID={}";

            URL url = new URL(url11);
            // 将url 以 open方法返回的urlConnection  连接强转为HttpURLConnection连接  (标识一个url所引用的远程对象连接)
            // 此时cnnection只是为一个连接对象,待连接中
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置连接输出流为true,默认false (post 请求是以流的方式隐式的传递参数)
            connection.setDoOutput(true);
            // 设置连接输入流为true
            connection.setDoInput(true);
            // 设置请求方式为post
            connection.setRequestMethod("POST");
            // post请求缓存设为false
            connection.setUseCaches(false);
            // 设置该HttpURLConnection实例是否自动执行重定向
            connection.setInstanceFollowRedirects(true);
            // 设置请求头里面的各个属性 (以下为设置内容的类型,设置为经过urlEncoded编码过的from参数)
            // application/x-javascript text/xml->xml数据 application/x-javascript->json对象 application/x-www-form-urlencoded->表单数据
            // ;charset=utf-8 必须要，不然妙兜那边会出现乱码【★★★★★】
            //addRequestProperty添加相同的key不会覆盖，如果相同，内容会以{name1,name2}
            //connection.addRequestProperty("from", "sfzh");  //来源哪个系统
            //setRequestProperty添加相同的key会覆盖value信息
            //setRequestProperty方法，如果key存在，则覆盖；不存在，直接添加。
            //addRequestProperty方法，不管key存在不存在，直接添加。
           // connection.setRequestProperty("user", "user");  //访问申请用户
            //InetAddress address = InetAddress.getLocalHost();
            //String ip=address.getHostAddress();//获得本机IP
            //connection.setRequestProperty("ip",ip);  //请求来源IP

            String basic = getBasic("test", "test");
            connection.setRequestProperty("Authorization", basic);
           // connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            // 建立连接 (请求未开始,直到connection.getInputStream()方法调用时才发起,以上各个参数设置需在此方法之前进行)
            connection.connect();
            // 创建输入输出流,用于往连接里面输出携带的参数,(输出内容为?后面的内容)
            DataOutputStream dataout = new DataOutputStream(connection.getOutputStream());
            // 格式 parm = aaa=111&bbb=222&ccc=333&ddd=444
          /*  body.add("grant_type","password");
            body.add("username","312sda1");
            body.add("password","1111");
            body.add("scope","all");*/
            String parm ="grant_type=password&username=3123&password=111&scope=all";
            System.out.println("传递参数："+parm);
            // 将参数输出到连接
            dataout.writeBytes(parm);
            // 输出完成后刷新并关闭流
            dataout.flush();
            dataout.close(); // 重要且易忽略步骤 (关闭流,切记!)
            //System.out.println(connection.getResponseCode());
            // 连接发起请求,处理服务器响应  (从连接获取到输入流并包装为bufferedReader)
            BufferedReader bf = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder(); // 用来存储响应数据

            // 循环读取流,若不到结尾处
            while ((line = bf.readLine()) != null) {
                //sb.append(bf.readLine());
                sb.append(line).append(System.getProperty("line.separator"));
            }
            bf.close();    // 重要且易忽略步骤 (关闭流,切记!)
            connection.disconnect(); // 销毁连接
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
