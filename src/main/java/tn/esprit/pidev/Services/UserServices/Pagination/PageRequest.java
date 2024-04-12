package tn.esprit.pidev.Services.UserServices.Pagination;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequest extends AbstractPageRequest {
    public PageRequest(int pageNumber, int pageSize) {
        super(pageNumber, pageSize);
    }

    @Override
    public Sort getSort() {
        return Sort.unsorted();
    }

    @Override
    public Pageable next() {
        return (Pageable) new UnsupportedOperationException();
    }

    @Override
    public Pageable previous() {
        return (Pageable) new UnsupportedOperationException();
    }

    @Override
    public Pageable first() {
        return  (Pageable) new UnsupportedOperationException();
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return (Pageable) new UnsupportedOperationException();
    }
}
