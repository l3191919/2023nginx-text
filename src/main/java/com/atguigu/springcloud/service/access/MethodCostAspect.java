package com.atguigu.springcloud.service.access;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
@Aspect
public class MethodCostAspect implements Ordered {
//    @Autowired
//    UserService userService;
    protected final Logger log = LoggerFactory.getLogger(MethodCostAspect.class);
    private static final String dataFrom = "invest";

    @Pointcut(
            "(within(com.atguigu.springcloud.controller.*))"
//              " && !execution( * com.autopuspp.sysmgmt.controller.CodeListController.findByType(..))"+//查询下拉菜单不进入切面
//              " && !execution( * com.autopuscustom.pp.trackingwall.TrackingWallController.*(..))"//final class报错
    )
//    @Pointcut("execution (* com.saicmaxus.pp.invest.web.controller.*.*(..))")
    public void managerMethods() {
    }

    @Around("managerMethods()")
    //@AfterReturning(value ="managerMethods()", returning="returnValue")
    public Object printMethodCost(ProceedingJoinPoint joinPoint) throws Throwable {
     log.info(joinPoint.getTarget().getClass().getName());
     HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
     String token = req.getHeader("user_token");
        String userId = "";
        String userName ="";
//        TUserDTO tUserDTO = null;
//     if(!StringUtils.isEmpty(token)) {
//         tUserDTO = userService.getUserThroughToken(token);
//         if(tUserDTO!=null) {
//             userId = tUserDTO.getId() == null ? "" : tUserDTO.getId().toString();
//             userName = tUserDTO.getUsername() == null ? "" : tUserDTO.getUsername();
//         }
//     }else{
//         log.warn("AccessLog: 尝试获取Token，没有获取到");
//
//     }

     return this.printMethodCost(joinPoint, dataFrom, userId, userName, "");
    }

    private Object printNothing(ProceedingJoinPoint joinPoint) throws Throwable{
        Object result = null;
        try{
            result = joinPoint.proceed();
        }catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
        return result;
    }

    @Override
    public int getOrder() {
        return -5;
    }

    private static final int StringMaxLen = 1995;



