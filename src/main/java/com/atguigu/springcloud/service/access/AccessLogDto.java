package com.atguigu.springcloud.service.access;


import lombok.Data;

@Data
public class AccessLogDto {
  private AccessLogDto() {}

  AccessLogDto(String dataFrom) {
    this.dataFrom = dataFrom;
  }

  private String requestUrl;
  private String methodNameZh;
  private String param;
  private String paramBody;
  private String userId;
  private String userName;
  private String loginname;
  private String beginTime;
  private String endTime;
  private long consumeTime;
  private String result;
  private String dataFrom;
  private String remark;



}
