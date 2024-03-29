package ru.mycompany.phrase.service.impl;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;

public interface PhraseService {
    ResponseEntity<Response> test();
}
