package ru.mycompany.phrase.user;


import ru.mycompany.phrase.domen.response.Response;

public interface UserService {

    ResponseEntity<Response> getMyPhrases(String accessToken);

    ResponseEntity<Response> publicPhrase(PublicPhraseReq req, String accessToken);

    ResponseEntity<Response> login(LoginReq req);

    ResponseEntity<Response> registration(RegistrationReq req);
}