package ru.mycompany.phrase.domen.response.api.user.common;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import ru.mycompany.phrase.domen.response.constant.RegExp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationReq {

    @NotBlank(message = "nickname должен быть заполнен")
    @Pattern(regexp = RegExp.nickname, message = "Некорректный nickname")
    private String nickname;

    @NotBlank(message = "password должен быть заполнен")
    @Pattern(regexp = RegExp.password, message = "Некорректный password")
    private String password;
}