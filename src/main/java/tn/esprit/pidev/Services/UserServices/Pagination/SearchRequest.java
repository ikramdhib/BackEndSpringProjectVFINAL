package tn.esprit.pidev.Services.UserServices.Pagination;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {
    public int size ;
    public int page;
}
