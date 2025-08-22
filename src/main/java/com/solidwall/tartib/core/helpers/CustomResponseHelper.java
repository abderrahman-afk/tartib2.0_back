package com.solidwall.tartib.core.helpers;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CustomResponseHelper<T> {

  Date timestamp;
  Integer status;
  boolean error;
  String message;
  T body;
  
}
