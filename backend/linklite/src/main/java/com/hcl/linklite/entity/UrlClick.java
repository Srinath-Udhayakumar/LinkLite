package com.hcl.linklite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;
import org.hibernate.Hibernate;
import java.time.LocalDateTime;

@Entity
@Table(name = "url_clicks",
        indexes = {
                @Index(name = "idx_clicked_at", columnList = "clicked_at")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UrlClick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    private Url url;

    @Column(name = "clicked_at", nullable = false)
    private LocalDateTime clickedAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @PrePersist
    protected void onCreate() {
        if (clickedAt == null) {
            clickedAt = LocalDateTime.now();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UrlClick urlClick = (UrlClick) o;
        return id != null && Objects.equals(id, urlClick.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
