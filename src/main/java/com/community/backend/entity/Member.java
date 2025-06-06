
package com.community.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MEMBER")
@Builder
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MB_ID", nullable = false)
    private Long id;

    @Column(name = "MB_NAME", nullable = false ,columnDefinition = "VARCHAR(20)")
    private String name;

    @Column(name = "MB_EMAIL" ,columnDefinition = "VARCHAR(20)")
    private String email;

    @Column(name = "MB_PHONE", length = 20 ,columnDefinition = "VARCHAR(20)")
    private String phoneNumber;

    @Column(name = "DEL_YN", nullable = false, columnDefinition = "CHAR(1)")
    private String delYn;

}
