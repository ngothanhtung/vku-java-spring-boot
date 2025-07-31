package com.example.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// Dùng cho InheritanceType.TABLE_PER_CLASS hoặc JOINED
@Entity
@Table(name = "shippers")
//public class Shipper extends User {
//  private String bikeNumber;
//  private String vehicleType;
//}

public class Shipper {
  @Id
  private Long id;
  private String bikeNumber;
  private String vehicleType;
}

