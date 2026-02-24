package com.leo.inventory_management_system.entity;

import jakarta.persistence.*;

@Entity
public class StockMovementLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movement_id", nullable = false)
    private StockMovement movement;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private StockLot lot;

    @Column(nullable = false)
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
