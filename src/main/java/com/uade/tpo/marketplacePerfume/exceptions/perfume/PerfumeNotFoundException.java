package com.uade.tpo.marketplacePerfume.exceptions.perfume;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "El perfume no existe")
public class PerfumeNotFoundException extends Exception {
    
}
