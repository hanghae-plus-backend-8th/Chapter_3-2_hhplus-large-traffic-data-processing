package kr.hhplus.be.server.shared.dto;

import lombok.Getter;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class ListDto<T> {

    private List<T> list;

    private Long totalCount;

    public <R> ListDto<R> map(Function<? super T, ? extends R> mapper) {
        List<R> mappedList = this.list.stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new ListDto<>(mappedList, this.totalCount);
    }

    public ListDto(List<T> list, Long totalCount) {
        this.list = list;
        this.totalCount = totalCount;
    }
}
