package com.kh.product.web.form.product;

import lombok.Data;

@Data
public class DetailForm {
  private Long pid;
  private String pname;
  private Long quantity;
  private Long price;
}
