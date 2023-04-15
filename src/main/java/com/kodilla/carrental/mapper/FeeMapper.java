package com.kodilla.carrental.mapper;

import com.kodilla.carrental.domain.Fee;
import com.kodilla.carrental.domain.FeeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeeMapper {

    public Fee mapToFee(FeeDto feeDto) {
        return new Fee(feeDto.getDescription(), feeDto.getValue());
    }

    public FeeDto mapToFeeDto(Fee fee) {
        return new FeeDto(fee.getDescription(), fee.getValue());
    }

    public List<FeeDto> mapToFeeDtoList(List<Fee> feeList) {
        return feeList.stream()
                .map(this::mapToFeeDto)
                .collect(Collectors.toList());
    }
}