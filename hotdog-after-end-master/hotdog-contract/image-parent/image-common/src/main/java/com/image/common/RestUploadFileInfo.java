package com.image.common;

import java.io.Serializable;

public class RestUploadFileInfo
  implements Serializable
{
  private static final long serialVersionUID = -3535922935017949524L;
  private boolean status;
  private String desc;
  private String filePath;
  private String fileName;
  private String serviceName;

  public boolean isStatus()
  {
    return this.status;
  }
  public void setStatus(boolean status) {
    this.status = status;
  }
  public String getDesc() {
    return this.desc;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public String getFilePath() {
    return this.filePath;
  }
  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }
  public String getFileName() {
    return this.fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  public String getServiceName() {
    return this.serviceName;
  }
  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}