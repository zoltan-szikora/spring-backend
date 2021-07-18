package hu.szikorazoltan.validation;

import com.fasterxml.jackson.core.JsonProcessingException;

public class MyException extends JsonProcessingException {

  protected MyException(String msg) {
    super(msg);
  }

}
