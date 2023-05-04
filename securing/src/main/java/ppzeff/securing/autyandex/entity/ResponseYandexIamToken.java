package ppzeff.securing.autyandex.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseYandexIamToken {

	@JsonProperty("expiresAt")
	private String expiresAt;

	@JsonProperty("iamToken")
	private String iamToken;
}
