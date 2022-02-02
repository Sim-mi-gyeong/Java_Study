package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 양방향 -> 연관관계의 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")    // 매핑을 무엇으로 할 것인지? -> member_id = Foregin Key 의 이름
    private Member member;   // order 와 member 는 다 : 일 관계

    // cascade = CascadeType.ALL : orderItems 에 데이터를 넣고 Order 를 저장하면 -> Order 에도 저장 (원래는, 모든 엔티티는 각각 persist 해야 함)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
    persist(orderItemA)
    persist(orderItemB)
    persist(orderItemC)
    persist(order)

    persist(order) 만 하면 된다.
     */

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)   // Order 만 persist 하고 저장하면 delivery 도 persist
    @JoinColumn(name="delivery_id")
    private Delivery delivery;

    // order_date
    private LocalDateTime orderTime;   // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;   // 주문 상태 [ORDER , CANCEL] -> enum

    // == 연관관계 (편의) 메서드 == // (양방향일 때 세팅)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }
    /*
    public void main(String[] args) {
        Member member = new Member();
        Order order = new Order();

        member.getOrders().add(order);
        order.setMember(member);
    }
     */

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
