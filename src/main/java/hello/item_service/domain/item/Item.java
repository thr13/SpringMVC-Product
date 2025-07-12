package hello.item_service.domain.item;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price; // 갯수가 없다는 표식인 NULL 을 넣기 위해 int 대신 Integer 사용
    private Integer quantity;

    public Item() {
    }

    // id 를 제외한 생성자
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
