package com.kh.product.web;

import com.kh.product.dao.Product;
import com.kh.product.svc.ProductSVC;
import com.kh.product.web.form.DetailForm;
import com.kh.product.web.form.SaveForm;
import com.kh.product.web.form.UpdateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/products") //요청메소드("요청url")
@RequiredArgsConstructor     //final 멤버 필드를 매개값으로 하는 생성자를 자동 생성
public class ProductController {

  //호출하려면 -> 참조값(주소값)을 알아야 함
  private final ProductSVC productSVC;

  //1.등록
  // 1)등록양식
  @GetMapping("/add")
  public String saveForm(Model model) {
    SaveForm saveForm = new SaveForm();
    model.addAttribute("saveForm", saveForm);
    return "product/saveForm"; //뷰(saveForm 화면) 반환
  }

  // 2)등록처리
  @PostMapping("/add")
  public String save(
      @Valid @ModelAttribute SaveForm saveForm,  //@Valid: 값이 채워질 때 검증하겠다
      BindingResult bindingResult,               //검증결과를 담을 객체. @Valid 바로 뒤에 위치해야 함
      RedirectAttributes redirectAttributes
  ) {
    log.info("saveForm={}", saveForm);

    //데이터 검증
    // 어노테이션 기반 검증
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";    //오류가 있으면 saveForm 페이지로 다시 돌아감
    }

    // 글로벌 오류 (업무 규칙)
    //  총액(수량 * 가격)이 1억원 이상되는 상품 등록 금지
    if(saveForm.getQuantity() * saveForm.getPrice() >= 100_000_000L) {
      bindingResult.reject("totalprice", new String[]{"1000"}, "");
    }

    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";     //오류가 있으면 saveForm 페이지로 다시 돌아감
    }

    //등록: DAO에서 처리
    Product product = new Product();            //product 객체 생성
    product.setPname(saveForm.getPname());      //생성한 객체에 값 넘겨주기
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Long savePid = productSVC.save(product);
    redirectAttributes.addAttribute("id", savePid);
    return "redirect:/products/{id}/detail";   //등록, 수정, 삭제시 redirect
    //  사용자 요청 처리 후 url로 하여금 다시 요청하도록 처리(사용자 모르게 내부적 처리) -> 조회화면으로 돌아감
  }

  //2.조회
  @GetMapping("/{id}/detail")
  public String findById(
      @PathVariable("id") Long id,
      Model model
  ) {
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    //도메인의 객체를 form 객체로 전환
    DetailForm detailForm = new DetailForm();     //detailForm 객체 생성
    detailForm.setPid(product.getPid());          //생성한 객체에 값 넘겨주기
    detailForm.setPname(product.getPname());
    detailForm.setQuantity(product.getQuantity());
    detailForm.setPrice(product.getPrice());

    model.addAttribute("detailForm", detailForm); //뷰에 데이터를 전달할 때 모델 객체에 담아서 전달
    return "product/detailForm";  //뷰(detailForm 화면) 반환
  }

  //3.수정
  // 1)수정양식
  @GetMapping("/{id}/edit")
  public String updateForm(
      @PathVariable("id") Long id,
      Model model
  ) {
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();       //updateForm 객체 생성
    updateForm.setPid(product.getPid());            //생성한 객체에 값 넘겨주기
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());

    model.addAttribute("updateForm", updateForm);
    return "product/updateForm";  //뷰(updateForm 화면) 반환

  }

  // 2)수정처리
  @PostMapping("/{id}/edit")
  public String update(
      @PathVariable("id") Long pid,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ) {
    //데이터 검증
    if(bindingResult.hasErrors()) {
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }

    //정상처리
    Product product = new Product();
    product.setPid(pid);
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());

    productSVC.update(pid, product);

    redirectAttributes.addAttribute("pid", pid);
    return "redirect:/products/{id}/detail";
  }

  //4.삭제
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long pid) {

    productSVC.delete(pid);
    return "redirect:/products";
  }

  //5.전체조회
  @GetMapping
  public String findAll(Model model) {
    List<Product> products = productSVC.findAll();
    model.addAttribute("products", products);

    return "product/all";   //뷰(all 화면) 반환
  }
}
