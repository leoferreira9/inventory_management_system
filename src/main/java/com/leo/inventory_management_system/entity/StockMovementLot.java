package com.leo.inventory_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "stock_movement_lots")
public class StockMovementLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movement_id", nullable = false)
    private StockMovement movement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private StockLot lot;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    public StockMovementLot(){}

    public Long getId() {
        return id;
    }

    public StockMovement getMovement() {
        return movement;
    }

    public void setMovement(StockMovement movement) {
        this.movement = movement;
    }

    public StockLot getLot() {
        return lot;
    }

    public void setLot(StockLot lot) {
        this.lot = lot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
