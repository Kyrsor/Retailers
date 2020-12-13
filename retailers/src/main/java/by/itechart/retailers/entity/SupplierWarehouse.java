package by.itechart.retailers.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static by.itechart.retailers.constant.TableConstants.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = SUPPLIER_WAREHOUSE_TABLE)
public class SupplierWarehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = SUPPLIER_WAREHOUSE_NAME)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = SUPPLIER_WAREHOUSE_CUSTOMER)
    private Customer customer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = SUPPLIER_WAREHOUSE_ADDRESS)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = SUPPLIER_WAREHOUSE_STATUS)
    private DeletedStatus status;

}
