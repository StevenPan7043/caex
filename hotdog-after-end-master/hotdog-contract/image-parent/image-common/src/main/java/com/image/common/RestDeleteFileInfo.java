package com.image.common;

import java.io.Serializable;

public class RestDeleteFileInfo
  implements Serializable
{
  private static final long serialVersionUID = -6985520131638767506L;
  private boolean status;
  private String desc;

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
}