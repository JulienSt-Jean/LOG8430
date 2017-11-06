package Api.Exceptions;

public class ServerErrorException extends WebApiException {

  public ServerErrorException(String message) {
    super(message);
  }
}
