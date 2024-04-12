package tn.esprit.pidev.Services.UserServices.Pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.pidev.entities.User;

import java.util.List;
@Setter
@Getter
public class PagedResponse<User>{
    public List<User> content;
    public long count ;
    public long totalCount;


    public PagedResponse(final List<User> content, final long count, final long totalCount) {
        this.content = content;
        this.count = count;
        this.totalCount = totalCount;
    }


}
