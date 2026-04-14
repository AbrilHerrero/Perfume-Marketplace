package com.uade.tpo.marketplacePerfume.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Administrator accounts cannot be deleted")
public class AdminUserCannotBeDeletedException extends RuntimeException {
}
