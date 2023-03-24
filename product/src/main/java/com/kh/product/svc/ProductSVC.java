package com.kh.product.svc;

//사용자 입장에서의 인터페이스

import com.kh.product.dao.Product;

import java.util.List;
import java.util.Optional;

public interface ProductSVC {
  //등록
  Long save(Product product);
  //  (input)Product(상품정보) -> (output)Product(상품정보)

  //조회
  Optional<Product> findById(Long pid);
  //  (input)pid -> (output)Product(상품정보)

  //수정
  int update(Long pid, Product product);
  //  (input)pid, Product -> (output)수정된 레코드 수

  //삭제
  int delete(Long pid);
  //  (input)pid -> (output)삭제된 레코드 수

  //전체조회
  List<Product> findAll();
  //  (input)Product -> (output)Product 컬렉션

  //상품 존재 유무
  boolean isExist(Long pid);

}
