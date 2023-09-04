package com.atguigu.springcloud.service.access;


//import com.alibaba.fastjson.JSON;
//import com.saicmaxus.pp.invest.manager.model.WebResult;
//import com.saicmaxus.pp.invest.manager.utils.RetrofitUtil;
//import com.saicmaxus.pp.invest.service.api.PostRequestInterface;
//import com.saicmaxus.pp.invest.service.api.model.AccessLogDto;
//import com.saicmaxus.pp.invest.service.api.model.PpCostPoInfoDTO;
//import com.saicmaxus.pp.invest.web.util.ContextStore;
//
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AccessLogThreadService  {
//    private String ppUrl = "";
//    private PostRequestInterface requestInterface = null;
//    public  AccessLogThreadService(String ppUrl){
//        this.ppUrl = ppUrl;
//        Map map = new HashMap<String,String>();
//        map.put("Content-Type", "application/json");
//        map.put("Accept", "application/json-api");
//        Retrofit retrofit = RetrofitUtil.createRetrofit(map, ppUrl);
//        requestInterface = retrofit.create(PostRequestInterface.class);
//
//    }
//

    @PostConstruct
    public  void startThread(){
        Thread t2 = new Thread(new AccessLog100Thread());
        t2.start();
    }



    private final  class AccessLog100Thread implements Runnable {
        @Override
        public void run() {
//            if(epDataUrl==null){
//                AppContext.setUserInfo(new ContextUserInfo(USER_ESB_ID, "ESB_USER", "ESB_USER",100L, "SYSTEM", null));
//                epDataUrl = ConfigManager.getValue("remote.EP_DATA_API_ADDRESS", String.class);
//            }
            List<AccessLogDto> dto = new ArrayList<>();
            while(true) {
                try {
                    while(ContextStore.ACCESS_LOG_QUEUE.size()>0){
                        AccessLogDto accessLogDto = ContextStore.ACCESS_LOG_QUEUE.poll();
                        dto.add(accessLogDto);
                    }
                     //AccessLogDto accessLogDto = ContextStore.ACCESS_LOG_QUEUE.poll();
                     //QueryEpDataApi queryEpDataApi = SpringBeanUtils.getBean("queryEpDataApi");
                     //List<Map<String,Object>> list =  queryEpDataApi.accessLogCreateList(dto);

                    if(!CollectionUtils.isEmpty(dto)) {
                       /* CloseableHttpClient client = HttpClientBuilder.create().build();
                        RequestConfig requestConfig = RequestConfig.custom()
                                .setSocketTimeout(10000)//服务器连接超时
                                .setConnectTimeout(10000)//客户端从服务器读取数据的timeout
                                .build();

                        HttpPost httpPost = new HttpPost(ppUrl + "/ep-data-server/epdataserver/coding/accessLog/createList");
                        httpPost.setHeader("Content-Type", "application/json");
                        httpPost.setHeader("Accept", "application/json-api");
                        httpPost.setConfig(requestConfig);
                        StringEntity entity = new StringEntity(JSON.toJSONString(dto), "UTF-8");
                        httpPost.setEntity(entity);

                        HttpResponse response = client.execute(httpPost);
                        HttpEntity httpEntity = response.getEntity();
                        String result = EntityUtils.toString(httpEntity, "UTF-8");*/

                       //Response<List<Map<String, Object>>> execute  =  requestInterface.addAccessLog(dto).execute();
                        //List<Map<String, Object>> result = execute.body();
                        log.info("循环List:{}",JSON.toJSONString(dto));

                    }
                   // HttpClient httpClient = new DefaultHttpClient();
//                    String json = httpClient.postJson(  getBaseUrl()+
//                            "ep-pp-invest-api/meeting/bpmFeedback", JSON.toJSONString(bpmRequest, SerializerFeature.WriteMapNullValue));
//                    log.info("Invest-api返回报文:"+json);
//                    JSONObject jo = JSON.parseObject(json);
//                    Boolean success = jo.getBoolean("success");
//                    flag =success;
                   //queryEpDataApi.accessLogCreate(accessLogDto);
                   // if (!StringUtils.isEmpty(accessLogDto.getRequestUrl())) {
//                        WebClient client = ApiClientUtil.getJsonClient(epDataUrl+ "ep-data-server/epdataserver/coding/accessLog/create");
//                        client.header("Content-Type", "application/json");
//                        client.header("Accept", "application/json-api");
//                        client.post(accessLogDto);
//                        HttpClient httpClient = new HttpClient();
//                        httpClient.postJson(epDataUrl + "ep-data-server/epdataserver/coding/accessLog/create", JSON.toJSONString(accessLogDto));
                   // }
                    //20秒循环一次
                    Thread.sleep(20000);
                    log.info("20秒循环一次");
                } catch (Throwable e) {
                    e.printStackTrace();
                }finally{
                    dto.clear();
                }
            }
        }
    }

}
