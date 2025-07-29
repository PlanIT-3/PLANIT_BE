package woojooin.planit.global.util.openData;

import org.springframework.beans.factory.annotation.Value;

public class OpenApiUtil {

	@Value("${open.api.auth-key}")
	private static String AUTH_KEY;

}
