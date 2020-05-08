package com.atixlabs.semillasmiddleware.app.didi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DidiAuthRequest {
    private String name;
    private String password;

    public DidiAuthRequest(String username, String password) {
        this.name = username;
        this.password = password;
    }
}


/*
{
	"name":"semillas",
	"password":"semillas_password"
}
 */

/*
{
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI1ZWE3MWUwMzljYzEyYjNkOTc5MzU3ZGMiLCJleHAiOjE2MDE3NTAxNzUsImlhdCI6MTU4ODc5MDE3NX0.8o_0bvT29hEUkQy725D4RybVyMj_gsBtBVran77ctxw"
  }
}
 */