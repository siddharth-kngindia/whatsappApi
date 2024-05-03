package com.api.whatsapp.model;
@Entity
@Table(name = "cover_images")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CoverImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
