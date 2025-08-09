package io.ketan.events.model;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContainerImage {

    private String url;
    private String name;
    private String tag;
}