    public Object printMethodCost(ProceedingJoinPoint joinPoint, String dataFrom, String userId, String userName, String loginname) throws Throwable {
        StringBuilder sb = new StringBuilder("AccessLog => ");
        AccessLogDto accessLogDto = new AccessLogDto(dataFrom);
        try {
            String traceLogId = getTraceLogId();
            Long beginTime = System.currentTimeMillis();
            Long endTime;
            String methodCnName = "";
            String requestUrl = "";
            String param = "";
            String paramBody = "";
            String remark = "";
            String returnData ="";
            // before 日志处理
            try {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                HttpServletRequest request = requestAttributes==null?null:((ServletRequestAttributes) requestAttributes).getRequest();
                requestUrl = getRequestUri(joinPoint);
                methodCnName = getMethodNameZh(joinPoint);
                paramBody = getParam(request);
                returnData = getReturn(joinPoint);
                remark = getRemark(request);
                sb.append("request:").append(requestUrl).append(",");
                sb.append("methodName:").append(methodCnName).append(",");
                sb.append("beginTime:").append(getCurrentDate()).append(",");
                sb.append("remark:").append(remark).append(",");
                sb.append("userId:").append(userId).append(",");
                sb.append("userName:").append(userName).append(",");
                sb.append("loginName:").append(loginname).append(",");
                accessLogDto.setRequestUrl(requestUrl);
                accessLogDto.setMethodNameZh(methodCnName);
                accessLogDto.setRemark(remark);
                accessLogDto.setUserId(userId);
                accessLogDto.setLoginname(loginname);
                accessLogDto.setUserName(userName);
                accessLogDto.setBeginTime(getCurrentDate());
                accessLogDto.setParamBody(getReturnBody(joinPoint));

                //accessLogDto.setResult(returnData);
                //methodAfterReturing(proceed);



            } catch (Exception e) {
                accessLogDto.setResult("请求参数："+e.getLocalizedMessage());
                e.printStackTrace();
                log.warn("AccessLog: 【{}】- 尝试打印请求参数发生错误，无法获取获取用户信息及方法名称。", traceLogId);
            }

            // 调用业务功能
            Object result = null;
            try{
                result = joinPoint.proceed();
            }catch (Throwable e) {
                accessLogDto.setResult("业务功能异常："+e.getLocalizedMessage());
                e.printStackTrace();
                throw e;
            }
            sb.append("paramBody:").append(paramBody).append(",");
            accessLogDto.setParamBody(paramBody);

            // after 日志处理
            try {
                endTime = System.currentTimeMillis();
                // 将返回的结果result, 转成字符串存储
                String resStr = parseResult(result);
                // log
                accessLogDto.setEndTime(getCurrentDate());
                accessLogDto.setResult(resStr);
                accessLogDto.setConsumeTime(endTime - beginTime);
                sb.append("endTime:").append(getCurrentDate()).append(",");
                sb.append("result:").append(resStr).append(",");
                sb.append("consumeTime:").append(endTime - beginTime).append(",");
            } catch (Exception e) {
                log.error("===>AOP:{} 当前请求发生错误错误，异常信息:{}", traceLogId, e);
                e.printStackTrace();
                accessLogDto.setResult("解析结果："+e.getLocalizedMessage());
            }

            return result;
        }finally {
            try {
                log.info(sb.toString());
                sb.setLength(0);
                sb = null;
                if (!isNull(accessLogDto.getRequestUrl())) {

//                    HttpClient httpClient = new HttpClient();
//                    httpClient.postJson(ConfigManager.getValue("remote.EP_DATA_API_ADDRESS", String.class)+"ep-data-server/epdataserver/coding/accessLog/create",JSON.toJSONString(accessLogDto));
                    ContextStore.addAccessLog(accessLogDto);

                }
                accessLogDto = null;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * parse 结果集
     * @param result
     * @return
     */
    private String parseResult(Object result) {
        if (result==null) return "";
        String s = "";
        try{
            try {
                s = JSON.toJSONString(result);
            }catch (Exception e){
                s = result.toString();
            }
            if (!isNull(s) && s.length() >= StringMaxLen) {
                s = s.substring(0, StringMaxLen).concat("...");
            }
        }catch (Exception e){
            s = e.getLocalizedMessage();
        }
        //return s;
        return s;
    }

    private boolean isNull(String s) {
        if (s==null) return true;
        if (s.trim().length()==0) return true;
        return false;
    }

    private String getCurrentDate() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(new Date());
    }

    private String getMethodNameZh(ProceedingJoinPoint joinPoint) {
        String methodCnName = "";
        try{
            methodCnName = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(ApiOperation.class).value();
        }catch (Exception e){
        }
        return methodCnName;
    }

    private String getRequestUri(ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String methodName = joinPoint.getSignature().getName();
        String requestUrl = className + "." + methodName;
        return requestUrl;
    }


    private String getTraceLogId() {
        String mills = String.valueOf(System.currentTimeMillis());
        String tail = String.valueOf((int) Math.round(Math.random() * (9999 - 1000) + 1000));
        return mills + tail;
    }

    private String getParam(HttpServletRequest request) {
        if (request==null) return "{\"request\":\"null\"}";

        Map<String, String[]> parameterMap = request.getParameterMap();
        String p1 = JSON.toJSONString(parameterMap);
        if (p1==null) p1 = "";
        if (p1.length()>=StringMaxLen) {
            p1 = p1.substring(0,StringMaxLen);
        }
        return p1;
    }

    private String getReturn(ProceedingJoinPoint joinPoint) throws Throwable {
        if (joinPoint.proceed()==null) return "{\"request\":\"null\"}";
        JSONObject json = JSON.parseObject(JSON.toJSONString(joinPoint.proceed()));

        String p1 = JSON.toJSONString(json.get("data"));
        if (p1==null) p1 = "";
        if (p1.length()>=StringMaxLen) {
            p1 = p1.substring(0,StringMaxLen);
        }
        return p1;
    }


    private String getParamBody(ProceedingJoinPoint joinPoint) {
        String p2 = JSON.toJSONString(joinPoint.getArgs());
        if (p2==null) p2 = "";
        if (p2.length()>=StringMaxLen) {
            p2 = p2.substring(0,StringMaxLen);
        }
        return p2;
    }
    private String getReturnBody(ProceedingJoinPoint joinPoint) {
        String p2 = JSON.toJSONString(joinPoint.getSignature());
        if (p2==null) p2 = "";
        if (p2.length()>=StringMaxLen) {
            p2 = p2.substring(0,StringMaxLen);
        }
        return p2;
    }

    private String getRemark(HttpServletRequest request) {
        if (request==null) return "{\"request\":\"null\"}";

        String userAgent = request.getHeader("User-Agent");
        String contentType = request.getHeader("Content-Type");

        JSONObject jo = new JSONObject();
        jo.put("userAgent", userAgent);
        jo.put("contentType", contentType);

        return jo.toJSONString();
    }

    /*
    private String getDataFromRequest(HttpServletRequest request, String traceLogId) {
        String type = request.getContentType();
        if (type == null) type = "application/x-www-form-urlencoded";
        StringBuilder sb = new StringBuilder();
        sb.append("params:");
        if ("application/x-www-form-urlencoded".equals(type)) {
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String key = String.valueOf(enu.nextElement());
                String value = request.getParameter(key);
                sb.append(key).append(":").append(value).append(",");
            }
        } else {
            sb.append(getJsonParameter(request));
        }
        String params = sb.toString();
        if (null != params && params.length() > 200) {
            params = params.substring(0, 200).concat("...");
        }
        return params;
    }
*/

    //获取参数 : 是text/plain、application/json这两种情况
    public String getJsonParameter(HttpServletRequest request)  {
        StringBuilder responseStrBuilder = new StringBuilder();
        try {
            int contentLength = request.getContentLength();
            if(contentLength<0){
                return null;
            }
            byte buffer[] = new byte[contentLength];
            for (int i = 0; i < contentLength;) {

                int readlen = request.getInputStream().read(buffer, i,
                        contentLength - i);
                if (readlen == -1) {
                    break;
                }
                i += readlen;
                String charEncoding = request.getCharacterEncoding();
                if (charEncoding == null) {
                    charEncoding = "UTF-8";
                }
                responseStrBuilder.append(new String(buffer, charEncoding));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return responseStrBuilder.toString();
    }
    public void methodAfterReturing(Object o) {
        try {
            if (o != null)
                log.info("Response内容:{}", JSONObject.toJSON(o));
        } catch (Exception e) {
            log.error("AOP methodAfterReturing:", e);
        }
    }
}



