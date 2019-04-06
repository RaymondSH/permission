package mmall.beans;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Created by Raymond on 2019/1/11.
 */
@Getter
@Setter
@Builder
@ToString
public class PageResult<T> {

    private List<T> data = Lists.newArrayList();

    private int total = 1;
}
