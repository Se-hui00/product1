package com.kh.product.web.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SaveForm {

  @NotBlank   //null, 빈 문자열("") 허용 안 함 (문자열 타입에만 사용)
  private String pname;
  @NotNull    //모든 타입에 대해 null 적용 안 함
  @Positive   //양수값
  private Long quantity;
  @NotNull
  @Positive
  @Min(1000)  //최소값
  private Long price;
}
