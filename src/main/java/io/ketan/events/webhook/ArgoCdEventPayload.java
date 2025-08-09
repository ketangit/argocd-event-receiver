package io.ketan.events.webhook;

import io.ketan.events.model.ContainerImage;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

import lombok.Setter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class ArgoCdEventPayload {
    @NotBlank
    private String event;

    @NotBlank
    private String appName;

    private String project;
    private String revision;
    private String status;
    private String syncStartedAt;
    private String syncCompletedAt;

    @NotBlank
    private String fingerprint;

    private List<ContainerImage> images;
}