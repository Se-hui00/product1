package com.kh.product.web;

import com.kh.product.domain.entity.Product;
import com.kh.product.domain.product.svc.ProductSVC;
import com.kh.product.web.form.product.DetailForm;
import com.kh.product.web.form.product.SaveForm;
import com.kh.product.web.form.product.UpdateForm;
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
@RequestMapping("/products")
@RequiredArgsConstructor  //final 멤버 필드를 매개값으로 하는 생성자를 자동 생성
public class ProductController {

  private final ProductSVC productSVC;


  //등록양식
  @GetMapping("/add")
  public String saveForm(Model model){
    SaveForm saveForm = new SaveForm();
    model.addAttribute("saveForm", saveForm);
    return "product/saveForm";
  }

  //등록처리
  @PostMapping("/add")
  public String save(
      @Valid @ModelAttribute SaveForm saveForm,
      BindingResult bindingResult,  //검증 결과를 담는 객체
      RedirectAttributes redirectAttributes
  ){
    log.info("saveForm={}", saveForm);

    //데이터 검증
    //어노테이션 기반 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";
    }

    // 글로벌오류
    // 총액(상품수량 * 단가) 1억원 초과금지
    if(saveForm.getQuantity() * saveForm.getPrice() > 100_000_000L){
      bindingResult.reject("totalprice", new String[]{"1"},"");
    }

    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/saveForm";
    }

    //등록
    Product product = new Product();
    product.setPname(saveForm.getPname());
    product.setQuantity(saveForm.getQuantity());
    product.setPrice(saveForm.getPrice());

    Long savedpid = productSVC.save(product);
    redirectAttributes.addAttribute("id", savedpid);
    return "redirect:/products/{id}/detail";
  }

  //조회
  @GetMapping("/{id}/detail")
  public String findById(
      @PathVariable("id") Long id,
      Model model
  ){
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    DetailForm detailForm = new DetailForm();
    detailForm.setPid(product.getPid());
    detailForm.setPname(product.getPname());
    detailForm.setQuantity(product.getQuantity());
    detailForm.setPrice(product.getPrice());

    model.addAttribute("detailForm", detailForm);
    return "product/detailForm";
  }

  //수정양식
  @GetMapping("/{id}/edit")
  public String updateForm(
      @PathVariable("id") Long id,
      Model model
  ){
    Optional<Product> findedProduct = productSVC.findById(id);
    Product product = findedProduct.orElseThrow();

    UpdateForm updateForm = new UpdateForm();
    updateForm.setPid(product.getPid());
    updateForm.setPname(product.getPname());
    updateForm.setQuantity(product.getQuantity());
    updateForm.setPrice(product.getPrice());

    model.addAttribute("updateForm", updateForm);
    return "product/updateForm";
  }

  //수정
  @PostMapping("/{id}/edit")
  public String update(
      @PathVariable("id") Long pid,
      @Valid @ModelAttribute UpdateForm updateForm,
      BindingResult bindingResult,
      RedirectAttributes redirectAttributes
  ){
    //데이터 검증
    if(bindingResult.hasErrors()){
      log.info("bindingResult={}", bindingResult);
      return "product/updateForm";
    }

    //정상 처리
    Product product = new Product();
    product.setPid(pid);
    product.setPname(updateForm.getPname());
    product.setQuantity(updateForm.getQuantity());
    product.setPrice(updateForm.getPrice());

    productSVC.update(pid, product);

    redirectAttributes.addAttribute("id", pid);
    return "redirect:/products/{id}/detail";
  }

  //삭제
  @GetMapping("/{id}/del")
  public String deleteById(@PathVariable("id") Long pid){

    productSVC.delete(pid);

    return "redirect:/products";
  }

  //선택삭제
  @PostMapping("/items/del")
  public String deleteParts(@RequestParam("chk") List<Long> pids) {
    log.info("pids={}", pids);
    if(pids.size() > 0) {
      productSVC.deleteParts(pids);
    }else {
      return "product/all";
    }
    return "redirect:/products";
  }

  //목록
  @GetMapping
  public String findAll(Model model){

    List<Product> products = productSVC.findAll();
    model.addAttribute("products", products);

    return "product/all";
  }
}
