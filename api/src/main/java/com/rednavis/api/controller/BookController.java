package com.rednavis.api.controller;

import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_COUNT;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_DELETE;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_FINDALL;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_INSERT;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_SAVE;

import com.rednavis.api.property.MaasProperty;
import com.rednavis.shared.dto.book.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(BOOK_URL)
@RequiredArgsConstructor
public class BookController {

  private final MaasProperty maasProperty;

  /**
   * insert.
   *
   * @param book book
   * @return
   */
  @PostMapping(BOOK_URL_INSERT)
  public Mono<Book> insert(@RequestBody Book book) {
    WebClient webClient = createWebClient();
    return webClient.post()
        .uri(uriBuilder -> uriBuilder.path(BOOK_URL_INSERT).build())
        .body(BodyInserters.fromValue(book))
        .exchange()
        .flatMap(response -> response.bodyToMono(Book.class));
  }

  /**
   * save.
   *
   * @param book book
   * @return
   */
  @PutMapping(BOOK_URL_SAVE)
  public Mono<Book> save(@RequestBody Book book) {
    WebClient webClient = createWebClient();
    return webClient.put()
        .uri(uriBuilder -> uriBuilder.path(BOOK_URL_SAVE).build())
        .body(BodyInserters.fromValue(book))
        .exchange()
        .flatMap(response -> response.bodyToMono(Book.class));
  }

  /**
   * findAll.
   *
   * @param page page
   * @param size size
   * @return
   */
  @GetMapping(BOOK_URL_FINDALL)
  public Flux<Book> findAll(@RequestParam int page, @RequestParam int size) {
    log.info("findAll get [page: {}, size: {}]", page, size);
    WebClient webClient = createWebClient();
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(BOOK_URL_FINDALL)
            .queryParam("page", page)
            .queryParam("size", size)
            .build())
        .exchange()
        .flatMapMany(response -> response.bodyToFlux(Book.class));
  }

  /**
   * count.
   *
   * @return
   */
  @GetMapping(BOOK_URL_COUNT)
  public Mono<Long> count() {
    WebClient webClient = createWebClient();
    return webClient.get()
        .uri(uriBuilder -> uriBuilder.path(BOOK_URL_COUNT).build())
        .exchange()
        .flatMap(response -> response.bodyToMono(Long.class));
  }

  /**
   * delete.
   *
   * @param bookId bookId
   */
  @DeleteMapping(BOOK_URL_DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void delete(@RequestParam String bookId) {
    WebClient webClient = createWebClient();
    webClient.delete()
        .uri(uriBuilder -> uriBuilder.path(BOOK_URL_DELETE)
            .queryParam("bookId", bookId)
            .build())
        .exchange();
  }

  private WebClient createWebClient() {
    return WebClient.builder()
        .baseUrl(maasProperty.getBpm().getServer()) //baseUrl
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();
  }
}
