package com.kh.product.dao;

//인터페이스

import java.util.List;
import java.util.Optional;

public interface ProductDAO {

  /**
   * 등록
   * @param product 상품
   * @return 상품아이디
   */
  Long save(Product product);
  //  (input)Product(상품정보) -> (output)Product(상품정보)

  /**
   * 조회
   * @param pid 상품아이디
   * @return 상품
   */
  Optional<Product> findById(Long pid);
  //  (input)pid -> (output)Product(상품정보)

  /**
   * 수정
   * @param pid 상품아이디
   * @param product 상품
   * @return 수정된 레코드 수
   */
  int update(Long pid, Product product);
  //  (input)pid, Product -> (output)수정된 레코드 수

  /**
   * 삭제
   * @param pid 상품아이디
   * @return 삭제된 레코드 수
   */
  int delete(Long pid);
  //  (input)pid -> (output)삭제된 레코드 수

//  /**
//   * 전체 삭제
//   * @return 삭제한 레코드 건수
//   */
//  int deleteAll();

  /**
   * 전체조회
   * @return 상품목록
   */
  List<Product> findAll();
  //  (input)Product -> (output)Product 컬렉션

  /**
   * 상품 존재 유무
   * @param pid 상품아이디
   * @return
   */
  boolean isExist(Long pid);

//  /**
//   * 등록된 상품수
//   * @return 레코드 수
//   */
//  int countOfRecord();

}
