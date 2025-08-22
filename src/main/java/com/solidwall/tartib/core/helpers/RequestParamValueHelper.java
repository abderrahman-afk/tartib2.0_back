package com.solidwall.tartib.core.helpers;

import lombok.Getter;
import lombok.Setter;

/**
 * RequestParamHelper
 */
@Getter
@Setter
public class RequestParamValueHelper {

  private Long valuelLong;
  private String valueString;

  public boolean isLong() {
    return valuelLong != null;
  }

  public boolean isString() {
    return valueString != null;
  }
}