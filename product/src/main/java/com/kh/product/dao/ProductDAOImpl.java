package com.kh.product.dao;

//구현체 : DB와 연동하여 CRUD

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository              //구현클래스와 연동
@RequiredArgsConstructor //final 멤버 필드를 매개값으로 하는 생성자 자동 생성
public class ProductDAOImpl implements ProductDAO {

  //호출하려면 -> DB 주소를 알아야 함
  private final NamedParameterJdbcTemplate template;  //CRUD 기능을 가진 객체

  /**
   * 등록
   * @param product 상품
   * @return 상품아이디
   */
  @Override
  public Long save(Product product) {
    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    StringBuffer sb = new StringBuffer(); //새로운 객체를 생성하지 않고 메모리에 덮어쓰기 함
    sb.append("insert into product(pid, pname, quantity, price) ");
    sb.append("     values(product_pid_seq.nextval, :pname, :quantity, :price) ");

    //2.파라미터 값을 매핑 -case1)Bean
    SqlParameterSource param = new BeanPropertySqlParameterSource(product); //Bean: 스프링 프레임워크에서 관리하는 자바 객체

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sb.toString(), param, keyHolder, new String[]{"pid"}); //(쿼리, 파라미터의 치환값, 값을 전달받을 객체, 읽어들이는 방법)

    long pid = keyHolder.getKey().longValue(); //키홀더를 통해 키를 받아옴. 키값이 long 타입이기에 longValue

    return pid;
  }

  /**
   * 조회
   * @param pid 상품아이디
   * @return 상품
   */
  @Override
  public Optional<Product> findById(Long pid) {
    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");
    sb.append(" where pid = :id ");

    //2.파라미터 값을 매핑 -case1)Map
    try {
      Map<String, Long> param = Map.of("id", pid); //(key, value)
      Product product = template.queryForObject(
          sb.toString(), param, productRowMapper()      //(쿼리, 파라미터, 받아올 값) //수동매핑
      );
      return Optional.of(product);
    } catch (EmptyResultDataAccessException e) {        //조회 결과가 없는 경우
      return Optional.empty();
    }
  }

  class RowMapperImpl implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
      Product product = new Product();
      product.setPid(rs.getLong("pid"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      product.setPrice(rs.getLong("price"));
      return product;
    }
  }
  //2-1.수동 매핑
  private RowMapper<Product> productRowMapper() {

    return (rs, rowNum) -> { //(resultSet, rowNumber)
      Product product = new Product();
      product.setPid(rs.getLong("pid"));
      product.setPname(rs.getString("pname"));
      product.setQuantity(rs.getLong("quantity"));
      product.setPrice(rs.getLong("price"));
      return product;
    };
  }


  /**
   * 수정
   * @param pid 상품아이디
   * @param product 상품
   * @return 수정된 레코드 수
   */
  @Override
  public int update(Long pid, Product product) {
    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    StringBuffer sb = new StringBuffer();
    sb.append("update product ");
    sb.append("   set pname    = :pname, ");
    sb.append("       quantity = :quantity, ");
    sb.append("       price    = :price ");
    sb.append(" where pid = :id ");

    //2.파라미터 값을 매핑 -case3)SqlParameterSource
    SqlParameterSource param = new MapSqlParameterSource() //(key, value)
        .addValue("id", pid)
        .addValue("pname", product.getPname())
        .addValue("quantity", product.getQuantity())
        .addValue("price", product.getPrice());

    return template.update(sb.toString(), param); //(쿼리, 파라미터)
  }

  /**
   * 삭제
   * @param pid 상품아이디
   * @return 삭제된 레코드 수
   */
  @Override
  public int delete(Long pid) {
    //sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    String sql = "delete from product where pid = :id ";

    return template.update(sql, Map.of("id", pid)); //(쿼리, 파라미터)
  }

//  /**
//   * 전체 삭제
//   * @return 삭제된 레코드 수
//   */
//  @Override
//  public int deleteAll() {
//    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
//    String sql = "delete from product ";
//
//    //2.파라미터 값을 매핑
//    Map<String, String> param = Map.of("", "");
//    int deletedRowCnt = template.update(sql, param);
//
//    return deletedRowCnt;
//  }

  /**
   * 전체 조회
   * @return 상품 목록
   */
  @Override
  public List<Product> findAll() {
    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    StringBuffer sb = new StringBuffer();
    sb.append("select pid, pname, quantity, price ");
    sb.append("  from product ");

    //2.파라미터 값을 매핑
    List<Product> list = template.query(              //(쿼리, 파라미터)
        sb.toString(),
        BeanPropertyRowMapper.newInstance(Product.class) //레코드 컬럼과 자바객체 멤버 필드가 동일한 이름인 경우 -> 자동매핑 가능
    );

    return list;
  }

  /**
   * 상품 존재 유무
   * @param pid 상품아이디
   * @return
   */
  @Override
  public boolean isExist(Long pid) {
    boolean isExist = false;
    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
    String sql = "select count(*) from product where pid = :pid ";

    //2.파라미터 값을 매핑
    Map<String, Long> param = Map.of("pid", pid); //(key, value)
    Integer integer = template.queryForObject(sql, param, Integer.class);
    isExist = (integer > 0) ? true : false;

    return isExist;
  }

  //등록된 상품수
//  @Override
//  public int countOfRecord() {
//    //1.sql내 검증된 쿼리 준비 -> sql내에서 변경되는 부분만 수정해줌
//    String sql = "select count(*) from product ";
//
//    //2.파라미터 값을 매핑
//    LinkedHashMap<String, String> param = new LinkedHashMap<>();
//    Integer rows = template.queryForObject(sql, param, Integer.class);
//
//    return rows;
//  }


}
