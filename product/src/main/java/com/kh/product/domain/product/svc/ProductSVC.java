package com.kh.product.domain.product.svc;

import com.kh.product.domain.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSVC {
  //등록
  Long save(Product product);
  //조회
  Optional<Product> findById(Long pid);
  //수정
  int update(Long pid, Product product);
  //삭제
  int delete(Long pid);
  //부분삭제
  int deleteParts(List<Long> pids);
  //목록
  List<Product> findAll();
  //상품 존재유무
  boolean isExist(Long pid);
}
