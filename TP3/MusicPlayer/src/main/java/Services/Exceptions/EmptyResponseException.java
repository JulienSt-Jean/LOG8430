package Services.Exceptions;

public class EmptyResponseException extends WebApiException {

  public EmptyResponseException() {
    super();
  }

  public EmptyResponseException(String message) {
    super(message);
  }
}
