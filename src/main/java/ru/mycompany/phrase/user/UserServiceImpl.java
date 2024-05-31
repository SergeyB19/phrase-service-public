package ru.mycompany.phrase.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;
import ru.mycompany.phrase.domen.response.Response;
import ru.mycompany.phrase.domen.response.SuccessResponse;
import ru.mycompany.phrase.domen.response.constant.Code;
import ru.mycompany.phrase.domen.response.exception.CommonException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ValidationUtils validationUtils;
    private final EncryptUtils encryptUtils;
    private final UserDao userDao;
    private final CommonDao commonDao;
    private final CommonService commonService;



    @Override
    public ResponseEntity<Response> getMyPhrases(String accessToken) {

        long userId = commonDao.getUserIdByToken(accessToken);

        List<PhraseResp> phrases = userDao.getPhrasesByUserId(userId);
        commonService.phraseEnrichment(phrases);

        return new ResponseEntity<>(SuccessResponse.builder().data(CommonPhrasesResp.builder().phrases(phrases).build()).build(), HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Response> publicPhrase(PublicPhraseReq req, String accessToken) {

        validationUtils.validationRequest(req);

        long userId = commonDao.getUserIdByToken(accessToken);
        long phraseId = userDao.addPhrase(userId, req.getText());
        log.info("userId: {}, phraseId: {}", userId, phraseId);

        for (String tag : req.getTags()) {
            userDao.addTag(tag);
            userDao.addPhraseTag(phraseId, tag);
        }

        return new ResponseEntity<>(SuccessResponse.builder().build(), HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Response> login(LoginReq req) {

        validationUtils.validationRequest(req);

        String encryptPassword = encryptUtils.encryptPassword(req.getAuthorization().getPassword());
        String accessToken = userDao.getAccessToken(User.builder().nickname(req.getAuthorization().getNickname()).encryptPassword(encryptPassword).build());
        return new ResponseEntity<>(SuccessResponse.builder().data(LoginResp.builder().accessToken(accessToken).build()).build(), HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Response> registration(RegistrationReq req) {

        validationUtils.validationRequest(req);

        if (userDao.isExistsNickname(req.getAuthorization().getNickname()))
            throw CommonException.builder().code(Code.NICKNAME_BUSY).userMessage("Этот ник уже занят, придумайте другой").httpStatus(HttpStatus.BAD_REQUEST).build();

        String accessToken = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis();
        String encryptPassword = encryptUtils.encryptPassword(req.getAuthorization().getPassword());
        userDao.insertNewUser(User.builder().nickname(req.getAuthorization().getNickname()).encryptPassword(encryptPassword).accessToken(accessToken).build());

        return new ResponseEntity<>(SuccessResponse.builder().data(RegistrationResp.builder().accessToken(accessToken).build()).build(), HttpStatus.OK);
    }
}
