package com.rednavis.api.controller;

import com.rednavis.api.dto.ProcessDefinitionDto;
import com.rednavis.api.dto.ProcessInstanceDto;
import com.rednavis.api.property.MaasProperty;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/bpm")
@RequiredArgsConstructor
public class BpmController {

  private final MaasProperty maasProperty;

  /**
   * API method to get the list of existing process definitions.
   *
   * @return Flux with Process Definitions
   */
  @GetMapping("/processes")
  public Flux<ProcessDefinitionDto> findAllProcesses() {
    WebClient webClient = createWebClient();
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path("/processes")
            .build())
        .exchange()
        .flatMapMany(response -> response.bodyToFlux(ProcessDefinitionDto.class));
  }

  /**
   * API method to start the process by it's id with parameters.
   * @param id Process id
   * @param params map of parameters
   * @return Mono with Process Instance
   */
  @PostMapping("/process/{id}")
  public Mono<ProcessInstanceDto> startProcess(@PathVariable String id, @RequestBody Map<String, Object> params) {
    log.info("Starting process {}", id);
    WebClient webClient = createWebClient();
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path("/process").pathSegment(id).build())
        .body(BodyInserters.fromValue(params))
        .exchange()
        .flatMap(response -> response.bodyToMono(ProcessInstanceDto.class));
  }

  private WebClient createWebClient() {
    return WebClient.builder()
        .baseUrl(maasProperty.getBpm().getServer()) //baseUrl
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
