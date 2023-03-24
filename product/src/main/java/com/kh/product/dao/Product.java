package com.kh.product.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               //@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@AllArgsConstructor //모든 맴버 필드를 매개변수로 하는 생성자 생성
@NoArgsConstructor  //디폴트 생성자
public class Product {
  private Long pid;      //pid      NUMBER(10,0)
  private String pname;  //pname    VARCHAR2(30 BYTE)
  private Long quantity; //quantity NUMBER(10,0)
  private Long price;    //price    NUMBER(10,0)
}
