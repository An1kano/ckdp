package org.ckzs.ckdp.VO;

import lombok.*;

import java.io.Serializable;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {
    private int id;
    private String username;
    private String token;
}
