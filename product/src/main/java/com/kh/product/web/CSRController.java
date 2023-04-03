package com.kh.product.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/csr")
public class CSRController {

  @GetMapping("/products")
  public String manageProduct(){
    return "/csr/product/product";
  }
}
