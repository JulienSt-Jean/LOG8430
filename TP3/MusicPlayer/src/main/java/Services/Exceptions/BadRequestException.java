package Services.Exceptions;

public class BadRequestException extends WebApiException {

  public BadRequestException(String message) {
    super(message);
  }

}
