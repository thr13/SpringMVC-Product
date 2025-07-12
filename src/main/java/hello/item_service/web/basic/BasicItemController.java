package hello.item_service.web.basic;

import hello.item_service.domain.item.Item;
import hello.item_service.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping()
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //    @PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                            @RequestParam("price") int price,
                            @RequestParam("quantity") Integer quantity,
                            Model model) {

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item); // 저장된 결과물을 바로 삽입

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item) { // Model 에 들어가는 값 "item,
        /*
        @ModelAttribute("item") 는 아래 코드와 같은 의미이다
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);
         */

        itemRepository.save(item);
        /*
        @ModelAttribute 는 Model 객체를 만들어 주고 자동으로 View 에 넣어준다 그러므로 아래 코드는 생략 가능
        model.addAttribute("item", item);
         */

        return "basic/item";
    }

    //    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        /*
        @ModelAttribute 파라미터 생략시, 뒤의 클래스명의 첫글자를 소문자로 변경하고 ModelAttribute 에 담기게 된다
        Item -> item
         */
        itemRepository.save(item);

        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV4(Item item) {
        /*
        단순 타입인 경우 @ModelAttribute 생략시 @RequestParam 이 적용되고
        객체인경우 @ModelAttribute 생략시 ModelAttribute 가 적용된다
        그러므로 Item -> item 로 변경되고 이게 ModelAttribute 로 담기게 된다
         */
        itemRepository.save(item);

        return "basic/item";
        /*
        서버 내부에서 POST 요청하고, HTTP 응답 데이터 만들어 줌 ->
        새로고침시 계속 POST 전송하므로 문제 발생함 ->
        그러므로 저장을 하고 나서 redirect 를 하면 된다
        (즉, 상품 저장 후 ViewTemplate 로 이동하는게 아닌, 상품 상세 화면으로 redirect 를 호출해주면 된다(URL 자체가 바뀌므로 계속 요청(GET)해도 문제가 되지 않는다)
         */
    }

    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId(); // RPG(Post/Redirect/Get) 적용
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item); // 리파지토리에 있는 값 변경
        return "redirect:/basic/items/{itemId}"; // 전송완료 후, URL 을 이동시킨다
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }


}
