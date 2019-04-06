package mmall.beans;

import lombok.*;

import java.util.Set;

/**
 * Created by Raymond on 2019/1/12.
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Mail {
    private String subject;
    private String message;
    private Set<String> receivers;
}
