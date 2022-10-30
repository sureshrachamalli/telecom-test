package ca.tele.chatr.chatrtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDto<T> implements Serializable {


    private ResponseState responseStatus;
    private T data;
    private List<ErrorDto> errors;

    public ResponseDto(ResponseState status, List<ErrorDto> errors){
        this.responseStatus = status;
        this.errors = errors;
    }

    public ResponseDto(ResponseState status, T data){
        this.responseStatus = status;
        this.data = data;
    }

    public static <T> ResponseDto<T> forSuccess(T data){
        return new ResponseDto(ResponseState.SUCCESS, data);
    }

    public static <T> ResponseDto<T> forFailure(List<ErrorDto> errorDtos){
        return new ResponseDto(ResponseState.ERROR, errorDtos);
    }

}
